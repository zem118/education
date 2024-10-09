package com.education.business.service.education;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.education.business.mapper.education.StudentInfoMapper;
import com.education.business.service.BaseService;
import com.education.common.constants.Constants;
import com.education.common.model.JwtToken;
import com.education.common.model.PageInfo;
import com.education.common.model.StudentInfoImport;
import com.education.common.utils.*;
import com.education.model.dto.StudentInfoDto;
import com.education.model.dto.StudentInfoSession;
import com.education.model.entity.GradeInfo;
import com.education.model.entity.StudentInfo;
import com.education.model.request.PageParam;
import com.education.model.request.UserLoginRequest;
import com.jfinal.kit.Kv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * 学员管理

 */
@Service
public class StudentInfoService extends BaseService<StudentInfoMapper, StudentInfo> {

    @Autowired
    private GradeInfoService gradeInfoService;
    @Autowired
    private JwtToken studentJwtToken;

    public PageInfo<StudentInfoDto> selectPageList(PageParam pageParam, StudentInfo studentInfo) {
        Page<StudentInfoDto> page = new Page<>(pageParam.getPageNumber(), pageParam.getPageSize());
        return selectPage(baseMapper.selectPageList(page, studentInfo));
    }

    @Override
    public boolean saveOrUpdate(StudentInfo studentInfo) {
        if (studentInfo.getId() == null) {
            String encrypt = Md5Utils.encodeSalt(Md5Utils.generatorKey());
            String password = Md5Utils.getMd5(studentInfo.getPassword(), encrypt); //生成默认密码
            studentInfo.setPassword(password);
            studentInfo.setEncrypt(encrypt);
        }
        return super.saveOrUpdate(studentInfo);
    }

    public void updatePassword(StudentInfoDto studentInfoDto) {
        String password = studentInfoDto.getPassword();
        String encrypt = studentInfoDto.getEncrypt();
        password = Md5Utils.getMd5(password,  encrypt);
        studentInfoDto.setPassword(password);
        super.updateById(studentInfoDto);
    }

    public int importStudentFromExcel(List<StudentInfoImport> studentList) throws Exception {
        List<GradeInfo> gradeInfoList = gradeInfoService.list();
        Map<String, Integer> gradeInfoMap = new HashMap<>();
        gradeInfoList.forEach(gradeInfo -> {
            gradeInfoMap.put(gradeInfo.getName(), gradeInfo.getId());
        });
        List<StudentInfo> studentInfoList = new ArrayList<>();
        Date now = new Date();
        for (StudentInfoImport studentInfoImport : studentList) {
            StudentInfo studentInfo = new StudentInfo();
            Integer gradeInfoId = gradeInfoMap.get(studentInfoImport.getGradeName());
            if (ObjectUtils.isEmpty(gradeInfoId)) {
                continue;
            }
            studentInfo.setMobile(studentInfoImport.getMobile());
            studentInfo.setGradeInfoId(gradeInfoId);
            studentInfo.setSex("男".equals(studentInfo.getSex()) ? ResultCode.SUCCESS : ResultCode.FAIL);
            String name = studentInfoImport.getName();
            studentInfo.setName(name);
            studentInfo.setAge(studentInfoImport.getAge());
            studentInfo.setAddress(studentInfoImport.getAddress());
            String loginName = SpellUtils.getSpellHeadChar(name); // 获取登录名
            String encrypt = Md5Utils.encodeSalt(Md5Utils.generatorKey());
            String password = Md5Utils.getMd5(loginName, encrypt); //生成默认密码
            studentInfo.setPassword(password);
            studentInfo.setEncrypt(encrypt);
            studentInfo.setLoginName(loginName);
            studentInfo.setMotherName(studentInfoImport.getMotherName());
            studentInfo.setFatherName(studentInfoImport.getFatherName());
            studentInfo.setCreateDate(now);
            studentInfoList.add(studentInfo);
        }
        super.saveBatch(studentInfoList);

        return studentInfoList.size();
    }

