package com.education.api.controller.admin.system;

import com.education.business.service.system.SystemAdminService;
import com.education.common.annotation.SystemLog;
import com.education.common.base.BaseController;
import com.education.common.model.PageInfo;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.model.dto.AdminRoleDto;
import com.education.model.entity.SystemAdmin;
import com.education.model.request.PageParam;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 管理员管理
 *   
 * 
 */
@RestController
@Api(tags = "管理员管理")
@RequestMapping("/system/admin")
public class SystemAdminController extends BaseController {

    @Autowired
    private SystemAdminService systemAdminService;

    /**
     * 管理员列表
     * @param pageParam
     * @param systemAdmin
     * @return
     */
    @GetMapping
    @SystemLog(describe = "获取管理员列表")
    @RequiresPermissions("system:admin:list")
    public Result<PageInfo<SystemAdmin>> list(PageParam pageParam, SystemAdmin systemAdmin) {
        return Result.success(systemAdminService.listPage(pageParam, systemAdmin));
    }

    /**
     * 管理员详情
     * @param id
     * @return
     */
    @GetMapping("selectById")
    @SystemLog(describe = "查看管理员详情")
    public Result selectById(Integer id) {
        return Result.success(systemAdminService.selectById(id));
    }

    /**
     * 添加或修改管理员
     * @param adminRoleDto
     * @return
     */
    @PostMapping
    @SystemLog(describe = "添加或修改管理员")
    @RequiresPermissions(value = {"system:admin:save", "system:admin:update"}, logical = Logical.OR)
    public Result saveOrUpdate(@RequestBody AdminRoleDto adminRoleDto) {
        systemAdminService.saveOrUpdate(adminRoleDto);
        return Result.success();
    }

    /**
     * 删除管理员
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @SystemLog(describe = "删除管理员")
    @RequiresPermissions("system:admin:deleteById")
    public Result deleteById(@PathVariable Integer id) {
        return systemAdminService.deleteById(id);
    }


    /**
     * 修改密码
     * @param adminRoleDto
     * @return
     */
    @PostMapping("updatePassword")
    @SystemLog(describe = "修改管理员密码")
    @RequiresPermissions("system:admin:updatePassword")
    public Result updatePassword(@RequestBody AdminRoleDto adminRoleDto) {
        systemAdminService.updatePassword(adminRoleDto);
        return Result.success(ResultCode.SUCCESS, "修改密码成功");
    }

    /**
     * 获取在线用户
     * @return
     */
    @GetMapping("getOnlineUserList")
    @SystemLog(describe = "获取在线用户列表")
    public Result<PageInfo<SystemAdmin>> getOnlineUserList(PageParam pageParam) {
        return Result.success(systemAdminService.getOnlineUserList(pageParam));
    }
}
