package com.education.api.controller.student;

import com.education.business.service.education.StudentWrongBookService;
import com.education.common.base.BaseController;
import com.education.common.model.PageInfo;
import com.education.common.utils.Result;
import com.education.model.dto.QuestionInfoAnswer;
import com.education.model.request.PageParam;
import com.education.model.request.WrongBookQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 错题本管理接口
 *   

 */
@RestController
@RequestMapping("/student/wrongBook")
public class WrongBookController extends BaseController {

    @Autowired
    private StudentWrongBookService studentWrongBookService;

    /**
     * 错题本列表
     * @param pageParam
     * @param wrongBookQuery
     * @return
     */
    @GetMapping
    public Result<PageInfo<QuestionInfoAnswer>> list(PageParam pageParam, WrongBookQuery wrongBookQuery) {
        wrongBookQuery.setStudentId(studentWrongBookService.getStudentInfo().getId());
        return Result.success(studentWrongBookService.selectPageList(pageParam, wrongBookQuery));
    }
}
