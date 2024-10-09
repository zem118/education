package com.education.business.correct;

import com.education.common.constants.EnumConstants;

public class QuestionCorrectManager {

    private static final QuestionCorrectManager questionCorrectManager = new QuestionCorrectManager();

    public static QuestionCorrectManager build(Integer correctType) {
        return questionCorrectManager;
    }

    public QuestionCorrect createQuestionCorrect(Integer correctType) {
        if (correctType == EnumConstants.CorrectType.SYSTEM.getValue()) {
            return null;
        }
        return null;
    }
}
