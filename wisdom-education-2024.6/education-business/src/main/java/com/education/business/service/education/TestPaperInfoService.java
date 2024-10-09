package com.education.business.service.education;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.education.business.mapper.education.TestPaperInfoMapper;
import com.education.business.service.BaseService;
import com.education.common.constants.Constants;
import com.education.common.constants.EnumConstants;
import com.education.common.exception.BusinessException;
import com.education.common.model.PageInfo;
import com.education.common.template.BaseTemplate;
import com.education.common.template.EnjoyTemplate;
import com.education.common.utils.FileUtils;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.ResultCode;
import com.education.model.dto.TestPaperInfoDto;
import com.education.model.dto.TestPaperQuestionDto;
import com.education.model.entity.StudentInfo;
import com.education.model.entity.TestPaperInfo;
import com.education.model.entity.TestPaperQuestionInfo;
import com.education.model.request.PageParam;
import com.education.model.request.TestPaperQuestionRequest;
import com.jfinal.kit.Kv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class TestPaperInfoService extends BaseService<TestPaperInfoMapper, TestPaperInfo> {

    @Autowired
    private TestPaperQuestionInfoService testPaperQuestionInfoService;

    /**
     * 试卷分页列表
     * @param pageParam
     * @param testPaperInfo
     * @return
     */
    public PageInfo<TestPaperInfoDto> selectPageList(PageParam pageParam, TestPaperInfo testPaperInfo) {
        Page<TestPaperInfoDto> page = new Page<>(pageParam.getPageNumber(), pageParam.getPageSize());
        StudentInfo studentInfo = getStudentInfo();
        if (studentInfo != null) {
            testPaperInfo.setGradeInfoId(studentInfo.getGradeInfoId());
        }
        return selectPage(baseMapper.selectPageList(page, testPaperInfo));
    }

    /**
     * 获取试卷试题列表
     * @param pageParam
     * @param testPaperQuestionRequest
     * @return
     */
    public PageInfo<TestPaperQuestionDto> selectPaperQuestionList(PageParam pageParam, TestPaperQuestionRequest testPaperQuestionRequest) {
        Page<TestPaperQuestionDto> page = new Page<>(pageParam.getPageNumber(), pageParam.getPageSize());
        return selectPage(baseMapper.selectPaperQuestionList(page, testPaperQuestionRequest));
    }


    /**
     * 修改试卷试题分数或者排序
     * @param testPaperQuestionDto
     */
    @Transactional
    public void updatePaperQuestionMarkOrSort(TestPaperQuestionDto testPaperQuestionDto) {
        // 更新试卷总分
        if (ObjectUtils.isNotEmpty(testPaperQuestionDto.getUpdateType()) &&
                testPaperQuestionDto.getUpdateType().intValue() == ResultCode.SUCCESS) {
            TestPaperInfo testPaperInfo = this.getById(testPaperQuestionDto.getTestPaperInfoId());
            int testPaperInfoMark = testPaperInfo.getMark();
            TestPaperQuestionInfo testPaperQuestionInfo = testPaperQuestionInfoService.getById(testPaperQuestionDto.getId());
            if (testPaperQuestionInfo.getMark() == 0) {
                testPaperInfo.setMark(testPaperQuestionDto.getMark() + testPaperInfoMark);
            } else {
                testPaperInfoMark -= testPaperQuestionInfo.getMark();
                testPaperInfoMark += testPaperQuestionDto.getMark();
                testPaperInfo.setMark(testPaperInfoMark);
            }
            this.updateById(testPaperInfo);
        }

        testPaperQuestionDto.setUpdateDate(new Date());
        LambdaUpdateWrapper updateWrapper = Wrappers.lambdaUpdate(TestPaperQuestionInfo.class)
                .eq(TestPaperQuestionInfo::getQuestionInfoId, testPaperQuestionDto.getQuestionInfoId())
                .eq(TestPaperQuestionInfo::getTestPaperInfoId, testPaperQuestionDto.getTestPaperInfoId())
                .set(TestPaperQuestionInfo::getMark, testPaperQuestionDto.getMark())
                .set(TestPaperQuestionInfo::getSort, testPaperQuestionDto.getSort());
        testPaperQuestionInfoService.update(updateWrapper);
    }

    @Override
    public boolean saveOrUpdate(TestPaperInfo testPaperInfo) {
        if (testPaperInfo.getId() != null && testPaperInfo.getExamNumber() > 0) {
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "试卷已被使用, 无法修改"));
        }
        return super.saveOrUpdate(testPaperInfo);
    }

    @Transactional
    public ResultCode deleteById(Integer id) {
        TestPaperInfo testPaperInfo = super.getById(id);
        if (testPaperInfo.getExamNumber() == 0) {
            super.removeById(id);
            // 删除试卷试题关联信息
            testPaperQuestionInfoService.deleteByTestPaperInfoId(id);
            return new ResultCode(ResultCode.SUCCESS, "删除成功");
        }
        return new ResultCode(ResultCode.FAIL, "试卷已被使用, 无法删除");
    }

    public ResultCode publishTestPaperInfo(Integer testPaperInfoId) {
        TestPaperInfo testPaperInfo = super.getById(testPaperInfoId);
        if (testPaperInfo.getPublishFlag()) {
            return new ResultCode(ResultCode.FAIL, "试卷已发布,请勿重复操作");
        }

        boolean flag = testPaperQuestionInfoService.hasTestPaperInfoQuestion(testPaperInfoId);
        if (!flag) {
            return new ResultCode(ResultCode.FAIL, "改试卷暂未关联试题,请关联试题之后在发布");
        }

        testPaperInfo.setPublishFlag(true);
        testPaperInfo.setPublishTime(new Date());
        super.updateById(testPaperInfo);
        return new ResultCode(ResultCode.SUCCESS, "发布成功");
    }

    public ResultCode cancelTestPaperInfo(Integer testPaperInfoId) {
        TestPaperInfo testPaperInfo = super.getById(testPaperInfoId);
        if (testPaperInfo.getExamNumber() > 0) {
            return new ResultCode(ResultCode.FAIL, "试卷已有学员作答, 无法撤回");
        }
        testPaperInfo.setPublishFlag(false);
        super.updateById(testPaperInfo);
        return new ResultCode(ResultCode.SUCCESS, "撤销成功");
    }

    @Transactional
    public void saveTestPaperInfoQuestion(List<TestPaperQuestionInfo> testPaperQuestionInfoList) {
        Date now = new Date();
        Integer testPaperInfoId = testPaperQuestionInfoList.get(0).getTestPaperInfoId();
        testPaperQuestionInfoList.forEach(item -> {
            item.setCreateDate(now);
        });
        // 更新试卷试题数量
        TestPaperInfo testPaperInfo = super.getById(testPaperInfoId);
        int questionNumber = testPaperInfo.getQuestionNumber() + testPaperQuestionInfoList.size();
        testPaperInfo.setQuestionNumber(questionNumber);
        super.updateById(testPaperInfo);
        // 保存试卷试题
        testPaperQuestionInfoService.saveBatch(testPaperQuestionInfoList);
    }

    /**
     * 删除试卷试题
     * @param testPaperQuestionInfo
     * @return
     */
    @Transactional
    public ResultCode removePaperQuestion(TestPaperQuestionInfo testPaperQuestionInfo) {
        TestPaperInfo testPaperInfo = super.getById(testPaperQuestionInfo.getTestPaperInfoId());
        if (testPaperInfo.getExamNumber() > 0) {
            return new ResultCode(ResultCode.FAIL, "试卷已被使用,无法移除试题");
        }
        Integer testPaperInfoId = testPaperQuestionInfo.getTestPaperInfoId();
        testPaperQuestionInfoService.removeById(testPaperQuestionInfo.getId());
        // 更新试卷试题数量
        int questionNumber = testPaperInfo.getQuestionNumber() - 1;
        int mark = testPaperInfo.getMark() - testPaperQuestionInfo.getMark();
        testPaperInfo.setQuestionNumber(questionNumber);
        LambdaUpdateWrapper updateWrapper = Wrappers.<TestPaperInfo>lambdaUpdate()
                .set(TestPaperInfo::getQuestionNumber, questionNumber)
                .set(TestPaperInfo::getMark, mark)
                .eq(TestPaperInfo::getId, testPaperInfoId);
        super.update(updateWrapper);
        return new ResultCode(ResultCode.SUCCESS, "删除成功");
    }

    /**
     * 更新考试人数
     * @param testPaperInfoId
     * @return
     */
    public boolean updateExamNumber(Integer testPaperInfoId) {
        return baseMapper.updateExamNumberById(testPaperInfoId);
      /*  // 考试人数加1
        redisTemplate.boundHashOps(testPaperInfoId).increment(CacheKey.TEST_PAPER_INFO_CACHE, 1);

        // 更新考试参考人数
        TestPaperInfo testPaperInfo = super.getById(testPaperInfoId);
        int examNumber = testPaperInfo.getExamNumber() + 1;
        LambdaUpdateWrapper updateWrapper = new LambdaUpdateWrapper<>(TestPaperInfo.class)
                .set(TestPaperInfo::getExamNumber, examNumber)
                .eq(TestPaperInfo::getId, testPaperInfo.getId());
        return super.update(updateWrapper);*/
    }

    /**
     * 打印试卷
     * @param testPaperInfoId
     */
    public String printPaperInfo(Integer testPaperInfoId) {
        TestPaperQuestionRequest testPaperQuestionRequest = new TestPaperQuestionRequest();
        testPaperQuestionRequest.setTestPaperInfoId(testPaperInfoId);
        testPaperQuestionRequest.setAddExamMonitor(false);
        PageInfo<TestPaperQuestionDto> pageInfo = this.selectPaperQuestionList(new PageParam(), testPaperQuestionRequest);
        List<TestPaperQuestionDto> testPaperQuestionDtoList = pageInfo.getDataList();

        TestPaperInfo testPaperInfo = super.getById(testPaperInfoId);
        Kv data = Kv.create().set("testPaperQuestionList", this.groupQuestion(testPaperQuestionDtoList))
                .set("title", testPaperInfo.getName());
        String htmlName = testPaperInfo + ".html";
        String outDirPath = "/paper_print/"
                + testPaperInfoId + "/" + htmlName;
        String paperTemplateSavePath = FileUtils.getUploadPath() + outDirPath;
        BaseTemplate template = new EnjoyTemplate(Constants.PAPER_INFO_TEMPLATE, paperTemplateSavePath);
        template.generateTemplate(data, htmlName);
        return outDirPath;
    }

    /**
     * 对试卷试题进行分组
     * @param testPaperQuestionDtoList
     * @return
     */
    public List<Map> groupQuestion(List<TestPaperQuestionDto> testPaperQuestionDtoList) {
        List<Map> testPaperQuestionGroupList = new ArrayList<>();
        for (EnumConstants.QuestionType questionType : EnumConstants.QuestionType.values()) {
            Map item = new HashMap<>();
            int questionTypeValue = questionType.getValue();
            List<TestPaperQuestionDto> groupQuestionList = testPaperQuestionDtoList.stream()
                    .filter(question -> question.getQuestionType().intValue() == questionTypeValue)
                    .collect(Collectors.toList());
            item.put("groupQuestionTypeTitle", questionType.getName());
            item.put("groupQuestionList", groupQuestionList);
            testPaperQuestionGroupList.add(item);
        }
        return testPaperQuestionGroupList;
    }
}
