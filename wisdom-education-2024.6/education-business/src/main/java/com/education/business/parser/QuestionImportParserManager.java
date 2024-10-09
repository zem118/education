package com.education.business.parser;

import com.education.common.constants.EnumConstants;

/**
 * 试题解析管理器
 *   

 */
public class QuestionImportParserManager {

    private QuestionImportParser excelQuestionParser;

    private static final QuestionImportParserManager me = new QuestionImportParserManager();

    public static QuestionImportParserManager build() {
        return me;
    }

    public void setExcelQuestionParser(QuestionImportParser excelQuestionParser) {
        this.excelQuestionParser = excelQuestionParser;
    }

    public QuestionImportParser getExcelQuestionParser() {
        return excelQuestionParser;
    }

    private QuestionImportParserManager() {

    }

    public QuestionImportParser createExcelQuestionParser(Integer questionType) {
        if (this.getExcelQuestionParser() != null) {
            return getExcelQuestionParser();
        }

        if (questionType == EnumConstants.QuestionType.SINGLE_QUESTION.getValue()
           || questionType == EnumConstants.QuestionType.MULTIPLE_QUESTION.getValue()) {
            return new SelectQuestionParser();
        } else if (questionType == EnumConstants.QuestionType.JUDGMENT_QUESTION.getValue()) {
            return new JudgmentQuestionParser();
        } else if (questionType == EnumConstants.QuestionType.FILL_QUESTION.getValue()) {
            return new ExerciseSubjectiveQuestionParser();
        }
        return new DefaultQuestionParser();
    }
}
