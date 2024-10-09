package com.education.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 学员错题本实体类
 *   
 *   
 */
@TableName("student_wrong_book")
public class StudentWrongBook extends BaseEntity<StudentWrongBook> {

    @TableField("student_id")
    private Integer studentId;
    @TableField("question_info_id")
    private Integer questionInfoId;
    @TableField("question_mark")
    private Integer questionMark;

    @TableField("student_answer")
    private String studentAnswer;

    @TableField("correct_status")
    private Integer correctStatus;

    public StudentWrongBook() {

    }

    public Integer getQuestionMark() {
        return questionMark;
    }

    public void setQuestionMark(Integer questionMark) {
        this.questionMark = questionMark;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public Integer getCorrectStatus() {
        return correctStatus;
    }

    public void setCorrectStatus(Integer correctStatus) {
        this.correctStatus = correctStatus;
    }

    public StudentWrongBook(Integer studentId, Integer questionInfoId, Integer questionMark) {
        this.studentId = studentId;
        this.questionInfoId = questionInfoId;
        this.questionMark = questionMark;
        this.createDate = new Date();
    }

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
}
