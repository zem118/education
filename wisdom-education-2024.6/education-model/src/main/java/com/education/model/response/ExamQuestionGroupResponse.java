package com.education.model.response;

import com.education.model.dto.StudentExamInfoDto;

/**
 *   
 *   

 */
public class ExamQuestionGroupResponse extends QuestionGroupResponse {

    private StudentExamInfoDto studentExamInfoDto;

    public StudentExamInfoDto getStudentExamInfoDto() {
        return studentExamInfoDto;
    }

    public void setStudentExamInfoDto(StudentExamInfoDto studentExamInfoDto) {
        this.studentExamInfoDto = studentExamInfoDto;
    }
}
