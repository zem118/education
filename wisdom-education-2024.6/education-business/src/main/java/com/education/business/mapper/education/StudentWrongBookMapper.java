package com.education.business.mapper.education;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.education.model.dto.QuestionInfoAnswer;
import com.education.model.entity.StudentWrongBook;
import com.education.model.request.WrongBookQuery;

/**
 
 */
public interface StudentWrongBookMapper extends BaseMapper<StudentWrongBook> {

    /**
     * 学员错题本列表
     * @param page
     * @param wrongBookQuery
     * @return
     */
    Page<QuestionInfoAnswer> selectPageList(Page<QuestionInfoAnswer> page, WrongBookQuery wrongBookQuery);
}
