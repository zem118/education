package com.education.model.dto;

import com.education.model.entity.SubjectInfo;

/**
 * 
 */
public class SubjectInfoDto extends SubjectInfo {

    private String gradeName;


    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getGradeName() {
        return gradeName;
    }
}
