package com.education.model.request;

import com.education.model.entity.QuestionInfo;

import java.util.List;

/**
 *   
 * 
 */
public class QuestionInfoQuery extends QuestionInfo {

    private List<Integer> questionIds;

    public void setQuestionIds(List<Integer> questionIds) {
        this.questionIds = questionIds;
    }

    public List<Integer> getQuestionIds() {
        return questionIds;
    }
}
