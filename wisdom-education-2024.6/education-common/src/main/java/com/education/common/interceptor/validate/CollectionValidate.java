package com.education.common.interceptor.validate;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

/**
 * 数组类型校验

 */
public class CollectionValidate extends AbstractValidate {

    private PropertyValidate propertyValidate = new PropertyValidate();

    @Override
    public boolean supportParamType(Object paramValue) {
        return paramValue instanceof Collection;
    }

    @Override
    public void validateParam(HttpServletRequest request, HttpServletResponse response, Integer errorCode, String errorMsg, Object paramValue) {
        Collection<Map> collection = (Collection) paramValue;
        collection.forEach(item -> {
            propertyValidate.validateParam(request, response, errorCode, errorMsg, item);
        });
    }
}
