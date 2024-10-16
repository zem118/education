package com.education.business.mapper.education;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.education.model.entity.MessageInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**

 */
public interface MessageInfoMapper extends BaseMapper<MessageInfo> {

    @Select("select count(0) number from message_info where read_flag = 0 and student_id = #{studentId}")
    long countUnReadMessage(@Param("studentId") Integer studentId);
}
