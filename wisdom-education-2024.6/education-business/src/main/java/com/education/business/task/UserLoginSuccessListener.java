package com.education.business.task;

import com.education.business.service.system.SystemAdminService;
import com.education.common.utils.IpUtils;
import com.education.model.dto.AdminUserSession;
import com.education.model.entity.SystemAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * 异步监听用户登录成功消息推送
 */
@Component
public class UserLoginSuccessListener implements TaskListener {

    @Autowired
    private SystemAdminService systemAdminService;

    @Override
    public void onMessage(TaskParam taskParam) {
        AdminUserSession userSession = (AdminUserSession) taskParam.get("userSession");
        HttpServletRequest request = (HttpServletRequest) taskParam.get("request");
        // 更新用户登录信息
        this.updateUserInfo(userSession, request);
    }

    private void updateUserInfo(AdminUserSession userSession, HttpServletRequest request) {
        // 更新用户登录信息
        SystemAdmin systemAdmin = userSession.getSystemAdmin();
        Integer count = systemAdmin.getLoginCount();
        systemAdmin.setLoginCount(count + 1);
        String ip = IpUtils.getAddressIp(request);
        systemAdmin.setLoginIp(ip);
        String ipAddress = IpUtils.getIpAddress(ip);
        systemAdmin.setIpAddress(ipAddress);
        systemAdmin.setLastLoginTime(new Date());
        systemAdminService.updateById(systemAdmin);
    }
}
