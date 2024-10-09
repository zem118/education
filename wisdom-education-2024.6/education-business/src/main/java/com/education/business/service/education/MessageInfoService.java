package com.education.business.service.education;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.education.business.mapper.education.MessageInfoMapper;
import com.education.business.service.BaseService;
import com.education.common.constants.EnumConstants;
import com.education.common.model.PageInfo;
import com.education.model.entity.MessageInfo;
import com.education.model.request.PageParam;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageInfoService extends BaseService<MessageInfoMapper, MessageInfo> {

    public PageInfo<MessageInfo> selectList(PageParam pageParam) {
        Integer studentId = getStudentInfo().getId();
        // 将未读消息设置为已读状态
        LambdaUpdateWrapper updateWrapper = Wrappers.lambdaUpdate(MessageInfo.class)
                .set(MessageInfo::getReadFlag, EnumConstants.Flag.YES.getValue())
                .set(MessageInfo::getUpdateDate, new Date())
                .eq(MessageInfo::getStudentId, studentId);
        super.update(updateWrapper);
        return super.selectPage(pageParam, Wrappers.lambdaQuery(MessageInfo.class)
                .eq(MessageInfo::getStudentId, studentId));
    }

    public long getUnReadMessageCount() {
        return baseMapper.countUnReadMessage(getStudentInfo().getId());
    }
}
