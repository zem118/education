package com.education.model.dto;

import com.education.model.entity.CourseInfo;

/**
 *   
 *   

 */
public class CourseInfoDto extends CourseInfo {

    private String subjectName;
    private String gradeInfoName;


    public String getGradeInfoName() {
        return gradeInfoName;
    }

    public void setGradeInfoName(String gradeInfoName) {
        this.gradeInfoName = gradeInfoName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
