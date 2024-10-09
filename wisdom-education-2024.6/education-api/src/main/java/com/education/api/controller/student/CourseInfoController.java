package com.education.api.controller.student;

import com.education.business.service.education.CourseInfoService;
import com.education.common.base.BaseController;
import com.education.common.utils.Result;
import com.education.model.entity.CourseInfo;
import com.education.model.request.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 课程管理接口
 *   

 */
@RestController("student-courseInfo")
@RequestMapping("/student/courseInfo")
public class CourseInfoController extends BaseController {

    @Autowired
    private CourseInfoService courseInfoService;

    @GetMapping
    public Result selectPageList(PageParam pageParam, CourseInfo courseInfo) {
        return Result.success(courseInfoService.selectPageList(pageParam, courseInfo));
    }
}
