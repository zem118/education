package com.education.common.interceptor.validate;


import com.education.common.utils.ObjectUtils;
import com.education.common.utils.RegexUtils;
import com.education.common.utils.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 正则表达式校验器
 *   

 */
public class RegexValidate extends AbstractValidate {

    @Override
    public boolean supportParamType(Object paramValue) {
        return true;
    }

    @Override
    public void validateParam(HttpServletRequest request, HttpServletResponse response,
                              Integer errorCode, String errorMsg, Object paramValue) {
        String regexp = getParam().regexp();
        if (ObjectUtils.isNotEmpty(regexp)) {
            boolean regexpFlag = RegexUtils.compile(regexp, paramValue);
            if (!regexpFlag) {
                Result.renderJson(response, Result.fail(errorCode, errorMsg));
            }
        }
    }
}
