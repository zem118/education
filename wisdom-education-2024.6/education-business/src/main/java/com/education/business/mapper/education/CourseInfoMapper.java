package com.education.business.mapper.education;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.education.model.dto.CourseInfoDto;
import com.education.model.entity.CourseInfo;


public interface CourseInfoMapper extends BaseMapper<CourseInfo> {

    /**
     * 课程分页列表
     * @param page
     * @param courseInfo
     * @return
     */
    Page<CourseInfoDto> selectPageList(Page<CourseInfoDto> page, CourseInfo courseInfo);
}
