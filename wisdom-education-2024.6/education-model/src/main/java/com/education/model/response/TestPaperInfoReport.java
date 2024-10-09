package com.education.model.response;


import com.education.model.dto.TestPaperInfoDto;

/**

 */
public class TestPaperInfoReport extends TestPaperInfoDto {

    private float avgSource; // 试卷平均分

    public float getAvgSource() {
        return avgSource;
    }

    public void setAvgSource(float avgSource) {
        this.avgSource = avgSource;
    }
}
