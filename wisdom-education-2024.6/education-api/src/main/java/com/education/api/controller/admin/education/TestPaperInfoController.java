package com.education.api.controller.admin.education;

import com.education.business.service.education.TestPaperInfoService;
import com.education.common.base.BaseController;
import com.education.common.constants.CacheKey;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.model.dto.TestPaperInfoDto;
import com.education.model.dto.TestPaperQuestionDto;
import com.education.model.entity.TestPaperInfo;
import com.education.model.entity.TestPaperQuestionInfo;
import com.education.model.request.PageParam;
import com.education.model.request.TestPaperQuestionRequest;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 试卷管理
 *   
 *   
 *
 */
@RestController
@RequestMapping("/system/testPaperInfo")
public class TestPaperInfoController extends BaseController {

    @Autowired
    private TestPaperInfoService testPaperInfoService;

    /**
     * 试卷列表
     * @param pageParam
     * @param testPaperInfoDto
     * @return
     */
    @GetMapping
    @RequiresPermissions("system:testPaperInfo:list")
    public Result list(PageParam pageParam, TestPaperInfoDto testPaperInfoDto) {
        return Result.success(testPaperInfoService.selectPageList(pageParam, testPaperInfoDto));
    }

    /**
     * 添加或修改试卷
     * @param testPaperInfo
     * @return
     */
    @PostMapping
    @RequiresPermissions(value = {"system:testPaperInfo:save", "system:testPaperInfo:update"}, logical = Logical.OR)
    public Result saveOrUpdate(@RequestBody TestPaperInfo testPaperInfo) {
        if (testPaperInfo.getId() != null) {
            TestPaperInfo dataBaseTestPaperInfo = testPaperInfoService.getById(testPaperInfo.getId());
            if (dataBaseTestPaperInfo.getExamNumber() > 0) {
                return Result.fail(ResultCode.FAIL, "试卷已被使用, 无法修改");
            }
        }
        testPaperInfoService.saveOrUpdate(testPaperInfo);
        return Result.success();
    }

    /**
     * 获取试卷试题列表
     * @param testPaperQuestionRequest
     * @return
     */
    @GetMapping("getPaperQuestionList")
    @RequiresPermissions("system:testPaperInfo:relevanceQuestion")
  //  @Cacheable(cacheNames = CacheKey.TEST_PAPER_INFO_CACHE, key = "#testPaperQuestionRequest.testPaperInfoId")
    public Result selectPaperQuestionList(PageParam pageParam, TestPaperQuestionRequest testPaperQuestionRequest) {
        return Result.success(testPaperInfoService.selectPaperQuestionList(pageParam, testPaperQuestionRequest));
    }

    /**
     * 修改试卷试题分数或者排序
     * @param testPaperQuestionDto
     * @return
     */
    @PostMapping("updatePaperQuestionMarkOrSort")
    @CacheEvict(cacheNames = CacheKey.TEST_PAPER_INFO_CACHE, key = "#testPaperQuestionDto.testPaperInfoId")
    public Result updatePaperQuestionMarkOrSort(@RequestBody TestPaperQuestionDto testPaperQuestionDto) {
        testPaperInfoService.updatePaperQuestionMarkOrSort(testPaperQuestionDto);
        return Result.success();
    }

    /**
     * 保存试卷试题
     * @param testPaperQuestionInfoList
     * @return
     */
    @PostMapping("saveTestPaperInfoQuestion")
    @CacheEvict(
        cacheNames = CacheKey.TEST_PAPER_INFO_CACHE,
        key = "#testPaperQuestionInfoList.get(0).testPaperInfoId"
    )
    public Result saveTestPaperInfoQuestion(@RequestBody List<TestPaperQuestionInfo> testPaperQuestionInfoList) {
        testPaperInfoService.saveTestPaperInfoQuestion(testPaperQuestionInfoList);
        return Result.success();
    }

    /**
     * 发布试卷
     * @param testPaperInfoId
     * @return
     */
    @PostMapping("publishTestPaperInfo/{testPaperInfoId}")
    @RequiresPermissions("system:testPaperInfo:publish")
    public Result publishTestPaperInfo(@PathVariable Integer testPaperInfoId) {
        return Result.success(testPaperInfoService.publishTestPaperInfo(testPaperInfoId));
    }

    /**
     * 撤销试卷
     * @param testPaperInfoId
     * @return
     */
    @PostMapping("cancelTestPaperInfo/{testPaperInfoId}")
    @RequiresPermissions("system:testPaperInfo:cancel")
    public Result cancelTestPaperInfo(@PathVariable Integer testPaperInfoId) {
        return Result.success(testPaperInfoService.cancelTestPaperInfo(testPaperInfoId));
    }


    /**
     * 删除试卷
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @RequiresPermissions("system:testPaperInfo:deleteById")
    @CacheEvict(cacheNames = CacheKey.TEST_PAPER_INFO_CACHE, key = "#id")
    public Result deleteById(@PathVariable Integer id) {
        return Result.success(testPaperInfoService.deleteById(id));
    }

    /**
     * 移除试卷试题
     * @param testPaperQuestionInfo
     * @return
     */
    @DeleteMapping("removePaperQuestion")
    @CacheEvict(cacheNames = CacheKey.TEST_PAPER_INFO_CACHE, key = "#testPaperQuestionInfo.testPaperInfoId")
    public Result removePaperQuestion(@RequestBody TestPaperQuestionInfo testPaperQuestionInfo) {
        return Result.success(testPaperInfoService.removePaperQuestion(testPaperQuestionInfo));
    }

    /**
     * 试卷打印
     * @param testPaperInfoId
     * @return
     */
    @GetMapping("printPaperInfo/{testPaperInfoId}")
    public Result printPaperInfo(@PathVariable Integer testPaperInfoId) {
        return Result.success(testPaperInfoService.printPaperInfo(testPaperInfoId));
    }
}
