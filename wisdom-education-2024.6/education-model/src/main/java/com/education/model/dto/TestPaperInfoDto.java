package com.education.model.dto;

import com.education.model.entity.TestPaperInfo;


/**
 *   
 */
public class TestPaperInfoDto extends TestPaperInfo {

    private String subjectName;
    private String gradeInfoName;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getGradeInfoName() {
        return gradeInfoName;
    }

    public void setGradeInfoName(String gradeInfoName) {
        this.gradeInfoName = gradeInfoName;
    }
}
