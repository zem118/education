package com.education.api.controller.student;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.education.business.service.education.SubjectInfoService;
import com.education.common.base.BaseController;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.Result;
import com.education.model.entity.SubjectInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学生端 科目管理接口

 */
@RestController("student-subjectInfo")
@RequestMapping("/student/subjectInfo")
public class SubjectInfoController extends BaseController {

    @Autowired
    private SubjectInfoService subjectInfoService;

    /**
     * 根据年级获取科目列表
     * @param gradeInfoId
     * @return
     */
    @GetMapping("selectByGradeInfoId")
    public Result selectByGradeInfoId(Integer gradeInfoId) {
        if (ObjectUtils.isEmpty(gradeInfoId)) {
            gradeInfoId = subjectInfoService.getStudentInfo().getGradeInfoId();
        }
        LambdaQueryWrapper queryWrapper = Wrappers.<SubjectInfo>lambdaQuery()
                .eq(SubjectInfo::getGradeInfoId, gradeInfoId);
        return Result.success(subjectInfoService.list(queryWrapper));
    }

}
