package com.education.model.dto;

import com.education.common.utils.ObjectUtils;
import com.education.model.entity.SystemAdmin;
import com.education.model.entity.SystemRole;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *   

 */
public class AdminRoleDto extends SystemAdmin {

    private List<SystemRole> systemRoleList;
    private Set<Integer> roleIds;
    private String newPassword; // 新密码
    private String confirmPassword; // 确认密码

    private String schoolName;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public List<SystemRole> getSystemRoleList() {
        return systemRoleList;
    }

    public void setSystemRoleList(List<SystemRole> systemRoleList) {
        this.systemRoleList = systemRoleList;
    }

    public void setRoleIds(Set<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public Set<Integer> getRoleIds() {
        if (ObjectUtils.isNotEmpty(systemRoleList)) {
            return systemRoleList.stream().map(SystemRole::getId).collect(Collectors.toSet());
        }
        else if (ObjectUtils.isEmpty(this.roleIds)) {
            return new HashSet<>();
        }
        return this.roleIds;
    }
}
