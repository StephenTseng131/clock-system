package com.example.clocksystem.service.impl;

import com.example.clocksystem.entity.*;
import com.example.clocksystem.entity.Class;
import com.example.clocksystem.mapper.*;
import com.example.clocksystem.service.OfficeService;
import com.example.clocksystem.utils.CreateClassCode;
import com.example.clocksystem.utils.CreateIdUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.clocksystem.utils.SearchJudge.searchJudge;

/**
 * @author 94548
 */
@Service
@Transactional
public class OfficeServiceImpl implements OfficeService {
    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private SocietyMapper societyMapper;
    @Autowired
    private SocietyApplicationMapper societyApplicationMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private ActivityApplicationMapper activityApplicationMapper;

    /**
     * 教务处查询教务处对象
     *
     * @param id 教务处账号
     * @return 教务处对象
     */
    @Override
    public Office officeFindOffice(String id) {
        return officeMapper.findOfficeByOfficeId(id);
    }

    /**
     * 教务处查询所有社团列表
     *
     * @param pageNum 页码
     * @return 社团列表
     */
    @Override
    public PageInfo<Society> officeFindSocieties(int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = societyMapper.findAllSocieties();
        return new PageInfo<>(list);
    }

    /**
     * 教务处查询所有社团申请
     *
     * @param pageNum 页码
     * @return 社团申请列表
     */
    @Override
    public PageInfo<SocietyApplication> officeFindAllSocietyApplication(int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = societyApplicationMapper.findSocietyApplicationsToOffice();
        return new PageInfo<>(list);
    }

    /**
     * 教务处查询社团申请详情记录
     *
     * @param applicationNo 申请编号
     * @return 社团申请对象
     */
    @Override
    public SocietyApplication officeFindSocietyApplication(Integer applicationNo) {
        return societyApplicationMapper.findSocietyApplicationByApplicationNo(applicationNo);
    }

    /**
     * 教务处更新社团申请的教务处状态
     *
     * @param applicationNo 申请编号
     * @param result        审批结果
     */
    @Override
    public void officeUpdateApplicationOfficeStatus(int applicationNo, int result) {
        Set<String> societyIdList = new HashSet<>();
        String societyId;
        List<Society> allSocieties = societyMapper.findAllSocieties();
        for (Society society : allSocieties) {
            societyIdList.add(society.getSocietyId());
        }
        // 社团申请对象
        SocietyApplication societyApplication = societyApplicationMapper.findSocietyApplicationByApplicationNo(applicationNo);
        societyApplication.setOfficeStatus(result);
        societyApplication.setStatus(result);
        // 更新审批状态
        societyApplicationMapper.updateStatus(societyApplication);
        //对申请创建社团申请的处理
        if (societyApplication.getType() == 0) {
            // 同意，创建账号
            if (result == 1) {
                do {
                    // 生成固定位数的课程码
                    societyId = CreateClassCode.generateRandomCourseCode(8);
                } while (societyIdList.contains(societyId));
                societyIdList.add(societyId);
                // 活动申请对象
                // 社团对象
                Society society = new Society();
                society.setSocietyId(societyId);
                society.setPassword("123456");
                society.setSocietyName(societyApplication.getSocietyName());
                society.setIntroduce(societyApplication.getIntroduce());
                society.setTNo(societyApplication.getTNo());
                society.setPicture(societyApplication.getPicture());
                society.setSNo(societyApplication.getSNo());
                // 创建社团
                societyMapper.addSociety(society);
                // 社团账号关联
                societyApplication.setSocietyId(societyId);
                societyApplicationMapper.updateSetSocietyId(societyApplication);
            }
        }
        //对申请修改社团信息申请的处理
        else if (societyApplication.getType() == 1) {
            //同意，修改社团信息
            if (result == 1) {
                Society society = societyMapper.findSocietyBySocietyId(societyApplication.getSocietyId());
                society.setPicture(societyApplication.getPicture());
                society.setSocietyName(societyApplication.getSocietyName());
                society.setIntroduce(societyApplication.getIntroduce());
                society.setTNo(societyApplication.getTNo());
                societyMapper.updateSocietyInfo(society);
            }
        }
    }

    /**
     * 教务处查询所有班级
     *
     * @param pageNum 页码
     * @return 班级列表
     */
    @Override
    public PageInfo<Class> officeFindClasses(int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = classMapper.findAll();
        return new PageInfo<>(list);
    }

