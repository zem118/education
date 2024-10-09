package com.education.model.dto;

import com.education.model.entity.LanguagePointsInfo;
import com.education.model.entity.QuestionInfo;

import java.util.List;

/**

 */
public class QuestionInfoDto extends QuestionInfo {

    private String subjectName;
    private String gradeInfoName;
    private String questionTypeName;

    private List<LanguagePointsInfo> languagePointsInfoList;

    public List<LanguagePointsInfo> getLanguagePointsInfoList() {
        return languagePointsInfoList;
    }

    public void setLanguagePointsInfoList(List<LanguagePointsInfo> languagePointsInfoList) {
        this.languagePointsInfoList = languagePointsInfoList;
    }

    /**
     * 知识点列表
     */
    private List<Integer> languagePointsInfoId;

    public void setLanguagePointsInfoId(List<Integer> languagePointsInfoId) {
        this.languagePointsInfoId = languagePointsInfoId;
    }

    public List<Integer> getLanguagePointsInfoId() {
        return languagePointsInfoId;
    }

    public void setQuestionTypeName(String questionTypeName) {
        this.questionTypeName = questionTypeName;
    }

    public String getQuestionTypeName() {
        return questionTypeName;
    }

    public void setGradeInfoName(String gradeInfoName) {
        this.gradeInfoName = gradeInfoName;
    }

    public String getGradeInfoName() {
        return gradeInfoName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
