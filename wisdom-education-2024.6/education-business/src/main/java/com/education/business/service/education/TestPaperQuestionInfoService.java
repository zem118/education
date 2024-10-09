package com.education.business.service.education;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.education.business.mapper.education.TestPaperQuestionInfoMapper;
import com.education.business.service.BaseService;
import com.education.model.entity.TestPaperQuestionInfo;
import org.springframework.stereotype.Service;


@Service
public class TestPaperQuestionInfoService extends BaseService<TestPaperQuestionInfoMapper, TestPaperQuestionInfo> {

    public boolean deleteByTestPaperInfoId(Integer testPaperInfoId) {
        LambdaQueryWrapper queryWrapper = Wrappers.lambdaQuery(TestPaperQuestionInfo.class)
                .eq(TestPaperQuestionInfo::getTestPaperInfoId, testPaperInfoId);
        return super.remove(queryWrapper);
    }


    /**
     * 校验试卷是否关联了试题
     * @param testPaperInfoId
     * @return
     */
    public boolean hasTestPaperInfoQuestion(Integer testPaperInfoId) {
        LambdaQueryWrapper queryWrapper = Wrappers.lambdaQuery(TestPaperQuestionInfo.class)
                .eq(TestPaperQuestionInfo::getTestPaperInfoId, testPaperInfoId)
                .select(TestPaperQuestionInfo::getId).last(" limit 1");
        TestPaperQuestionInfo testPaperQuestionInfo = super.getOne(queryWrapper);
        return ObjectUtils.isNotEmpty(testPaperQuestionInfo);
    }
}
