package com.education.model.dto;

import com.education.common.cache.CacheBean;
import com.education.common.utils.ObjectUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 在线用户管理器
 *   
 * 
 */
@Component
public final class OnlineUserManager {
    @Resource(name = "redisCacheBean")
    private CacheBean cacheBean;
    // 用户id 集合
    public static final String USER_ID_CACHE = "user:id:cache";

    /**
     * 添加用户 （用户id 相同直接覆盖之前的登录用户信息）
     * @param adminUserSession
     */

    public void addOnlineUser(String sessionId, AdminUserSession adminUserSession, int liveSeconds) {
        cacheBean.put(USER_ID_CACHE, sessionId, adminUserSession, liveSeconds);
    }

    public void removeOnlineUser(String sessionId) {
        cacheBean.remove(USER_ID_CACHE, sessionId);
    }

    public AdminUserSession getOnlineUser(String cacheKey) {
        return cacheBean.get(cacheKey);
    }

    public AdminUserSession getOnlineUser(Integer userId) {
        Set<String> sessionIdList = (Set<String>) cacheBean.getKeys(USER_ID_CACHE);
        for (String sessionId : sessionIdList) {
            AdminUserSession adminUserSession = getOnlineUser(sessionId);
            if (adminUserSession != null && adminUserSession.getAdminId().intValue() == userId.intValue()) {
                return adminUserSession;
            }
        }
        return null;
    }

    /**
     * 删除所有用户
     */
    public void clearOnlineUser() {
        this.cacheBean.remove(USER_ID_CACHE);
    }
}
