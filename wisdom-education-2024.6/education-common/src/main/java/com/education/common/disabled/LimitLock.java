package com.education.common.disabled;


import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LimitLock {

    /**
     * 标识 指定sec时间段内的访问次数限制
     * @return
     */
    int limit() default 5;

    /**
     * 标识 时间段
     * @return
     */
    int sec() default 5;

    /**
     * 限流类型
     * @return
     */
    LimitType limitType() default LimitType.URL_KEY;

    /**
     * 限流消息提示
     * @return
     */
    String message() default "操作过于频繁，先休息一会吧";
}
