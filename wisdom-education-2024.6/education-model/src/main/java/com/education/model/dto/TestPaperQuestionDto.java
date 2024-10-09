package com.education.model.dto;

import com.education.common.constants.EnumConstants;
import com.education.model.entity.TestPaperQuestionInfo;

/**
 *   
 */
public class TestPaperQuestionDto extends TestPaperQuestionInfo {

    private Integer questionType;
    private String content;
    private String options;
    private String answer;
    private Integer updateType; // 更新字段
    private String questionTypeName;

    public String getQuestionTypeName() {
        if (questionType != null) {
            return EnumConstants.QuestionType.getName(this.questionType);
        }
        return questionTypeName;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getOptions() {
        return options;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setUpdateType(Integer updateType) {
        this.updateType = updateType;
    }

    public Integer getUpdateType() {
        return updateType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public Integer getQuestionType() {
        return questionType;
    }
}
