package com.education.model.dto;

import com.education.model.entity.QuestionInfo;

/**

 */
public class TxtQuestionInfo extends QuestionInfo {

    private String errorMsg;

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
