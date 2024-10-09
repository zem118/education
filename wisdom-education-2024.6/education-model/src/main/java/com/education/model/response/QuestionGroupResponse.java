package com.education.model.response;


import java.util.List;

/**
 *   
 *   
 */
public class QuestionGroupResponse {

    private List<QuestionGroupItemResponse> questionGroupItemResponseList;
    private long totalQuestion;

    public long getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(long totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public List<QuestionGroupItemResponse> getQuestionGroupItemResponseList() {
        return questionGroupItemResponseList;
    }

    public void setQuestionGroupItemResponseList(List<QuestionGroupItemResponse> questionGroupItemResponseList) {
        this.questionGroupItemResponseList = questionGroupItemResponseList;
    }
}
