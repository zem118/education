package com.education.business.mapper.education;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.education.model.dto.ExamCount;
import com.education.model.dto.StudentExamInfoDto;
import com.education.model.entity.ExamInfo;
import com.education.model.entity.TestPaperInfo;
import com.education.model.response.ExamInfoDetail;
import com.education.model.response.ExamInfoReport;
import com.education.model.response.TestPaperInfoReport;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface ExamInfoMapper extends BaseMapper<ExamInfo> {

    /**
     * 学员考试记录列表
     * @param page
     * @param studentExamInfoDto
     * @return
     */
    Page<StudentExamInfoDto> selectStudentExamList(Page<StudentExamInfoDto> page, StudentExamInfoDto studentExamInfoDto);


    /**
     * 考试列表
     * @param page
     * @param studentExamInfoDto
     * @return
     */
    Page<StudentExamInfoDto> selectExamList(Page<StudentExamInfoDto> page, StudentExamInfoDto studentExamInfoDto);


    StudentExamInfoDto selectById(@Param("id") Integer id);

    /**
     * 考试统计
     * @param page
     * @param testPaperInfo
     * @return
     */
    Page<TestPaperInfoReport> selectExamReportList(Page<TestPaperInfoReport> page, TestPaperInfo testPaperInfo);

    /**
     * 考试成绩列表
     * @param testPaperInfoId
     * @return
     */
    Page<ExamInfoReport> selectExamListByTestPaperInfoId(Page<ExamInfoReport> page, @Param("testPaperInfoId") Integer testPaperInfoId);


    /**
     * 考试详细分析
     * @param params
     * @return
     */
    ExamInfoDetail selectExamInfoDetail(Map params);

    List<ExamCount> countByDateTime(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
