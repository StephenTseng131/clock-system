package com.example.clocksystem.service;

import com.example.clocksystem.entity.*;
import com.example.clocksystem.entity.Class;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Set;

public interface TeacherService {
    /**
     * 教师修改信息
     *
     * @param teacher 教师对象
     * @return 影响的行数
     */
    int teacherUpdateInfo(Teacher teacher);

    /**
     * 教师发布打卡
     *
     * @param courseClass 课程码
     * @param startTime   打卡起始时间
     * @param endTime     打卡结束时间
     * @param code        打卡码
     * @param teacher     教师对象
     * @return 0:发布打卡成功，1:发布打卡失败
     */
    int teacherAttendance(String courseClass, String startTime, String endTime, String code, Teacher teacher);

    /**
     * 教师打卡记录筛选
     * @param searchContent 搜索内容
     * @param tNo 教师号
     * @return 打卡记录对象列表
     */
    List<Attendance> teacherAttendancesSearch(String searchContent, String tNo);

    /**
     * 教师查找有关教师的请假审批记录
     * @param tNo 教师号
     * @param pageNum 页码
     * @return 教师请假审批记录
     */
    PageInfo<TeacherLeaveRecord> teacherFindLeaveRecordsToTeacher(String tNo,int pageNum);

    /**
     * 教师查询请假记录
     *
     * @param leaveNo 请假编号
     * @return 请假记录对象
     */
    LeaveRecord teacherFindTeacherLeaveRecord(int leaveNo);

    /**
     * 教师的学生请假记录筛选
     * @param searchContent 搜索内容
     * @return 教师请假记录列表
     */
    List<TeacherLeaveRecord> teacherLeaveRecordSearch(String searchContent, String tNo);

    /**
     * 教师审批请假
     *
     * @param leaveNo 请假编号
     * @param result  审批结果
     * @param teacher 教师对象
     * @return 数据更新后的教师对象
     */
    Teacher teacherUpdateLeaveStatus(Integer leaveNo, Integer result, Teacher teacher);

    /**
     * 教师查询有关教师的学生申诉记录
     *
     * @param tNo     教师号
     * @param pageNum 页码
     * @return 教师学生申诉列表
     */
    PageInfo<TeacherStuAppeal> teacherFindStudentAppealToTeacher(String tNo, int pageNum);

    /**
     * 教师查看申诉详情
     *
     * @param appealNo 申诉编号
     * @return 申诉对象
     */
    StuAppeal teacherFindStudentAppealDetail(Integer appealNo);

    /**
     * 教师审批考勤申诉
     *
     * @param appealNo 申诉编号
     * @param result   审批结果
     * @param teacher  教师对象
     * @return 数据更新后的教师对象
     */
    Teacher teacherUpdateAppealStatus(Integer appealNo, Integer result, Teacher teacher);

    /**
     * 老师筛选审批记录
     * @param searchContent 搜索内容
     * @param tNo 教师号
     * @return 有关教师的学生申诉列表对象
     */
    List<TeacherStuAppeal> teacherClockRecordJudgeSearch(String searchContent, String tNo);

    /**
     * 任课教师查询自己的课程
     *
     * @param tNo 教师号
     * @return 课程列表
     */
    List<Course> teacherFindCourse(String tNo);

    /**
     * 查询有课程码的课程
     * @param tNo 教师号
     * @return 课程列表
     */
    List<Course> teacherFindCourseCodeCourses(String tNo);

    /**
     * 查询没有课程码的课程
     * @param tNo 教师号
     * @return 课程列表
     */
    List<Course> teacherFindNoCourseCodeCourses(String tNo);

    /**
     * 辅导员查询自己所带的班级
     *
     * @param tNo 教师号
     * @return 班级列表
     */
    PageInfo<Class> manageTeacherFindClass(String tNo, int pageNum);

    /**
     * 辅导员查询自己所带的班级筛选
     * @param searchContent 搜索内容
     * @param tNo 教师
     * @return 班级列表
     */
    List<Class> magageTeacherClockCountClassSearch(String searchContent, String tNo);

    /**
     * 通过教师号查询教师对象
     *
     * @param tNo 教师号
     * @return 教师对象
     */
    Teacher teacherFindTeacher(String tNo);

    /**
     * 任课老师查找某一门课程的所有时间段的课程
     *
     * @param courseName 课程名称
     * @param teacherNo  老师的教师号
     * @return 课程列表
     */
    PageInfo<Course> teacherFindManageTeacherCourse(String courseName, String teacherNo, int pageNum);

    /**
     * 教师根据班级号查询整个班级相关信息
     *
     * @param classNo 需要查询的班级的班级号
     * @return 返回一个班级
     */
    Class teacherFindClass(String classNo);

    /**
     * 根据班级号查询整个班级的学生
     *
     * @param classNo 被查询的班级的班级号
     * @return 班级里的所有学生
     */
    PageInfo<Student> teacherFindStudents(String classNo, int pageNum);

