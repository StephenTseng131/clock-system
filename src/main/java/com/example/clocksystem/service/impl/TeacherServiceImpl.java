package com.example.clocksystem.service.impl;

import com.example.clocksystem.entity.*;
import com.example.clocksystem.entity.Class;
import com.example.clocksystem.mapper.*;
import com.example.clocksystem.service.TeacherService;
import com.example.clocksystem.utils.TimeConvert;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.clocksystem.utils.SearchJudge.searchJudge;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TeacherLeaveRecordMapper teacherLeaveRecordMapper;
    @Autowired
    private LeaveRecordMapper leaveRecordMapper;
    @Autowired
    private AttendanceMapper attendanceMapper;
    @Autowired
    private StuAppealMapper stuAppealMapper;
    @Autowired
    private TeacherStuAppealMapper teacherStuAppealMapper;
    @Autowired
    private ClockRecordMapper clockRecordMapper;
    @Autowired
    private TeacherCourseMapper teacherCourseMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private SocietyApplicationMapper societyApplicationMapper;
    @Autowired
    private TeacherSocietyApplicationMapper teacherSocietyApplicationMapper;
    @Autowired
    private SocietyMapper societyMapper;
    @Autowired
    private TeacherActivityApplicationMapper teacherActivityApplicationMapper;

    /**
     * 教师修改信息
     *
     * @param teacher 教师对象
     */
    @Override
    public int teacherUpdateInfo(Teacher teacher) {
        return teacherMapper.updateTeacher(teacher);
    }

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
    @Override
    public int teacherAttendance(String courseClass, String startTime, String endTime, String code, Teacher teacher) {
        Timestamp start = TimeConvert.timeConvert1(startTime);
        Timestamp end = TimeConvert.timeConvert1(endTime);
        // 判断这门课是否有学生
        int flag = 0;
        TeacherCourse teacherCourse = teacherCourseMapper.findByCourseClass(courseClass);
        // 打卡记录对象封装
        Attendance attendance = new Attendance();
        attendance.setCourseNo(teacherCourse.getCourseNo());
        attendance.setStartTime(start);
        attendance.setEndTime(end);
        attendance.setCode(code);
        attendance.setTNo(teacher.getTNo());
        // 先将选课学生打卡记录都设为缺勤
        List<Student> studentList = studentMapper.findByCourseNo(teacherCourse.getCourseNo());
        if (studentList.isEmpty()) {
            // 没有学生
            flag = 1;
            return flag;
        }
        else {
            // 有学生
            // 考勤记录
            ClockRecord clockRecord = new ClockRecord();
            for (Student student : studentList
            ) {
                // 查询该学生的审批通过的请假记录
                List<LeaveRecord> studentLeaveList = leaveRecordMapper.findApprovedLeaveRecordsBySno(student.getSNo());
                if (studentLeaveList.isEmpty()) {
                    clockRecord.setStatus(1);
                }
                else {
                    for (LeaveRecord leaveRecord : studentLeaveList) {
                        if (leaveRecord.getStartTime().getTime() <= start.getTime() && leaveRecord.getEndTime().getTime() >= end.getTime()) {
                            // 状态改为请假
                            clockRecord.setStatus(2);
                        }
                        else {
                            clockRecord.setStatus(1);
                        }
                    }
                }
                //封装考勤记录
                clockRecord.setCourseTNo(teacher.getTNo());
                clockRecord.setManageTNo(student.getStuClass().getTNo());
                clockRecord.setCourseNo(teacherCourse.getCourseNo());
                clockRecord.setSNo(student.getSNo());
                // 缺勤
                clockRecord.setTime(start);
                clockRecord.setAppealStatus(0);
                clockRecordMapper.addClockRecord(clockRecord);
            }
            attendanceMapper.addAttendance(attendance);
            return flag;
        }
    }

    /**
     * 教师打卡记录筛选
     *
     * @param searchContent 搜索内容
     * @param tNo           教师号
     * @return 打卡记录对象列表
     */
    @Override
    public List<Attendance> teacherAttendancesSearch(String searchContent, String tNo) {
        var attendances = attendanceMapper.findByTNo(tNo);
        var attendanceList = new ArrayList<Attendance>();
        //进行搜索
        for (Attendance attendance : attendances) {
            if (searchJudge(searchContent, attendance.toString())) {
                attendanceList.add(attendance);
            }
        }
        return attendanceList;
    }

    /**
     * 教师查找有关教师的请假审批记录
     *
     * @param tNo     教师号
     * @param pageNum 页码
     * @return 教师请假审批记录
     */
    @Override
    public PageInfo<TeacherLeaveRecord> teacherFindLeaveRecordsToTeacher(String tNo, int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = teacherLeaveRecordMapper.findLeaveRecordByTno(tNo);
        return new PageInfo<>(list);
    }


    /**
     * 教师查询请假记录
     *
     * @param leaveNo 请假编号
     * @return 请假记录对象
     */
    @Override
    public LeaveRecord teacherFindTeacherLeaveRecord(int leaveNo) {
        return leaveRecordMapper.findByLeaveNo(leaveNo);
    }

    /**
     * 教师的学生请假记录筛选
     *
     * @param searchContent 搜索内容
     * @return 教师请假记录列表
     */
    @Override
    public List<TeacherLeaveRecord> teacherLeaveRecordSearch(String searchContent, String tNo) {
        var teacherLeaveRecords = teacherLeaveRecordMapper.findLeaveRecordByTno(tNo);
        var leaveRecordList = new ArrayList<TeacherLeaveRecord>();
        //进行搜索
        for (TeacherLeaveRecord teacherLeaveRecord : teacherLeaveRecords) {
            if (searchJudge(searchContent, teacherLeaveRecord.toString())) {
                leaveRecordList.add(teacherLeaveRecord);
            }
        }
        return leaveRecordList;
    }

    /**
     * 教师审批请假
     *
     * @param leaveNo 请假编号
     * @param result  审批结果
     * @param teacher 教师对象
     * @return 教师对象
     */
    @Override
    public Teacher teacherUpdateLeaveStatus(Integer leaveNo, Integer result, Teacher teacher) {
        //封装对象
        TeacherLeaveRecord teacherLeaveRecord = new TeacherLeaveRecord();
        teacherLeaveRecord.setLeaveNo(leaveNo);
        teacherLeaveRecord.setTNo(teacher.getTNo());
        teacherLeaveRecord.setStatus(result);
        //更新请假状态
        teacherLeaveRecordMapper.updateLeaveStatus(teacherLeaveRecord);
        // 判断是否审核通过
        List<TeacherLeaveRecord> teacherLeaveRecords = teacherLeaveRecordMapper.findByLeaveNo(leaveNo);
        boolean flag = true;
        for (TeacherLeaveRecord record : teacherLeaveRecords) {
            // 一有不是通过的数据就改为false
            if (record.getStatus() != 1) {
                flag = false;
                break;
            }
        }
        LeaveRecord leaveRecord = new LeaveRecord();
        if (flag) {
            // 更改状态
            leaveRecord.setLeaveNo(leaveNo);
            leaveRecord.setStatus(1);
            leaveRecordMapper.updateStatus(leaveRecord);
        } else {
            leaveRecord.setLeaveNo(leaveNo);
            leaveRecord.setStatus(2);
            leaveRecordMapper.updateStatus(leaveRecord);
        }
        return teacherMapper.findByTno(teacher.getTNo());
    }

    /**
     * 教师查询有关教师的学生申诉记录
     *
     * @param tNo     教师号
     * @param pageNum 页码
     * @return 教师学生申诉列表
     */
    @Override
    public PageInfo<TeacherStuAppeal> teacherFindStudentAppealToTeacher(String tNo, int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = teacherStuAppealMapper.findStuAppealByTno(tNo);
        return new PageInfo<>(list);
    }

    /**
     * 教师查看申诉详情
     *
     * @param appealNo 申诉编号
     * @return 申诉对象
     */
    @Override
    public StuAppeal teacherFindStudentAppealDetail(Integer appealNo) {
        return stuAppealMapper.findByAppealNo(appealNo);
    }

    /**
     * 教师审批考勤申诉
     *
     * @param appealNo 申诉编号
     * @param result   审批结果
     * @param teacher  教师对象
     * @return 数据更新后的教师对象
     */
    @Override
    public Teacher teacherUpdateAppealStatus(Integer appealNo, Integer result, Teacher teacher) {
        //封装对象
        TeacherStuAppeal appeal = new TeacherStuAppeal();
        appeal.setAppealNo(appealNo);
        appeal.setStatus(result);
        appeal.setTNo(teacher.getTNo());
        //更新审批状态
        teacherStuAppealMapper.updateAppealStatus(appeal);
        // 查询是否任课教师和辅导员都通过
        List<TeacherStuAppeal> teacherStuAppealList = teacherStuAppealMapper.findByAppealNo(appealNo);
        boolean flag = true;
        for (TeacherStuAppeal teacherStuAppeal : teacherStuAppealList) {
            // 只要不为通过就false
            if (teacherStuAppeal.getStatus() != 1) {
                flag = false;
                break;
            }
        }
        StuAppeal stuAppeal = stuAppealMapper.findByAppealNo(appealNo);
        // 修改考勤记录
        ClockRecord clockRecord = new ClockRecord();
        clockRecord.setClockNo(stuAppeal.getClockNo());
        if (flag) {
            // 都通过则改为出勤
            clockRecord.setStatus(0);
            clockRecordMapper.updateStatusByClockNo(clockRecord);
        } else {
            // 有不通过就改为缺勤
            clockRecord.setStatus(1);
            clockRecordMapper.updateStatusByClockNo(clockRecord);
        }
        return teacherMapper.findByTno(teacher.getTNo());
    }

    /**
     * 老师筛选审批记录
     *
     * @param searchContent 搜索内容
     * @param tNo           教师号
     * @return 有关教师的学生申诉列表对象
     */
    @Override
    public List<TeacherStuAppeal> teacherClockRecordJudgeSearch(String searchContent, String tNo) {
        var appeals = teacherStuAppealMapper.findStuAppealByTno(tNo);
        var appealList = new ArrayList<TeacherStuAppeal>();
        for (TeacherStuAppeal appeal : appeals) {
            if (searchJudge(searchContent, appeal.toString())) {
                appealList.add(appeal);
            }
        }
        return appealList;
    }

    /**
     * 任课教师查询自己的课程
     *
     * @param tNo 教师号
     * @return 课程列表
     */
    @Override
    public List<Course> teacherFindCourse(String tNo) {
        return courseMapper.findCoursesByTno(tNo);
    }

    /**
     * 查询有课程码的课程
     *
     * @param tNo 教师号
     * @return 课程列表
     */
    @Override
    public List<Course> teacherFindCourseCodeCourses(String tNo) {
        List<Course> courseList = courseMapper.findCoursesByTno(tNo);
        List<Course> courses = new ArrayList<>();
        for (Course course : courseList) {
            // 返回没有添加课程码的课程
            if (course.getTeacherCourse() != null) {
                courses.add(course);
            }
        }
        return courses;
    }

    /**
     * 查询没有课程码的课程
     *
     * @param tNo 教师号
     * @return 课程列表
     */
    @Override
    public List<Course> teacherFindNoCourseCodeCourses(String tNo) {
        List<Course> courseList = courseMapper.findCoursesByTno(tNo);
        List<Course> courses = new ArrayList<>();
        for (Course course : courseList) {
            // 返回没有添加课程码的课程
            if (course.getTeacherCourse() == null) {
                courses.add(course);
            }
        }
        return courses;
    }

    /**
     * 辅导员查询自己所带的班级
     *
     * @param tNo 教师号
     * @return 班级列表
     */
    @Override
    public PageInfo<Class> manageTeacherFindClass(String tNo, int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = classMapper.findByTNo(tNo);
        //将没有人的班级去除掉
        list.removeIf(aClass -> aClass.getNumber() == 0);
        return new PageInfo<>(list);
    }

    /**
     * 辅导员查询自己所带的班级筛选
     *
     * @param searchContent 搜索内容
     * @param tNo           教师
     * @return 班级列表
     */
    @Override
    public List<Class> magageTeacherClockCountClassSearch(String searchContent, String tNo) {
        var classes = classMapper.findByTNo(tNo);
        var classList = new ArrayList<Class>();
        for (Class aClass : classes) {
            if (searchJudge(searchContent, aClass.toString())) {
                classList.add(aClass);
            }
        }
        return classList;
    }

    /**
     * 通过教师号查询教师对象
     *
     * @param tNo 教师号
     * @return 教师对象
     */
    @Override
    public Teacher teacherFindTeacher(String tNo) {
        return teacherMapper.findByTno(tNo);
    }

    /**
     * 任课老师查找某一门课程的所有时间段的课程
     *
     * @param courseName 课程名称
     * @param teacherNo  老师的教师号
     * @return 课程列表
     */
    @Override
    public PageInfo<Course> teacherFindManageTeacherCourse(String courseName, String teacherNo, int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = courseMapper.findByCourseNameAndTno(courseName, teacherNo);
        //去除无人的课程
        list.removeIf(course -> course.getNumber() == 0);
        return new PageInfo<>(list);
    }

    /**
     * 根据班级号查询整个班级相关信息
     *
     * @param classNo 需要查询的班级的班级号
     * @return 返回一个课程
     */
    @Override
    public Class teacherFindClass(String classNo) {
        return classMapper.findByClassNo(classNo);
    }

    /**
     * 根据班级号查询学生
     *
     * @param classNo 需要查询的班级的班级号
     * @return 返回一个学生列表
     */
    @Override
    public PageInfo<Student> teacherFindStudents(String classNo, int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = studentMapper.findByClassNo(classNo);
        return new PageInfo<>(list);
    }

    /**
     * 根据学号和考勤记录的类型查询考勤记录
     *
     * @param sno    学生的学号
     * @param status 考勤记录的类型
     * @return 一个列表的考勤记录
     */
    @Override
    public PageInfo<ClockRecord> teacherFindClockRecords(String sno, String status, int pageNum) {
        PageHelper.startPage(pageNum, 13);
        int type;
        if ("present".equals(status)) {
            type = 0;
        } else {
            type = 1;
        }
        var list = clockRecordMapper.findBySnoAndStatus(sno, type);
        return new PageInfo<>(list);
    }

    /**
     * 根据课程号和考勤记录的类型查询考勤记录
     *
     * @param courseNo 课程号
     * @param status   考勤记录的类型
     * @return 一个列表的考勤记录
     */
    @Override
    public List<Student> teacherFindStudents(int courseNo, String status) {
        if ("present".equals(status)) {
            return studentMapper.findAttendStudentsByCourseNo(courseNo);
        } else {
            return studentMapper.findAbsentStudentsByCourseNo(courseNo);
        }
    }

    /**
     * 教师添加课程编码
     *
     * @param teacher     教师对象
     * @param courseNo    课程码
     * @param courseClass 课程号
     * @return 0：添加成功，1：添加失败
     */
    @Override
    public int teacherAddCourseCode(Teacher teacher, Integer courseNo, String courseClass) {
        List<TeacherCourse> allTeacherCourses = teacherCourseMapper.findAllTeacherCourses();
        for (TeacherCourse teacherCourse : allTeacherCourses) {
            // 课程码相同不可添加课程
            if (courseClass.equals(teacherCourse.getCourseClass())) {
                return 1;
            }
        }
        // 对象封装
        TeacherCourse teacherCourse = new TeacherCourse();
        teacherCourse.setCourseNo(courseNo);
        teacherCourse.setCourseClass(courseClass);
        teacherCourse.setTNo(teacher.getTNo());
        teacherCourseMapper.addTeacherCourse(teacherCourse);
        return 0;
    }

    /**
     * 教师查找自己指导的社团
     *
     * @param tNo     教师号
     * @param pageNum 页码
     * @return 社团列表
     */
    @Override
    public PageInfo<Society> teacherFindSocieties(String tNo, int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = societyMapper.findSocietiesByTno(tNo);
        return new PageInfo<>(list);
    }

    /**
     * 教师筛选自己指导的社团
     *
     * @param searchContent 搜索内容
     * @param tNo           教师号
     * @return 社团列表
     */
    @Override
    public List<Society> teacherSocietyManageSearch(String searchContent, String tNo) {
        var societies = societyMapper.findSocietiesByTno(tNo);
        var societyArrayList = new ArrayList<Society>();
        for (Society society : societies) {
            if (searchJudge(searchContent, society.toString())) {
                societyArrayList.add(society);
            }
        }
        return societyArrayList;
    }

    /**
     * 教师查询自己的社团申请
     *
     * @return 教师社团申请列表
     */
    @Override
    public PageInfo<TeacherSocietyApplication> teacherFindSocietyApplication(String tNo, int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = teacherSocietyApplicationMapper.findTeacherSocietyApplicationsByTno(tNo);
        return new PageInfo<>(list);
    }

    /**
     * 筛选教师的社团申请
     *
     * @param searchContent 搜索内容
     * @param tNo           教师号
     * @return 教师社团申请列表
     */
    @Override
    public List<TeacherSocietyApplication> teacherSocietyGuideSearch(String searchContent, String tNo) {
        var applications = teacherSocietyApplicationMapper.findTeacherSocietyApplicationsByTno(tNo);
        var applicationList = new ArrayList<TeacherSocietyApplication>();
        for (TeacherSocietyApplication application : applications) {
            if (searchJudge(searchContent, application.toString())) {
                applicationList.add(application);
            }
        }
        return applicationList;
    }

    /**
     * 教师查看社团申请详情
     *
     * @param applicationNo 申请编号
     * @param tNo           教师号
     * @return 教师的对应社团申请对象
     */
    @Override
    public SocietyApplication teacherFindTeacherSocietyApplication(Integer applicationNo, String tNo) {
        TeacherSocietyApplication teacherSocietyApplication = teacherSocietyApplicationMapper.findTeacherSocietyApplicationsByApplicationNoAndTno(applicationNo, tNo);
        // 查询申请记录
        return societyApplicationMapper.findSocietyApplicationByApplicationNo(teacherSocietyApplication.getApplicationNo());
    }

    /**
     * 教师查看社团详情
     *
     * @param societyId 社团账号
     * @return 社团对象
     */
    @Override
    public Society teacherFindSociety(String societyId) {
        return societyMapper.findSocietyBySocietyId(societyId);
    }

    /**
     * 查看教师课程码
     *
     * @return 教师课程列表
     */
    @Override
    public Set<String> teacherFindCourseCode() {
        var teacherCourses = teacherCourseMapper.findAllTeacherCourses();
        Set<String> courseCodeSet = new HashSet<>();
        for (TeacherCourse teacherCourse : teacherCourses) {
            courseCodeSet.add(teacherCourse.getCourseClass());
        }
        return courseCodeSet;
    }

    /**
     * 教师查询自己发起打卡的记录
     *
     * @param tNo 教师号
     * @return 发起打卡记录列表
     */
    @Override
    public PageInfo<Attendance> teacherFindAttendance(String tNo, int pageNum) {
        PageHelper.startPage(pageNum, 10);
        var list = attendanceMapper.findByTNo(tNo);
        return new PageInfo<>(list);
    }

    /**
     * 教师更新社团申请状态
     *
     * @param applicationNo 申请编号
     * @param result        状态
     * @return 0:成功，1：失败
     */
    @Override
    public int teacherUpdateSocietyApplication(int applicationNo, String tNo, int result) {
        // 对象封装
        TeacherSocietyApplication teacherSocietyApplication = new TeacherSocietyApplication();
        teacherSocietyApplication.setApplicationNo(applicationNo);
        teacherSocietyApplication.setTNo(tNo);
        teacherSocietyApplication.setStatus(result);
        if (teacherSocietyApplicationMapper.updateStatus(teacherSocietyApplication) == 0) {
            return 1;
        } else {
            //同意
            return 0;
        }
    }

    /**
     * 教师查询活动审批记录
     *
     * @param tNo 教师
     * @return 教师活动审批列表
     */
    @Override
    public PageInfo<TeacherActivityApplication> teacherFindActivityApplicationsByTno(String tNo, int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = teacherActivityApplicationMapper.findTeacherActivityApplicationsByTno(tNo);
        return new PageInfo<>(list);
    }

    /**
     * 筛选教师活动审批记录
     *
     * @param searchContent 搜索内容
     * @param tNo           教师号
     * @return 教师活动审批列表
     */
    @Override
    public List<TeacherActivityApplication> teacherActivityCheckSearch(String searchContent, String tNo) {
        var applications = teacherActivityApplicationMapper.findTeacherActivityApplicationsByTno(tNo);
        var applicationList = new ArrayList<TeacherActivityApplication>();
        for (TeacherActivityApplication application : applications) {
            if (searchJudge(searchContent, application.toString())) {
                applicationList.add(application);
            }
        }
        return applicationList;
    }

    /**
     * 教师查找某条活动审批记录
     *
     * @param applicationNo 申请编号
     * @param tNo           教师号
     * @return 教师活动审批对象
     */
    @Override
    public TeacherActivityApplication teacherFindActivityApplication(Integer applicationNo, String tNo) {
        return teacherActivityApplicationMapper.findTeacherActivityApplicationByApplicationNoAndTno(applicationNo, tNo);
    }

    /**
     * 教师更新活动审批状态
     *
     * @param application 申请编号
     * @param tNo         教师号
     * @param result      审批状态
     * @return 0：成功；1：失败
     */
    @Override
    public int teacherUpdateActivityApplication(Integer application, String tNo, Integer result) {
        //封装对象
        TeacherActivityApplication teacherActivityApplication = new TeacherActivityApplication();
        teacherActivityApplication.setApplicationNo(application);
        teacherActivityApplication.setTNo(tNo);
        teacherActivityApplication.setStatus(result);
        if (teacherActivityApplicationMapper.updateTeacherActivityApplication(teacherActivityApplication) != 0) {
            //同意
            return 0;
        }
        return 1;
    }

    /**
     * 教师查看请假记录
     *
     * @param tNo     教师号
     * @param pageNum 页码
     * @return 教师请假记录列表
     */
    @Override
    public PageInfo<TeacherLeaveRecord> teacherFindTeacherLeaveRecord(String tNo, int pageNum) {
        PageHelper.startPage(pageNum, 10);
        var list = teacherLeaveRecordMapper.findLeaveRecordByTno(tNo);
        return new PageInfo<>(list);
    }

    /**
     * 教师查看考勤记录
     *
     * @param teacher 教师
     * @param pageNum 页码
     * @return 教师考勤记录列表
     */
    @Override
    public PageInfo<ClockRecord> teacherFindClockRecord(Teacher teacher, int pageNum) {
        PageHelper.startPage(pageNum, 10);
        var list = clockRecordMapper.findClockRecordsByTno(teacher.getTNo());
        return new PageInfo<>(list);
    }

    /**
     * 教师筛选考勤记录
     *
     * @param searchContent 搜索内容
     * @param teacher       教师对象
     * @return 考勤记录列表对象
     */
    @Override
    public List<ClockRecord> teacherClockRecordTSearch(String searchContent, Teacher teacher) {
        List<ClockRecord> clockRecords;
        if (teacher.getRoleName().equals("辅导员")) {
            clockRecords = clockRecordMapper.findByManageTno(teacher.getTNo());
        } else {
            clockRecords = clockRecordMapper.findByCourseTno(teacher.getTNo());
        }
        var clockRecordList = new ArrayList<ClockRecord>();
        for (ClockRecord clockRecord : clockRecords) {
            if (searchJudge(searchContent, clockRecord.toString())) {
                clockRecordList.add(clockRecord);
            }
        }
        return clockRecordList;
    }

    /**
     * 教师按班级号和状态查找学生
     *
     * @param classNo 班级号
     * @param status  状态值
     * @return 学生列表
     */
    @Override
    public List<Student> teacherFindStudentByClassAndStatus(String classNo, String status) {
        if ("present".equals(status)) {
            return studentMapper.findAttendStudentsByClassNo(classNo);
        } else {
            return studentMapper.findAbsentStudentsByClassNo(classNo);
        }
    }
}
