package com.education.business.service.education;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.education.business.mapper.education.StudentWrongBookMapper;
import com.education.business.service.BaseService;
import com.education.common.constants.EnumConstants;
import com.education.common.model.PageInfo;
import com.education.model.dto.QuestionInfoAnswer;
import com.education.model.entity.StudentWrongBook;
import com.education.model.request.PageParam;
import com.education.model.request.QuestionAnswer;
import com.education.model.request.WrongBookQuery;
import org.springframework.stereotype.Service;

/**
 * 错题本管理
 *   
 *   

 */
@Service
public class StudentWrongBookService extends BaseService<StudentWrongBookMapper, StudentWrongBook> {

    public StudentWrongBook newStudentWrongBook(Integer studentId, QuestionAnswer questionAnswer) {
        StudentWrongBook studentWrongBook = new StudentWrongBook(studentId, questionAnswer.getQuestionInfoId(),
                questionAnswer.getQuestionMark());
        studentWrongBook.setStudentAnswer(questionAnswer.getStudentAnswer());
        studentWrongBook.setCorrectStatus(EnumConstants.CorrectStatus.ERROR.getValue());
        return studentWrongBook;
    }

    /**
     * 错题本列表
     * @param pageParam
     * @param wrongBookQuery
     * @return
     */
    public PageInfo<QuestionInfoAnswer> selectPageList(PageParam pageParam, WrongBookQuery wrongBookQuery) {
        Page<QuestionInfoAnswer> page = new Page<>(pageParam.getPageNumber(), pageParam.getPageSize());
        return selectPage(baseMapper.selectPageList(page, wrongBookQuery));
    }
}
