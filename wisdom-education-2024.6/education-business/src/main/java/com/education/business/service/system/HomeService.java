package com.education.business.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.education.business.service.education.ExamInfoService;
import com.education.business.service.education.QuestionInfoService;
import com.education.business.service.education.StudentInfoService;
import com.education.business.service.education.TestPaperInfoService;
import com.education.common.constants.EnumConstants;
import com.education.common.utils.DateUtils;
import com.education.model.entity.ExamInfo;
import com.jfinal.kit.Kv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 首页数据统计
 *   
 *   
 
 */
@Service
public class HomeService {

    private static final Logger logger = LoggerFactory.getLogger(HomeService.class);
    @Autowired
    private QuestionInfoService questionInfoService;
    @Autowired
    private TestPaperInfoService testPaperInfoService;
    @Autowired
    private StudentInfoService studentInfoService;
    @Autowired
    private ExamInfoService examInfoService;

    public Kv countData() {
        int questionNumber = questionInfoService.count();
        int testPaperInfoNumber = testPaperInfoService.count();
        int studentNumber = studentInfoService.count();
        LambdaQueryWrapper queryWrapper = Wrappers.lambdaQuery(ExamInfo.class)
                .eq(ExamInfo::getCorrectFlag, EnumConstants.Flag.NO.getValue());
        int toBeCorrectedExamNumber = examInfoService.count(queryWrapper);

        return Kv.create().set("questionNumber", questionNumber)
                .set("testPaperInfoNumber", testPaperInfoNumber)
                .set("examInfoData", examInfoService.selectExamInfoData())
                .set("toBeCorrectedExamNumber", toBeCorrectedExamNumber)
                .set("studentNumber", studentNumber);
    }



   /* public Result getRegionInfoData() {
        List<ModelBeanMap> data = schoolService.getSchoolRegionInfo();
        return Result.success(data);
    }*/
}
