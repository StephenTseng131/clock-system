package com.example.clocksystem.service.impl;

import com.example.clocksystem.entity.*;
import com.example.clocksystem.entity.Class;
import com.example.clocksystem.mapper.*;
import com.example.clocksystem.service.AdminService;
import com.example.clocksystem.utils.CreateIdUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.clocksystem.utils.SearchJudge.searchJudge;

/**
 * @author 94548
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private StudentCreditMapper studentCreditMapper;


    /**
     * 管理员查找所有学生
     *
     * @param pageNum 页码
     * @return 学生列表
     */
    @Override
    public PageInfo<Student> adminFindAllStudents(int pageNum) {
        PageHelper.startPage(pageNum, 10);
        var list = studentMapper.findAllStudents();
        return new PageInfo<>(list);
    }

    /**
     * 管理员查找所有行政班
     *
     * @return 班级列表
     */
    @Override
    public List<Class> adminFindAllClasses() {
        return classMapper.findAll();
    }

    /**
     * 管理员添加学生
     *
     * @param student 学生对象
     * @return 0：添加成功，1：添加失败
     */
    @Override
    public int adminAddstudent(Student student) {
        String sNo = "";
        Student lastStudent = studentMapper.findLastStudentByClassNo(student.getClassNo());
        if (lastStudent == null) {
            sNo = student.getClassNo() + "00";
        } else {
            sNo = CreateIdUtils.createSno(lastStudent.getSNo());
        }
        if ("error".equals(sNo)) {
            // 班级不可添加
            return 1;
        } else {
            // 可添加学生
            student.setSNo(sNo);
            studentMapper.addStudent(student);
            StudentCredit studentCredit = new StudentCredit();
            studentCredit.setSNo(sNo);
            studentCreditMapper.addStudentCredit(studentCredit);
            return 0;
        }
    }

    /**
     * 管理员删除学生
     *
     * @param sNo 学号
     * @return 0:删除失败，否则删除成功
     */
    @Override
    public int adminDeleteStudent(String sNo) {
        return studentMapper.deleteBySno(sNo);
    }

    /**
     * 管理员查找指定学生
     *
     * @param sNo 学号
     * @return 学生对象
     */
    @Override
    public Student adminFindStudent(String sNo) {
        return studentMapper.findBySno(sNo);
    }

    /**
     * 管理员更新学生信息
     *
     * @param student 学生对象
     * @return 0:更新失败，否则更新成功
     */
    @Override
    public int adminUpdateStudent(Student student) {
        return studentMapper.updateStudent(student);
    }

    /**
     * 管理员学生管理页面筛选功能
     *
     * @param searchContent 筛选框
     * @return 学生列表
     */
    @Override
    public List<Student> adminStudentSearch(String searchContent) {
        var students = studentMapper.findAllStudents();
        var studentList = new ArrayList<Student>();
        //进行搜索
        for (var student : students) {
            if (searchJudge(searchContent, student.toString())) {
                studentList.add(student);
            }
        }
        return studentList;
    }

    /**
     * 管理员查找指定教师
     *
     * @param tNo 教师号
     * @return 教师对象
     */
    @Override
    public Teacher adminFindTeacher(String tNo) {
        return teacherMapper.findByTno(tNo);
    }

    /**
     * 管理员添加教师
     *
     * @param teacher 教师对象
     * @return 0:添加成功，1:添加失败
     */
    @Override
    public int adminAddTeacher(Teacher teacher) {
        String tNo = "";
        Teacher lastTeacher = teacherMapper.findLastTeacherByDepartmentNo(teacher.getDepartmentNo());
        if (lastTeacher == null) {
            tNo = teacher.getDepartmentNo() + "000";
        } else {
            tNo = CreateIdUtils.createTno(lastTeacher.getTNo());
        }
        if ("error".equals(tNo)) {
            // 学院不可添加
            return 1;
        } else {
            teacher.setTNo(tNo);
            teacherMapper.addTeacher(teacher);
            return 0;
        }
    }

    /**
     * 管理员查询学院信息
     *
     * @return 学院列表
     */
    @Override
    public List<Department> adminFindDepartments() {
        return departmentMapper.findAllDepartments();
    }

    /**
     * 管理员查找所有教师
     *
     * @param pageNum 页码
     * @return 教师列表
     */
    @Override
    public PageInfo<Teacher> adminFindAllTeachers(int pageNum) {
        PageHelper.startPage(pageNum, 10);
        var list = teacherMapper.findAll();
        return new PageInfo<>(list);
    }

    /**
     * 管理员删除教师
     *
     * @param tNo 教师号
     * @return 0:删除失败，否则删除成功
     */
    @Override
    public int adminDeleteTeacher(String tNo) {
        return teacherMapper.deleteByTno(tNo);
    }

    /**
     * 管理员更新教师信息
     *
     * @param teacher 教师对象
     * @return 0:更新失败，否则更新成功
     */
    @Override
    public int adminUpdateTeacher(Teacher teacher) {
        return teacherMapper.updateTeacher(teacher);
    }

    /**
     * 管理员教师管理页面筛选功能
     *
     * @param searchContent 筛选框内容
     * @return 教师列表
     */
    @Override
    public List<Teacher> adminTeacherSearch(String searchContent) {
        var teachers = teacherMapper.findAll();
        var teacherList = new ArrayList<Teacher>();
        //进行搜索
        for (var teacher : teachers) {
            if (searchJudge(searchContent, teacher.toString())) {
                teacherList.add(teacher);
            }
        }
        return teacherList;
    }

    /**
     * 管理员查找所有管理员
     *
     * @param pageNum 页码
     * @return 管理员列表
     */
    @Override
    public PageInfo<Admin> adminFindAllAdmins(Integer pageNum) {
        PageHelper.startPage(pageNum, 10);
        var admins = adminMapper.findAll();
        return new PageInfo<>(admins, 10);
    }

    /**
     * 管理员删除其他管理员
     *
     * @param id 管理员id
     * @return 0:删除失败，否则删除成功
     */
    @Override
    public int adminDeleteAdmin(String id, Admin admin) {
        if (admin.getId().equals(id)) {
            //现在登录的管理员不能删除
            return 0;
        } else {
            return adminMapper.deleteAdmin(id);
        }
    }

    /**
     * 管理员查看管理员详情
     *
     * @param id 管理员id
     * @return 管理员对象
     */
    @Override
    public Admin adminFindAdmin(String id) {
        return adminMapper.findById(id);
    }

    /**
     * 管理员添加管理员
     *
     * @param admin 管理员对象
     * @return 0:添加成功，1:添加失败
     */
    @Override
    public int adminAddAdmin(Admin admin) {
        if (adminMapper.findById(admin.getId()) == null) {
            // id号不存在才可以添加管理员
            adminMapper.addAdmin(admin);
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * 添加管理员的失去焦点事件
     *
     * @param id 管理员id
     * @return 0：id不存在，1：id存在
     */
    @Override
    public int adminJudgeAdmin(String id) {
        if (adminMapper.findById(id) != null) {
            // id存在
            return 1;
        } else {
            // id不存在
            return 0;
        }
    }

    /**
     * 管理员在管理管理员页面筛选功能
     *
     * @param searchContent 筛选框内容
     * @return 管理员列表
     */
    @Override
    public List<Admin> adminAdminSearch(String searchContent) {
        var admins = adminMapper.findAll();
        var adminList = new ArrayList<Admin>();
        //进行搜索
        for (var admin : admins) {
            if (searchJudge(searchContent, admin.toString())) {
                adminList.add(admin);
            }
        }
        return adminList;
    }

    /**
     * 管理员更新教务处信息
     *
     * @param officeId 教务处号
     * @return 0：更新成功，1：更新失败
     */
    @Override
    public int resetOffice(String officeId) {
        var office = officeMapper.findOfficeByOfficeId(officeId);
        office.setPassword("123456");
        if (officeMapper.updateOffice(office) > 0) {
            return 0;
        }
        return 1;
    }
}
