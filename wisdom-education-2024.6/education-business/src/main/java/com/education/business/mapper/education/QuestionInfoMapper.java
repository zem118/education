package com.education.business.mapper.education;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.education.common.model.QuestionInfoImport;
import com.education.model.dto.QuestionInfoDto;
import com.education.model.entity.QuestionInfo;
import com.education.model.request.QuestionInfoQuery;
import org.apache.ibatis.annotations.Select;

/**

 */
public interface QuestionInfoMapper extends BaseMapper<QuestionInfo> {

    /**
     * 试题分页列表
     * @param page
     * @param questionInfo
     * @return
     */
    Page<QuestionInfoDto> selectPageList(Page<QuestionInfoDto> page, QuestionInfoQuery questionInfo);


    QuestionInfoDto selectById(Integer id);
}
