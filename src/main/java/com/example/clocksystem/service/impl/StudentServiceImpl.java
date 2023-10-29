package com.example.clocksystem.service.impl;

import com.example.clocksystem.entity.*;
import com.example.clocksystem.mapper.*;
import com.example.clocksystem.service.StudentService;
import com.example.clocksystem.utils.TimeConvert;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.clocksystem.utils.SearchJudge.searchJudge;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private LeaveRecordMapper leaveRecordMapper;
    @Autowired
    private ClockRecordMapper clockRecordMapper;
    @Autowired
    private StuAppealMapper stuAppealMapper;
    @Autowired
    private StudentCourseMapper studentCourseMapper;
    @Autowired
    private TeacherCourseMapper teacherCourseMapper;
    @Autowired
    private TeacherLeaveRecordMapper teacherLeaveRecordMapper;
    @Autowired
    private TeacherStuAppealMapper teacherStuAppealMapper;
    @Autowired
    private AttendanceMapper attendanceMapper;
    @Autowired
    private SocietyApplicationMapper societyApplicationMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TeacherSocietyApplicationMapper teacherSocietyApplicationMapper;
    @Autowired
    private StudentActivityMapper studentActivityMapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private StudentCreditMapper studentCreditMapper;


    /**
     * 学生更新信息
     *
     * @param student 学生对象
     * @return 影响的行数
     */
    @Override
    public int studentUpdateInfo(Student student) {
        return studentMapper.updateStudent(student);
    }

    /**
     * 学生打卡
     *
     * @param courseClass 课程码
     * @param code        打卡码
     * @return 0:成功，1:失败
     */
    @Override
    public int studentAttendance(String courseClass, String code, Student student) {
        // 获取当前时间
        Timestamp now = TimeConvert.timeConvert(System.currentTimeMillis());
        //查找该课程的最新一条打卡记录
        Attendance attendance = attendanceMapper.findLastAttendanceByCourseClass(courseClass);
        //如果在有效时间内
        if (now.before(attendance.getEndTime()) && now.after(attendance.getStartTime())) {
            //签到码匹配
            if (code.equals(attendance.getCode())) {
                //获取学生的考勤信息
                ClockRecord clockRecord = clockRecordMapper.findLastClockRecordBySno(student.getSNo());
                //更改考勤状态
                clockRecord.setStatus(0);
                clockRecordMapper.updateStatusByClockNo(clockRecord);
                //打卡成功
                return 0;
            }
        }
        // 打卡失败
        return 1;
    }

    /**
     * 学生添加课程
     *
     * @param courseClass 课程码
     * @param student     student对象
     * @return 0:添加成功，1:添加失败（课程码错误），2：添加失败（时间冲突）
     */
    @Override
    public int studentAddCourse(String courseClass, Student student) {
        //通过课程码查询课程数据
        TeacherCourse teacherCourse = teacherCourseMapper.findByCourseClass(courseClass);
        List<Course> studentCourseList = studentCourseMapper.findBySno(student.getSNo());
        if (teacherCourse == null) {
            return 1;
        }
        boolean flag = false;
        if (!studentCourseList.isEmpty()) {
            for (var course : student.getStuCourseList()) {
                // 同星期
                if (course.getWeek() == teacherCourse.getCourse().getWeek()) {
                    if (course.getStartTime().getTime() > teacherCourse.getCourse().getEndTime().getTime() || course.getEndTime().getTime() < teacherCourse.getCourse().getStartTime().getTime()) {
                        // 课程无交集
                        flag = true;
                    }
                    else {
                        // 课程有交集
                        flag = false;
                        return 2;
                    }
                }
                else {
                    // 星期不同
                    flag = true;
                }
            }
        }
        else {
            // 无课程
            flag = true;
        }

        if (flag) {
            // 添加课程
            // 课程数据对象封装
            StudentCourse studentCourse = new StudentCourse();
            studentCourse.setCourseNo(teacherCourse.getCourseNo());
            studentCourse.setSNo(student.getSNo());
            studentCourseMapper.addStudentCourse(studentCourse);
            return 0;
        }
        else {
            return 1;
        }
    }

    /**
     * 学生查询所有请假记录
     *
     * @param student 学生对象
     * @param pageNum 页码
     * @return 学生请假记录
     */
    @Override
    public PageInfo<LeaveRecord> studentLeaveRecord(Student student, int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = studentMapper.findBySno(student.getSNo()).getStuLeaveRecordList();
        //获取请假数据
        return new PageInfo<>(list);
    }

    /**
     * 学生筛选请假记录
     *
     * @param searchContent 搜索内容
     * @param student       学生对象
     * @return 学生请假记录
     */
    @Override
    public List<LeaveRecord> studentLeaveRecordSearch(String searchContent, Student student) {
        var leaveRecords = leaveRecordMapper.findBySno(student.getSNo());
        var leaveRecordList = new ArrayList<LeaveRecord>();
        for (LeaveRecord leaveRecord : leaveRecords) {
            if (searchJudge(searchContent, leaveRecord.toString())) {
                leaveRecordList.add(leaveRecord);
            }
        }
        return leaveRecordList;
    }

    /**
     * 学生查看请假详情
     *
     * @param leaveNo 请假编号
     * @return 请假记录
     */
    @Override
    public LeaveRecord studentFindLeaveRecordDetail(int leaveNo) {
        return leaveRecordMapper.findByLeaveNo(leaveNo);
    }

    /**
     * 学生发起请假
     *
     * @param startTime 请假起始时间
     * @param endTime   请假结束时间
     * @param reason    请假原因
     * @param sNo       学号
     * @return 学生对象
     */
    @Override
    public Student studentLeaveRequest(String startTime, String endTime, String reason, String sNo) {
        var student = studentMapper.findBySno(sNo);
        // 请假时间
        Timestamp start = TimeConvert.timeConvert1(startTime);
        Timestamp end = TimeConvert.timeConvert1(endTime);
        // 请假记录对象封装
        LeaveRecord leaveRecord = new LeaveRecord();
        leaveRecord.setSNo(student.getSNo());
        leaveRecord.setReason(reason);
        leaveRecord.setStartTime(start);
        leaveRecord.setEndTime(end);
        leaveRecord.setStatus(0);
        // 添加学生请假记录
        leaveRecordMapper.addLeaveRecord(leaveRecord);
        // 给辅导员插入该学生请假记录
        int leaveNo = leaveRecordMapper.findBySnoAndTime(student.getSNo(), start).getLeaveNo();
        TeacherLeaveRecord teacherLeaveRecord = new TeacherLeaveRecord();
        teacherLeaveRecord.setLeaveNo(leaveNo);
        teacherLeaveRecord.setTNo(student.getStuClass().getTNo());
        // 审核中
        teacherLeaveRecord.setStatus(0);
        // 给辅导员提交学生请假记录
        teacherLeaveRecordMapper.addTeacherLeave(teacherLeaveRecord);
        // 给任课教师插入学生请假记录
        Set<String> teacherNo = TimeConvert.findTeacherNo(start, end, student.getStuCourseList());
        for (String tno : teacherNo) {
            if (!tno.equals(student.getStuClass().getTNo())) {
                teacherLeaveRecord.setTNo(tno);
                teacherLeaveRecordMapper.addTeacherLeave(teacherLeaveRecord);
            }
        }
        return student;
    }

    /**
     * 学生查询考勤记录
     *
     * @param clockNo 考勤编号
     * @return 考勤记录
     */
    @Override
    public ClockRecord studentFindClockRecord(int clockNo) {
        return clockRecordMapper.findByClockNo(clockNo);
    }

    /**
     * 学生对考勤记录进行申诉
     *
     * @param reason      申诉原因
     * @param student     学生对象
     * @param clockNo     考勤编号
     */
    @Override
    public void studentAppeal(String reason, Student student, Integer clockNo) {
        var clockRecord = clockRecordMapper.findByClockNo(clockNo);
        // 封装对象
        var stuAppeal = new StuAppeal();
        stuAppeal.setSNo(student.getSNo());
        stuAppeal.setClockNo(clockRecord.getClockNo());
        stuAppeal.setStatus(clockRecord.getStatus());
        stuAppeal.setReason(reason);
        // 添加学生申诉记录
        stuAppealMapper.addStuAppeal(stuAppeal);
        var studentClockRecord = new ClockRecord();
        studentClockRecord.setClockNo(clockRecord.getClockNo());
        // 已申诉
        studentClockRecord.setAppealStatus(1);
        clockRecordMapper.updateAppealStatusByClockNo(studentClockRecord);
        // 给辅导员以及任课老师添加申诉关联
        String courseTNo = clockRecord.getCourseTNo();
        String manageTNo = clockRecord.getManageTNo();
        // 获取添加的申诉记录
        var appeal = stuAppealMapper.findBySnoAndClockNo(student.getSNo(), clockRecord.getClockNo());
        // 给辅导员添加申诉记录
        var teacherStuAppeal = new TeacherStuAppeal();
        teacherStuAppeal.setAppealNo(appeal.getAppealNo());
        teacherStuAppeal.setTNo(manageTNo);
        teacherStuAppealMapper.addTeacherStuAppeal(teacherStuAppeal);
        // 辅导员和任课老师不是一个人
        if (!courseTNo.equals(manageTNo)) {
            teacherStuAppeal.setTNo(courseTNo);
            teacherStuAppealMapper.addTeacherStuAppeal(teacherStuAppeal);
        }
    }

    /**
     * 按学号查询学生信息
     *
     * @param sNo 学号
     * @return 学生对象
     */
    @Override
    public Student studentFindStudent(String sNo) {
        return studentMapper.findBySno(sNo);
    }

    /**
     * 学生查询考勤记录
     *
     * @param sNo     学号
     * @param pageNum 页码
     * @return 考勤列表
     */
    @Override
    public PageInfo<ClockRecord> studentFindClockRecords(String sNo, int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = clockRecordMapper.findBySno(sNo);
        return new PageInfo<>(list);
    }

    /**
     * 学生筛选考勤记录
     *
     * @param searchContent 搜索内容
     * @param sNo           学号
     * @return 考勤列表
     */
    @Override
    public List<ClockRecord> studentClockRecordSearch(String searchContent, String sNo) {
        var clockRecords = clockRecordMapper.findBySno(sNo);
        var clockRecordList = new ArrayList<ClockRecord>();
        for (ClockRecord clockRecord : clockRecords) {
            if (searchJudge(searchContent, clockRecord.toString())) {
                clockRecordList.add(clockRecord);
            }
        }
        return clockRecordList;
    }

    /**
     * 学生查询社团申请记录
     *
     * @param sNo     学号
     * @param pageNum 页码
     * @return 社团申请列表
     */
    @Override
    public PageInfo<SocietyApplication> studentFindSocietyApplication(String sNo, int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = societyApplicationMapper.findSocietyApplicationsBySno(sNo);
        return new PageInfo<>(list);
    }

    /**
     * 学生查询社团申请记录的筛选
     *
     * @param searchContent 搜索内容
     * @param sNo           学号
     * @return 社团申请列表
     */
    @Override
    public List<SocietyApplication> studentSocietyManageSearch(String searchContent, String sNo) {
        var applications = societyApplicationMapper.findSocietyApplicationsBySno(sNo);
        var applicationList = new ArrayList<SocietyApplication>();
        for (SocietyApplication application : applications) {
            if (searchJudge(searchContent, application.toString())) {
                applicationList.add(application);
            }
        }
        return applicationList;
    }

    /**
     * 学生查找社团申请详情
     *
     * @param applicationNo 申请编号
     * @return 社团申请对象
     */
    @Override
    public SocietyApplication studentFindSocietyApplicationDetail(int applicationNo) {
        return societyApplicationMapper.findSocietyApplicationByApplicationNo(applicationNo);
    }

    /**
     * 学生查看学院
     *
     * @return 学院列表
     */
    @Override
    public List<Department> studentFindDepartments() {
        return departmentMapper.findAllDepartments();
    }

    /**
     * 学生查看学院教师
     *
     * @param departmentId 学院教师
     * @return 教师列表
     */
    @Override
    public List<Teacher> studentFindTeacherListByDepartment(String departmentId) {
        return teacherMapper.findTeachersByDepartmentNo(departmentId);
    }

    /**
     * 学生发起创建社团请求
     *
     * @param picture     图片
     * @param societyName 社团名
     * @param teacherNo   教师号
     * @param introduce   介绍
     * @param reason      原因
     * @param student     学生对象
     * @return 0：发起成功，1：发起失败
     */
    @Override
    public int studentCreateSocietyApplication(MultipartFile picture, String societyName, String teacherNo, String introduce, String reason, Student student) {
        try {
            // 封装对象
            byte[] imageData = picture.getBytes();
            SocietyApplication societyApplication = new SocietyApplication();
            societyApplication.setSocietyName(societyName);
            // 类型:创建
            societyApplication.setType(0);
            societyApplication.setPicture(imageData);
            societyApplication.setIntroduce(introduce);
            societyApplication.setReason(reason);
            // 审核中
            societyApplication.setOfficeStatus(0);
            societyApplication.setStatus(0);
            societyApplication.setSNo(student.getSNo());
            societyApplication.setTNo(teacherNo);
            if (societyApplicationMapper.addSocietyApplication(societyApplication) == 0) {
                // 插入失败
                return 1;
            } else {
                TeacherSocietyApplication teacherSocietyApplication = new TeacherSocietyApplication();
                // 给教师关联上记录
                SocietyApplication lastApplication = societyApplicationMapper.findLastSocietyApplicationBySno(student.getSNo());
                teacherSocietyApplication.setApplicationNo(lastApplication.getApplicationNo());
                teacherSocietyApplication.setTNo(teacherNo);
                teacherSocietyApplication.setStatus(0);
                teacherSocietyApplicationMapper.addTeacherSocietyApplication(teacherSocietyApplication);
                return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * 学生查询其活动列表
     *
     * @param sNo     学号
     * @param pageNum 页码
     * @return 活动列表
     */
    @Override
    public PageInfo<StudentActivity> studentFindActivityList(String sNo, int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = studentActivityMapper.findStudentActivitiesBySno(sNo);
        return new PageInfo<>(list);
    }

    /**
     * 学生查询其活动列表的筛选
     *
     * @param searchContent 搜索内容
     * @param sNo           学号
     * @return 活动列表
     */
    @Override
    public List<StudentActivity> studentActivityManageSearch(String searchContent, String sNo) {
        var activities = studentActivityMapper.findStudentActivitiesBySno(sNo);
        var activityList = new ArrayList<StudentActivity>();
        for (StudentActivity activity : activities) {
            if (searchJudge(searchContent, activity.toString())) {
                activityList.add(activity);
            }
        }
        return activityList;
    }

    /**
     * 学生查询所有活动列表
     *
     * @param sNo     学号
     * @param pageNum 页码
     * @return 活动列表
     */
    @Override
    public PageInfo<Activity> studentFindAllActivityList(String sNo, int pageNum) {
        PageHelper.startPage(pageNum, 8);
        Timestamp now = TimeConvert.timeConvert(System.currentTimeMillis());
        var list = activityMapper.findActivitiesToStudent(sNo, now);
        return new PageInfo<>(list);
    }

    /**
     * 学生查询所有活动列表的筛选
     *
     * @param searchContent 搜索内容
     * @param sNo           学号
     * @return 活动列表
     */
    @Override
    public List<Activity> studentActivityListSearch(String searchContent, String sNo) {
        Timestamp now = TimeConvert.timeConvert(System.currentTimeMillis());
        var activities = activityMapper.findActivitiesToStudent(sNo, now);
        var activityList = new ArrayList<Activity>();
        for (Activity activity : activities) {
            if (searchJudge(searchContent, activity.toString())) {
                activityList.add(activity);
            }
        }
        return activityList;
    }

    /**
     * 学生查询活动详情
     *
     * @param sNo        学号
     * @param activityNo 活动号
     * @return 学生活动对象
     */
    @Override
    public StudentActivity studentFindStudentActivity(String sNo, Integer activityNo) {
        return studentActivityMapper.findStudentActivityByActivityNoAndSno(activityNo, sNo);
    }

    /**
     * 学生查询活动详情
     *
     * @param activityNo 活动号
     * @return 活动对象
     */
    @Override
    public Activity studentFindActivity(Integer activityNo) {
        return activityMapper.findActivityByNo(activityNo);
    }

    /**
     * 学生参加活动
     *
     * @param student    学生对象
     * @param activityNo 活动编号
     * @return 0:参加成功，1：参加失败
     */
    @Override
    public int studentJoinActivity(Student student, Integer activityNo) {
        // 学生参加的活动
        var studentActivityList = studentActivityMapper.findStudentActivitiesBySno(student.getSNo());
        for (var activity : studentActivityList) {
            if (activityNo.equals(activity.getActivityNo())) {
                return 1;
            }
        }
        // 对象封装
        StudentActivity studentActivity = new StudentActivity();
        studentActivity.setActivityNo(activityNo);
        studentActivity.setSNo(student.getSNo());
        studentActivity.setStatus(0);

        // 参加活动
        if (studentActivityMapper.addStudentActivity(studentActivity) == 1) {
            // 添加成功
            return 0;
        } else {
            // 添加失败
            return 1;
        }
    }

    /**
     * 学生进行活动打卡
     *
     * @param sNo        学号
     * @param activityNo 活动号
     * @param code       签到码
     * @return 0:打卡成功，1：打卡失败
     */
    @Override
    public int studentActivityAttendance(String sNo, Integer activityNo, String code) {
        // 对象封装
        var studentActivity = new StudentActivity();
        studentActivity.setActivityNo(activityNo);
        studentActivity.setSNo(sNo);
        // 查询此活动签到码
        var activity = activityMapper.findActivityByNo(activityNo);
        if (code.equals(activity.getCode())) {
            // 签到成功
            studentActivity.setStatus(1);
            studentActivityMapper.updateStudentActivity(studentActivity);
            var type = activity.getType();
            if (type == 0) {
                studentCreditMapper.updateMoral(activity.getCredit(), sNo);
                studentCreditMapper.updateTotal(sNo);
                return 0;
            } else if (type == 1) {
                studentCreditMapper.updateIntellectual(activity.getCredit(), sNo);
                studentCreditMapper.updateTotal(sNo);
                return 0;
            } else if (type == 2) {
                studentCreditMapper.updatePhysical(activity.getCredit(), sNo);
                studentCreditMapper.updateTotal(sNo);
                return 0;
            } else if (type == 3) {
                studentCreditMapper.updateAesthetic(activity.getCredit(), sNo);
                studentCreditMapper.updateTotal(sNo);
                return 0;
            } else if (type == 4) {
                studentCreditMapper.updateLabor(activity.getCredit(), sNo);
                studentCreditMapper.updateTotal(sNo);
                return 0;
            }
            return 1;
        } else {
            return 1;
        }
    }

    /**
     * 学生查找学分
     *
     * @param sNo 学号
     * @return studentCredit对象
     */
    @Override
    public StudentCredit studentFindStudentCredit(String sNo) {
        return studentCreditMapper.findStudentCreditBySno(sNo);
    }

    /**
     * 学生按时间范围筛选学分
     *
     * @param sNo       学号
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return studentCredit对象
     */
    @Override
    public StudentCredit studentCreditSearch(String sNo, String startTime, String endTime) {
        Timestamp head = TimeConvert.timeConvert1(startTime);
        Timestamp tail;
        //如果不为空
        if (!endTime.equals("")) {
            tail = TimeConvert.timeConvert1(endTime);
        } else {
            tail = TimeConvert.timeConvert(System.currentTimeMillis());
        }
        //根据时间范围查找学生参加过的、活动的结束时间在范围之内的、签到状态为1的活动列表
        var activityList = activityMapper.findActivitiesBySnoAndTimeScopeAndStatus(sNo, head, tail);
        //创建studentCredit对象
        StudentCredit studentCredit = new StudentCredit();
        studentCredit.setSNo(sNo);
        studentCredit.setMoral(0);
        studentCredit.setIntellectual(0);
        studentCredit.setPhysical(0);
        studentCredit.setAesthetic(0);
        studentCredit.setLabor(0);
        studentCredit.setTotal(0);
        //遍历活动列表，计算学分分数
        for (Activity activity : activityList) {
            if (activity.getType() == 0) {
                studentCredit.setMoral(studentCredit.getMoral() + activity.getCredit());
            } else if (activity.getType() == 1) {
                studentCredit.setIntellectual(studentCredit.getIntellectual() + activity.getCredit());
            } else if (activity.getType() == 2) {
                studentCredit.setPhysical(studentCredit.getPhysical() + activity.getCredit());
            } else if (activity.getType() == 3) {
                studentCredit.setAesthetic(studentCredit.getAesthetic() + activity.getCredit());
            } else if (activity.getType() == 4) {
                studentCredit.setLabor(studentCredit.getLabor() + activity.getCredit());
            }
        }
        //计算学分总和
        studentCredit.setTotal(studentCredit.getMoral() + studentCredit.getIntellectual() + studentCredit.getPhysical() + studentCredit.getAesthetic() + studentCredit.getLabor());
        return studentCredit;
    }

    /**
     * @param sNo 学号
     * @return 课程列表
     */
    @Override
    public List<Course> studentFindCourseList(String sNo) {
        return studentCourseMapper.findBySno(sNo);
    }

}
