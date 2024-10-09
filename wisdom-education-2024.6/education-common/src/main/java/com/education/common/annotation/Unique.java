package com.education.common.annotation;

import java.lang.annotation.*;

/**
 * 实体类唯一约束注解
 *   
 *   
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Unique {

    String value() default "";
}