    /**
     * 教务处删除班级
     *
     * @param classNo 班级编号
     * @return 0：失败，否则成功
     */
    @Override
    public int officeDeleteClass(String classNo) {
        return classMapper.deleteClass(classNo);
    }

    /**
     * 教务处添加班级
     *
     * @param partment 班级对象
     * @return 0：成功/1：失败
     */
    @Override
    public int officeAddClass(Class partment) {
        String classNo = "";
        Class lastClass = classMapper.findLastClassByYear(partment.getClassNo());
        partment.setClassName(partment.getClassNo() + partment.getClassName());
        if (lastClass == null) {
            classNo = partment.getClassNo() + "00";
        } else {
            classNo = CreateIdUtils.createClassNo(lastClass.getClassNo());
        }
        if ("error".equals(classNo)) {
            // 班级不可添加
            return 1;
        } else {
            // 可添加班级
            partment.setClassNo(classNo);
            classMapper.addClass(partment);
            return 0;
        }
    }

    /**
     * 教务处查找班级详情
     *
     * @param classNo 班级号
     * @return 班级对象
     */
    @Override
    public Class officeFindClassDetail(String classNo) {
        return classMapper.findByClassNo(classNo);
    }

    /**
     * 教务处更新班级信息
     *
     * @param partment 班级对象
     * @return 0：更新成功/1：更新失败
     */
    @Override
    public int officeUpdateClass(Class partment) {
        if (classMapper.updateClass(partment) != 0) {
            return 0;
        }
        return 1;
    }

    /**
     * 教务处查询所有课程
     *
     * @param pageNum 页码
     * @return 课程列表
     */
    @Override
    public PageInfo<Course> officeFindCourses(int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = courseMapper.findAllCourses();
        return new PageInfo<>(list);
    }

    /**
     * 教务处查询学院信息
     *
     * @return 学院列表
     */
    @Override
    public List<Department> officeFindDepartments() {
        return departmentMapper.findAllDepartments();
    }

    /**
     * 教务处查询学院教师
     *
     * @param departmentNo 学院号
     * @return 学院教师列表
     */
    @Override
    public List<Teacher> officeFindTeacherListByDepartment(String departmentNo) {
        return teacherMapper.findTeachersByDepartmentNo(departmentNo);
    }

