package com.education.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *  
 */
@TableName("student_question_answer")
public class StudentQuestionAnswer extends BaseEntity<StudentQuestionAnswer> {

    @TableField("question_info_id")
    private Integer questionInfoId;

    @TableField("student_id")
    private Integer studentId;

    @TableField("student_answer")
    private String studentAnswer;

    @TableField("exam_info_id")
    private Integer examInfoId;

    private Integer mark;

    @TableField("question_points")
    private Integer questionPoints;

    private String comment;

    @TableField("correct_status")
    private Integer correctStatus;

    public Integer getQuestionInfoId() {
        return questionInfoId;
    }

    public void setQuestionInfoId(Integer questionInfoId) {
        this.questionInfoId = questionInfoId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public Integer getQuestionPoints() {
        return questionPoints;
    }

    public Integer getExamInfoId() {
        return examInfoId;
    }

    public void setExamInfoId(Integer examInfoId) {
        this.examInfoId = examInfoId;
    }

    public void setQuestionPoints(Integer questionPoints) {
        this.questionPoints = questionPoints;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getCorrectStatus() {
        return correctStatus;
    }

    public void setCorrectStatus(Integer correctStatus) {
        this.correctStatus = correctStatus;
    }
}
