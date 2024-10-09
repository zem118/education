package com.education.common.model;

import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;

/**

 */
public class ExcelResult {

 //   private boolean success = true; // 默认excel 数据校验成功
    private String errorMsg;
    private String errorExcelUrl;

    private ExcelImportResult excelImportResult;

    public void setExcelImportResult(ExcelImportResult excelImportResult) {
        this.excelImportResult = excelImportResult;
    }

    public ExcelImportResult getExcelImportResult() {
        return excelImportResult;
    }

    public static ExcelResult build() {
        return new ExcelResult();
    }

  /*  public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
        return;
    }*/

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorExcelUrl() {
        return errorExcelUrl;
    }

    public void setErrorExcelUrl(String errorExcelUrl) {
        this.errorExcelUrl = errorExcelUrl;
    }
}
