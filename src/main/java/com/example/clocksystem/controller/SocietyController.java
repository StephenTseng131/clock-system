package com.example.clocksystem.controller;

import com.example.clocksystem.entity.*;
import com.example.clocksystem.service.SocietyService;
import com.example.clocksystem.utils.ImageConvert;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 94548
 */
@Controller
@RequestMapping("/society")
public class SocietyController {
    @Autowired
    SocietyService societyService;

    /**
     * 展示社团主页
     * @return 社团主页视图
     */
    @GetMapping("/societyHome")
    public String showSocietyHomePage() {
        return "/society/society";
    }

    //第一个模块：社团详情和修改信息
    /**
     * 展示社团信息页
     * @return 社团信息视图
     */
    @GetMapping("/societyInfo")
    public String showSocietyInfoPage(HttpSession session, Model model) {
        var society = (Society) session.getAttribute("society");
        model.addAttribute("societyDetail", societyService.societyFindSociety(society.getSocietyId()));
        model.addAttribute("departmentList", societyService.societyFindDepartments());
        model.addAttribute("teacherList", societyService.societyFindTeacherListByDepartmentNo(society.getTeacher().getDepartmentNo()));
        model.addAttribute("imageData", ImageConvert.convertByteArrayToBase64(society.getPicture()));
        return "/society/societyInfo/societyInfo";
    }

    @PostMapping("/modifySocietyInfo")
    public String updateSocietyInfo(MultipartFile picture, String societyName, String password, String tNo, String introduce, HttpSession session, Model model) {
        var society = (Society) session.getAttribute("society");
        //密码直接修改
        society.setPassword(password);
        societyService.societyUpdatePassword(society);
        //修改其他信息需要提交申请修改
        if (societyService.societyModifySocietyInfoApplication(picture, societyName, tNo, introduce, society) == 1) {
            model.addAttribute("errMsg", "申请失败,图片大小超过5MB!");
            return showSocietyInfoPage(session, model);
        }
        else {
            return showSocietyHomePage();
        }
    }

    //第二个模块：活动管理
    /**
     * 展示活动管理页面视图
     * @param model model对象
     * @param session session对象
     * @param pageNum 页码
     * @return 活动管理页面视图
     */
    @GetMapping("activityManage")
    public String showActivityManagePage(HttpSession session, Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var society = (Society) session.getAttribute("society");
        model.addAttribute("pages", societyService.societyFindActivities(society.getSocietyId(), pageNum));
        return "/society/activityManage/activityManage";
    }

    /**
     * 展示社团活动详情页
     * @param activityNo 活动编号
     * @param session session对象
     */
    @PostMapping("/activityDetail")
    @ResponseBody
    public void showActivityDetailPage(Integer activityNo, HttpSession session) {
        session.setAttribute("activityNo", activityNo);
    }

    /**
     * 接受第二次跳转请求至活动详情页
     * @param session session对象
     * @param model model对象
     * @param pageNum 页码
     * @return 详情页视图
     */
    @GetMapping("/activity")
    public String jumpActivityDetailPage(HttpSession session, Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var activityNo = (Integer) session.getAttribute("activityNo");
        var activityDetail = societyService.societyFindSocietyAcitivty(activityNo);
        var studentActivityList = societyService.societyFindStudentActivitiesByActivityNo(activityNo, pageNum);
        model.addAttribute("activityDetail", activityDetail);
        model.addAttribute("pages", studentActivityList);
        return "/society/activityManage/activityManageDetails";
    }

    /**
     * 设置活动的签到码
     * @param session session对象
     * @param model model对象
     * @param code 签到码
     * @return 活动详情页
     */
    @PostMapping("/setActivityCode")
    public String setActivityCode(HttpSession session, Model model, String code) {
        var activityNo = (Integer) session.getAttribute("activityNo");
        var activity = societyService.societyFindSocietyAcitivty(activityNo);
        activity.setCode(code);
        societyService.societySetAcitivtyCode(activity);
        return jumpActivityDetailPage(session, model, 1);
    }

    /**
     * 活动申请记录视图
     * @param session session对象
     * @param model model对象
     * @param pageNum 页码
     * @return 活动申请记录页面视图
     */
    @GetMapping("/activityApprovalRecord")
    public String showActivityApprovalRecordPage(HttpSession session, Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var society = (Society) session.getAttribute("society");
        var activityApplicationList = societyService.societyFindActivityApplicationsBySocietyId(society.getSocietyId(), pageNum);
        model.addAttribute("pages", activityApplicationList);
        return "/society/activityManage/activityApprovalRecord";
    }

    /**
     * 展示活动申请页面视图
     * @return 活动申请页面视图
     */
    @GetMapping("activityApproval")
    public String showActivityApprovalPage() {
        return "/society/activityManage/activityApproval";
    }

    @PostMapping("/addActivityApproval")
    public String addActivityApproval(HttpSession session, Model model, String activityName, String introduce, int type, int credit, String startTime, String endTime) {
        var society = (Society) session.getAttribute("society");
        societyService.societyAddActivityApplication(society, activityName, introduce, type, credit, startTime, endTime);
        return showActivityApprovalRecordPage(session, model, 1);
    }


    //辅助控制器
    /**
     * 学院信息
     *
     * @return 学院列表
     */
    @GetMapping("/departmentData")
    @ResponseBody
    public List<Department> getAllDepartments() {
        return societyService.societyFindDepartments();
    }

    /**
     * 学院教师
     *
     * @return 教师列表
     */
    @GetMapping("/departmentTeachers")
    @ResponseBody
    public List<Teacher> getTeachers(String departmentNo) {
        return societyService.societyFindTeacherListByDepartmentNo(departmentNo);
    }

    /**
     * 活动审批记录筛选
     *
     * @param searchContent 筛选框
     * @param model         model对象
     * @return 课程管理页面视图
     */
    @PostMapping("/societyActivityApprovalSearch")
    public String societyActivityApprovalSearch(String searchContent, Model model,HttpSession session) {
        var society = (Society) session.getAttribute("society");
        // 搜索框没内容默认全显示
        if (searchContent == null || searchContent.isEmpty()) {
            return showActivityApprovalRecordPage(session,model,1);
        }
        // 筛选
        var pages = new PageInfo<>(societyService.societyActivityApprovalSearch(searchContent,society.getSocietyId()));
        model.addAttribute("pages", pages);
        return "/society/activityManage/activityApprovalRecord";
    }

    /**
     * 活动管理筛选
     *
     * @param searchContent 筛选框
     * @param model         model对象
     * @return 课程管理页面视图
     */
    @PostMapping("/societyActivitySearch")
    public String societyActivitySearch(String searchContent, Model model,HttpSession session) {
        var society = (Society) session.getAttribute("society");
        // 搜索框没内容默认全显示
        if (searchContent == null || searchContent.isEmpty()) {
            return showActivityManagePage(session,model,1);
        }
        // 筛选
        var pages = new PageInfo<>(societyService.societyActivitySearch(searchContent,society.getSocietyId()));
        model.addAttribute("pages", pages);
        return "/society/activityManage/activityManage";
    }
}
