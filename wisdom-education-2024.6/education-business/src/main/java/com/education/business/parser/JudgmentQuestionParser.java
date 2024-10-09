package com.education.business.parser;


/**
 *   
 
 */
public class JudgmentQuestionParser extends AbstractExcelQuestionParser {

    private static final int RIGHT = 1;
    private static final int ERROR = 0;

    @Override
    public String parseAnswerText(String answer) {
        if ("对".equals(answer)) {
            return String.valueOf(RIGHT);
        }
        return String.valueOf(ERROR);
    }
}
