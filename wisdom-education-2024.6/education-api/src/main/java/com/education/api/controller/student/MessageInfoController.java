package com.education.api.controller.student;

import com.education.business.service.education.MessageInfoService;
import com.education.common.base.BaseController;
import com.education.common.model.PageInfo;
import com.education.common.utils.Result;
import com.education.model.entity.MessageInfo;
import com.education.model.request.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息通知接口

 */
@RestController
@RequestMapping("/student/message")
public class MessageInfoController extends BaseController {

    @Autowired
    private MessageInfoService messageInfoService;

    @GetMapping
    public Result<PageInfo<MessageInfo>> list(PageParam pageParam) {
        return Result.success(messageInfoService.selectList(pageParam));
    }

    /**
     * 获取未读消息数量
     * @return
     */
    @GetMapping("getUnReadMessageCount")
    public Result getUnReadMessageCount() {
        return Result.success(messageInfoService.getUnReadMessageCount());
    }
}
