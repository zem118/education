package com.education.business.mapper.education;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.education.model.dto.TestPaperInfoDto;
import com.education.model.dto.TestPaperQuestionDto;
import com.education.model.entity.QuestionInfo;
import com.education.model.entity.TestPaperInfo;
import com.education.model.request.TestPaperQuestionRequest;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface TestPaperInfoMapper extends BaseMapper<TestPaperInfo> {

    /**
     * 试卷分列表
     * @param page
     * @param testPaperInfo
     * @return
     */
    Page<TestPaperInfoDto> selectPageList(Page<TestPaperInfoDto> page, TestPaperInfo testPaperInfo);

    /**
     * 试卷试题列表
     * @param page
     * @param testPaperQuestionRequest
     * @return
     */
    Page<TestPaperQuestionDto> selectPaperQuestionList(Page<TestPaperQuestionDto> page, TestPaperQuestionRequest testPaperQuestionRequest);


    /**
     * 更新试卷参考人数
     * @param id
     * @return
     */
    @Update("update test_paper_info set exam_number = exam_number + 1 where id = #{id}")
    boolean updateExamNumberById(@Param("id") Integer id);
}
