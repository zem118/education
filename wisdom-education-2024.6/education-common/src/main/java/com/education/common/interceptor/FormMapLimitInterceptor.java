package com.education.common.interceptor;

import com.education.common.annotation.FormLimit;
import com.education.common.utils.IpUtils;
import com.education.common.utils.RequestUtils;
import org.springframework.web.method.HandlerMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class FormMapLimitInterceptor extends BaseInterceptor {

    private static final ConcurrentHashMap<String, Token> lockMap = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Date nowDate = new Date();
        String ip = IpUtils.getAddressIp(request); // 获取请求的url地址
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            FormLimit formLimit = handlerMethod.getMethod().getAnnotation(FormLimit.class);
            int timeOut = formLimit.timeOut();
            String formKey = ip + ":" + RequestUtils.getRequestUrl();
            Token token = lockMap.get(formKey);
            if (token != null) {
                long time = nowDate.getTime() - token.getDate().getTime(); // 如果当前时间与上一次提交表单的间隔时间小于 5 秒视为重复提交
                if (time <= timeOut * 1000) {
                   /* String message = formLimit.message();
                    if (ObjectUtils.isNotEmpty(message)) {
                        renderJson(response, Result.fail(formLimit.errorCode(), message));
                    } */
                    return false;
                } else {
                    lockMap.remove(formKey); // 否则移除这个key，因为下面使用的是putIfAbsent ，所以会导致key无法设置成功
                }
            }
            token = new Token(formKey, nowDate);
            Token oldToken = lockMap.putIfAbsent(formKey, token); // 如果key 已存在，则返回原来的值
            // 并发情况下判断下token 是否为null  如果是并发情况的话，只有一个线程能够成功put token
            if (oldToken != null) {
                // 所以此处需要判断 获取的 oldToke 是否为新创建的对象token ， 如果不是新设置的token, 则视为同一时刻请求当前接口
                if (oldToken.getDate().getTime() != token.getDate().getTime()) {
                  /*  String message = formLimit.message();
                    if (ObjectUtils.isNotEmpty(message)) {
                        renderJson(response, Result.fail(formLimit.errorCode(), message));
                    } */
                    return false;
                }
            }
        }
        return true;
    }

    class Token {
        private String key;
        private Date date;

        public String getKey() {
            return key;
        }

        public Date getDate() {
            return date;
        }

        public Token(String key, Date date) {
            this.key = key;
            this.date = date;
        }
    }
}
