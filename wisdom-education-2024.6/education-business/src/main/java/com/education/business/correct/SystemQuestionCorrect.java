package com.education.business.correct;

import com.education.model.request.QuestionAnswer;
import com.education.model.request.StudentQuestionRequest;
import java.util.List;


public class SystemQuestionCorrect extends QuestionCorrect {

    public SystemQuestionCorrect(StudentQuestionRequest studentQuestionRequest) {
        super(studentQuestionRequest);
    }

    @Override
    void doCorrectStudentQuestion(List<QuestionAnswer> questionInfoList) {

    }
}
