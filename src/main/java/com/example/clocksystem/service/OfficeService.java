package com.example.clocksystem.service;

import com.example.clocksystem.entity.*;
import com.example.clocksystem.entity.Class;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author 94548
 */
public interface OfficeService {
    /**
     * 教务处查询教务处对象
     *
     * @param id 教务处账号
     * @return 教务处对象
     */
    Office officeFindOffice(String id);

    /**
     * 教务处查询所有社团列表
     * @param pageNum 页码
     * @return 社团列表
     */
    PageInfo<Society> officeFindSocieties(int pageNum);

    /**
     * 教务处查询所有社团申请
     * @param pageNum 页码
     * @return 社团申请列表
     */
    PageInfo<SocietyApplication> officeFindAllSocietyApplication(int pageNum);

    /**
     * 教务处查询社团申请详情记录
     * @param applicationNo 申请编号
     * @return 社团申请对象
     */
    SocietyApplication officeFindSocietyApplication(Integer applicationNo);

    /**
     * 教务处更新社团申请的教务处状态
     * @param applicationNo 申请编号
     * @param result 审批结果
     */
    void officeUpdateApplicationOfficeStatus(int applicationNo, int result);

    /**
     * 教务处查询所有班级
     * @param pageNum 页码
     * @return 班级列表
     */
    PageInfo<Class> officeFindClasses(int pageNum);

    /**
     * 教务处删除班级
     * @param classNo 班级编号
     * @return 0：删除失败/1：删除成功
     */
    int officeDeleteClass(String classNo);

    /**
     * 教务处添加班级
     * @param partment 班级对象
     * @return 0：成功/1：失败
     */
    int officeAddClass(Class partment);

    /**
     * 教务处查找班级详情
     * @param classNo 班级号
     * @return 班级对象
     */
    Class officeFindClassDetail(String classNo);

    /**
     * 教务处更新班级信息
     * @param partment 班级对象
     * @return 0：更新成功/1：更新失败
     */
    int officeUpdateClass(Class partment);

    /**
     * 教务处查询所有课程
     * @return 课程列表
     */
    PageInfo<Course> officeFindCourses(int pageNum);

    /**
     * 教务处查询学院信息
     * @return 学院列表
     */
    List<Department> officeFindDepartments();

    /**
     * 教务处查询学院教师
     * @param departmentNo 学院号
     * @return 学院教师列表
     */
    List<Teacher> officeFindTeacherListByDepartment(String departmentNo);

    /**
     * 教务处为教师添加课程
     * @param courseName 课程名
     * @param teacherNo 教师号
     * @param week 星期
     * @param startTime 上课小节
     * @param endTime 下课小节
     * @return 0：添加成功，1：添加失败
     */
    int officeAddCourse(String courseName, String teacherNo, Integer week, Integer startTime, Integer endTime);

    /**
     * 教务处删除课程
     * @param courseNo 课程号
     * @return 0:删除失败，否则删除成功
     */
    int officeDeleteCourse(Integer courseNo);

    /**
     * 教务处查询辅导员
     * @return 辅导员列表
     */
    List<Teacher> officeFindManageTeachers();

    /**
     * 教务处查询活动列表
     * @return 活动列表
     */
    List<Activity> officeFindActivities();

    /**
     * 教务处删除社团
     * @param societyId 社团ID
     * @return 0：删除失败，否则，删除成功
     */
    int officeDeleteSociety(String societyId);

    /**
     * 教务处查找教师审核通过的活动审批
     * @param pageNum 页码
     * @return 活动列表
     */
    PageInfo<ActivityApplication> officeFindActivityApplications(int pageNum);

    /**
     * 教务处查找活动申请详情
     * @param applicationNo 申请编号
     * @return 活动申请详情对象
     */
    ActivityApplication officeFindActivityApplicationDetail(Integer applicationNo);

    /**
     * 教务处跟新活动审批状态
     * @param applicationNo 申请编号
     * @param result 审批结果
     * @return 0：成功；1：失败
     */
    int officeUpdateActivityApplicationStatus(int applicationNo, int result);

    /**
     * 教务处活动管理页面筛选功能
     * @param searchContent 筛选框
     * @return 活动列表
     */
    List<Activity> officeActivitySearch(String searchContent);

    /**
     * 教务处班级管理页面筛选功能
     * @param searchContent 筛选框
     * @return 活动列表
     */
    List<Class> officeClassSearch(String searchContent);

    /**
     * 教务处课程管理页面筛选功能
     * @param searchContent 筛选框
     * @return 活动列表
     */
    List<Course> officeCourseSearch(String searchContent);

    /**
     * 教务处社团申请管理页面筛选功能
     * @param searchContent 筛选框
     * @return 活动列表
     */
    List<SocietyApplication> officeSocietyApprovalSearch(String searchContent);

    /**
     * 教务处社团管理页面筛选功能
     * @param searchContent 筛选框
     * @return 活动列表
     */
    List<Society> officeSocietySearch(String searchContent);
}
