package com.education.model.dto;

import com.education.common.constants.EnumConstants;
import com.education.common.utils.ObjectUtils;
import com.education.model.entity.QuestionInfo;

/**
 *   

 */
public class QuestionInfoAnswer extends QuestionInfo {

    private String studentAnswer;
    private Integer studentMark;

    private String subjectName;
    private String questionTypeName;
    private int questionMark;
    private Integer correctStatus;

    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getQuestionTypeName() {
        if (ObjectUtils.isNotEmpty(this.getQuestionType())) {
            return EnumConstants.QuestionType.getName(this.getQuestionType());
        }
        return questionTypeName;
    }

    public void setQuestionTypeName(String questionTypeName) {
        this.questionTypeName = questionTypeName;
    }

    public void setQuestionMark(int questionMark) {
        this.questionMark = questionMark;
    }

    public int getQuestionMark() {
        return questionMark;
    }

    public void setCorrectStatus(Integer correctStatus) {
        this.correctStatus = correctStatus;
    }

    public Integer getCorrectStatus() {
        return correctStatus;
    }

    public void setStudentMark(Integer studentMark) {
        this.studentMark = studentMark;
    }

    public Integer getStudentMark() {
        return studentMark;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }
}
