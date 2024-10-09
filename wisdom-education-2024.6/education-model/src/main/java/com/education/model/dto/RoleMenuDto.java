package com.education.model.dto;

import com.education.model.entity.SystemMenu;

import java.util.List;

/**
 *   
 
 */
public class RoleMenuDto {

    /**
     * 角色id
     */
    private Integer roleId;

    private List<Integer> menuIds;

    /**
     * 角色权限列表
     */
    private List<MenuTree> menuList;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public List<Integer> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Integer> menuIds) {
        this.menuIds = menuIds;
    }

    public void setMenuList(List<MenuTree> menuList) {
        this.menuList = menuList;
    }

    public List<MenuTree> getMenuList() {
        return menuList;
    }
}
