package com.education.common.model; 

import cn.afterturn.easypoi.excel.annotation.Excel;
import javax.validation.constraints.NotNull;


@Deprecated
public class QuestionInfoImport extends BaseExcelModel {

    @Excel(name = "试题类型")
    @NotNull(message = "请输入试题类型")
    private String questionType; // 试题类型 (excel内容)

    @Excel(name = "试题内容")
    @NotNull(message = "试题内容不能为空")
    private String content; //试题内容

    @Excel(name = "答案")
    @NotNull(message = "请输入试题答案")
    private String answer; // 答案

    @Excel(name = "选项内容")
    private String options; // 选择题 选项（多个以逗号隔开）

    @Excel(name = "试题解析")
    private String analysis; // 试题解析

    @Excel(name = "总结升华")
    private String summarize;

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getSummarize() {
        return summarize;
    }

    public void setSummarize(String summarize) {
        this.summarize = summarize;
    }
}
