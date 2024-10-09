package com.education.api.controller.admin.education;

import com.education.business.service.education.ExamInfoService;
import com.education.common.base.BaseController;
import com.education.common.constants.CacheKey;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.model.dto.StudentExamInfoDto;
import com.education.model.entity.TestPaperInfo;
import com.education.model.request.PageParam;
import com.education.model.request.StudentQuestionRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

/**
 * 考试管理
 *   
 *   

 */
@RestController("/system")
@RequestMapping("/system/exam")
public class ExamInfoController extends BaseController {

    @Autowired
    private ExamInfoService examInfoService;

    @GetMapping
    @RequiresPermissions("system:exam:list")
    public Result list(PageParam pageParam, StudentExamInfoDto studentExamInfoDto) {
        return Result.success(examInfoService.selectExamInfoList(pageParam, studentExamInfoDto));
    }

    /**
     * 获取学员试卷试题列表
     * @param studentId
     * @param examInfoId
     * @return
     */
    @GetMapping("getExamQuestionList/{studentId}/{examInfoId}")
    @Cacheable(cacheNames = CacheKey.EXAM_CACHE, key = "#studentId + ':' + #examInfoId")
    public Result getExamQuestionList(@PathVariable Integer studentId, @PathVariable Integer examInfoId) {
        return Result.success(examInfoService.selectExamQuestionAnswer(studentId, examInfoId));
    }

    /**
     * 批改学员试卷
     * @param studentQuestionRequest
     * @return
     */
    @PostMapping("correctExamQuestion")
    @RequiresPermissions("system:exam:correct")
    @CacheEvict(cacheNames = CacheKey.EXAM_CACHE, key = "#studentQuestionRequest.examInfoId")
    public Result correctExamQuestion(@RequestBody StudentQuestionRequest studentQuestionRequest) {
        examInfoService.correctStudentExam(studentQuestionRequest);
        return Result.success(ResultCode.SUCCESS, "批改成功");
    }

    /**
     * 考试统计
     * @param testPaperInfo
     * @return
     */
    @GetMapping("getExamReportList")
    @RequiresPermissions("system:exam:examReportList")
    public Result selectExamReportList(PageParam pageParam, TestPaperInfo testPaperInfo) {
        return Result.success(examInfoService.selectExamReportList(pageParam, testPaperInfo));
    }

    /**
     * 考试成绩分析
     * @param testPaperInfoId
     * @return
     */
    @GetMapping("getExamDetailReport/{testPaperInfoId}")
    public Result getExamDetailReport(@PathVariable Integer testPaperInfoId) {
        return Result.success(examInfoService.examDetailReport(testPaperInfoId));
    }

    /**
     * 获取考试排名列表
     * @param testPaperInfoId
     * @return
     */
    @GetMapping("getExamRankingList/{testPaperInfoId}")
    public Result getExamRankingList(PageParam pageParam, @PathVariable Integer testPaperInfoId) {
        return Result.success(examInfoService.getExamRankingList(pageParam, testPaperInfoId));
    }
}
