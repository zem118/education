package com.education.business.service.education;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.education.business.mapper.education.ExamInfoMapper;
import com.education.business.service.BaseService;
import com.education.business.task.TaskParam;
import com.education.business.task.WebSocketMessageTask;
import com.education.common.constants.Constants;
import com.education.common.constants.EnumConstants;
import com.education.common.exception.BusinessException;
import com.education.common.model.PageInfo;
import com.education.common.utils.*;
import com.education.model.dto.ExamCount;
import com.education.model.dto.QuestionInfoAnswer;
import com.education.model.dto.StudentExamInfoDto;
import com.education.model.entity.ExamInfo;
import com.education.model.entity.StudentQuestionAnswer;
import com.education.model.entity.StudentWrongBook;
import com.education.model.entity.TestPaperInfo;
import com.education.model.request.PageParam;
import com.education.model.request.QuestionAnswer;
import com.education.model.request.StudentQuestionRequest;
import com.education.model.response.*;
import com.jfinal.kit.Kv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ExamInfoService extends BaseService<ExamInfoMapper, ExamInfo> {

    @Autowired
    private StudentQuestionAnswerService studentQuestionAnswerService;
    @Autowired
    private StudentWrongBookService studentWrongBookService;
    @Autowired
    private QuestionInfoService questionInfoService;
    @Autowired
    private TestPaperInfoService testPaperInfoService;
    @Autowired
    private ExamMonitorService examMonitorService;
    /**
     * 后台考试列表
     * @param pageParam
     * @param studentExamInfoDto
     * @return
     */
    public PageInfo<StudentExamInfoDto> selectExamInfoList(PageParam pageParam, StudentExamInfoDto studentExamInfoDto) {
        Page<StudentExamInfoDto> page = new Page(pageParam.getPageNumber(), pageParam.getPageSize());
        return selectPage(baseMapper.selectExamList(page, studentExamInfoDto));
    }


    /**
     * 获取学员考试记录列表
     * @param pageParam
     * @param studentExamInfoDto
     * @return
     */
    public PageInfo<StudentExamInfoDto> selectStudentExamInfoList(PageParam pageParam, StudentExamInfoDto studentExamInfoDto) {
        studentExamInfoDto.setStudentId(getStudentId());
        Page<StudentExamInfoDto> page = new Page(pageParam.getPageNumber(), pageParam.getPageSize());
        return selectPage(baseMapper.selectStudentExamList(page, studentExamInfoDto));
    }

    @Transactional
    public Integer commitTestPaperInfoQuestion(StudentQuestionRequest studentQuestionRequest) {
        Integer studentId = getStudentInfo().getId();
        return this.batchSaveStudentQuestionAnswer(studentQuestionRequest, studentId, new ExamInfo());
    }

    /**
     * 批量保存学员试题答案
     * @param studentQuestionRequest
     * @param studentId
     * @param examInfo
     * @return
     */
    private Integer batchSaveStudentQuestionAnswer(StudentQuestionRequest studentQuestionRequest,
                                                Integer studentId, ExamInfo examInfo) {
        Integer testPaperInfoId = studentQuestionRequest.getTestPaperInfoId();
        Date now = new Date();
        List<StudentQuestionAnswer> studentQuestionAnswerList = new ArrayList<>();
        int systemMark = 0;
       // int objectiveQuestionNumber = 0; // 客观题数量
        int subjectiveQuestionNumber = 0; // 主观题数量
        int rightNumber = 0;
        int errorNumber = 0;
        int teacherMark = 0;
        int teacherErrorNumber = 0; // 记录教师后台批改指定的错题数
        int questionNumber = studentQuestionRequest.getQuestionAnswerList().size();
        List<StudentWrongBook> studentWrongBookList = new ArrayList<>(); // 存储学员考试错题
        for (QuestionAnswer item : studentQuestionRequest.getQuestionAnswerList()) {
            StudentQuestionAnswer studentQuestionAnswer = new StudentQuestionAnswer();
            studentQuestionAnswer.setQuestionInfoId(item.getQuestionInfoId());
            studentQuestionAnswer.setStudentId(studentId);
            studentQuestionAnswer.setQuestionPoints(item.getQuestionMark());
            String studentAnswer = item.getStudentAnswer();
            // 验证试题是否为客观题 // 教师评分直接跳过客观题
            if (isObjectiveQuestion(item.getQuestionType()) && !studentQuestionRequest.isTeacherCorrectFlag()) {
                String questionAnswer = item.getAnswer().replaceAll(",", "");
                String studentAnswerProxy = null;
                String questionAnswerProxy = ObjectUtils.charSort(questionAnswer);
                if (ObjectUtils.isNotEmpty(studentAnswer)) {
                    studentAnswerProxy = ObjectUtils.charSort(studentAnswer.replaceAll(",", ""));
                }
                // 此处需要注意多选题 答案排序问题  例如前端传递过来的答案为B,A,C, 而答案为A,B,C
                if (questionAnswerProxy.equals(studentAnswerProxy)) {
                    studentQuestionAnswer.setMark(item.getQuestionMark());
                    systemMark += item.getQuestionMark();
                    rightNumber++;
                    studentQuestionAnswer.setCorrectStatus(EnumConstants.CorrectStatus.RIGHT.getValue());
                } else {
                    studentWrongBookList.add(studentWrongBookService.newStudentWrongBook(studentId, item));
                    errorNumber++;
                    studentQuestionAnswer.setCorrectStatus(EnumConstants.CorrectStatus.ERROR.getValue());
                }
              //  objectiveQuestionNumber++;
            } else {
                if (studentQuestionRequest.isTeacherCorrectFlag()) {
                    // 答案为空，系统已批改，直接跳过
                    if (ObjectUtils.isEmpty(item.getStudentAnswer())) {
                        continue;
                    }
                    studentQuestionAnswer.setMark(item.getStudentMark());
                    teacherMark += item.getStudentMark();
                    // 后台指定加入学员错题本
                    if (item.isErrorQuestionFlag()) {
                        teacherErrorNumber++; // 错题数+1
                        studentQuestionAnswer.setCorrectStatus(EnumConstants.CorrectStatus.ERROR.getValue());
                        studentWrongBookList.add(studentWrongBookService.newStudentWrongBook(studentId, item));
                    } else {
                        studentQuestionAnswer.setCorrectStatus(EnumConstants.CorrectStatus.CORRECTED.getValue());
                    }
                } else {
                    // 主观题答案为空，系统自动判错 // 错题数+1
                    if (ObjectUtils.isEmpty(item.getStudentAnswer())) {
                        studentQuestionAnswer.setCorrectStatus(EnumConstants.CorrectStatus.ERROR.getValue());
                        studentWrongBookList.add(studentWrongBookService.newStudentWrongBook(studentId, item));
                        errorNumber++;
                    } else {
                        studentQuestionAnswer.setCorrectStatus(EnumConstants.CorrectStatus.CORRECT_RUNNING.getValue());
                    }
                }
                subjectiveQuestionNumber++;
            }

            studentQuestionAnswer.setStudentAnswer(studentAnswer);
            studentQuestionAnswer.setCreateDate(now);
            studentQuestionAnswer.setComment(item.getComment());
            studentQuestionAnswerList.add(studentQuestionAnswer);
        }

        if (studentWrongBookList.size() > 0) {
            studentWrongBookService.saveBatch(studentWrongBookList); // 批量保存错题
        }

        if (!studentQuestionRequest.isTeacherCorrectFlag()) {
            // 保存考试记录表
            examInfo.setStudentId(studentId);
            examInfo.setQuestionNumber(questionNumber);
            examInfo.setTestPaperInfoId(testPaperInfoId);
            examInfo.setCreateDate(now);
            examInfo.setSystemMark(systemMark);
            examInfo.setMark(systemMark);
            examInfo.setSubjectiveQuestionNumber(subjectiveQuestionNumber);
            long examTime = studentQuestionRequest.getExamTime();
            examInfo.setExamTime(DateUtils.getDate(examTime));
            examInfo.setExamTimeLongValue(examTime);
            if (subjectiveQuestionNumber == 0) { // 如果全部为客观题的话（主观题数量为0），直接设置为已批改状态
                examInfo.setCorrectFlag(EnumConstants.Flag.YES.getValue());
                examInfo.setCorrectType(EnumConstants.CorrectType.SYSTEM.getValue());
            }
            examInfo.setRightNumber(rightNumber);
            examInfo.setErrorNumber(errorNumber);
            examInfo.setCreateDate(now);
            super.save(examInfo);
            // 更新考试参考人数
            testPaperInfoService.updateExamNumber(examInfo.getTestPaperInfoId());

            examMonitorService.removeStudent(studentId, testPaperInfoId); // 离开考试监控
        } else {
            // 主观题数量为0， 设置为教师评分
            if (examInfo.getSubjectiveQuestionNumber() == 0) {
                examInfo.setCorrectType(EnumConstants.CorrectType.TEACHER.getValue());
            } else {
                // 系统 + 教师评分
                examInfo.setCorrectType(EnumConstants.CorrectType.SYSTEM_AND_TEACHER.getValue());
            }
            examInfo.setTeacherMark(teacherMark);
            examInfo.setMark(examInfo.getSystemMark() + teacherMark);
            examInfo.setUpdateDate(now);
            examInfo.setErrorNumber(examInfo.getErrorNumber() + teacherErrorNumber);
            examInfo.setCorrectFlag(EnumConstants.Flag.YES.getValue());
            examInfo.setAdminId(getAdminUserId());
            super.updateById(examInfo);

            // 发送批改消息通知
            TaskParam taskParam = new TaskParam(WebSocketMessageTask.class);
            taskParam.put("message_type", EnumConstants.MessageType.EXAM_CORRECT.getValue());
            taskParam.put("sessionId", RequestUtils.getCookieValue(Constants.DEFAULT_SESSION_ID));
            taskParam.put("studentId", studentId);
            taskParam.put("testPaperInfoId", examInfo.getTestPaperInfoId());
            taskManager.pushTask(taskParam);
        }
        studentQuestionAnswerList.stream().forEach(item -> item.setExamInfoId(examInfo.getId()));
        // 批量保存学员试题答案
        studentQuestionAnswerService.saveBatch(studentQuestionAnswerList);
        return examInfo.getId();
    }


    public QuestionGroupResponse selectExamQuestionAnswer(Integer studentId, Integer examInfoId) {
        StudentExamInfoDto studentExamInfoDto = this.getExamInfoById(examInfoId);
        List<QuestionInfoAnswer> examQuestionAnswerList = studentQuestionAnswerService
                .getQuestionAnswerByExamInfoId(studentId, examInfoId);
        List<QuestionGroupItemResponse> list = questionInfoService.groupQuestion(examQuestionAnswerList);
        ExamQuestionGroupResponse examQuestionResponse = new ExamQuestionGroupResponse();
        examQuestionResponse.setQuestionGroupItemResponseList(list);
        examQuestionResponse.setTotalQuestion(list.size());
        examQuestionResponse.setStudentExamInfoDto(studentExamInfoDto);
        return examQuestionResponse;
    }

    public StudentExamInfoDto getExamInfoById(Integer examInfoId) {
        return baseMapper.selectById(examInfoId);
    }

    /**
     * 批改学员试卷
     * @param studentQuestionRequest
     */
    @Transactional
    public void correctStudentExam(StudentQuestionRequest studentQuestionRequest) {
        ExamInfo examInfo = super.getById(studentQuestionRequest.getExamInfoId());
        if (examInfo.getCorrectFlag().intValue() == ResultCode.SUCCESS) {
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "试卷已被批改"));
        }
        Integer studentId = studentQuestionRequest.getStudentId();
        studentQuestionAnswerService.deleteByExamInfoId(studentId, examInfo.getId());
        studentQuestionRequest.setTestPaperInfoId(examInfo.getTestPaperInfoId());
        this.batchSaveStudentQuestionAnswer(studentQuestionRequest, studentQuestionRequest.getStudentId(), examInfo);
    }

    public PageInfo<TestPaperInfoReport> selectExamReportList(PageParam pageParam, TestPaperInfo testPaperInfo) {
        Page<TestPaperInfoReport> page = new Page(pageParam.getPageNumber(), pageParam.getPageSize());
        return selectPage(baseMapper.selectExamReportList(page, testPaperInfo));
    }

    public ExamInfoDetail examDetailReport(Integer testPaperInfoId) {
        TestPaperInfo testPaperInfo = testPaperInfoService.getById(testPaperInfoId);
        Integer mark = testPaperInfo.getMark();
        double passMark = NumberUtils.doubleToBigDecimal(mark * Constants.PASS_MARK_RATE);
        double niceMark = NumberUtils.doubleToBigDecimal(mark * Constants.NICE_MARK_RATE);
        Kv params = Kv.create().set("testPaperInfoId", testPaperInfoId).set("passMark", passMark)
                .set("niceMark", niceMark);
        ExamInfoDetail examInfoDetail = baseMapper.selectExamInfoDetail(params);
        examInfoDetail.setPassExamMark(passMark);
        examInfoDetail.setNiceExamMark(niceMark);
        examInfoDetail.setExamTime(DateUtils.getDate(testPaperInfo.getExamTime()));
        examInfoDetail.setExamNumber(testPaperInfo.getExamNumber());
        return examInfoDetail;
    }

    public PageInfo<ExamInfoReport> getExamRankingList(PageParam pageParam, Integer testPaperInfoId) {
        Page<ExamInfoReport> page = new Page(pageParam.getPageNumber(), pageParam.getPageSize());
        return selectPage(baseMapper.selectExamListByTestPaperInfoId(page, testPaperInfoId));
    }

    /**
     * 获取近七天考试记录统计
     * @return
     */
    public List<ExamCount> selectExamInfoData() {
        Date now = new Date();
        String startTime = DateUtils.getDayBefore(DateUtils.getSecondDate(now), 7);
        String endTime = DateUtils.getDayBefore(DateUtils.getSecondDate(now), 1);
        Map params = new HashMap<>();
        // 获取近七天的开始时间和结束时间
        params.put("startTime", startTime + " 00:00:00");
        params.put("endTime", endTime + " 23:59:59");

        List<ExamCount> dataList = baseMapper.countByDateTime(startTime, endTime);
        Map<String, Integer> dataTimeMap = new HashMap<>();
        dataList.forEach(data -> {
            String day = data.getDayGroup();
            Integer examNumber = data.getExamNumber();
            dataTimeMap.put(day, examNumber);
        });

        List<String> weekDateList = DateUtils.getSectionByOneDay(8);
        // 近七天日期集合
        weekDateList.remove(weekDateList.size() - 1); // 移除最后一天，也就是当天的日期
        List<ExamCount> resultDataList = new ArrayList<>();
        weekDateList.forEach(day -> {
            ExamCount item = new ExamCount();
            item.setDayGroup(day);
            item.setExamNumber(ObjectUtils.isNotEmpty(dataTimeMap.get(day)) ? dataTimeMap.get(day) : 0);
            resultDataList.add(item);
        });

        return resultDataList;
    }
}
