package com.example.clocksystem.service;

import com.example.clocksystem.entity.*;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

public interface StudentService {
    /**
     * 学生更新信息
     * @param student 学生对象
     * @return 0:成功 1:失败
     */
    int studentUpdateInfo(Student student);

    /**
     * 学生打卡
     *
     * @param courseClass 课程码
     * @param code        打卡码
     * @return 0:成功，1:失败
     */
    int studentAttendance(String courseClass, String code,Student student);

    /**
     * 学生添加课程
     *
     * @param courseClass 课程码
     * @param student     student对象
     * @return 00:添加成功，1:添加失败（课程码错误），2：添加失败（时间冲突）
     */
    int studentAddCourse(String courseClass, Student student);

    /**
     * 学生查询所有请假记录
     * @param student 学生对象
     * @param pageNum 页码
     * @return 学生请假记录
     */
    PageInfo<LeaveRecord> studentLeaveRecord(Student student, int pageNum);

    /**
     * 学生筛选请假记录
     * @param searchContent 搜索内容
     * @param student 学生对象
     * @return 学生请假记录
     */
    List<LeaveRecord> studentLeaveRecordSearch(String searchContent, Student student);

    /**
     * 学生查看请假详情
     * @param leaveNo 请假编号
     * @return 请假记录
     */
    LeaveRecord studentFindLeaveRecordDetail(int leaveNo);

    /**
     * 学生发起请假
     * @param startTime 请假起始时间
     * @param endTime   请假结束时间
     * @param reason    请假原因
     * @param sNo       学号
     * @return 0:请假成功，1:请假失败
     */
    Student studentLeaveRequest(String startTime, String endTime, String reason,String sNo);

    /**
     * 学生查询考勤记录
     * @param clockNo 考勤编号
     * @return 考勤记录
     */
    ClockRecord studentFindClockRecord(int clockNo);

    /**
     * 学生对考勤记录进行申诉
     * @param reason 申诉原因
     * @param student 学生对象
     * @param clockNo 考勤编号
     */
    void studentAppeal(String reason,Student student,Integer clockNo);

    /**
     * 按学号查询学生信息
     * @param sNo 学号
     * @return 学生对象
     */
    Student studentFindStudent(String sNo);

    /**
     * 学生查询考勤记录
     * @param sNo 学号
     * @param pageNum 页码
     * @return 考勤列表
     */
    PageInfo<ClockRecord> studentFindClockRecords(String sNo, int pageNum);

    /**
     * 学生筛选考勤记录
     * @param searchContent 搜索内容
     * @param sNo 学号
     * @return 考勤列表
     */
    List<ClockRecord> studentClockRecordSearch(String searchContent, String sNo);

    /**
     * 学生查询社团申请记录
     * @param sNo 学号
     * @param pageNum 页码
     * @return 社团申请列表
     */
    PageInfo<SocietyApplication> studentFindSocietyApplication(String sNo, int pageNum);

    /**
     * 学生查询社团申请记录的筛选
     * @param searchContent 搜索内容
     * @param sNo 学号
     * @return 社团申请列表
     */
    List<SocietyApplication> studentSocietyManageSearch(String searchContent, String sNo);

    /**
     * 学生查找社团申请详情
     * @param applicationNo 申请编号
     * @return 社团申请对象
     */
    SocietyApplication studentFindSocietyApplicationDetail(int applicationNo);

    /**
     * 学生查看学院
     * @return 学院列表
     */
    List<Department> studentFindDepartments();

    /**
     * 学生查看学院教师
     * @param departmentId 学院教师
     * @return 教师列表
     */
    List<Teacher> studentFindTeacherListByDepartment(String departmentId);

    /**
     * 学生发起创建社团请求
     * @param picture 图片
     * @param societyName 社团名
     * @param teacherNo 教师号
     * @param introduce 介绍
     * @param reason 原因
     * @param student 学生对象
     * @return 0：发起成功，1：发起失败
     */
    int studentCreateSocietyApplication(MultipartFile picture, String societyName, String teacherNo, String introduce, String reason,Student student);

    /**
     * 学生查询其活动列表
     * @param sNo 学号
     * @param pageNum 页码
     * @return 活动列表
     */
    PageInfo<StudentActivity> studentFindActivityList(String sNo, int pageNum);

    /**
     * 学生查询其活动列表的筛选
     * @param searchContent 搜索内容
     * @param sNo 学号
     * @return 活动列表
     */
    List<StudentActivity> studentActivityManageSearch(String searchContent, String sNo);

    /**
     * 学生查询所有活动列表
     * @param sNo 学号
     * @param pageNum 页码
     * @return 活动列表
     */
    PageInfo<Activity> studentFindAllActivityList(String sNo,int pageNum);

    /**
     * 学生查询所有活动列表的筛选
     * @param searchContent 搜索内容
     * @param sNo 学号
     * @return 活动列表
     */
    List<Activity> studentActivityListSearch(String searchContent, String sNo);

    /**
     * 学生查询其活动详情
     * @param sNo 学号
     * @param activityNo 活动号
     * @return 学生活动对象
     */
    StudentActivity studentFindStudentActivity(String sNo, Integer activityNo);

    /**
     * 学生查询活动详情
     * @param activityNo 活动号
     * @return 活动对象
     */
    Activity studentFindActivity(Integer activityNo);

    /**
     * 学生参加活动
     * @param student 学生对象
     * @param activityNo 活动编号
     * @return 0:参加成功，1：参加失败
     */
    int studentJoinActivity(Student student,Integer activityNo);

    /**
     * 学生进行活动打卡
     * @param sNo 学号
     * @param activityNo 活动号
     * @param code 签到码
     * @return 0:打卡成功，1：打卡失败
     */
    int studentActivityAttendance(String sNo,Integer activityNo,String code);

    /**
     * 学生查找学分
     * @param sNo 学号
     * @return studentCredit对象
     */
    StudentCredit studentFindStudentCredit(String sNo);

    /**
     * 学生按时间范围筛选学分
     * @param sNo 学号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return studentCredit对象
     */
    StudentCredit studentCreditSearch(String sNo, String startTime, String endTime);

    /**
     *
     * @param sNo 学号
     * @return 课程列表
     */
    List<Course> studentFindCourseList(String sNo);
}
