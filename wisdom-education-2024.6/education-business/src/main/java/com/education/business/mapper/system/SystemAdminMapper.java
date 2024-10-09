package com.education.business.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.education.model.dto.AdminRoleDto;
import com.education.model.entity.SystemAdmin;


/**
 *   
 *   

public interface SystemAdminMapper extends BaseMapper<SystemAdmin> {

    /**
     * 管理员列表条件分页查询
     * @param page
     * @param systemAdmin
     * @return
     */
    Page<AdminRoleDto> selectPageList(Page<AdminRoleDto> page, SystemAdmin systemAdmin);


    AdminRoleDto selectById(Integer adminId);
}