    /**
     * 教务处为教师添加课程
     *
     * @param courseName 课程名
     * @param teacherNo  教师号
     * @param week       星期
     * @param startTime  上课小节
     * @param endTime    下课小节
     * @return 0:添加成功，1:添加失败
     */
    @Override
    public int officeAddCourse(String courseName, String teacherNo, Integer week, Integer startTime, Integer endTime) {
        Timestamp start = courseMapper.findTimeFrameByTimeNo(startTime).getStartTime();
        Timestamp end = courseMapper.findTimeFrameByTimeNo(endTime).getEndtime();
        // 该教师所有的课程
        List<Course> courseList = courseMapper.findCoursesByTno(teacherNo);
        // 判断是否能够添加这门课
        boolean flag = false;
        //判断课程list是否为空
        if (!courseList.isEmpty()) {
            for (Course c : courseList) {
                // 同一星期
                if (c.getWeek() == week) {
                    if (c.getStartTime().getTime() > end.getTime() || c.getEndTime().getTime() < start.getTime()) {
                        // 课程无交集设为true
                        flag = true;
                    } else {
                        flag = false;
                        break;
                    }
                } else {
                    flag = true;
                }
            }
        } else {
            flag = true;
        }


        if (flag) {
            // 时间不冲突才可以添加课程
            Course course = new Course();
            course.setCourseName(courseName);
            course.setStartTime(start);
            course.setEndTime(end);
            course.setWeek(week);
            course.setTNo(teacherNo);
            courseMapper.addCourse(course);
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * 教务处删除课程
     *
     * @param courseNo 课程号
     * @return 0:删除失败，否则删除成功
     */
    @Override
    public int officeDeleteCourse(Integer courseNo) {
        return courseMapper.deleteByCourseNo(courseNo);
    }

    /**
     * 教务处查询辅导员
     *
     * @return 辅导员列表
     */
    @Override
    public List<Teacher> officeFindManageTeachers() {
        return teacherMapper.findByRoleName("辅导员");
    }

    /**
     * 教务处查询活动列表
     *
     * @return 活动列表
     */
    @Override
    public List<Activity> officeFindActivities() {
        return activityMapper.findAllActivities();
    }

    /**
     * 教务处删除社团
     *
     * @param societyId 社团ID
     * @return 0：删除失败，否则，删除成功
     */
    @Override
    public int officeDeleteSociety(String societyId) {
        return societyMapper.deleteSociety(societyId);
    }

    /**
     * 教务处查找教师审核通过的活动审批
     *
     * @param pageNum 页码
     * @return 活动列表
     */
    @Override
    public PageInfo<ActivityApplication> officeFindActivityApplications(int pageNum) {
        PageHelper.startPage(pageNum, 8);
        var list = activityApplicationMapper.findActivityApplicationsToOffice();
        return new PageInfo<>(list);
    }

    /**
     * 教务处查找活动申请详情
     *
     * @param applicationNo 申请编号
     * @return 活动申请详情对象
     */
    @Override
    public ActivityApplication officeFindActivityApplicationDetail(Integer applicationNo) {
        return activityApplicationMapper.findActivityApplicationByApplicationNo(applicationNo);
    }

    /**
     * 教务处跟新活动审批状态
     *
     * @param applicationNo 申请编号
     * @param result        审批结果
     * @return 0：成功；1：失败
     */
    @Override
    public int officeUpdateActivityApplicationStatus(int applicationNo, int result) {
        ActivityApplication activityApplication = activityApplicationMapper.findActivityApplicationByApplicationNo(applicationNo);
        //修改状态
        activityApplication.setOfficeStatus(result);
        activityApplication.setStatus(result);
        if (activityApplicationMapper.updateStatus(activityApplication) != 0) {
            //审批同意
            if (result == 1) {
                //在活动表中插入这条活动
                Activity activity = new Activity();
                //封装活动对象
                activity.setActivityName(activityApplication.getActivityName());
                activity.setIntroduce(activityApplication.getIntroduce());
                activity.setType(activityApplication.getType());
                activity.setCredit(activityApplication.getCredit());
                activity.setStartTime(activityApplication.getStartTime());
                activity.setEndTime(activityApplication.getEndTime());
                activity.setSocietyId(activityApplication.getSocietyId());
                if (activityMapper.addActivity(activity) != 0) {
                    //添加成功
                    return 0;
                }
                return 1;
            }
        }
        return 1;
    }

    /**
     * 教务处活动管理页面筛选功能
     * @param searchContent 筛选框
     * @return 活动列表
     */
    public List<Activity> officeActivitySearch(String searchContent){
        var activities = activityMapper.findAllActivities();
        var activityList = new ArrayList<Activity>();
        //进行搜索
        for (var activity : activities) {
            if (searchJudge(searchContent, activity.toString())) {
                activityList.add(activity);
            }
        }
        return activityList;
    }

    /**
     * 教务处班级管理页面筛选功能
     * @param searchContent 筛选框
     * @return 活动列表
     */
    public List<Class> officeClassSearch(String searchContent){
        var classes = classMapper.findAll();
        var classList = new ArrayList<Class>();
        //进行搜索
        for (var aClass : classes) {
            if (searchJudge(searchContent, aClass.toString())) {
                classList.add(aClass);
            }
        }
        return classList;
    }

    /**
     * 教务处课程管理页面筛选功能
     * @param searchContent 筛选框
     * @return 活动列表
     */
    public List<Course> officeCourseSearch(String searchContent){
        var courses = courseMapper.findAllCourses();
        var courseList = new ArrayList<Course>();
        //进行搜索
        for (var aCourse : courses) {
            if (searchJudge(searchContent, aCourse.toString())) {
                courseList.add(aCourse);
            }
        }
        return courseList;
    }

    /**
     * 教务处社团申请管理页面筛选功能
     * @param searchContent 筛选框
     * @return 活动列表
     */
    public List<SocietyApplication> officeSocietyApprovalSearch(String searchContent){
        var societyApplications = societyApplicationMapper.findSocietyApplicationsToOffice();
        var societyApplicationList = new ArrayList<SocietyApplication>();
        //进行搜索
        for (var aSocietyApplication : societyApplications) {
            if (searchJudge(searchContent, aSocietyApplication.toString())) {
                societyApplicationList.add(aSocietyApplication);
            }
        }
        return societyApplicationList;
    }
    /**
     * 教务处社团管理页面筛选功能
     * @param searchContent 筛选框
     * @return 活动列表
     */
    public List<Society> officeSocietySearch(String searchContent){
        var societies = societyMapper.findAllSocieties();
        var societyList = new ArrayList<Society>();
        //进行搜索
        for (var aSociety : societies) {
            if (searchJudge(searchContent, aSociety.toString())) {
                societyList.add(aSociety);
            }
        }
        return societyList;
    }
}
