package com.education.common.config;

import com.education.common.cache.*;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class CacheBeanConfiguration {


    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        //配置redisTemplate
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置序列化
        RedisSerializer redisSerializer = new FstRedisSerializer();
        redisTemplate.setKeySerializer(new StringRedisSerializer());//key序列化
        redisTemplate.setValueSerializer(redisSerializer);//value序列化
        redisTemplate.setHashKeySerializer(redisSerializer);//Hash key序列化
        redisTemplate.setHashValueSerializer(redisSerializer);//Hash value序列化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    @Bean
    public CacheBean redisCacheBean(RedisTemplate redisTemplate) {
        return new RedisCacheBean(redisTemplate);
    }

    @Bean
    public CacheManager ehCacheManager(net.sf.ehcache.CacheManager educationCacheManager) {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(educationCacheManager);
        return ehCacheManager;
    }

    @Bean
    public net.sf.ehcache.CacheManager educationCacheManager() {
        return net.sf.ehcache.CacheManager.create(this.getClass()
                .getClassLoader()
                .getResourceAsStream("ehcache.xml"));
    }


    @Bean
    public CacheBean ehcacheBean(net.sf.ehcache.CacheManager cacheManager) {
        return new EhcacheBean(cacheManager);
    }

}
