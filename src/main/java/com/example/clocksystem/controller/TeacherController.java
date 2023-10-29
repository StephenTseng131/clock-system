package com.example.clocksystem.controller;

import com.example.clocksystem.entity.*;
import com.example.clocksystem.entity.Class;
import com.example.clocksystem.service.TeacherService;
import com.example.clocksystem.utils.CreateClassCode;
import com.example.clocksystem.utils.ImageConvert;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.clocksystem.utils.SearchJudge.searchJudge;

/**
 * TeacherController
 *
 * @author 94548
 * @date 2023/9/10
 */
@Controller
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    TeacherService teacherService;

    /**
     * 展示教师主页
     *
     * @return 教师主页视图
     */
    @GetMapping("/teacherHome")
    public String teacherHome() {
        return "/teacher/teacher";
    }

//    第一个功能：修改信息

    /**
     * 展示修改教师详情信息页
     *
     * @return 教师修改详情页视图
     */
    @GetMapping("/teacherUpdate")
    public String showStudentDetailPage() {
        return "/teacher/teacherInfo/teacherUpdate";
    }

    /**
     * 教师修改信息
     *
     * @param teacher teacher对象
     * @param session session对象
     * @return 0:修改成功，1:修改失败
     */
    @PostMapping("/updateTeacher")
    @ResponseBody
    public int updateTeacher(Teacher teacher, HttpSession session) {
        if (teacherService.teacherUpdateInfo(teacher) > 0) {
            // 修改成功，修改session
            session.setAttribute("teacher", teacher);
            return 0;
        }
        // 修改失败
        return 1;
    }

