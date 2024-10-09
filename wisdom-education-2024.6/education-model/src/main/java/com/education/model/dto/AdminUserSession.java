package com.education.model.dto;

import com.education.common.constants.EnumConstants;
import com.education.common.utils.ObjectUtils;
import com.education.model.entity.SystemAdmin;
import com.education.model.entity.SystemMenu;
import com.education.model.entity.SystemRole;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *   
 
 */
public class AdminUserSession extends BaseDto {

    private String sessionId;
    private String token;
    private SystemAdmin systemAdmin; // 存储管理员信息
    private List<SystemRole> roleList;
    private List<SystemMenu> menuList;
    private List<MenuTree> menuTreeList; // 树形菜单列表
    private Set<String> permissionList = new HashSet<>();

    public void setMenuTreeList(List<MenuTree> menuTreeList) {
        this.menuTreeList = menuTreeList;
    }

    public List<MenuTree> getMenuTreeList() {
        return menuTreeList;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public Integer getAdminId() {
        return systemAdmin.getId();
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public AdminUserSession(SystemAdmin systemAdmin) {
        this.systemAdmin = systemAdmin;
    }

    public SystemAdmin getSystemAdmin() {
        return systemAdmin;
    }

    public void setSystemAdmin(SystemAdmin systemAdmin) {
        this.systemAdmin = systemAdmin;
    }

    public List<SystemRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SystemRole> roleList) {
        this.roleList = roleList;
    }

    public List<SystemMenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<SystemMenu> menuList) {
        this.menuList = menuList;
    }

    public Set<String> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(Set<String> permissionList) {
        this.permissionList = permissionList;
    }

    /**
     * 是否超级管理员
     * @return
     */
    public boolean isSuperAdmin() {
        if (ObjectUtils.isEmpty(systemAdmin)) {
            return false;
        }
        return systemAdmin.getSuperFlag() == EnumConstants.Flag.YES.getValue();
    }

}
