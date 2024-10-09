package com.education.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 前台学员用户信息实体类
 *   
 
 */
@Data
public class FrontUserInfoSession implements Serializable {

    private String sessionId;
    private Map userInfoMap;

    public Integer getUserId() {
        return (Integer) userInfoMap.get("id");
    }
}
