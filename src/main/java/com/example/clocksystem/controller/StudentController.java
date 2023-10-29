package com.example.clocksystem.controller;

import com.example.clocksystem.entity.*;
import com.example.clocksystem.service.StudentService;
import com.example.clocksystem.utils.ImageConvert;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.example.clocksystem.utils.SearchJudge.searchJudge;

/**
 * StudentController
 *
 * @author 94548
 * @date 2023/9/10
 */
@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    /**
     * 展示学生主页
     *
     * @return 学生主页视图
     */
    @GetMapping("/studentHome")
    public String studentHome() {
        return "/student/student";
    }

    //    第一个功能：修改信息

    /**
     * 展示修改学生详细信息页
     *
     * @return 学生详情页视图
     */
    @GetMapping("/studentUpdate")
    public String showStudentDetailPage(HttpSession session, Model model) {
        var student = (Student) session.getAttribute("student");
        model.addAttribute("student", student);
        return "/student/studentInfo/studentUpdate";
    }

    /**
     * 更改学生信息
     *
     * @param student 接收学生对象
     * @param session 获取登录的session对象
     * @return 页面视图
     */
    @PostMapping("/updateStudent")
    @ResponseBody
    public int updateStudent(Student student, HttpSession session) {
        Student sessionStudent = (Student) session.getAttribute("student");
        student.setClassNo(sessionStudent.getClassNo());
        if (studentService.studentUpdateInfo(student) > 0) {
            // 信息更改成功
            // 更新session中的数据
            sessionStudent.setSName(student.getSName());
            sessionStudent.setPassword(student.getPassword());
            sessionStudent.setSPhone(student.getSPhone());
            session.setAttribute("student", sessionStudent);
            return 0;
        } else {
            //信息更改不成功
            return 1;
        }
    }

    //    第二个功能：考勤打卡

    /**
     * 展示打卡页面视图
     *
     * @return 打卡页面视图
     */
    @GetMapping("/attendance")
    public String showAttendPage(HttpSession session, Model model) {
        var student = (Student) session.getAttribute("student");
        var courseList = studentService.studentFindCourseList(student.getSNo());
        model.addAttribute("courseList", courseList);
        return "/student/attendance/attendance";
    }

    /**
     * 学生打卡
     *
     * @param courseClass 课程码
     * @param code        签到码
     * @param session     登录session对象
     * @return 0:打卡成功，1:打卡失败
     */
    @PostMapping("/studentAttendance")
    @ResponseBody
    public int attendance(String courseClass, String code, HttpSession session) {
        // 获取学生对象
        var student = (Student) session.getAttribute("student");
        return studentService.studentAttendance(courseClass, code, student);
    }

    /**
     * 展示添加课程页面视图
     *
     * @return 添加课程页面视图
     */
    @GetMapping("/selectCourse")
    public String showSelectCoursePage() {
        return "/student/attendance/selectCourse";
    }

    /**
     * 学生添加课程
     *
     * @param courseClass 课程码
     * @param session     登录session对象
     * @return 0:添加成功，1:添加失败（课程码错误），2：添加失败（时间冲突）
     */
    @PostMapping("/courseSelect")
    @ResponseBody
    public int addCourse(String courseClass, HttpSession session) {
        var student = (Student) session.getAttribute("student");
        return studentService.studentAddCourse(courseClass, student);
    }

    //    第三个功能：请假记录

    /**
     * 展示查询请假记录视图
     *
     * @param session 登录session对象
     * @param model   model对象
     * @param pageNum 页码
     * @return 请假记录视图
     */
    @GetMapping("/stuLeaveRecord")
    public String showLeaveRecordPage(HttpSession session, Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var student = (Student) session.getAttribute("student");
        var leaveList = studentService.studentLeaveRecord(student, pageNum);
        model.addAttribute("pages", leaveList);
        return "/student/leaveRecord/leaveRecord";
    }

    /**
     * 展示请假详情视图
     *
     * @param leaveNo 请假编号
     * @param session 登录session对象
     */
    @PostMapping("/stuLeaveDetail")
    @ResponseBody
    public void showLeaveDetailRecord(Integer leaveNo, HttpSession session) {
        session.setAttribute("leaveNo", leaveNo);
    }

    /**
     * 接受二次跳转请求至请假详情页
     *
     * @return 请假详情视图
     */
    @GetMapping("/leaveDetail")
    public String jumpLeaveDetailPage(HttpSession session, Model model) {
        var leaveNo = (Integer) session.getAttribute("leaveNo");
        var leaveRecord = studentService.studentFindLeaveRecordDetail(leaveNo);
        model.addAttribute("leaveRecord", leaveRecord);
        return "/student/leaveRecord/leaveDetail";
    }

    /**
     * 展示请假页
     *
     * @return 请假页视图
     */
    @GetMapping("/addLeave")
    public String showAddLeavePage() {
        return "/student/leaveRecord/addLeave";
    }

    /**
     * 删除请假详情的session记录
     *
     * @param session session对象
     */
    @GetMapping("/destroy")
    @ResponseBody
    public void destroyLeaveDetail(HttpSession session) {
        if (session != null && session.getAttribute("leaveDetail") != null) {
            session.removeAttribute("leaveDetail");
        }
    }

    /**
     * 学生请假
     *
     * @param session   登录session对象
     * @param model     model对象
     * @param startTime 请假起始时间
     * @param endTime   请假结束时间
     * @param reason    请假原因
     * @return 请假记录页面视图
     */
    @PostMapping("/studentLeaveRequest")
    public String leaveRequest(HttpSession session, Model model, String startTime, String endTime, String reason) {
        var student = (Student) session.getAttribute("student");
        studentService.studentLeaveRequest(startTime, endTime, reason, student.getSNo());
        return showLeaveRecordPage(session, model, 1);
    }

    /**
     * 请假记录筛选
     *
     * @param searchContent 搜索框内容
     * @param session       session对象
     * @param model         model对象
     * @return 请假记录页面视图
     */
    @PostMapping("/leaveRecordSearch")
    public String leaveRecordSearch(String searchContent, HttpSession session, Model model) {
        var student = (Student) session.getAttribute("student");
        //搜索框没内容默认全显示
        if (searchContent == null || searchContent.isEmpty()) {
            return showLeaveRecordPage(session, model, 1);
        }
        var list = new PageInfo<>(studentService.studentLeaveRecordSearch(searchContent, student));
        model.addAttribute("pages", list);
        return "/student/leaveRecord/leaveRecord";
    }

    //    第四个功能：考勤记录

    /**
     * 展示考勤记录页面
     *
     * @param session 登录session对象
     * @param model   model对象
     * @param pageNum 页码
     * @return 考勤记录视图
     */
    @GetMapping("/clockRecord")
    public String showClockRecordPage(HttpSession session, Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var student = (Student) session.getAttribute("student");
        var stuClockRecordList = studentService.studentFindClockRecords(student.getSNo(), pageNum);
        model.addAttribute("pages", stuClockRecordList);
        return "/student/clockRecord/clockRecord";
    }

    /**
     * 展示申诉页面
     *
     * @param clockNo 申诉的考勤编号
     * @param session session对象
     */
    @PostMapping("/stuAppeal")
    @ResponseBody
    public void showAddClockPage(Integer clockNo, HttpSession session) {
        session.setAttribute("clockNo", clockNo);
    }

    /**
     * 接受二次跳转请求至申诉页面
     *
     * @param session session对象
     * @return 申诉页面视图
     */
    @GetMapping("/appeal")
    public String jumpAddClockPage(HttpSession session, Model model) {
        var clockNo = (Integer) session.getAttribute("clockNo");
        var clockRecordDetail = studentService.studentFindClockRecord(clockNo);
        model.addAttribute("clockRecordDetail", clockRecordDetail);
        return "/student/clockRecord/addClock";
    }

    /**
     * 学生申诉
     *
     * @param session session对象
     * @param reason  申诉原因
     * @param model   model对象
     * @return 考勤记录视图
     */
    @PostMapping("/submitAppeal")
    public String appeal(HttpSession session, String reason, Model model) {
        var clockNo = (Integer) session.getAttribute("clockNo");
        var student = (Student) session.getAttribute("student");
        // 申诉
        studentService.studentAppeal(reason, student, clockNo);
        return showClockRecordPage(session, model, 1);
    }

    /**
     * 考勤记录筛选
     *
     * @param searchContent 筛选框内容
     * @param session       session对象
     * @param model         model对象
     * @return 考勤记录页面视图
     */
    @PostMapping("/clockRecordSearch")
    public String clockRecordSearch(String searchContent, HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("student");
        //搜索框没内容默认全显示
        if (searchContent == null || searchContent.isEmpty()) {
            return showClockRecordPage(session, model, 1);
        }
        var list = new PageInfo<>(studentService.studentClockRecordSearch(searchContent, student.getSNo()));
        model.addAttribute("pages", list);
        return "/student/clockRecord/clockRecord";
    }

    // 第五个功能：活动打卡

    /**
     * 展示活动管理页面
     *
     * @param model   model对象
     * @param session session对象
     * @param pageNum 页码
     * @return 活动列表页面视图
     */
    @GetMapping("/activityManage")
    public String showActivityManagePage(Model model, HttpSession session, @RequestParam(defaultValue = "1") Integer pageNum) {
        var student = (Student) session.getAttribute("student");
        var studentActivityList = studentService.studentFindActivityList(student.getSNo(), pageNum);
        model.addAttribute("pages", studentActivityList);
        return "/student/activityManage/activityManage";
    }

    /**
     * 学生活动筛选
     * @param session session对象
     * @param model model对象
     * @param searchContent 搜索内容
     * @return 活动列表页面视图
     */
    @PostMapping("/activityManageSearch")
    public String activityManageSearch(HttpSession session, Model model, String searchContent) {
        if (searchContent == null || searchContent.isEmpty()) {
            return showActivityManagePage(model, session, 1);
        }
        Student student = (Student) session.getAttribute("student");
        var list = new PageInfo<>(studentService.studentActivityManageSearch(searchContent, student.getSNo()));
        model.addAttribute("pages", list);
        return "/student/activityManage/activityManage";
    }

    /**
     * 展示学生活动详情页面
     *
     * @param activityNo 活动号
     * @param session    session对象
     */
    @PostMapping("/studentActivityDetail")
    @ResponseBody
    public void showStudentActivityDetailPage(Integer activityNo, HttpSession session) {
        session.setAttribute("activityNo", activityNo);
    }

    /**
     * 接受二次跳转请求至详情页
     *
     * @param session session对象
     * @param model   model对象
     * @return 活动详情页视图
     */
    @GetMapping("/studentActivity")
    public String jumpStudentActivityDetailPage(HttpSession session, Model model) {
        var activityNo = (Integer) session.getAttribute("activityNo");
        var student = (Student) session.getAttribute("student");
        var studentActivity = studentService.studentFindStudentActivity(student.getSNo(), activityNo);
        model.addAttribute("studentActivity", studentActivity);
        return "/student/activityManage/activityManageDetails";
    }

    /**
     * 学生进行活动打卡
     *
     * @param session session对象
     * @param code    签到码
     * @return 0:打卡成功，1：打卡失败
     */
    @PostMapping("/studentActivityAttendance")
    @ResponseBody
    public int studentActivityAttendance(HttpSession session, String code) {
        var activityNo = (Integer) session.getAttribute("activityNo");
        var student = (Student) session.getAttribute("student");
        return studentService.studentActivityAttendance(student.getSNo(), activityNo, code);
    }

    /**
     * 学生查看所有活动
     *
     * @param session session对象
     * @param model   model对象
     * @param pageNum 页码
     * @return 活动视图
     */
    @GetMapping("/activityList")
    public String showActivityListPage(HttpSession session, Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var student = (Student) session.getAttribute("student");
        var activityList = studentService.studentFindAllActivityList(student.getSNo(), pageNum);
        model.addAttribute("pages", activityList);
        return "/student/activityManage/activityList";
    }

    /**
     * 活动筛选
     * @param session session对象
     * @param model model对象
     * @param searchContent 搜索内容
     * @return 活动视图
     */
    @PostMapping("/activityListSearch")
    public String activityListSearch(HttpSession session, Model model, String searchContent) {
        if (searchContent == null || searchContent.isEmpty()) {
            return showActivityListPage(session, model, 1);
        }
        Student student = (Student) session.getAttribute("student");
        var list = new PageInfo<>(studentService.studentActivityListSearch(searchContent, student.getSNo()));
        model.addAttribute("pages", list);
        return "/student/activityManage/activityList";
    }

    /**
     * 展示活动列表
     */
    @PostMapping("/allActivity")
    @ResponseBody
    public void showActivityListPage(Integer activityNo, HttpSession session) {
        session.setAttribute("activityNo", activityNo);
    }

    /**
     * 接受二次跳转至详情页（所有活动）
     *
     * @param session session对象
     * @param model   model对象
     * @return 活动详情视图
     */
    @GetMapping("/activityDetail")
    public String jumpActivityDetailPage(HttpSession session, Model model) {
        var activityNo = (Integer) session.getAttribute("activityNo");
        var activity = studentService.studentFindActivity(activityNo);
        model.addAttribute("activity", activity);
        return "/student/activityManage/activityListDetails";
    }

    /**
     * 学生参加活动
     *
     * @param activityNo 活动编号
     * @param session    session对象
     * @return 0:添加成功,1:添加失败
     */
    @PostMapping("/joinActivity")
    @ResponseBody
    public int studentJoinActivity(Integer activityNo, HttpSession session) {
        var student = (Student) session.getAttribute("student");
        return studentService.studentJoinActivity(student, activityNo);
    }

    // 第六个功能：申请社团

    /**
     * 展示社团管理页面
     *
     * @param model   model对象
     * @param session session对象
     * @return 社团管理页面视图
     */
    @GetMapping("/societyManage")
    public String showSocietyManagePage(Model model, HttpSession session, @RequestParam(defaultValue = "1") Integer pageNum) {
        var student = (Student) session.getAttribute("student");
        var societyApplications = studentService.studentFindSocietyApplication(student.getSNo(), pageNum);
        model.addAttribute("pages", societyApplications);
        return "/student/societyManage/societyManage";
    }

    /**
     * 展示社团管理页面的筛选
     * @param session session对象
     * @param model model对象
     * @param searchContent 搜索内容
     * @return 社团管理页面视图
     */
    @PostMapping("/societyManageSearch")
    public String societyManageSearch(HttpSession session, Model model, String searchContent) {
        if (searchContent == null || searchContent.isEmpty()) {
            return showSocietyManagePage(model, session, 1);
        }
        Student student = (Student) session.getAttribute("student");
        var list = new PageInfo<>(studentService.studentSocietyManageSearch(searchContent, student.getSNo()));
        model.addAttribute("pages", list);
        return "/student/societyManage/societyManage";
    }

    /**
     * 展示社团申请详情
     *
     * @param session       session对象
     * @param applicationNo 申请编号
     */
    @PostMapping("/societyApplicationDetail")
    @ResponseBody
    public void showSocietyApplicationDetail(HttpSession session, int applicationNo) {
        session.setAttribute("applicationNo", applicationNo);
    }

    /**
     * 接受二次跳转至社团申请详情页
     *
     * @param session session对象
     * @param model   model对象
     * @return 社团申请详情视图
     */
    @GetMapping("/societyApplication")
    public String jumpSocietyApplicationDetail(HttpSession session, Model model) {
        var applicationNo = (Integer) session.getAttribute("applicationNo");
        var societyApplication = studentService.studentFindSocietyApplicationDetail(applicationNo);
        model.addAttribute("societyApplication", societyApplication);
        model.addAttribute("imageData", ImageConvert.convertByteArrayToBase64(societyApplication.getPicture()));
        return "/student/societyManage/societyCreateDetails";
    }

    /**
     * 展示社团申请页面
     *
     * @return 社团申请视图
     */
    @GetMapping("/societyCreate")
    public String showSocietyCreatePage() {
        return "/student/societyManage/societyCreate";
    }

    /**
     * 学院信息
     *
     * @return 学院列表
     */
    @GetMapping("/departmentData")
    @ResponseBody
    public List<Department> getAllDepartments() {
        return studentService.studentFindDepartments();
    }

    /**
     * 学院教师
     *
     * @param departmentId 学院号
     * @return 教师列表
     */
    @GetMapping("/departmentTeachers")
    @ResponseBody
    public List<Teacher> getDepartmentTeachers(String departmentId) {
        return studentService.studentFindTeacherListByDepartment(departmentId);
    }

    /**
     * 学生申请创建社团
     *
     * @param picture     图片
     * @param societyName 社团名
     * @param teacherNo   教师号
     * @param introduce   介绍
     * @param reason      原因
     * @param session     session对象
     * @param model       model对象
     * @return 对应视图
     */
    @PostMapping("/createSocietyApplication")
    public String createSocietyApplication(MultipartFile picture, String societyName, String teacherNo, String introduce, String reason, HttpSession session, Model model) {
        var student = (Student) session.getAttribute("student");
        if (studentService.studentCreateSocietyApplication(picture, societyName, teacherNo, introduce, reason, student) == 0) {
            return showSocietyManagePage(model, session, 1);
        } else {
            model.addAttribute("errMsg", "申请失败,图片大小超过5MB!");
            return "/student/societyManage/societyCreate";
        }
    }


    // 第七个功能：学分统计

    /**
     * 学分统计视图
     *
     * @param session session对象
     * @param model   model对象
     * @return 学生学分统计视图
     */
    @GetMapping("/creditCount")
    public String showCreditCount(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("student");
        model.addAttribute("credit", studentService.studentFindStudentCredit(student.getSNo()));
        return "/student/creditCount/creditCount";
    }

    /**
     * 筛选学分
     *
     * @param session   session对象
     * @param model     model对象
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 学分展示视图
     */
    @PostMapping("/creditSearch")
    public String creditSearch(HttpSession session, Model model, String startTime, String endTime) {
        //判断时间是否为空
        if (startTime.equals("")) {
            startTime = "1970-01-01T08:00";
            if (endTime.equals("")) {
                //都为空则回到开始的学分视图
                return showCreditCount(session, model);
            }
        }
        //筛选
        Student student = (Student) session.getAttribute("student");
        StudentCredit studentCredit = studentService.studentCreditSearch(student.getSNo(), startTime, endTime);
        model.addAttribute("credit", studentCredit);
        return "/student/creditCount/creditCount";
    }
}