    public Result doLogin(UserLoginRequest userLoginRequest) {
        LambdaQueryWrapper queryWrapper = Wrappers.<StudentInfo>lambdaQuery()
                .eq(StudentInfo::getLoginName, userLoginRequest.getUserName());
        StudentInfo studentInfo = baseMapper.selectOne(queryWrapper);
        Result result = new Result(ResultCode.SUCCESS, "登录成功");
        if (ObjectUtils.isEmpty(studentInfo)) {
            return Result.fail(ResultCode.FAIL, "用户不存在");
        } else {
            if (studentInfo.isDisabledFlag()) {
                return Result.fail(ResultCode.FAIL, "账号已被禁用");
            }

            String password = userLoginRequest.getPassword();
            boolean rememberMe = userLoginRequest.isChecked(); // 是否记住密码
            long sessionTime = Constants.TWO_HOUR_SECOND; // 默认session 会话为2小时 (单位秒)
            if (rememberMe) {
                sessionTime = Constants.SESSION_TIME_OUT;
            }
            String dataBasePassword = studentInfo.getPassword();
            String encrypt = studentInfo.getEncrypt();
            if (dataBasePassword.equals(Md5Utils.getMd5(password, encrypt))) {
                String token = studentJwtToken.createToken(studentInfo.getId(), sessionTime * 1000); // 默认缓存5天
                GradeInfo gradeInfo = gradeInfoService.getById(studentInfo.getGradeInfoId());
                this.cacheStudentInfoSession(studentInfo, token, sessionTime);
                Kv kv = Kv.create().set("name", studentInfo.getName())
                        .set("token", token)
                        .set("gradeInfoId", gradeInfo.getId())
                        .set("headImg", studentInfo.getHeadImg())
                        .set("sex", studentInfo.getSex())
                        .set("age", studentInfo.getAge())
                        .set("address", studentInfo.getAddress())
                        .set("mobile", studentInfo.getMobile())
                        .set("gradeInfoName", gradeInfo.getName())
                        .set("id", studentInfo.getId());
                return Result.success(ResultCode.SUCCESS, "登录成功", kv);
            } else {
                result.setCode(ResultCode.FAIL);
                result.setMessage("用户名或密码错误");
            }
            return result;
        }
    }

    /**
     * 缓存学员登录信息
     * @param studentInfo
     * @param token
     */
    private void cacheStudentInfoSession(StudentInfo studentInfo, String token, long sessionTime) {
        StudentInfoSession studentInfoSession = new StudentInfoSession();
        studentInfoSession.setToken(token);
        studentInfoSession.setStudentInfo(studentInfo);
        int value = new Long(sessionTime).intValue();
        cacheBean.put(Constants.SESSION_NAME, token, studentInfoSession, value);
        Date now = new Date();
        RequestUtils.createCookie(Constants.SESSION_NAME, token, value);
        studentInfo.setLastLoginTime(now);
        studentInfo.setLoginIp(IpUtils.getAddressIp(RequestUtils.getRequest()));
        int loginCount = studentInfo.getLoginCount();
        studentInfo.setLoginCount(++loginCount);
        studentInfo.setUpdateDate(now);
        super.updateById(studentInfo);
    }


    public ResultCode updatePassword(String password, String newPassword, String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            return new ResultCode(ResultCode.FAIL, "新密码与确认密码不一致");
        }

        // 验证新密码是否正确
        StudentInfo studentInfo = getStudentInfo();
        String encrypt = studentInfo.getEncrypt();
        String dataBasePassword = Md5Utils.getMd5(password, encrypt);
        if (dataBasePassword.equals(password)) {
            return new ResultCode(ResultCode.FAIL, "原始密码错误");
        }

        password = Md5Utils.getMd5(newPassword, encrypt);

        LambdaUpdateWrapper updateWrapper = Wrappers.lambdaUpdate(StudentInfo.class)
                .set(StudentInfo::getPassword, password)
                .eq(StudentInfo::getId, studentInfo.getId());
        super.update(updateWrapper);
        return new ResultCode(ResultCode.SUCCESS, "密码修改成功,退出后请使用新密码进行登录");
    }

    public boolean updateInfo(StudentInfo studentInfo) {
        studentInfo.setId(getStudentInfo().getId());
        return super.updateById(studentInfo);
    }
}
