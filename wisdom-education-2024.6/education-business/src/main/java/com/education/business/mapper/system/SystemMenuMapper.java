package com.education.business.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.education.model.dto.MenuTree;
import com.education.model.entity.SystemMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
public interface SystemMenuMapper extends BaseMapper<SystemMenu> {

    /**
     * 获取角色菜单列表
     * @param roleIds
     * @return
     */
    List<SystemMenu> getMenuListByRoles(@Param("roleIds") List<Integer> roleIds);

    /**
     * 获取系统树形菜单
     * @return
     */
    List<MenuTree> getTreeMenuList();

    /**
     * 根据id 获取MenuTree
     * @param id
     * @return
     */
    MenuTree selectMenuTreeById(Integer id);

    /**
     * 获取角色tree 菜单
     * @param roleId
     * @return
     */
    List<MenuTree> selectTreeMenuListByRoleId(Integer roleId);
}
