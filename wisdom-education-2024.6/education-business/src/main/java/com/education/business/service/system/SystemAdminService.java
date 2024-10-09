package com.education.business.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.education.business.mapper.system.SystemAdminMapper;
import com.education.business.service.BaseService;
import com.education.common.exception.BusinessException;
import com.education.common.model.PageInfo;
import com.education.common.utils.Md5Utils;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.model.dto.*;
import com.education.model.entity.SystemAdmin;
import com.education.model.entity.SystemAdminRole;
import com.education.model.entity.SystemMenu;
import com.education.model.entity.SystemRole;
import com.education.model.request.PageParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
public class SystemAdminService extends BaseService<SystemAdminMapper, SystemAdmin> {

    @Autowired
    private SystemMenuService systemMenuService;
    @Autowired
    private SystemRoleService systemRoleService;
    @Autowired
    private SystemAdminRoleService systemAdminRoleService;

    public PageInfo<SystemAdmin> listPage(PageParam pageParam, SystemAdmin systemAdmin) {
        Page<SystemAdmin> page = new Page<>(pageParam.getPageNumber(), pageParam.getPageSize());
        LambdaQueryWrapper queryWrapper = Wrappers.<SystemAdmin>lambdaQuery()
                .like(ObjectUtils.isNotEmpty(systemAdmin.getLoginName()),
                        SystemAdmin::getLoginName, systemAdmin.getLoginName());
        return selectPage(super.page(page, queryWrapper));
    }

    public AdminRoleDto selectById(Integer id) {
        return baseMapper.selectById(id);
    }

    /**
     * 加载用户菜单及权限标识
     *
     * @param userSession
     */
    public void loadUserMenuAndPermission(AdminUserSession userSession) {
        List<SystemMenu> menuList = null;
        if (userSession.isSuperAdmin()) {
            menuList = systemMenuService.list(Wrappers.lambdaQuery(SystemMenu.class)
                    .orderByAsc(SystemMenu::getSort));
        } else {
            Integer adminId = userSession.getAdminId();
            List<SystemRole> systemRoleList = systemRoleService.findRoleListByAdminId(adminId);
            userSession.setRoleList(systemRoleList);
            List<Integer> roleIds = systemRoleList.stream()
                    .map(SystemRole::getId)
                    .collect(Collectors.toList());
            menuList = systemMenuService.getMenuListByRoles(roleIds);
        }
        if (ObjectUtils.isNotEmpty(menuList)) {
            Set<String> permissionList = menuList.stream()
                    .filter(systemMenu -> ObjectUtils.isNotEmpty(systemMenu.getPermission()))
                    .map(SystemMenu::getPermission)
                    .collect(Collectors.toSet());
            List<MenuTree> menuTreeList = systemMenuService.getTreeMenuList(menuList);
            userSession.setPermissionList(permissionList);
            userSession.setMenuTreeList(menuTreeList);
        }
    }

    public Result login(String loginName, String password) {
        Result result = new Result();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
        try {
            subject.login(token);
            result.setCode(ResultCode.SUCCESS);
            result.setMessage("登录成功");
        } catch (Exception e) {
            log.error("登录失败", e);
            result.setCode(ResultCode.FAIL);
            if (e instanceof UnknownAccountException) {
                result.setMessage("用户不存在");
            } else {
                result.setMessage("用户名或密码错误");
            }
        }
        return result;
    }

    @Transactional
    public void saveOrUpdate(AdminRoleDto adminRoleDto) {

        if (ObjectUtils.isEmpty(adminRoleDto.getId())) {
            String password = adminRoleDto.getPassword();
            if (ObjectUtils.isEmpty(password)) {
                throw new BusinessException(new ResultCode(ResultCode.FAIL, "请输入密码"));
            }
            String confirmPassword = adminRoleDto.getConfirmPassword();
            if (!password.equals(confirmPassword)) {
                throw new BusinessException(new ResultCode(ResultCode.FAIL, "密码与确认密码不一致"));
            }
            String encrypt = Md5Utils.encodeSalt(Md5Utils.generatorKey());
            adminRoleDto.setEncrypt(encrypt);
            password = Md5Utils.getMd5(password, encrypt);
            adminRoleDto.setPassword(password);
        }

        super.saveOrUpdate(adminRoleDto);

        // 保存管理员角色信息
        if (ObjectUtils.isNotEmpty(adminRoleDto.getRoleIds())) {
            List<SystemAdminRole> adminRoleList = new ArrayList<>();
            adminRoleDto.getRoleIds().forEach(roleId -> {
                SystemAdminRole systemAdminRole = new SystemAdminRole();
                systemAdminRole.setAdminId(adminRoleDto.getId());
                systemAdminRole.setRoleId(roleId);
                adminRoleList.add(systemAdminRole);
            });

            systemAdminRoleService.deleteByAdminId(adminRoleDto.getId());
            systemAdminRoleService.saveBatch(adminRoleList);
        }
    }   

    @Transactional
    public Result deleteById(Integer id) {
        SystemAdmin systemAdmin = super.getById(id);
        if (systemAdmin.isSuper()) {
            return Result.fail(ResultCode.FAIL, "不允许删除超级管理员");
        }
        super.removeById(id);
        systemAdminRoleService.deleteByAdminId(id);
        return Result.success(ResultCode.SUCCESS, "删除管理员" + systemAdmin.getLoginName() + "成功");
    }

    /**
     * 重置密码
     * @param adminRoleDto
     */
    public void updatePassword(AdminRoleDto adminRoleDto) {
        String passWord = adminRoleDto.getPassword();
        String newPassword = adminRoleDto.getNewPassword();
        String confirmPassword = adminRoleDto.getConfirmPassword();
        SystemAdmin systemAdmin = super.getById(adminRoleDto.getId());
        String encrypt = systemAdmin.getEncrypt();

        if (!newPassword.equals(confirmPassword)) { // 管理员列表重置密码
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "密码与确认密码不一致"));
        }

        // 原始密码不为空校验密码是否正确
        if (ObjectUtils.isNotEmpty(passWord)) {
            // 密码输入正确才能修改新密码
            passWord = Md5Utils.getMd5(passWord, encrypt);
            String userPassword = systemAdmin.getPassword();
            if (!passWord.equals(userPassword)) {
                throw new BusinessException(new ResultCode(ResultCode.FAIL, "密码输入错误"));
            }
        }

        passWord = Md5Utils.getMd5(newPassword, encrypt); // 对新密码进行加密
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.set("password", passWord);
        updateWrapper.eq("id", adminRoleDto.getId());
        super.update(updateWrapper);
    }

    /**
     * 在线用户分页列表
     * @param pageParam
     * @return
     */
    public PageInfo<SystemAdmin> getOnlineUserList(PageParam pageParam) {
        List<AdminUserSession> adminUserSessionList = new ArrayList<>();
        Collection userIds = cacheBean.getKeys(OnlineUserManager.USER_ID_CACHE);
        userIds.forEach(userId -> {
            AdminUserSession onlineUser = cacheBean.get(userId);
            if (onlineUser != null) {
                adminUserSessionList.add(onlineUser);
            }
        });

        if (ObjectUtils.isNotEmpty(adminUserSessionList)) {
            List<SystemAdmin> systemAdminList = adminUserSessionList.stream()
                    .map(AdminUserSession::getSystemAdmin)
                    .collect(Collectors.toList());
            return ObjectUtils.selectPageList(pageParam.getPageNumber(), pageParam.getPageSize(), systemAdminList);
        }
        return new PageInfo();
    }
}

