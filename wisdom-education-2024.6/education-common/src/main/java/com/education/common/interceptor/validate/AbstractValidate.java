package com.education.common.interceptor.validate;


import com.education.common.annotation.Param;

/**
 *   
 *   
 *      9:56
 */
public abstract class AbstractValidate implements Validate {

    private Param param;

    public void setParam(Param param) {
        this.param = param;
    }

    public Param getParam() {
        return param;
    }
}
