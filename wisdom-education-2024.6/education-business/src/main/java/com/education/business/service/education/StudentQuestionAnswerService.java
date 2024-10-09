package com.education.business.service.education;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.education.business.mapper.education.StudentQuestionAnswerMapper;
import com.education.business.service.BaseService;
import com.education.common.constants.EnumConstants;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.ResultCode;
import com.education.model.dto.QuestionInfoAnswer;
import com.education.model.entity.StudentQuestionAnswer;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class StudentQuestionAnswerService extends BaseService<StudentQuestionAnswerMapper, StudentQuestionAnswer> {

    /**
     * 获取考试试题及学员试题答案
     * @return
     */
    public List<QuestionInfoAnswer> getQuestionAnswerByExamInfoId(Integer studentId, Integer examInfoId) {
        return baseMapper.selectQuestionAnswerList(studentId, examInfoId);
    }

    /**
     * 删除学员考试未批改的答题记录
     * @param studentId
     * @param examInfoId
     * @return
     */
    public boolean deleteByExamInfoId(Integer studentId, Integer examInfoId) {
        LambdaQueryWrapper queryWrapper = Wrappers.lambdaQuery(StudentQuestionAnswer.class)
                .eq(StudentQuestionAnswer::getStudentId, studentId)
                .eq(StudentQuestionAnswer::getCorrectStatus, EnumConstants.CorrectStatus.CORRECT_RUNNING.getValue())
                .eq(StudentQuestionAnswer::getExamInfoId, examInfoId);
        return super.remove(queryWrapper);
    }

    public StudentQuestionAnswer selectByQuestionInfoId(Integer questionInfoId) {
        LambdaQueryWrapper queryWrapper = Wrappers.<StudentQuestionAnswer>lambdaQuery()
                .eq(StudentQuestionAnswer::getQuestionInfoId, questionInfoId)
                .last(" limit 1");
        return super.getOne(queryWrapper);
    }

}
