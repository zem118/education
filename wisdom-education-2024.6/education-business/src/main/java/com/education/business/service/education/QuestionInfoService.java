package com.education.business.service.education;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.education.business.mapper.education.QuestionInfoMapper;
import com.education.business.parser.QuestionImportParser;
import com.education.business.parser.QuestionImportParserManager;
import com.education.business.service.BaseService;
import com.education.common.constants.EnumConstants;
import com.education.common.model.PageInfo;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.ResultCode;
import com.education.model.dto.QuestionInfoAnswer;
import com.education.model.dto.QuestionInfoDto;
import com.education.model.entity.QuestionInfo;
import com.education.model.entity.QuestionLanguagePointsInfo;
import com.education.model.entity.StudentQuestionAnswer;
import com.education.model.entity.TestPaperQuestionInfo;
import com.education.model.request.PageParam;
import com.education.model.request.QuestionInfoQuery;
import com.education.model.response.QuestionGroupItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 试题管理
 *   

 */
@Service
public class QuestionInfoService extends BaseService<QuestionInfoMapper, QuestionInfo> {

    @Autowired
    private QuestionLanguagePointsInfoService questionLanguagePointsInfoService;
    @Autowired
    private TestPaperQuestionInfoService testPaperQuestionInfoService;
    @Autowired
    private StudentQuestionAnswerService studentQuestionAnswerService;

    /**
     * 试题分页列表
     * @param pageParam
     * @param questionInfoQuery
     * @return
     */
    public PageInfo<QuestionInfoDto> selectPageList(PageParam pageParam, QuestionInfoQuery questionInfoQuery) {
        Page<QuestionInfoDto> page = new Page<>(pageParam.getPageNumber(), pageParam.getPageSize());
        return selectPage(baseMapper.selectPageList(page, questionInfoQuery));
    }

    /**
     * 添加或修改试题
     * @param questionInfoDto
     * @return
     */
    @Transactional
    public ResultCode saveOrUpdateQuestionInfo(QuestionInfoDto questionInfoDto) {
        List<Integer> languagePointsInfoIdList = questionInfoDto.getLanguagePointsInfoId();
        Integer questionInfoId = questionInfoDto.getId();
        if (ObjectUtils.isNotEmpty(questionInfoId)) {
            if (!this.verificationQuestionInfo(questionInfoId)) {
                return new ResultCode(ResultCode.FAIL, "试题已被使用，禁止修改");
            }
            // 删除试题知识点关联
            LambdaQueryWrapper queryWrapper = Wrappers.<QuestionLanguagePointsInfo>lambdaQuery()
                    .eq(QuestionLanguagePointsInfo::getQuestionInfoId, questionInfoDto.getId());
            questionLanguagePointsInfoService.remove(queryWrapper);
        }

        super.saveOrUpdate(questionInfoDto); // 保存试题记录

        // 保存试题知识点关联信息
        List<QuestionLanguagePointsInfo> questionLanguagePointsInfoList = new ArrayList<>();
        languagePointsInfoIdList.forEach(languagePointsInfoId -> {
            QuestionLanguagePointsInfo questionLanguagePointsInfo = new QuestionLanguagePointsInfo();
            questionLanguagePointsInfo.setLanguagePointsInfoId(languagePointsInfoId);
            questionLanguagePointsInfo.setQuestionInfoId(questionInfoDto.getId());
            questionLanguagePointsInfo.setCreateDate(new Date());
            questionLanguagePointsInfoList.add(questionLanguagePointsInfo);
        });
        questionLanguagePointsInfoService.saveBatch(questionLanguagePointsInfoList);
        return new ResultCode(ResultCode.SUCCESS, "操作成功");
    }


    public QuestionInfoDto selectById(Integer id) {
        return baseMapper.selectById(id);
    }


    /**
     * 对用户作答试题分组
     * @param questionInfoAnswerList
     * @return
     */
    public List<QuestionGroupItemResponse> groupQuestion(List<QuestionInfoAnswer> questionInfoAnswerList) {
        List<QuestionGroupItemResponse> list = new ArrayList<>();
        for (EnumConstants.QuestionType item : EnumConstants.QuestionType.values()) {
            int value = item.getValue();
            List<QuestionInfoAnswer> questionList = questionInfoAnswerList.stream()
                    .filter(questionInfoAnswer -> value == questionInfoAnswer.getQuestionType().intValue())
                    .collect(Collectors.toList());
            if (ObjectUtils.isNotEmpty(questionList)) {
                QuestionGroupItemResponse examQuestionItemResponse = new QuestionGroupItemResponse();
                examQuestionItemResponse.setQuestionTypeName(item.getName());
                examQuestionItemResponse.setQuestionInfoAnswerList(questionList);
                list.add(examQuestionItemResponse);
            }
        }
        return list;
    }

    public ResultCode deleteById(Integer questionInfoId) {
        if (verificationQuestionInfo(questionInfoId)) {
            super.removeById(questionInfoId);
            return new ResultCode(ResultCode.SUCCESS, "删除成功");
        }
        return new ResultCode(ResultCode.FAIL, "试题已被使用，无法删除");

    }

    private boolean verificationQuestionInfo(Integer questionInfoId) {
        LambdaQueryWrapper queryWrapper = Wrappers.<TestPaperQuestionInfo>lambdaQuery()
                .eq(TestPaperQuestionInfo::getQuestionInfoId, questionInfoId)
                .last(" limit 1");
        TestPaperQuestionInfo testPaperQuestionInfo = testPaperQuestionInfoService.getOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(testPaperQuestionInfo)) {
            return false;
        }

        StudentQuestionAnswer studentQuestionAnswer = studentQuestionAnswerService.selectByQuestionInfoId(questionInfoId);
        if (ObjectUtils.isNotEmpty(studentQuestionAnswer)) {
            return false;
        }
        return true;
    }

    public int importQuestion(Integer schoolType, Integer gradeInfoId, Integer subjectId, List<QuestionInfo> questionInfoList) {
        questionInfoList.forEach(questionInfo -> {
            for (EnumConstants.QuestionType item : EnumConstants.QuestionType.values()) {
                if (item.getName().equals(questionInfo.getQuestionTypeName())) {
                    questionInfo.setQuestionType(item.getValue());
                    break;
                }
            }
            QuestionImportParser excelQuestionParser = QuestionImportParserManager.build()
                    .createExcelQuestionParser(questionInfo.getQuestionType());
            questionInfo.setAnswer(excelQuestionParser.parseAnswerText(questionInfo.getAnswer()));
            questionInfo.setOptions(excelQuestionParser.parseOptionText(questionInfo.getOptions()));
            questionInfo.setGradeInfoId(gradeInfoId);
            questionInfo.setSchoolType(schoolType);
            questionInfo.setCreateDate(new Date());
            questionInfo.setSubjectId(subjectId);
        });
        super.saveBatch(questionInfoList);
        return questionInfoList.size();
    }
}
