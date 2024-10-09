package com.education.business.correct;

import com.education.model.entity.ExamInfo;
import com.education.model.entity.QuestionInfo;
import com.education.model.request.QuestionAnswer;
import com.education.model.request.StudentQuestionRequest;

import java.util.List;


/**

 */
public abstract class QuestionCorrect {

    private StudentQuestionRequest studentQuestionRequest;
    private ExamInfo examInfo;

    private int rightQuestionNumber; // 答对题数
    private int errorQuestionNumber; // 答错题数
    private int questionNumber; // 试题总数

    public QuestionCorrect(StudentQuestionRequest studentQuestionRequest) {
        this.questionNumber = studentQuestionRequest.getQuestionAnswerList().size();
    }

    public void correctStudentQuestion() {
        doCorrectStudentQuestion(studentQuestionRequest.getQuestionAnswerList());
    }

    abstract void doCorrectStudentQuestion(List<QuestionAnswer> questionInfoList);
}
