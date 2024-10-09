package com.education.business.parser;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.afterturn.easypoi.util.PoiValidationUtil;
import com.education.common.constants.EnumConstants;
import com.education.common.model.ExcelResult;
import com.education.common.utils.*;
import com.education.model.entity.QuestionInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;


public class ExcelQuestionImportResult extends QuestionImportResult {

    private final Logger logger = LoggerFactory.getLogger(ExcelQuestionImportResult.class);

    public ExcelQuestionImportResult(InputStream inputStream) {
        super(inputStream);
    }

    public ExcelQuestionImportResult(MultipartFile file, HttpServletResponse response) throws IOException {
        super(file, response);
    }

    public ExcelQuestionImportResult(MultipartFile file) throws IOException {
        super(file);
    }

    @Override
    public void readTemplate() {
        try {
            ImportParams importParams = new ImportParams();
            importParams.setNeedVerfiy(true); // 设置需要校验
            // 设置excel文件路径
            importParams.setSaveUrl(FileUtils.getUploadPath() + importParams.getSaveUrl());
            importParams.setVerifyHandler(new IExcelVerifyHandler<QuestionInfo>() {
                @Override
                public ExcelVerifyHandlerResult verifyHandler(QuestionInfo questionInfo) {
                    ExcelVerifyHandlerResult verifyHandlerResult = new ExcelVerifyHandlerResult();

                    String errorMsg = PoiValidationUtil.validation(questionInfo, importParams.getVerfiyGroup());
                    if (StringUtils.isNotEmpty(errorMsg)) {
                        verifyHandlerResult.setMsg(errorMsg);
                        return verifyHandlerResult;
                    }

                    // 校验试题类型是否合法
                    String questionTypeName = questionInfo.getQuestionTypeName();
                    boolean flag = false;
                    for (EnumConstants.QuestionType value : EnumConstants.QuestionType.values()) {
                        if (value.equals(questionTypeName)) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                    }
                    if (!flag) {
                        verifyHandlerResult.setSuccess(false);
                        verifyHandlerResult.setMsg("试题类型不正确");
                    }
                    verifyHandlerResult.setSuccess(true);
                    return verifyHandlerResult;
                }
            });

            ExcelResult excelResult = ExcelUtils.importExcel(super.getInputStream(),
                    QuestionInfo.class, importParams, "/question/importExcelError/");
            ExcelImportResult result = excelResult.getExcelImportResult();
            if (ObjectUtils.isEmpty(result) || ObjectUtils.isEmpty(result.getList())) {
                super.setHasData(false);
                super.setErrorMsg("导入excel模板内容为空,请添加数据之后再导入");
            } else {
                super.setHasData(true);
                super.setErrorMsg(excelResult.getErrorMsg());
                super.setErrorFileUrl(excelResult.getErrorExcelUrl());
                super.setSuccessImportQuestionList(result.getList());
                super.setFailImportQuestionList(result.getFailList());
            }
        } catch (Exception e) {
            logger.error("模板数据解析异常", e);
        }
    }


   /* private void downLoadErrorExcelInfo(ExcelResult excelResult) {
        //文件输入流
        FileInputStream fis = null;
        XSSFWorkbook wb = null;
        //使用XSSFWorkbook对象读取excel文件
        try {
            fis = new FileInputStream(FileUtils.getUploadPath() + excelResult.getErrorExcelUrl());
            wb = new XSSFWorkbook(fis);
            fis.close();

            HttpServletResponse response = getResponse();
            //设置contentType为vnd.ms-excel
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("utf-8");

            response.setHeader("Content-disposition", "attachment;filename=stuTemplateExcel.xlsx");

            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }*/
}
