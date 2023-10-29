package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class Teacher {
    @NotNull
    private String tNo;
    @NotNull
    private String tName;
    @NotNull
    private String password;
    @NotNull
    @Size(min = 13, max = 13)
    private String tPhone;
    @NotNull
    private String roleName;
    @NotNull
    private String departmentNo;
    @Valid
    private Department department;
    //根据teacherCourse表
    @Valid
    private List<Course> teacherCourseList;
    //存储有关教师的请假审批，根据teacher_leaverecord表
    @Valid
    private List<TeacherLeaveRecord> teacherLeaveRecordList;
    //存储有关教师的申诉审批，根据teacher_stuapeal表
    @Valid
    private List<TeacherStuAppeal> teacherStuAppealList;
    //存储有关教师的发起打卡的记录
    @Valid
    private List<Attendance> teacherAttendanceList;

    @Override
    public String toString() {
        return "Teacher{" +
                "tNo='" + tNo + '\'' +
                ", tName='" + tName + '\'' +
                ", password='" + password + '\'' +
                ", tPhone='" + tPhone + '\'' +
                ", roleName='" + roleName + '\'' +
                ", departmentNo='" + departmentNo + '\'' +
                '}';
    }
}
