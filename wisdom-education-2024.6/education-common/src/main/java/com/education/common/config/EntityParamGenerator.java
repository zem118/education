package com.education.common.config;

import com.education.common.utils.Md5Utils;
import com.jfinal.json.Jackson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;


@Component
public class EntityParamGenerator implements KeyGenerator {

    @Autowired
    private KeyGenerator keyGenerator;

    private final Jackson jackJson = new Jackson();

    @Override
    public Object generate(Object target, Method method, Object... params) {
        if (params.length >= 1) {
            String methodName = method.getName();
            params[params.length - 1] = methodName;
            String key = jackJson.toJson(params);
            return Md5Utils.getMd5(key);
        }
        return keyGenerator.generate(target, method, params);
    }
}
