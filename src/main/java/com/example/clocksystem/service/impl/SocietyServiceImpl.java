package com.example.clocksystem.service.impl;

import com.example.clocksystem.entity.*;
import com.example.clocksystem.mapper.*;
import com.example.clocksystem.service.SocietyService;
import com.example.clocksystem.utils.TimeConvert;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.clocksystem.utils.SearchJudge.searchJudge;

/**
 * @author 94548
 */
@Service
@Transactional
public class SocietyServiceImpl implements SocietyService {
    @Autowired
    private SocietyMapper societyMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private SocietyApplicationMapper societyApplicationMapper;
    @Autowired
    private TeacherSocietyApplicationMapper teacherSocietyApplicationMapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private StudentActivityMapper studentActivityMapper;
    @Autowired
    private ActivityApplicationMapper activityApplicationMapper;
    @Autowired
    private TeacherActivityApplicationMapper teacherActivityApplicationMapper;

    /**
     * 社团查询社团
     *
     * @param id 社团账号
     * @return 社团对象
     */
    @Override
    public Society societyFindSociety(String id) {
        return societyMapper.findSocietyBySocietyId(id);
    }

    /**
     * 社团更新社团密码
     *
     * @param society 社团对象
     * @return 0：更新失败/否则，更新成功
     */
    @Override
    public int societyUpdatePassword(Society society) {
        return societyMapper.updateSocietyPassword(society);
    }

