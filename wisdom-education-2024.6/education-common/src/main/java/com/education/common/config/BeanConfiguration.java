package com.education.common.config; 

import com.education.common.constants.Constants;
import com.education.common.model.JwtToken;
import com.education.common.model.WeChatInfo;
import com.education.common.utils.ObjectUtils;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public JwtToken studentJwtToken() {
        return new JwtToken(Constants.EDUCATION_FRONT_SECRET_KEY);
    }

    @Bean
    public JwtToken adminJwtToken() {
        return new JwtToken(Constants.EDUCATION_ADMIN_SECRET_KEY);
    }

    @Value("${weChat.appId}")
    private String appId;
    @Value("${weChat.token}")
    private String token;
    @Value("${weChat.appSecret}")
    private String appSecret;
    @Value("${spring.redis.host}")
    private String redisUrl;
    @Value("${spring.redis.port}")
    private Integer redisPort = 6379;
    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean
    public WeChatInfo weChatInfo() {
        WeChatInfo weChatInfo = new WeChatInfo();
        weChatInfo.setAppId(appId);
        weChatInfo.setToken(token);
        weChatInfo.setAppSecret(appSecret);
        return weChatInfo;
    }

    @Bean
    public ApiConfig apiConfig(WeChatInfo weChatInfo) {
        ApiConfig apiConfig = new ApiConfig();
        apiConfig.setAppId(weChatInfo.getAppId());
        apiConfig.setAppSecret(weChatInfo.getAppSecret());
        ApiConfigKit.setThreadLocalAppId(weChatInfo.getAppId());
        ApiConfigKit.putApiConfig(apiConfig);
        return apiConfig;
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress("redis://" + redisUrl + ":" + redisPort);
        if (ObjectUtils.isNotEmpty(redisPassword)) {
           serverConfig.setPassword(redisPassword);
        }
        return Redisson.create(config);
    }
}
