package com.education.api.controller.student;

import com.education.business.service.education.ExamInfoService;
import com.education.business.service.education.ExamMonitorService;
import com.education.business.service.education.TestPaperInfoService;
import com.education.common.base.BaseController;
import com.education.common.constants.CacheKey;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.model.dto.ExamMonitor;
import com.education.model.dto.TestPaperInfoDto;
import com.education.model.request.PageParam;
import com.education.model.request.StudentQuestionRequest;
import com.education.model.request.TestPaperQuestionRequest;
import com.education.model.response.StudentExamRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

/**
 * 学生端试卷管理
 *   
 *   

 */
@RestController("student-testPaperInfo")
@RequestMapping("/student/testPaperInfo")
public class TestPaperInfoController extends BaseController {

    @Autowired
    private TestPaperInfoService testPaperInfoService;
    @Autowired
    private ExamInfoService examInfoService;
    @Autowired
    private ExamMonitorService examMonitorService;

    /**
     * 试卷列表
     * @param pageParam
     * @param testPaperInfoDto
     * @return
     */
    @GetMapping("list")
    public Result list(PageParam pageParam, TestPaperInfoDto testPaperInfoDto) {
        testPaperInfoDto.setPublishFlag(true);
        return Result.success(testPaperInfoService.selectPageList(pageParam, testPaperInfoDto));
    }

    /**
     * 获取试卷试题
     * @param id
     * @return
     */
    @GetMapping("selectPaperQuestionById")
    @Cacheable(cacheNames = CacheKey.TEST_PAPER_INFO_CACHE, key = "#id")
    public Result selectPaperQuestionById(Integer id) {
        PageParam pageParam = new PageParam(); // 设置不分页
        TestPaperQuestionRequest testPaperQuestionRequest = new TestPaperQuestionRequest();
        testPaperQuestionRequest.setTestPaperInfoId(id);
        return Result.success(testPaperInfoService.selectPaperQuestionList(pageParam, testPaperQuestionRequest)) ;
    }

    /**
     * 提交试卷试题
     * @param studentQuestionRequest
     * @return
     */
    @PostMapping("commitPaper")
    public Result commitPaper(@RequestBody StudentQuestionRequest studentQuestionRequest) {
        Integer examInfoId = examInfoService.commitTestPaperInfoQuestion(studentQuestionRequest);
        return Result.success(ResultCode.SUCCESS, "提交成功", examInfoId);
    }

    /**
     * 更新学员考试答题进度
     * @param studentExamRate
     * @return
     */
    @PostMapping("updateStudentExamRate")
    public Result updateStudentExamRate(@RequestBody StudentExamRate studentExamRate) {
        ExamMonitor examMonitor = examMonitorService.getExamMonitorStudent(studentExamRate.getTestPaperInfoId(),
                examInfoService.getStudentId());
        examMonitor.setAnswerQuestionCount(studentExamRate.getAnswerQuestionCount());
        examMonitor.setTestPaperInfoId(studentExamRate.getTestPaperInfoId());
        examMonitorService.addStudentToExamMonitor(examMonitor);
        return Result.success();
    }
}
