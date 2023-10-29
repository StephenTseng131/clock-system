package com.example.clocksystem.controller;

import com.example.clocksystem.entity.*;
import com.example.clocksystem.entity.Class;
import com.example.clocksystem.service.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    public AdminService adminService;

    // 第一个模块：管理员管理
    /**
     * 返回管理员主页
     *
     * @return 主页的视图
     */
    @GetMapping("/admin")
    public String admin() {
        return "/admin/admin";
    }

    /**
     * 展示管理员管理页面
     *
     * @param model model对象
     * @return 管理员管理页面视图
     */
    @GetMapping("/adminManage")
    public String adminManage(Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var pages = adminService.adminFindAllAdmins(pageNum);
        model.addAttribute("pages", pages);
        return "/admin/adminManage/adminManage";
    }

    /**
     * 展示添加管理员页面
     */
    @GetMapping("/addAdmin")
    public String showAddAdminPage() {
        return "/admin/adminManage/addAdmin";
    }

    /**
     * 添加管理员
     *
     * @param admin 管理员对象
     * @param model model对象
     * @return 管理管理员页面视图
     */
    @PostMapping("/adminAdd")
    public String adminAdd(Admin admin, Model model) {
        if (adminService.adminAddAdmin(admin) == 0) {
            // 添加成功
            return adminManage(model, 1);
        } else {
            return "/admin/adminManage/addAdmin";
        }
    }

    /**
     * 添加管理员进行id判定
     *
     * @param id id
     * @return 1：id存在，0：id不存在
     */
    @PostMapping("/adminAddJudge")
    @ResponseBody
    public int adminAddJudge(String id) {
        return adminService.adminJudgeAdmin(id);
    }

    /**
     * 删除管理员
     *
     * @param id      管理员id
     * @param session session对象
     * @return 标识
     */
    @PostMapping("/adminDelete")
    @ResponseBody
    public int adminDelete(String id, HttpSession session) {
        var admin = (Admin) session.getAttribute("admin");
        return adminService.adminDeleteAdmin(id, admin);
    }

    /**
     * 管理员管理中的筛选功能
     * @param searchContent 筛选框内容
     * @param model model对象
     * @return 筛选内容视图
     */
    @PostMapping("/adminSearch")
    public String adminSearch(String searchContent, Model model) {
        //搜索框没内容默认全显示
        if (searchContent == null || searchContent.isEmpty()) {
            return adminManage(model, 1);
        }
        var pages = new PageInfo<>(adminService.adminAdminSearch(searchContent));
        model.addAttribute("pages", pages);
        return "/admin/adminManage/adminManage";
    }

    // 第二个功能：教师管理
    /**
     * 展示管理教师页面
     *
     * @param pageNum 页码
     * @param model   model对象
     * @return 管理教师页面视图
     */
    @GetMapping("/teacherManage")
    public String showTeacherManagePage(Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var pages = adminService.adminFindAllTeachers(pageNum);
        model.addAttribute("pages", pages);
        return "/admin/teacherManage/manageTeacher";
    }

    /**
     * 管理教师筛选
     *
     * @param searchContent 筛选框
     * @param model         model对象
     * @return 管理教师页面视图
     */
    @PostMapping("/teacherSearch")
    public String teacherSearch(String searchContent, Model model) {
        //搜索框没内容默认全显示
        if (searchContent == null || searchContent.isEmpty()) {
            return showTeacherManagePage(model, 1);
        }
        var pages = new PageInfo<>(adminService.adminTeacherSearch(searchContent));
        model.addAttribute("pages", pages);
        return "/admin/teacherManage/manageTeacher";
    }

    /**
     * 展示添加老师页面
     */
    @GetMapping("/addTeacher")
    public String showAddTeacherPage() {
        return "/admin/teacherManage/addTeacher";
    }

    /**
     * 根据教师号删除教师
     *
     * @param id 教师号
     * @return 删除标识
     */
    @PostMapping("/teacherDelete")
    @ResponseBody
    public int teacherDelete(String id) {
        return adminService.adminDeleteTeacher(id);
    }

    /**
     * 展示修改教师信息页面
     *
     * @param id      教师号
     * @param session session对象
     */
    @PostMapping("/teacherUpdate")
    @ResponseBody
    public void showTeacherUpdatePage(String id, HttpSession session) {
        var teacher = adminService.adminFindTeacher(id);
        session.setAttribute("teacherUpdate", teacher);
    }

    /**
     * 跳转转到修改老师信息页面
     */
    @GetMapping("/teacherInfo")
    public String showTeacherInfoPage() {
        return "/admin/teacherManage/teacherInfo";
    }

    /**
     * 修改教师信息
     *
     * @param teacher 教师对象
     * @param session session对象
     * @param model   model对象
     * @return 管理教师视图
     */
    @PostMapping("/teacherUpdateSave")
    public String teacherUpdateSave(Teacher teacher, HttpSession session, Model model) {
        adminService.adminUpdateTeacher(teacher);
        if (session.getAttribute("teacherUpdate") != null) {
            session.removeAttribute("teacherUpdate");
        }
        return showTeacherManagePage(model, 1);
    }

    /**
     * 学院信息
     *
     * @return 学院列表
     */
    @GetMapping("/departmentData")
    @ResponseBody
    public List<Department> getAllDepartments() {
        return adminService.adminFindDepartments();
    }

    /**
     * 添加教师
     *
     * @param teacher 教师对象
     * @param model   model对象
     * @return 添加教师页面视图
     */
    @PostMapping("/teacherAdd")
    public String teacherAdd(Teacher teacher, Model model) {
        //该教师能添加
        if (adminService.adminAddTeacher(teacher) == 0) {
            return showTeacherManagePage(model, 1);
        }
        return showAddTeacherPage();
    }

    /**
     * 删除教师session
     *
     * @param session session对象
     */
    @PostMapping("/teacherSessionDelete")
    @ResponseBody
    public void teacherSessionDelete(HttpSession session) {
        if (session.getAttribute("teacherUpdate") != null) {
            session.removeAttribute("teacherUpdate");
        }
    }

    // 第三个功能：学生管理
    /**
     * 跳转到学生页面
     *
     * @param model model对象
     * @return 管理学生视图
     */
    @GetMapping("/studentManage")
    public String showStudentManagePage(Model model, @RequestParam(defaultValue = "1") Integer pageNum) {
        var pages = adminService.adminFindAllStudents(pageNum);
        model.addAttribute("pages", pages);
        return "/admin/studentManage/manageStudent";
    }

    /**
     * 学生管理筛选
     *
     * @param searchContent 筛选框
     * @param model         model对象
     * @return 学生管理页面视图
     */
    @PostMapping("/studentSearch")
    public String studentSearch(String searchContent, Model model) {
        // 搜索框没内容默认全显示
        if (searchContent == null || searchContent.isEmpty()) {
            return showStudentManagePage(model, 1);
        }
        // 筛选
        var pages = new PageInfo<>(adminService.adminStudentSearch(searchContent));
        model.addAttribute("pages", pages);
        return "/admin/studentManage/manageStudent";
    }

    /**
     * 展示添加学生页面
     *
     * @param model model对象
     * @return 添加学生页面视图
     */
    @GetMapping("/addStudent")
    public String addStudent(Model model) {
        var classes = adminService.adminFindAllClasses();
        model.addAttribute("classes", classes);
        return "/admin/studentManage/addStudent";
    }

    /**
     * 添加学生
     *
     * @param student 学生对象
     * @param model   model对象
     * @return 添加学生页面
     */
    @PostMapping("/studentAdd")
    public String studentAdd(Student student, Model model) {
        if (adminService.adminAddstudent(student) == 0) {
            // 学生添加成功
            return showStudentManagePage(model, 1);
        } else {
            // 学生添加失败
            return addStudent(model);
        }
    }

    /**
     * 删除学生
     *
     * @param id 学号
     * @return 0:删除失败 ，>0删除成功
     */
    @PostMapping("/studentDelete")
    @ResponseBody
    public int studentDelete(String id) {
        return adminService.adminDeleteStudent(id);
    }

    /**
     * 展示学生信息详情
     *
     * @param id      学号
     * @param session session对象
     */
    @PostMapping("/studentUpdate")
    @ResponseBody
    public void showStudentUpdatePage(String id, HttpSession session) {
        var student = adminService.adminFindStudent(id);
        var classes = adminService.adminFindAllClasses();
        session.setAttribute("classes", classes);
        session.setAttribute("studentUpdate", student);
    }

    /**
     * 删除学生session
     *
     * @param session session对象
     */
    @PostMapping("/studentSessionDelete")
    @ResponseBody
    public void studentSessionDelete(HttpSession session) {
        if (session.getAttribute("studentUpdate") != null) {
            session.removeAttribute("studentUpdate");
        }
        if (session.getAttribute("classes") != null) {
            session.removeAttribute("classes");
        }
    }

    /**
     * 修改学生信息
     *
     * @param student 学生对象
     * @param session session对象
     * @param model   model对象
     * @return 管理学生页面视图
     */
    @PostMapping("/studentUpdateSave")
    public String studentUpdateSave(Student student, HttpSession session, Model model) {
        adminService.adminUpdateStudent(student);
        if (session.getAttribute("studentUpdate") != null) {
            session.removeAttribute("studentUpdate");
        }
        return showStudentManagePage(model, 1);
    }

    /**
     * 跳转到学生修改信息页面
     */
    @GetMapping("/studentInfo")
    public String showStudentInfoPage() {
        return "/admin/studentManage/studentInfo";
    }

    /**
     * 重置教务处账号密码
     */
    @GetMapping("/resetPassword")
    public String resetPassword() {
        adminService.resetOffice("jiaowuc");
        return admin();
    }
}
