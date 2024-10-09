package com.education.business.task;

import com.education.business.service.system.SystemLogService;
import com.education.common.constants.EnumConstants;
import com.education.common.utils.ObjectUtils;
import com.education.model.dto.AdminUserSession;
import com.education.model.entity.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 异步记录系统日志，提升系统响应速度
 *   

 */
@Component
public class SystemLogListener implements TaskListener {


    @Autowired
    private SystemLogService systemLogService;

    @Override
    public void onMessage(TaskParam taskParam) {
        SystemLog systemLog = new SystemLog();
        AdminUserSession adminUserSession = (AdminUserSession) taskParam.get("adminUserSession");
        if (ObjectUtils.isNotEmpty(adminUserSession)) {
            systemLog.setUserId(adminUserSession.getAdminId());
            systemLog.setOperationName(adminUserSession.getSystemAdmin().getLoginName());
            systemLog.setPlatformType(EnumConstants.PlatformType.WEB_ADMIN.getValue());
        } else {
            systemLog.setOperationName("匿名用户");
        }
        systemLog.setCreateDate(new Date());
        systemLog.setPlatformType(EnumConstants.PlatformType.WEB_ADMIN.getValue());
        long startTime = taskParam.getLong("startTime");
        systemLog.setRequestTime((System.currentTimeMillis() - startTime) + "ms");
        systemLog.setRequestUrl(taskParam.getStr("request_url"));
        systemLog.setException(taskParam.getStr("exception"));
        systemLog.setIp(taskParam.getStr("ip"));
        systemLog.setMethod(taskParam.getStr("method"));
        systemLog.setParams(taskParam.getStr("params"));
        systemLog.setOperationDesc(taskParam.getStr("operation_desc"));
        systemLogService.save(systemLog);
    }
}
