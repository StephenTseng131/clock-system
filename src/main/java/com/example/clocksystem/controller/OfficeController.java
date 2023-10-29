package com.example.clocksystem.controller;

import com.example.clocksystem.entity.*;
import com.example.clocksystem.entity.Class;
import com.example.clocksystem.service.OfficeService;
import com.example.clocksystem.utils.ImageConvert;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 94548
 */
@Controller
@RequestMapping("/office")
public class OfficeController {
    @Autowired
    private OfficeService officeService;

    /**
     * 展示教务处主页
     *
     * @return 教务处主页视图
     */
    @GetMapping("/officeHome")
    public String showOfficeHomePage() {
        return "/office/office";
    }

    // 第一个模块：班级管理
    /**
     * 展示班级管理页面
     *
     * @return 班级管理视图
     */
    @GetMapping("/classManage")
    public String showClassManagePage(Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var classList = officeService.officeFindClasses(pageNum);
        model.addAttribute("pages", classList);
        return "/office/classManage/classManage";
    }

    /**
     * 教务处删除班级
     *
     * @param classNo 班级号
     * @return 删除标识
     */
    @PostMapping("/classDelete")
    @ResponseBody
    public int classDelete(String classNo) {
        return officeService.officeDeleteClass(classNo);
    }

    /**
     * 展示添加班级页面
     * @param model model对象
     * @return 添加课程的页面视图
     */
    @GetMapping("/addClassDetail")
    public String showAddClassPage(Model model){
        var teacherList = officeService.officeFindManageTeachers();
        model.addAttribute("teacherList",teacherList);
        return "/office/classManage/addClass";
    }

    /**
     * 添加班级
     * @param model model对象
     * @param partment 班级对象
     * @return 添加成功，返回展示班级管理页面；否则，返回添加 班级页面
     */
    @PostMapping("/addClass")
    public String addClass(Model model, Class partment) {
        if (officeService.officeAddClass(partment) == 0) {
            //添加成功
            return showClassManagePage(model,1);
        }
        return showAddClassPage(model);
    }

    /**
     * 展示班级详情页
     * @param session session对象
     * @param classNo 班级号
     */
    @PostMapping ("/classInfo")
    @ResponseBody
    public void showClassInfoPage(HttpSession session, String classNo) {
        var classDetail = officeService.officeFindClassDetail(classNo);
        session.setAttribute("classDetail", classDetail);
    }

    /**
     * 接受二次跳转请求至修改班级信息详情页
     * @return 修改班级信息视图
     */
    @GetMapping("/jumpClassInfo")
    public String jumpClassInfoPage(Model model) {
        var teacherList = officeService.officeFindManageTeachers();
        model.addAttribute("teacherList", teacherList);
        return "/office/classManage/classInfo";
    }

    /**
     * 提交修改班级信息
     * @param model model对象
     * @param partment 班级对象
     * @return 展示班级管理视图
     */
    @PostMapping("/classUpdate")
    public String classUpdate(Model model, Class partment) {
        officeService.officeUpdateClass(partment);
        return showClassManagePage(model,1);
    }


    // 第二个模块：课程管理
    /**
     * 展示课程管理页面
     *
     * @param model model对象
     * @param pageNum 页码
     * @return 课程管理页面视图
     */
    @GetMapping("/courseManage")
    public String showCourseManagePage(Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var courseList = officeService.officeFindCourses(pageNum);
        model.addAttribute("pages", courseList);
        return "/office/courseManage/courseManage";
    }

    /**
     * 展示添加课程页面
     *
     * @return 添加课程视图
     */
    @GetMapping("/addCourse")
    public String showAddCoursePage() {
        return "/office/courseManage/addCourse";
    }

    /**
     * 学院信息
     *
     * @return 学院列表
     */
    @GetMapping("/departmentData")
    @ResponseBody
    public List<Department> getAllDepartments() {
        return officeService.officeFindDepartments();
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
        return officeService.officeFindTeacherListByDepartment(departmentId);
    }

