package com.education.api.controller.admin.education;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.education.business.service.education.StudentInfoService;
import com.education.common.base.BaseController;
import com.education.common.model.ExcelResult;
import com.education.common.model.StudentInfoImport;
import com.education.common.utils.*;
import com.education.model.dto.StudentInfoDto;
import com.education.model.entity.StudentInfo;
import com.education.model.request.PageParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.List;

/**
 * 学员管理

 */
@RequestMapping("/system/student")
@RestController
public class StudentInfoController extends BaseController {

    @Autowired
    private StudentInfoService studentInfoService;

    /**
     * 学员分页列表
     * @param pageParam
     * @param studentInfo
     * @return
     */
    @GetMapping
    @RequiresPermissions("system:student:list")
    public Result selectPage(PageParam pageParam, StudentInfo studentInfo) {
        return Result.success(studentInfoService.selectPageList(pageParam, studentInfo));
    }

    /**
     * 添加或修改学员
     * @param studentInfoDto
     * @return
     */
    @PostMapping
    @RequiresPermissions(value = {"system:student:save", "system:student:update"}, logical = Logical.OR)
    public Result saveOrUpdate(@RequestBody StudentInfoDto studentInfoDto) {
        if (ObjectUtils.isEmpty(studentInfoDto.getId()) && !checkPassword(studentInfoDto)) {
            return Result.fail(ResultCode.FAIL, "密码与确认密码不一致");
        }
        studentInfoService.saveOrUpdate(studentInfoDto);
        return Result.success();
    }

    private boolean checkPassword(StudentInfoDto studentInfoDto) {
        String password = studentInfoDto.getPassword();
        String confirmPassword = studentInfoDto.getConfirmPassword();
        if (!password.equals(confirmPassword)) {
            return false;
        }
        return true;
    }

    /**
     * 重置学员密码
     * @param studentInfoDto
     * @return
     */
    @PostMapping("updatePassword")
    @RequiresPermissions("system:student:updatePassword")
    public Result updatePassword(@RequestBody StudentInfoDto studentInfoDto) {
        if (!checkPassword(studentInfoDto)) {
            return Result.fail(ResultCode.FAIL, "密码与确认密码不一致");
        }
        studentInfoService.updatePassword(studentInfoDto);
        return Result.success();
    }

    /**
     * 删除学员
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @RequiresPermissions("system:student:deleteById")
    public Result deleteById(@PathVariable Integer id) {
        studentInfoService.removeById(id);
        return Result.success();
    }

    /**
     * excel 导入学员
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping("importStudent")
    @RequiresPermissions("system:student:import")
    public Result importStudent(@RequestParam MultipartFile file) throws Exception {
        if (!excelTypes.contains(file.getContentType())) {
            return Result.fail(ResultCode.FAIL, "只能导入excel文件");
        }

        InputStream inputStream = file.getInputStream();
        ImportParams importParams = new ImportParams();
        importParams.setNeedVerfiy(true);
        importParams.setSaveUrl(baseUploadPath + "/image"); // 设置头像上传路径

        ExcelResult excelResult = ExcelUtils.importExcel(inputStream,
                StudentInfoImport.class, importParams, "/student/importExcelError/");
        int successCount = studentInfoService.importStudentFromExcel(excelResult.getExcelImportResult().getList());

        List<StudentInfoImport> studentInfoImportList = excelResult.getExcelImportResult().getFailList();

        int failCount = 0;
        if (studentInfoImportList != null) {
            failCount = studentInfoImportList.size();
        }

        String msg = successCount + "名学员数据导入成功";
        if (failCount > 0) {
            msg += failCount + "名学员数据导入失败(分别为)" + excelResult.getErrorMsg();
            return Result.success(ResultCode.EXCEL_VERFIY_FAIL, msg, excelResult.getErrorExcelUrl());
        }
        return Result.success(ResultCode.SUCCESS, msg);
    }
}
