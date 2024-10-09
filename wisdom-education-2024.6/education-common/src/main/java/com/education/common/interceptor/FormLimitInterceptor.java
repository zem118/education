package com.education.common.interceptor;

import com.education.common.annotation.FormLimit;
import com.education.common.utils.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 基于redis表单重复提交校验拦截器
 *   
 *   
 * @since 1.0.0
 */
@Component
public class FormLimitInterceptor extends BaseInterceptor {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String target = RequestUtils.getRequestUrl(request); // 获取请求的url地址
        String ip = IpUtils.getAddressIp(request); // 获取ip地址
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            FormLimit formLimit = handlerMethod.getMethod().getAnnotation(FormLimit.class);
            if (ObjectUtils.isNotEmpty(formLimit)) {
                int timeOut = formLimit.timeOut();
                String formKey = ip + ":" + target;
                boolean flag = redisTemplate.opsForValue().setIfAbsent(formKey, System.currentTimeMillis(), timeOut, TimeUnit.SECONDS);
                if (!flag) {
                    String message = formLimit.message();
                    if (ObjectUtils.isNotEmpty(message)) {
                        Result.renderJson(response, Result.fail(formLimit.errorCode(), message));
                    }
                    return false;
                }
            }
        }
        return true;
    }
}
