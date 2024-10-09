package com.education.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.education.common.annotation.Param;
import com.education.common.annotation.ParamsType;
import com.education.common.annotation.ParamsValidate;
import com.education.common.annotation.Property;
import com.education.common.interceptor.validate.Validate;
import com.education.common.interceptor.validate.ValidateManager;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.RegexUtils;
import com.education.common.utils.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * controller 层参数校验拦截器
 *   
 *
 */
@Component
public class ParamsValidateInterceptor extends BaseInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            ParamsValidate paramsValidate = handlerMethod.getMethod().getAnnotation(ParamsValidate.class);
            if (paramsValidate != null) {
                Param[] params = paramsValidate.params();
                ParamsType paramsType = paramsValidate.paramsType();
                return checkParam(request, response, params, paramsType);
            }
        }
        return true;
    }

    private boolean checkParam(HttpServletRequest request, HttpServletResponse response, Param[] params,
                               ParamsType paramsType) {
        Map dataMap = null;
        boolean isJsonData = false;

        if (paramsType == ParamsType.JSON_DATA) {
            String data = readData(request);
            dataMap = JSONObject.parseObject(data);
            isJsonData = true;
        }
        //ValidateManager validateManager = ValidateManager.build();
        for (Param param : params) {
            String name = param.name();
            Object value = null;
            if (paramsType == ParamsType.FORM_DATA) {
                value = request.getParameter(name);
            } else if (isJsonData && dataMap != null) {
                value = dataMap.get(name);
            }
          /*  List<Validate> validateList = validateManager.getValidateList();
            for (Validate validate: validateList) {
                if (validate.supportParamType(value)) {
                    validate.setParam(param);
                    validate.validateParam(request, response, param.errorCode(), param.message(), value);
                }
            }*/


            if (ObjectUtils.isEmpty(value)) {
                Result.renderJson(response, Result.fail(param.errorCode(), param.message()));
                return false;
            }

            String regexp = param.regexp();
            if (ObjectUtils.isNotEmpty(param.regexp())) {
                boolean regexpFlag = RegexUtils.compile(regexp, value);
                if (!regexpFlag) {
                    Result.renderJson(response, Result.fail(param.errorCode(), param.regexpMessage()));
                    return false;
                }
            }
        }
        return true;
    }
}