    /**
     * 教务处为教师添加课程
     *
     * @param courseName 课程名
     * @param teacherNo  教师号
     * @param week       上课星期
     * @param startTime  起始节
     * @param endTime    结束节
     * @param model      model对象
     * @return 相应的页面视图
     */
    @PostMapping("/officeAddCourse")
    public String officeAddCourse(String courseName, String teacherNo, Integer week, Integer startTime, Integer endTime, Model model) {
        if (officeService.officeAddCourse(courseName, teacherNo, week, startTime, endTime) == 0) {
            return showCourseManagePage(model, 1);
        }
        else {
            model.addAttribute("errMsg", "课程时间冲突，不可添加!");
            return showAddCoursePage();
        }
    }

    /**
     * 教务处删除课程
     * @param courseNo 课程号
     * @return 0：删除失败，否则删除成功
     */
    @PostMapping("/officeDelete")
    @ResponseBody
    public int officeDeleteCourse(Integer courseNo){
        return officeService.officeDeleteCourse(courseNo);
    }

    //第三个模块：社团审批
    /**
     * 展示社团审批页面
     *
     * @return 社团审批视图
     */
    @GetMapping("/societyApproval")
    public String showSocietyApprovalPage(Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var societyApplicationList = officeService.officeFindAllSocietyApplication(pageNum);
        model.addAttribute("pages", societyApplicationList);
        return "/office/societyApproval/societyApproval";
    }

