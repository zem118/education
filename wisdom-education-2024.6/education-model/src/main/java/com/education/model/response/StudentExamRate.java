package com.education.model.response;

/**
 *   

 */
public class StudentExamRate {

    private Integer answerQuestionCount = 0; // 已答题
    private Integer questionCount;
    private Integer testPaperInfoId;

    private Integer studentId;

    public Integer getAnswerQuestionCount() {
        return answerQuestionCount;
    }

    public void setAnswerQuestionCount(Integer answerQuestionCount) {
        this.answerQuestionCount = answerQuestionCount;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public Integer getTestPaperInfoId() {
        return testPaperInfoId;
    }

    public void setTestPaperInfoId(Integer testPaperInfoId) {
        this.testPaperInfoId = testPaperInfoId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
}
