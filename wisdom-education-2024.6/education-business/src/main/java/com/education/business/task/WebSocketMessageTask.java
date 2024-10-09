package com.education.business.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.education.business.service.education.MessageInfoService;
import com.education.business.service.education.TestPaperInfoService;
import com.education.business.webSocket.SystemWebSocketHandler;
import com.education.common.constants.EnumConstants;
import com.education.common.utils.IpUtils;
import com.education.common.utils.ResultCode;
import com.education.model.entity.MessageInfo;
import com.education.model.entity.TestPaperInfo;
import com.jfinal.kit.Kv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * websocket 异步发送消息通知
 *   

 */
@Component
@Slf4j
public class WebSocketMessageTask implements TaskListener {

    @Autowired
    private SystemWebSocketHandler systemWebSocketHandler;
    @Autowired
    private TestPaperInfoService testPaperInfoService;
    @Autowired
    private MessageInfoService messageInfoService;

    @Override
    public void onMessage(TaskParam taskParam) {
        try {
            Thread.sleep(5000); // 休眠5秒后在发送消息到前端
            Integer messageType = taskParam.getInt("message_type");
            String content = "";
            if (messageType == EnumConstants.MessageType.STUDENT_LOGIN.getValue()) {
                String ip = taskParam.getStr("ip");
                String address = IpUtils.getIpAddress(ip);
                content = "您的账号已在" + address + "登录，" +
                        "5秒后将自动下线，如非本人操作请重新登录并及时修改密码";
            } else if (messageType == EnumConstants.MessageType.EXAM_CORRECT.getValue()) {
               this.publishExamMessage(taskParam);
            }

            Kv message = Kv.create().set("code", ResultCode.SUCCESS)
                    .set("message_type", messageType)
                    .set("content", content);
            String sessionId = taskParam.getStr("sessionId");
            systemWebSocketHandler.sendMessageToPage(sessionId, JSONObject.toJSONString(message));
        } catch (Exception e) {
            log.error("websocket消息发送异常", e);
        }
    }

    private void publishExamMessage(TaskParam taskParam) {
        TestPaperInfo testPaperInfo = testPaperInfoService.getOne(Wrappers.lambdaQuery(TestPaperInfo.class)
                .select(TestPaperInfo::getName)
                .eq(TestPaperInfo::getId, taskParam.getInt("testPaperInfoId")));

        MessageInfo messageInfo = new MessageInfo();
        String content = "您参加的考试【" + testPaperInfo.getName() + "】已被管理员批改,赶紧去查看吧!";
        messageInfo.setContent(content);
        messageInfo.setStudentId(taskParam.getInt("studentId"));
        messageInfo.setMessageType(EnumConstants.MessageType.EXAM_CORRECT.getValue());
        messageInfo.setCreateDate(new Date());
        messageInfoService.save(messageInfo);
    }
}