//    第二个功能：打卡记录

    /**
     * 展示打卡记录页面
     *
     * @param session session对象
     * @param model   model对象
     * @return 打卡记录视图
     */
    @GetMapping("/attendRecord")
    public String showAttendRecordPage(HttpSession session, Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var teacher = (Teacher) session.getAttribute("teacher");
        var attendances = teacherService.teacherFindAttendance(teacher.getTNo(), pageNum);
        model.addAttribute("pages", attendances);
        return "/teacher/attendRecord/attendRecord";
    }

    /**
     * 展示发起打卡页面
     *
     * @param session session对象
     * @param model   model对象
     * @return 发起打卡页面视图
     */
    @GetMapping("/addAttend")
    public String showAddAttendPage(HttpSession session, Model model) {
        var teacher = (Teacher) session.getAttribute("teacher");
        var teacherCourseList = teacherService.teacherFindCourseCodeCourses(teacher.getTNo());
        model.addAttribute("teacherCourseList", teacherCourseList);
        return "/teacher/attendRecord/addAttend";
    }

    /**
     * 教师发布打卡
     *
     * @param courseClass 课程码
     * @param startTime   打卡起始时间
     * @param endTime     打卡结束时间
     * @param code        打卡码
     * @param session     session对象
     * @return 发布打卡页面视图
     */
    @PostMapping("/attendance")
    @ResponseBody
    public int attendance(String courseClass, String startTime, String endTime, String code, HttpSession session) {
        var teacher = (Teacher) session.getAttribute("teacher");

        if (courseClass.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || code.isEmpty()) {
            return 2;
        }
        if (teacherService.teacherAttendance(courseClass, startTime, endTime, code, teacher) == 0) {
            // 打卡发布成功
            return 0;
        }
        return 1;
    }

    /**
     * 展示添加课程页面
     *
     * @return 添加课程页面视图
     */
    @GetMapping("/addCourse")
    public String showAddCoursePage(Model model, HttpSession session) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        List<Course> courseList = teacherService.teacherFindNoCourseCodeCourses(teacher.getTNo());
        model.addAttribute("courseList", courseList);
        return "/teacher/attendRecord/addCourse";
    }

    /**
     * 产生课程码
     *
     * @return 课程码
     */
    @GetMapping("/generateCourseCode")
    @ResponseBody
    public String generateCourseCode() {
        String courseCode;
        Set<String> usedCourseCodes = teacherService.teacherFindCourseCode();
        do {
            // 生成固定位数的课程码
            courseCode = CreateClassCode.generateRandomCourseCode(8);
        } while (usedCourseCodes.contains(courseCode));
        usedCourseCodes.add(courseCode);
        return courseCode;
    }

    /**
     * 教师添加课程的课程码
     *
     * @param session     session对象
     * @param model       model对象
     * @param courseNo    课程编号
     * @param courseClass 课程码
     * @return 对应的页面视图
     */
    @PostMapping("/addCourseCode")
    public String addCourseCode(HttpSession session, Model model, Integer courseNo, String courseClass) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        // 添加课程
        teacherService.teacherAddCourseCode(teacher, courseNo, courseClass);
        return showAddAttendPage(session, model);
    }

    /**
     * 打卡筛选功能
     *
     * @param session       session对象
     * @param searchContent 筛选框内容
     * @param model         model对象
     * @return 打卡记录页面视图
     */
    @PostMapping("/attendSearch")
    public String attendSearch(HttpSession session, String searchContent, Model model) {
        var teacher = (Teacher) session.getAttribute("teacher");
        //搜索框没内容默认全显示
        if (searchContent == null || searchContent.isEmpty()) {
            return showAttendRecordPage(session, model, 1);
        }
        var attendanceList = new PageInfo<>(teacherService.teacherAttendancesSearch(searchContent, teacher.getTNo()));
        model.addAttribute("pages", attendanceList);
        return "/teacher/attendRecord/attendRecord";
    }

    // 第三个功能：请假记录

    /**
     * 请假记录视图
     *
     * @param model   model对象
     * @param session session对象
     * @param pageNum 页码
     * @return 请假记录视图
     */
    @GetMapping("/leaveRecord")
    public String showLeaveRecordPage(Model model, HttpSession session, @RequestParam(defaultValue = "1") Integer pageNum) {
        var teacher = (Teacher) session.getAttribute("teacher");
        var leaveRecord = teacherService.teacherFindTeacherLeaveRecord(teacher.getTNo(), pageNum);
        model.addAttribute("pages", leaveRecord);
        return "/teacher/leaveRecord/leaveRecord";
    }

    /**
     * 请假记录筛选
     *
     * @param searchContent 搜索内容
     * @param model         model对象
     * @param session       session对象
     * @return 请假记录视图
     */
    @PostMapping("/leaveRecordTSearch")
    public String leaveRecordTSearch(String searchContent, Model model, HttpSession session) {
        if (searchContent == null || searchContent.isEmpty()) {
            return showLeaveRecordPage(model, session, 1);
        }
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        var pages = new PageInfo<>(teacherService.teacherLeaveRecordSearch(searchContent, teacher.getTNo()));
        model.addAttribute("pages", pages);
        return "/teacher/leaveRecord/leaveRecord";
    }

    // 第四个功能：请假审批

    /**
     * 展示请假审批页面
     *
     * @param session session对象
     * @param model   model对象
     * @return 请假审批页面视图
     */
    @GetMapping("/leaveRecordJudge")
    public String showLeaveRecordJudgePage(HttpSession session, Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var teacher = (Teacher) session.getAttribute("teacher");
        var teacherLeaveRecordList = teacherService.teacherFindLeaveRecordsToTeacher(teacher.getTNo(), pageNum);
        model.addAttribute("pages", teacherLeaveRecordList);
        return "/teacher/leaveRecordJudge/leaveRecordJudge";
    }

    /**
     * 展示请假审批详情页
     *
     * @param leaveNo 请假编号
     * @param session session对象
     */
    @PostMapping("/leaveDetailT")
    @ResponseBody
    public void showLeaveDetail(Integer leaveNo, HttpSession session) {
        var leaveRecord = teacherService.teacherFindTeacherLeaveRecord(leaveNo);
        session.setAttribute("leaveRecordT", leaveRecord);
    }

    /**
     * 接受二次跳转请求至请假审批详情页
     *
     * @return 请假审批详情页
     */
    @GetMapping("/jumpLeaveDetailT")
    public String jumpLeaveDetailPage() {
        return "/teacher/leaveRecordJudge/leaveDetailT";
    }

    /**
     * 更新请假记录
     *
     * @param leaveNo 请假编号
     * @param result  审批结果
     * @param session session对象
     * @param model   model对象
     * @return 请假审批页视图
     */
    @PostMapping("/updateLeaveStatus")
    public String updateLeaveStatus(Integer leaveNo, Integer result, HttpSession session, Model model) {
        var teacher = (Teacher) session.getAttribute("teacher");
        var newTeacher = teacherService.teacherUpdateLeaveStatus(leaveNo, result, teacher);
        //更新session
        session.setAttribute("teacher", newTeacher);
        return showLeaveRecordJudgePage(session, model, 1);
    }

    /**
     * 请假审批筛选功能
     *
     * @param searchContent 筛选框内容
     * @param session       session对象
     * @param model         model对象
     * @return 请假审批页面视图
     */
    @PostMapping("/leaveRecordJudgeSearch")
    public String leaveRecordJudgeSearch(String searchContent, HttpSession session, Model model) {
        var teacher = (Teacher) session.getAttribute("teacher");
        //搜索框没内容默认全显示
        if (searchContent == null || searchContent.isEmpty()) {
            return showLeaveRecordJudgePage(session, model, 1);
        }
        var pages = new PageInfo<>(teacherService.teacherLeaveRecordSearch(searchContent, teacher.getTNo()));
        model.addAttribute("pages", pages);
        return "/teacher/leaveRecordJudge/leaveRecordJudge";
    }

    // 第五个功能：考勤记录

    /**
     * 考勤统计视图
     *
     * @param model   model对象
     * @param session session对象
     * @param pageNum 页码
     * @return 考勤统计视图
     */
    @GetMapping("/clockRecord")
    public String showClockRecordPage(Model model, HttpSession session, @RequestParam(defaultValue = "1") Integer pageNum) {
        var teacher = (Teacher) session.getAttribute("teacher");
        var clockRecords = teacherService.teacherFindClockRecord(teacher, pageNum);
        model.addAttribute("pages", clockRecords);
        return "/teacher/clockRecord/clockRecord";
    }

    /**
     * 考勤记录筛选
     *
     * @param session       session对象
     * @param model         model对象
     * @param searchContent 搜索内容
     * @return 考勤记录视图
     */
    @PostMapping("/clockRecordTSearch")
    public String clockRecordTSearch(HttpSession session, Model model, String searchContent) {
        var teacher = (Teacher) session.getAttribute("teacher");
        if (searchContent == null || searchContent.isEmpty()) {
            return showClockRecordPage(model, session, 1);
        }
        var clockRecordList = new PageInfo<>(teacherService.teacherClockRecordTSearch(searchContent, teacher));
        model.addAttribute("pages", clockRecordList);
        return "/teacher/clockRecord/clockRecord";
    }

    //第四个功能：考勤申诉

    /**
     * 展示申诉页面
     *
     * @param session session对象
     * @param model   model对象
     * @return 申诉审批页面视图
     */
    @GetMapping("/clockRecordJudge")
    public String showClockRecordJudgePage(HttpSession session, Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var teacher = (Teacher) session.getAttribute("teacher");
        var appealList = teacherService.teacherFindStudentAppealToTeacher(teacher.getTNo(), pageNum);
        model.addAttribute("pages", appealList);
        return "/teacher/clockRecordJudge/clockRecordJudge";
    }

    /**
     * 查看申诉详情
     *
     * @param appealNo 申诉编号
     * @param session  session对象
     */
    @PostMapping("/clockDetail")
    @ResponseBody
    public void showClockDetailPage(Integer appealNo, HttpSession session) {
        var stuAppeal = teacherService.teacherFindStudentAppealDetail(appealNo);
        session.setAttribute("appeal", stuAppeal);
    }

    /**
     * 接受二次跳转至申诉审批详情
     *
     * @return 申诉审批页面视图
     */
    @GetMapping("/jumpClockDetail")
    public String jumpClockDetail() {
        return "/teacher/clockRecordJudge/clockDetail";
    }

    /**
     * 更新考勤状态
     *
     * @param appealNo 申诉编号
     * @param result   考勤状态
     * @param session  session对象
     * @param model    model对象
     * @return 考勤审批页面视图
     */
    @PostMapping("/updateClockStatus")
    public String updateClockStatus(Integer appealNo, Integer result, HttpSession session, Model model) {
        var teacher = (Teacher) session.getAttribute("teacher");
        //更新session
        var newTeacher = teacherService.teacherUpdateAppealStatus(appealNo, result, teacher);
        session.setAttribute("teacher", newTeacher);
        return showClockRecordJudgePage(session, model, 1);
    }

    /**
     * 考勤申诉审批记录筛选
     *
     * @param searchContent 筛选框内容
     * @param session       session对象
     * @param model         model对象
     * @return 考勤申诉审批页面视图
     */
    @PostMapping("/clockRecordJudgeSearch")
    public String clockRecordJudgeSearch(String searchContent, HttpSession session, Model model) {
        var teacher = (Teacher) session.getAttribute("teacher");
        //搜索框没内容默认全显示
        if (searchContent == null || searchContent.isEmpty()) {
            return showClockRecordJudgePage(session, model, 1);
        }
        var list = new PageInfo<>(teacherService.teacherClockRecordJudgeSearch(searchContent, teacher.getTNo()));
        model.addAttribute("pages", list);
        return "/teacher/clockRecordJudge/clockRecordJudge";
    }

