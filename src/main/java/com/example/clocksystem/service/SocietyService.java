package com.example.clocksystem.service;

import com.example.clocksystem.entity.*;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 94548
 */
public interface SocietyService {
    //社团信息
    /**
     * 社团查询社团
     * @param id 社团账号
     * @return 社团对象
     */
    Society societyFindSociety(String id);

    /**
     * 社团更新社团密码
     * @param society 社团对象
     * @return 0：更新失败/否则，更新成功
     */
    int societyUpdatePassword(Society society);

    /**
     * 社团修改社团信息需要提交申请
     * @param picture 社团图片
     * @param societyName 社团名称
     * @param tNo 指导老师教师
     * @param introduce 社团介绍
     * @param society 社团对象
     * @return 0：发起成功，1：发起失败，2：信息未改动
     */
    int societyModifySocietyInfoApplication(MultipartFile picture, String societyName, String tNo, String introduce, Society society);

    //活动管理的方法

    /**
     * 社团展示活动列表
     * @param societyId 社团账号
     * @param pageNum 页码
     * @return 属于该社团的活动列表
     */
    PageInfo<Activity> societyFindActivities(String societyId, int pageNum);

    /**
     * 社团按活动编号查找活动详情
     * @param activityNo 活动编号
     * @return 活动对象
     */
    Activity societyFindSocietyAcitivty(Integer activityNo);

    /**
     * 社团按活动编号查找活动学生列表
     * @param activityNo 活动编号
     * @param pageNum 页码
     * @return 参与该活动的活动学生信息列表
     */
    PageInfo<StudentActivity> societyFindStudentActivitiesByActivityNo(Integer activityNo, Integer pageNum);

    /**
     * 社团设置活动签到码
     * @param activity 活动对象
     * @return 0：设置成功；1：设置失败
     */
    int societySetAcitivtyCode(Activity activity);

    /**
     * 社团查找活动申请列表
     * @param societyId 社团账号
     * @param pageNum 页码
     * @return 该社团的活动申请列表
     */
    PageInfo<ActivityApplication> societyFindActivityApplicationsBySocietyId(String societyId, int pageNum);

    /**
     * 社团发起申请活动申请
     * @param society 社团对象
     * @param activityName 活动名称
     * @param introduce 活动介绍
     * @param type 活动类别
     * @param credit 学分
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 0：发起成功；1：发起失败
     */
    int societyAddActivityApplication(Society society, String activityName, String introduce, int type, int credit, String startTime, String endTime);

    //辅助方法
    /**
     * 社团查询学院信息
     * @return 学院列表
     */
    List<Department> societyFindDepartments();

    /**
     * 社团查询学院教师
     * @return 学院教师列表
     */
    List<Teacher> societyFindTeacherListByDepartmentNo(String departmentNo);

    /**
     * 社团活动审核记录页面筛选功能
     * @param searchContent 筛选框
     * @param societyId 社团的id
     * @return 活动审核列表
     */
    List<ActivityApplication> societyActivityApprovalSearch(String searchContent,String societyId);

    /**
     * 社团活动管理页面筛选功能
     * @param searchContent 筛选框
     * @param societyId 社团的id
     * @return 活动列表
     */
    List<Activity> societyActivitySearch(String searchContent,String societyId);
}