    /**
     * 根据学号和考勤记录的类型查询考勤记录
     *
     * @param sno    学生的学号
     * @param status 考勤记录的类型
     * @return 一个列表的考勤记录
     */
    PageInfo<ClockRecord> teacherFindClockRecords(String sno, String status, int pageNum);

    /**
     * 根据课程号和考勤记录的类型查询考勤记录
     *
     * @param courseNo 课程的课程号
     * @param status   考勤记录的类型
     * @return 一个列表的考勤记录
     */
    List<Student> teacherFindStudents(int courseNo, String status);

    /**
     * 教师添加课程编码
     *
     * @param teacher     教师对象
     * @param courseNo  课程码
     * @param courseClass 课程号
     * @return 0：添加成功，1：添加失败
     */
    int teacherAddCourseCode(Teacher teacher,Integer courseNo,String courseClass);

    /**
     * 教师查找自己指导的社团
     * @param tNo 教师号
     * @param pageNum 页码
     * @return 社团列表
     */
    PageInfo<Society> teacherFindSocieties(String tNo, int pageNum);

    /**
     * 教师筛选自己指导的社团
     * @param searchContent 搜索内容
     * @param tNo 教师号
     * @return 社团列表
     */
    List<Society> teacherSocietyManageSearch(String searchContent, String tNo);

    /**
     * 教师查询自己的社团申请
     * @return 教师社团申请列表
     */
    PageInfo<TeacherSocietyApplication> teacherFindSocietyApplication(String tNo, int pageNum);

    /**
     * 筛选教师的社团申请
     * @param searchContent 搜索内容
     * @param tNo 教师号
     * @return 教师社团申请列表
     */
    List<TeacherSocietyApplication> teacherSocietyGuideSearch(String searchContent, String tNo);

    /**
     * 教师查看社团申请详情
     * @param applicationNo 申请编号
     * @param tNo 教师号
     * @return 教师的对应社团申请对象
     */
    SocietyApplication teacherFindTeacherSocietyApplication(Integer applicationNo,String tNo);

    /**
     * 教师查看社团详情
     * @param societyId 社团账号
     * @return 社团对象
     */
    Society teacherFindSociety(String societyId);

    /**
     * 查看教师课程码
     * @return 教师课程列表
     */
    Set<String> teacherFindCourseCode();

    /**
     * 教师查询自己发起打卡的记录
     * @param tNo 教师号
     * @param pageNum 页码
     * @return 发起打卡记录列表
     */
    PageInfo<Attendance> teacherFindAttendance(String tNo, int pageNum);

    /**
     * 教师更新社团申请状态
     * @param applicationNo 申请编号
     * @param result 状态
     * @return 0:成功，1：失败
     */
    int teacherUpdateSocietyApplication(int applicationNo, String tNo, int result);

    /**
     * 教师查询活动审批记录
     * @param tNo 教师
     * @param pageNum 页码
     * @return 教师活动审批列表
     */
    PageInfo<TeacherActivityApplication> teacherFindActivityApplicationsByTno(String tNo, int pageNum);

    /**
     * 筛选教师活动审批记录
     * @param searchContent 搜索内容
     * @param tNo 教师号
     * @return 教师活动审批列表
     */
    List<TeacherActivityApplication> teacherActivityCheckSearch(String searchContent, String tNo);

    /**
     * 教师查找某条活动审批记录
     * @param applicationNo 申请编号
     * @param tNo 教师号
     * @return 教师活动审批对象
     */
    TeacherActivityApplication teacherFindActivityApplication(Integer applicationNo, String tNo);

    /**
     * 教师更新活动审批状态
     * @param application 申请编号
     * @param tNo 教师号
     * @param result 审批状态
     * @return 0：成功；1：失败
     */
    int teacherUpdateActivityApplication(Integer application, String tNo, Integer result);

    /**
     * 教师查看请假记录
     * @param tNo 教师号
     * @param pageNum 页码
     * @return 教师请假记录列表
     */
    PageInfo<TeacherLeaveRecord> teacherFindTeacherLeaveRecord(String tNo,int pageNum);

    /**
     * 教师查看考勤记录
     * @param teacher 教师对象
     * @param pageNum 页码
     * @return 教师考勤记录列表
     */
    PageInfo<ClockRecord> teacherFindClockRecord(Teacher teacher,int pageNum);

    /**
     * 教师筛选考勤记录
     * @param searchContent 搜索内容
     * @param teacher 教师号
     * @return 考勤记录列表对象
     */
    List<ClockRecord> teacherClockRecordTSearch(String searchContent, Teacher teacher);

    /**
     * 教师按班级号和状态查找学生
     * @param classNo 班级号
     * @param status 状态值
     * @return 学生列表
     */
    List<Student> teacherFindStudentByClassAndStatus(String classNo, String status);
}
