package com.education.api.controller.admin;

import com.education.business.service.WebSocketMessageService;
import com.education.business.service.system.SystemAdminService;
import com.education.business.task.TaskManager;
import com.education.business.task.TaskParam;
import com.education.business.task.UserLoginSuccessListener;
import com.education.common.annotation.*;
import com.education.common.base.BaseController;
import com.education.common.constants.Constants;
import com.education.common.model.JwtToken;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.RequestUtils;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.model.dto.AdminUserSession;
import com.education.model.dto.OnlineUserManager;
import com.education.model.request.UserLoginRequest;
import com.jfinal.kit.Kv;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * 系统登录
 *   
 *   

 */
@RestController
@RequestMapping("/system")
@Api(value = "管理员登录登出接口", tags = "系统登录登出接口")
public class LoginController extends BaseController {

    @Autowired
    private SystemAdminService systemAdminService;
    @Autowired
    private JwtToken adminJwtToken;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private WebSocketMessageService webSocketMessageService;
    @Autowired
    private OnlineUserManager onlineUserManager;


    /**
     * 管理员登录接口
     * @param userLoginRequest
     * @return
     */
    @PostMapping("/login")
    @SystemLog(describe = "登录管理系统")
    @ParamsValidate(params = {
        @Param(name = "userName", message = "请输入用户名"),
        @Param(name = "password", message = "请输入密码"),
        @Param(name = "key", message = "请传递一个验证码时间戳"),
        @Param(name = "password", message = "请输入密码"),
    }, paramsType = ParamsType.JSON_DATA)
    @FormLimit
    public Result<Map> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response,
                        HttpServletRequest request) {
        String codeKey = userLoginRequest.getKey();
        String imageCode = userLoginRequest.getCode();
        String cacheCode = cacheBean.get(codeKey);
        if (!imageCode.equalsIgnoreCase(cacheCode)) {
            return Result.fail(ResultCode.CODE_ERROR, "验证码输入错误");
        }
        Result result = systemAdminService.login(userLoginRequest.getUserName(), userLoginRequest.getPassword());

        if (result.isSuccess()) {
            Integer adminUserId = systemAdminService.getAdminUserId();
            String token = adminJwtToken.createToken(adminUserId, Constants.SESSION_TIME_OUT_MILLISECONDS); // 默认缓存5天
            AdminUserSession userSession = systemAdminService.getAdminUserSession();
            systemAdminService.loadUserMenuAndPermission(userSession);
            String sessionId = request.getSession().getId();
            userSession.setSessionId(sessionId);
            userSession.setToken(token);
            synchronized (this) { // 防止相同账号并发登录, 并发登录情况可能造成相同账号同时在线
                // 分布式情况下建议使用redission分布式锁
                webSocketMessageService.checkOnlineUser(adminUserId);
                onlineUserManager.addOnlineUser(sessionId, userSession,
                        new Long(Constants.SESSION_TIME_OUT).intValue());
            }

            // 是否记住密码登录
            boolean rememberMe = userLoginRequest.isChecked();
            if (rememberMe) {
                // 先删除JSESSIONID
                Cookie cookie = RequestUtils.getCookie(Constants.DEFAULT_SESSION_ID);
                if (ObjectUtils.isNotEmpty(cookie)) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
                // 重新创建JSESSIONID 并设置过期时间, 默认过期时间为5天
                RequestUtils.createCookie(Constants.DEFAULT_SESSION_ID, request.getSession().getId(), Constants.SESSION_TIME_OUT);
            }

            Kv userInfo = Kv.create().set("token", token).set("id", userSession.getAdminId())
                    .set("permissionList", userSession.getPermissionList())
                    .set("menuList", userSession.getMenuTreeList())
                    .set("login_name", userSession.getSystemAdmin().getLoginName());
            Map resultMap = new HashMap<>();
            resultMap.put("userInfo", userInfo);
            // 异步更新用户相关信息
            TaskParam taskParam = new TaskParam(UserLoginSuccessListener.class);
            taskParam.put("userSession", userSession);
            taskParam.put("request", RequestUtils.getRequest());
            taskManager.pushTask(taskParam);
            // 更新shiro 用户信息，避免与redis 缓存中用户信息不一致问题
            systemAdminService.updateShiroCacheUserInfo(userSession);
            result.setData(resultMap);
        }
        return result;
    }



    /**
     * 系统退出
     * @return
     */
    @PostMapping("logout")
    @ApiOperation(value="系统退出接口", notes="用户退出接口")
    @SystemLog(describe = "退出管理系统")
    @FormLimit
    public Result logout() {
        onlineUserManager.removeOnlineUser(systemAdminService.getAdminUserSession().getSessionId());
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return Result.success(ResultCode.SUCCESS, "退出成功");
    }

    @GetMapping("unAuth")
    public Result unAuth() {
        return Result.success(ResultCode.UN_AUTH_ERROR_CODE, "用户未认证");
    }
}