    /**
     * 社团修改社团信息需要提交申请
     *
     * @param picture     社团图片
     * @param societyName 社团名称
     * @param tNo         指导老师教师
     * @param introduce   社团介绍
     * @param society     社团对象
     * @return 0：发起成功，1：发起失败，2：信息未改动
     */
    @Override
    public int societyModifySocietyInfoApplication(MultipartFile picture, String societyName, String tNo, String introduce, Society society) {
        try {
            //创建申请对象
            var societyApplication = new SocietyApplication();
            boolean changed = false;
            //封装对象
            //如果更新的社团团徽不为空，则放入对象；否则用原团徽
            if (!picture.isEmpty()) {
                byte[] imageData = picture.getBytes();
                societyApplication.setPicture(imageData);
                changed = true;
            }
            else {
                societyApplication.setPicture(society.getPicture());
            }
            //判断是否改变了信息
            if (!societyName.equals(society.getSocietyName()) || !introduce.equals(society.getIntroduce()) || !tNo.equals(society.getTNo())) {
                changed = true;
            }
            //改变了信息则提交申请
            if (changed) {
                societyApplication.setSocietyId(society.getSocietyId());
                societyApplication.setSocietyName(societyName);
                societyApplication.setIntroduce(introduce);
                societyApplication.setSNo(society.getSNo());
                societyApplication.setTNo(tNo);
                societyApplication.setType(1);
                societyApplication.setOfficeStatus(0);
                societyApplication.setStatus(0);
                if (societyApplicationMapper.addSocietyApplication(societyApplication) == 0) {
                    return 1;
                }
                else {
                    var teacherSocietyApplication = new TeacherSocietyApplication();
                    //给教师关联上
                    var lastSocietyApplication = societyApplicationMapper.findLastSocietyApplicationBySocietyId(society.getSocietyId());
                    teacherSocietyApplication.setApplicationNo(lastSocietyApplication.getApplicationNo());
                    teacherSocietyApplication.setTNo(society.getTNo());
                    teacherSocietyApplication.setStatus(0);
                    teacherSocietyApplicationMapper.addTeacherSocietyApplication(teacherSocietyApplication);
                    //教师不一样则给新的指导老师添加审批记录
                    if (!tNo.equals(society.getTNo())) {
                        teacherSocietyApplication.setTNo(tNo);
                        teacherSocietyApplicationMapper.addTeacherSocietyApplication(teacherSocietyApplication);
                    }
                }
                return 0;
            }
            return 2;
        }
        catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * 社团展示活动列表
     *
     * @param societyId 社团账号
     * @param pageNum 页码
     * @return 属于该社团的活动列表
     */
    @Override
    public PageInfo<Activity> societyFindActivities(String societyId, int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = activityMapper.findActivitiesBySocietyId(societyId);
        return new PageInfo<>(list);
    }

    /**
     * 社团按活动编号查找活动详情
     *
     * @param activityNo 活动编号
     * @return 活动对象
     */
    @Override
    public Activity societyFindSocietyAcitivty(Integer activityNo) {
        return activityMapper.findActivityByNo(activityNo);
    }

    /**
     * 社团按活动编号查找活动学生列表
     *
     * @param activityNo 活动编号
     * @return 参与该活动的活动学生信息列表
     */
    @Override
    public PageInfo<StudentActivity> societyFindStudentActivitiesByActivityNo(Integer activityNo, Integer pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = studentActivityMapper.findStudentActivitiesByActivityNo(activityNo);
        return new PageInfo<>(list);
    }

    /**
     * 社团设置活动签到码
     *
     * @param activity 活动对象
     * @return 0：设置成功；1：设置失败
     */
    @Override
    public int societySetAcitivtyCode(Activity activity) {
        if (activityMapper.updateActivityCode(activity) != 0) {
            return 0;
        }
        return 1;
    }

    /**
     * 社团查找活动申请列表
     *
     * @param societyId 社团账号
     * @return 该社团的活动申请列表
     */
    @Override
    public PageInfo<ActivityApplication> societyFindActivityApplicationsBySocietyId(String societyId, int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = activityApplicationMapper.findActivityApplicationsBySocietyId(societyId);
        return new PageInfo<>(list);
    }

    /**
     * 社团发起申请活动申请
     *
     * @param society    社团对象
     * @param activityName 活动名称
     * @param introduce    活动介绍
     * @param type         活动类别
     * @param credit       学分
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return 0：发起成功；1：发起失败
     */
    @Override
    public int societyAddActivityApplication(Society society, String activityName, String introduce, int type, int credit, String startTime, String endTime) {
        //活动开始和结束时间
        var start = TimeConvert.timeConvert1(startTime);
        var end = TimeConvert.timeConvert1(endTime);
        //封装活动申请对象
        var activityApplication = new ActivityApplication();
        activityApplication.setActivityName(activityName);
        activityApplication.setIntroduce(introduce);
        activityApplication.setType(type);
        activityApplication.setCredit(credit);
        activityApplication.setStartTime(start);
        activityApplication.setEndTime(end);
        activityApplication.setOfficeStatus(0);
        activityApplication.setStatus(0);
        activityApplication.setSocietyId(society.getSocietyId());
        if (activityApplicationMapper.addActivityApplication(activityApplication) != 0) {
            //封装教师活动审批对象
            var application = activityApplicationMapper.findLastActivityApplicationBySocietyId(society.getSocietyId());
            var teacherActivityApplication = new TeacherActivityApplication();
            teacherActivityApplication.setApplicationNo(application.getApplicationNo());
            teacherActivityApplication.setTNo(society.getTNo());
            teacherActivityApplication.setStatus(0);
            if (teacherActivityApplicationMapper.addTeacherActivityApplication(teacherActivityApplication) != 0) {
                return 0;
            }
            return 1;
        }
        return 1;
    }

    /**
     * 社团查询学院信息
     *
     * @return 学院列表
     */
    @Override
    public List<Department> societyFindDepartments() {
        return departmentMapper.findAllDepartments();
    }

    /**
     * 社团查询学院教师
     *
     * @return 学院教师列表
     */
    @Override
    public List<Teacher> societyFindTeacherListByDepartmentNo(String departmentNo) {
        return teacherMapper.findTeachersByDepartmentNo(departmentNo);
    }

    /**
     * 社团活动审核记录页面筛选功能
     * @param searchContent 筛选框
     * @param societyId 社团的id
     * @return 活动审核列表
     */
    public List<ActivityApplication> societyActivityApprovalSearch(String searchContent,String societyId){
        var activityApplications = activityApplicationMapper.findActivityApplicationsBySocietyId(societyId);
        var activityApplicationList = new ArrayList<ActivityApplication>();
        //进行搜索
        for (var activityApplication : activityApplications) {
            if (searchJudge(searchContent, activityApplication.toString())) {
                activityApplicationList.add(activityApplication);
            }
        }
        return activityApplicationList;
    }

    /**
     * 社团活动管理页面筛选功能
     * @param searchContent 筛选框
     * @param societyId 社团的id
     * @return 活动列表
     */
    public List<Activity> societyActivitySearch(String searchContent,String societyId){
        var activities = activityMapper.findActivitiesBySocietyId(societyId);
        var activityList = new ArrayList<Activity>();
        //进行搜索
        for (var activity : activities) {
            if (searchJudge(searchContent, activity.toString())) {
                activityList.add(activity);
            }
        }
        return activityList;
    }
}
