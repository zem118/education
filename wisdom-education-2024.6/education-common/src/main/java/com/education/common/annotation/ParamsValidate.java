package com.education.common.annotation;

import java.lang.annotation.*;

/**
 * 接口参数校验注解
 *   

 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamsValidate {

    Param[] params();

    ParamsType paramsType() default ParamsType.FORM_DATA; //参数类型
}