//    第五个功能：考勤统计

    /**
     * 任课教师展示考勤统计页面
     *
     * @return 课程考勤记录页面视图
     */
    @GetMapping("/clockCount")
    public String showClockCountPage(HttpSession session, Model model) {
        var teacher = (Teacher) session.getAttribute("teacher");
        var courses = teacherService.teacherFindCourse(teacher.getTNo());
        model.addAttribute("courses", courses);
        return "/teacher/clockCount/clockCountCourse";
    }

    /**
     * 辅导员展示考勤统计页面
     *
     * @return 班级考勤记录页面视图
     */
    @GetMapping("/clockCountClass")
    public String showClockCountClassPage(HttpSession session, Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var teacher = (Teacher) session.getAttribute("teacher");
        var classes = teacherService.manageTeacherFindClass(teacher.getTNo(), pageNum);
        model.addAttribute("pages", classes);
        return "/teacher/clockCount/clockCountClass";
    }

    /**
     * 班级考勤统计筛选
     *
     * @param session       session对象
     * @param model         model对象
     * @param searchContent 搜索内容
     * @return
     */
    @PostMapping("/clockCountClassSearch")
    public String clockCountClassSearch(HttpSession session, Model model, String searchContent) {
        if (searchContent == null || searchContent.isEmpty()) {
            return showClockCountClassPage(session, model, 1);
        }
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        var list = new PageInfo<>(teacherService.magageTeacherClockCountClassSearch(searchContent, teacher.getTNo()));
        model.addAttribute("pages", list);
        return "/teacher/clockCount/clockCountClass";
    }

    /**
     * 跳转到老师课程考勤统计管理页面
     *
     * @param courseName 课程名字
     * @param model      传数据的model
     * @param session    用于获取老师数据
     * @return 对应的视图
     */
    @GetMapping("/courseCount/{courseName}")
    public String showCourseCountPage(@PathVariable("courseName") String courseName, Model model, HttpSession session, @RequestParam(defaultValue = "1") Integer pageNum) {
        var teacher = (Teacher) session.getAttribute("teacher");
        var courses = teacherService.teacherFindManageTeacherCourse(courseName, teacher.getTNo(), pageNum);
        model.addAttribute("pages", courses);
        model.addAttribute("courseName", courseName);
        return "/teacher/clockCount/courseCount";
    }

    /**
     * 设置班级号号传入session
     *
     * @param classNo 获取需要了解的班级的班级号
     * @param session session对象
     */
    @PostMapping("/classCount")
    @ResponseBody
    public void setClassCountData(String classNo, HttpSession session) {
        session.setAttribute("classNo", classNo);
    }

    /**
     * 设置班级和学生传入model
     *
     * @return 返回跳转到的视图
     */
    @GetMapping("/classCount")
    public String showClassCountPage(HttpSession session, Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var classNo = (String) session.getAttribute("classNo");
        var aClass = teacherService.teacherFindClass(classNo);
        var students = teacherService.teacherFindStudents(classNo, pageNum);
        model.addAttribute("aClass", aClass);
        model.addAttribute("pages", students);
        return "/teacher/clockCount/classCount";
    }

    /**
     * 设置学号和查询的考勤类型传入session
     *
     * @param sno     获取需要了解的学生的学号
     * @param value   获取的考勤类型
     * @param session session对象
     */
    @PostMapping("/classCountDetail")
    @ResponseBody
    public void setClassCountDetailData(String sno, String value, HttpSession session) {
        session.setAttribute("sno", sno);
        session.setAttribute("value", value);
    }

    /**
     * 设置学号和考勤记录传入session
     *
     * @return 返回跳转到的视图
     */
    @GetMapping("/classCountDetail")
    public String showClassCountDetailPage(HttpSession session, Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var sno = (String) session.getAttribute("sno");
        var value = (String) session.getAttribute("value");
        var clockRecords = teacherService.teacherFindClockRecords(sno, value, pageNum);
        model.addAttribute("pages", clockRecords);
        return "/teacher/clockCount/classCountDetail";
    }

    /**
     * 设置班级和查询的考勤类型传入session
     *
     * @param classNo 获取需要了解的班级的班级号
     * @param value   获取的考勤类型
     * @param session session对象
     */
    @PostMapping("/toCourseCountDetail")
    @ResponseBody
    public void setToClassCountDetailData(String classNo, String value, HttpSession session) {
        session.setAttribute("classNo", classNo);
        session.setAttribute("value", value);
    }

    /**
     * 设置班级号和考勤记录传入session
     *
     * @return 返回跳转到的视图
     */
    @GetMapping("/toCourseCountDetail")
    public String showToClassCountDetailPage(HttpSession session, Model model) {
        var classNo = (String) session.getAttribute("classNo");
        var value = (String) session.getAttribute("value");
        var students = teacherService.teacherFindStudentByClassAndStatus(classNo, value);
        System.out.println("aaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbb");
        System.out.println(students);
        model.addAttribute("students", students);
        return "/teacher/clockCount/courseCountDetail";
    }


    /**
     * 设置学号和查询的考勤类型传入session
     *
     * @param courseNo 获取需要了解的班级的班级号
     * @param value    获取的考勤类型
     * @param session  session对象
     */
    @PostMapping("/courseCountDetail")
    @ResponseBody
    public void setCourseCountDetailData(int courseNo, String value, HttpSession session) {
        session.setAttribute("courseNo", courseNo);
        session.setAttribute("value", value);
    }

    /**
     * 设置学号和考勤记录传入session
     *
     * @return 返回跳转到的视图
     */
    @GetMapping("/courseCountDetail")
    public String showCourseCountDetailPage(HttpSession session, Model model) {
        var courseNo = (int) session.getAttribute("courseNo");
        var value = (String) session.getAttribute("value");
        var students = teacherService.teacherFindStudents(courseNo, value);
        model.addAttribute("students", students);
        return "/teacher/clockCount/courseCountDetail";
    }

    // 第六个功能：社团指导

    /**
     * 展示社团管理页面
     *
     * @param session session对象
     * @param model   model对象
     * @return 社团指导视图
     */
    @GetMapping("/societyManage")
    public String showSocietyManagePage(HttpSession session, Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        var societyList = teacherService.teacherFindSocieties(teacher.getTNo(), pageNum);
        model.addAttribute("pages", societyList);
        return "/teacher/societyGuide/societyManage";
    }

    /**
     * 筛选社团管理
     *
     * @param session       session对象
     * @param model         model对象
     * @param searchContent 搜索内容
     * @return 展示社团管理页面
     */
    @PostMapping("/societyManageSearch")
    public String societyManageSearch(HttpSession session, Model model, String searchContent) {
        if (searchContent == null || searchContent.isEmpty()) {
            return showSocietyManagePage(session, model, 1);
        }
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        var societyList = new PageInfo<>(teacherService.teacherSocietyManageSearch(searchContent, teacher.getTNo()));
        model.addAttribute("pages", societyList);
        return "/teacher/societyGuide/societyManage";
    }

    /**
     * 展示社团管理详情页面
     *
     * @param societyId 社团账号
     * @param session   session对象
     */
    @PostMapping("/societyManageDetail")
    @ResponseBody
    public void showSocietyManageDetailPage(String societyId, HttpSession session) {
        session.setAttribute("societyId", societyId);
    }

    /**
     * 接受二次跳转请求至社团详情页
     *
     * @param session session对象
     * @param model   model对象
     * @return 详情页视图
     */
    @GetMapping("/societyDetail")
    public String jumpSocietyManageDetailPage(HttpSession session, Model model) {
        try {
            var societyId = (String) session.getAttribute("societyId");
            var society = teacherService.teacherFindSociety(societyId);
            System.out.println(society);
            model.addAttribute("imageData", ImageConvert.convertByteArrayToBase64(society.getPicture()));
            model.addAttribute("societyDetail", society);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "/teacher/societyGuide/societyManageDetails";
    }


    /**
     * 展示社团审批页面
     *
     * @param session session对象
     * @param model   model对象
     * @param pageNum 页码
     * @return 社团指导视图
     */
    @GetMapping("/societyGuide")
    public String showSocietyGuidePage(HttpSession session, Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        var teacherSocietyApplicationList = teacherService.teacherFindSocietyApplication(teacher.getTNo(), pageNum);
        model.addAttribute("pages", teacherSocietyApplicationList);
        return "/teacher/societyGuide/societyGuide";
    }

    /**
     * 筛选社团申请
     *
     * @param session       session对象
     * @param model         model对象
     * @param searchContent 搜索对象
     * @return 社团指导视图
     */
    @PostMapping("/societyGuideSearch")
    public String societyGuideSearch(HttpSession session, Model model, String searchContent) {
        if (searchContent == null || searchContent.isEmpty()) {
            return showSocietyGuidePage(session, model, 1);
        }
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        var list = new PageInfo<>(teacherService.teacherSocietyGuideSearch(searchContent, teacher.getTNo()));
        model.addAttribute("pages", list);
        return "/teacher/societyGuide/societyGuide";
    }

    /**
     * 展示社团审批详情页面
     *
     * @param applicationNo 申请编号
     * @param session       session对象
     */
    @PostMapping("/societyGuideDetail")
    @ResponseBody
    public void showSocietyGuideDetailPage(Integer applicationNo, HttpSession session) {
        session.setAttribute("applicationNo", applicationNo);
    }

    /**
     * 接受二次跳转请求至申请详情页
     *
     * @param session session对象
     * @param model   model对象
     * @return 详情页视图
     */
    @GetMapping("/application")
    public String jumpSocietyGuideDetailPage(HttpSession session, Model model) {
        var applicationNo = (Integer) session.getAttribute("applicationNo");
        var teacher = (Teacher) session.getAttribute("teacher");
        var applicationDetail = teacherService.teacherFindTeacherSocietyApplication(applicationNo, teacher.getTNo());
        model.addAttribute("imageData", ImageConvert.convertByteArrayToBase64(applicationDetail.getPicture()));
        model.addAttribute("applicationDetail", applicationDetail);
        return "/teacher/societyGuide/societyGuideDetail";
    }

    /**
     * 教师更新申请状态
     *
     * @param applicationNo 申请编号
     * @param result        状态
     * @param session       session对象
     * @param model         model对象
     * @return 社团指导页面
     */
    @PostMapping("/updateApplicationStatus")
    public String updateApplicationStatus(Integer applicationNo, Integer result, HttpSession session, Model model) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        // 更新状态
        teacherService.teacherUpdateSocietyApplication(applicationNo, teacher.getTNo(), result);
        return showSocietyGuidePage(session, model, 1);
    }


    //第七个功能：活动审批

    /**
     * 活动审批视图
     *
     * @param session session对象
     * @param model   model对象
     * @return 活动审批视图
     */
    @GetMapping("/activityCheckReord")
    public String showActivityCheckRecord(HttpSession session, Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        var teacherActivityApplicationList = teacherService.teacherFindActivityApplicationsByTno(teacher.getTNo(), pageNum);
        model.addAttribute("pages", teacherActivityApplicationList);
        return "/teacher/activityCheck/activityCheck";
    }

    /**
     * 活动审批记录筛选
     *
     * @param session       session对象
     * @param model         model对象
     * @param searchContent 搜索内容
     * @return 活动审批视图
     */
    @PostMapping("/activityCheckSearch")
    public String activityCheckSearch(HttpSession session, Model model, String searchContent) {
        if (searchContent == null || searchContent.isEmpty()) {
            return showActivityCheckRecord(session, model, 1);
        }
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        var list = new PageInfo<>(teacherService.teacherActivityCheckSearch(searchContent, teacher.getTNo()));
        model.addAttribute("pages", list);
        return "/teacher/activityCheck/activityCheck";
    }

    /**
     * 展示活动申请详情页面
     *
     * @param applicationNo 申请编号
     * @param session       session对象
     */
    @PostMapping("/teacherActivityApplicationDetail")
    @ResponseBody
    public void showTeacherActivityApplicationDetail(Integer applicationNo, HttpSession session) {
        session.setAttribute("applicationNo", applicationNo);
    }

    /**
     * 接受二次跳转请求至活动申请详情页
     *
     * @param session session对象
     * @param model   model对象
     * @return 活动审批详情视图
     */
    @GetMapping("/teacherActivityApplication")
    public String jumpTeacherActivityApplicationDetailPage(HttpSession session, Model model) {
        var applicationNo = (Integer) session.getAttribute("applicationNo");
        var teacher = (Teacher) session.getAttribute("teacher");
        var applicationDetail = teacherService.teacherFindActivityApplication(applicationNo, teacher.getTNo());
        model.addAttribute("applicationDetail", applicationDetail);
        return "/teacher/activityCheck/activityCheckDetails";
    }

    @PostMapping("/updateActivityApplicationStatus")
    public String updateActivityApplicationStatus(HttpSession session, Integer applicationNo, Integer result, Model model) {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        //更新状态
        teacherService.teacherUpdateActivityApplication(applicationNo, teacher.getTNo(), result);
        return showActivityCheckRecord(session, model, 1);
    }

}

