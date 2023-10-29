package com.example.clocksystem.service;

import com.example.clocksystem.entity.*;
import com.example.clocksystem.entity.Class;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AdminService {
    /**
     * 管理员查找所有学生
     * @param pageNum 页码
     * @return 学生列表
     */
    PageInfo<Student> adminFindAllStudents(int pageNum);

    /**
     * 管理员查找所有行政班
     * @return 班级列表
     */
    List<Class> adminFindAllClasses();

    /**
     * 管理员添加学生
     * @param student 学生对象
     * @return 0：添加成功，1：添加失败
     */
    int adminAddstudent(Student student);

    /**
     * 管理员删除学生
     * @param sNo 学号
     * @return 0:删除失败，否则删除成功
     */
    int adminDeleteStudent(String sNo);

    /**
     * 管理员查找指定学生
     * @param sNo 学号
     * @return 学生对象
     */
    Student adminFindStudent(String sNo);

    /**
     * 管理员更新学生信息
     * @param student 学生对象
     * @return 0:更新失败，否则更新成功
     */
    int adminUpdateStudent(Student student);

    /**
     * 管理员学生管理页面筛选功能
     * @param searchContent 筛选框
     * @return 学生列表
     */
    List<Student> adminStudentSearch(String searchContent);

    /**
     * 管理员添加教师
     * @param teacher 教师对象
     * @return 0:添加成功，1:添加失败
     */
    int adminAddTeacher(Teacher teacher);

    /**
     * 管理员查询学院信息
     * @return 学院列表
     */
    List<Department> adminFindDepartments();

    /**
     * 管理员查找指定教师
     * @param tNo 教师号
     * @return 教师对象
     */
    Teacher adminFindTeacher(String tNo);

    /**
     * 管理员查找所有教师
     * @param pageNum 页码
     * @return 教师列表
     */
    PageInfo<Teacher> adminFindAllTeachers(int pageNum);

    /**
     * 管理员删除教师
     * @param tNo 教师号
     * @return 0:删除失败，否则删除成功
     */
    int adminDeleteTeacher(String tNo);

    /**
     * 管理员更新教师信息
     * @param teacher 教师对象
     * @return 0:更新失败，否则更新成功
     */
    int adminUpdateTeacher(Teacher teacher);

    /**
     * 管理员教师管理页面筛选功能
     * @param searchContent 筛选框内容
     * @return 教师列表
     */
    List<Teacher> adminTeacherSearch(String searchContent);

    /**
     * 管理员查找所有管理员
     * @param pageNum 页码
     * @return 管理员列表
     */
    PageInfo<Admin> adminFindAllAdmins(Integer pageNum);

    /**
     * 管理员删除其他管理员
     * @param id 管理员id
     * @return 0:删除失败，否则删除成功
     */
    int adminDeleteAdmin(String id,Admin admin);

    /**
     * 管理员添加管理员
     * @param admin 管理员对象
     * @return 0:添加成功，1:添加失败
     */
    int adminAddAdmin(Admin admin);

    /**
     * 添加管理员的失去焦点事件
     * @param id 管理员id
     * @return 0：id不存在，1：id存在
     */
    int adminJudgeAdmin(String id);

    /**
     * 管理员在管理管理员页面筛选功能
     * @param searchContent 筛选框内容
     * @return 管理员列表
     */
    List<Admin> adminAdminSearch(String searchContent);

    /**
     * 管理员查看管理员详情
     * @param id 管理员id
     * @return 管理员对象
     */
    Admin adminFindAdmin(String id);

    /**
     * 管理员更新教务处信息
     * @param officeId 教务处号
     * @return 0：更新成功，1：更新失败
     */
    int resetOffice(String officeId);
}
