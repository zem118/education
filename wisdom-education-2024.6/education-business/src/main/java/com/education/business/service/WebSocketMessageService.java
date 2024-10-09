package com.education.business.service;

import com.education.business.task.TaskManager;
import com.education.business.task.TaskParam;
import com.education.business.task.WebSocketMessageTask;
import com.education.common.cache.CacheBean;
import com.education.common.constants.Constants;
import com.education.common.constants.EnumConstants;
import com.education.common.utils.IpUtils;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.RequestUtils;
import com.education.model.dto.AdminUserSession;
import com.education.model.dto.OnlineUserManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WebSocketMessageService {

    @Autowired
    private OnlineUserManager onlineUserManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private CacheBean redisCacheBean;

    /**
     * 校验用户是否已在线
     * @param userId
     * @param
     */
    public void checkOnlineUser(Integer userId) {
        AdminUserSession onlineUser = onlineUserManager.getOnlineUser(userId);
        if (ObjectUtils.isNotEmpty(onlineUser)) {
            String sessionId = onlineUser.getSessionId();
            onlineUserManager.removeOnlineUser(sessionId); // 移除在线用户
            redisCacheBean.remove(Constants.SESSION_KEY, sessionId); // 移除shiro session 会话
            TaskParam taskParam = new TaskParam(WebSocketMessageTask.class);
            taskParam.put("sessionId", sessionId);
            taskParam.put("message_type", EnumConstants.MessageType.STUDENT_LOGIN.getValue());
            taskParam.put("ip", IpUtils.getAddressIp(RequestUtils.getRequest()));
            taskManager.pushTask(taskParam);
        }
    }
}