    /**
     * 展示申请详情页面
     *
     * @param applicationNo 申请编号
     * @param session       session对象
     */
    @PostMapping("/applicationDetail")
    @ResponseBody
    public void showApplicationDetailPage(Integer applicationNo, HttpSession session) {
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
    public String jumpApplicationDetailPage(HttpSession session, Model model) {
        var applicationNo = (Integer) session.getAttribute("applicationNo");
        var applicationDetail = officeService.officeFindSocietyApplication(applicationNo);
        model.addAttribute("imageData", ImageConvert.convertByteArrayToBase64(applicationDetail.getPicture()));
        model.addAttribute("applicationDetail", applicationDetail);
        return "/office/societyApproval/societyApprovalDetail";
    }

    /**
     * 社团审批状态更新
     *
     * @param applicationNo 申请编号
     * @param result        审批结果
     * @param model         model对象
     * @return 社团审批视图
     */
    @PostMapping("/updateOfficeStatus")
    public String updateOfficeStatus(int applicationNo, int result, Model model) {
        // 更新申请状态
        officeService.officeUpdateApplicationOfficeStatus(applicationNo, result);
        return showSocietyApprovalPage(model, 1);
    }

    //第四个模块：社团管理
    /**
     * 展示社团管理页面
     * @param pageNum 页码
     * @param model model模型
     * @return 社团管理视图
     */
    @GetMapping("/societyManage")
    public String showSocietyManagePage(Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        model.addAttribute("pages", officeService.officeFindSocieties(pageNum));
        return "/office/societyManage/societyManage";
    }

    /**
     * 教务处删除社团
     * @param societyId 社团ID
     * @return 0：删除失败，否则成功
     */
    @PostMapping("/deleteSociety")
    @ResponseBody
    public int deleteSociety(String societyId) {
        return officeService.officeDeleteSociety(societyId);
    }

    //第五个模块：活动管理
    /**
     * 展示活动管理页面
     * @param model model对象
     * @param pageNum 页码
     * @return 活动管理视图
     */
    @GetMapping("/activityManage")
    public String showActivityManagePage(Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var activityApplicationList = officeService.officeFindActivityApplications(pageNum);
        model.addAttribute("pages", activityApplicationList);
        return "/office/activityManage/activityManage";
    }

    /**
     * 展示活动申请详情页
     * @param session session对象
     * @param applicationNo 申请编号
     */
    @PostMapping("/activityApplicationDetail")
    @ResponseBody
    public void showActivityApplicationDetailPage(HttpSession session,Integer applicationNo) {
        session.setAttribute("applicationNo", applicationNo);
    }

    /**
     * 接受二次跳转请求至活动申请详情页
     * @param session session对象
     * @param model model对象
     * @return 活动申请详情页视图
     */
    @GetMapping("/activityApplication")
    public String jumpActivityApplicationDeatilPage(HttpSession session, Model model) {
        var applicationNo = (Integer) session.getAttribute("applicationNo");
        var activityApplicationDetail = officeService.officeFindActivityApplicationDetail(applicationNo);
        model.addAttribute("activityApplicationDetail", activityApplicationDetail);
        return "/office/activityManage/activityDetails";
    }

    /**
     * 更改教务处活动审批状态
     * @param applicationNo 申请编号
     * @param result 审批结果
     * @param model model对象
     * @return 活动审批记录页面视图
     */
    @PostMapping("/updateActicityStatus")
    public String updateActivityApplicationStatus(int applicationNo, int result, Model model) {
        //更新状态
        officeService.officeUpdateActivityApplicationStatus(applicationNo, result);
        return showActivityManagePage(model, 1);
    }

    /**
     * 活动管理筛选
     *
     * @param searchContent 筛选框
     * @param model         model对象
     * @return 活动管理页面视图
     */
    @PostMapping("/officeActivitySearch")
    public String activitySearch(String searchContent, Model model) {
        // 搜索框没内容默认全显示
        if (searchContent == null || searchContent.isEmpty()) {
            return showActivityManagePage(model, 1);
        }
        // 筛选
        var pages = new PageInfo<>(officeService.officeActivitySearch(searchContent));
        model.addAttribute("pages", pages);
        return "/office/activityManage/activityManage";
    }

    /**
     * 班级管理筛选
     *
     * @param searchContent 筛选框
     * @param model         model对象
     * @return 班级管理页面视图
     */
    @PostMapping("/officeClassSearch")
    public String classSearch(String searchContent, Model model) {
        // 搜索框没内容默认全显示
        if (searchContent == null || searchContent.isEmpty()) {
            return showClassManagePage(model, 1);
        }
        // 筛选
        var pages = new PageInfo<>(officeService.officeClassSearch(searchContent));
        model.addAttribute("pages", pages);
        return "/office/classManage/classManage";
    }

    /**
     * 课程管理筛选
     *
     * @param searchContent 筛选框
     * @param model         model对象
     * @return 课程管理页面视图
     */
    @PostMapping("/officeCourseSearch")
    public String courseSearch(String searchContent, Model model) {
        // 搜索框没内容默认全显示
        if (searchContent == null || searchContent.isEmpty()) {
            return showCourseManagePage(model, 1);
        }
        // 筛选
        var pages = new PageInfo<>(officeService.officeCourseSearch(searchContent));
        model.addAttribute("pages", pages);
        return "/office/courseManage/courseManage";
    }

    /**
     * 社团审核管理筛选
     *
     * @param searchContent 筛选框
     * @param model         model对象
     * @return 课程管理页面视图
     */
    @PostMapping("/officeSocietyApprovalSearch")
    public String societyApprovalSearch(String searchContent, Model model) {
        // 搜索框没内容默认全显示
        if (searchContent == null || searchContent.isEmpty()) {
            return showSocietyApprovalPage(model, 1);
        }
        // 筛选
        var pages = new PageInfo<>(officeService.officeSocietyApprovalSearch(searchContent));
        model.addAttribute("pages", pages);
        return "/office/societyApproval/societyApproval";
    }

    /**
     * 社团管理筛选
     *
     * @param searchContent 筛选框
     * @param model         model对象
     * @return 课程管理页面视图
     */
    @PostMapping("/officeSocietySearch")
    public String societySearch(String searchContent, Model model) {
        // 搜索框没内容默认全显示
        if (searchContent == null || searchContent.isEmpty()) {
            return showSocietyManagePage(model, 1);
        }
        // 筛选
        var pages = new PageInfo<>(officeService.officeSocietySearch(searchContent));
        model.addAttribute("pages", pages);
        return "/office/societyManage/societyManage";
    }
}
