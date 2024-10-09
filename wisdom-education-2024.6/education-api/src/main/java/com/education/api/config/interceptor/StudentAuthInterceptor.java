package com.education.api.config.interceptor;

import com.education.business.service.education.StudentInfoService;
import com.education.common.interceptor.BaseInterceptor;
import com.education.common.model.JwtToken;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.model.dto.StudentInfoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 学员端api 拦截器
 *   
 *   

 */
@Component
public class StudentAuthInterceptor extends BaseInterceptor {

    @Autowired
    private JwtToken studentJwtToken;
    @Autowired
    private StudentInfoService studentInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StudentInfoSession studentInfoSession = studentInfoService.getStudentInfoSession();
        if (ObjectUtils.isEmpty(studentInfoSession)) {
            Result.renderJson(response, Result.fail(ResultCode.UN_AUTH_ERROR_CODE));
            return false;
        }
         return checkToken(studentJwtToken, request, response);
    }
}
