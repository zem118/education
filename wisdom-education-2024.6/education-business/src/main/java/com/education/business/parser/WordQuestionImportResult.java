package com.education.business.parser;

import java.io.InputStream;

/**
 * word 模板试题导入

 */
public class WordQuestionImportResult extends QuestionImportResult {

    public WordQuestionImportResult(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public void readTemplate() {
        throw new RuntimeException("Word Template Import is Not Finish");
    }
}
