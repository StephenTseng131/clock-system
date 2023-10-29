package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class Student {
    @NotNull
    private String sNo;
    @NotNull
    private String sName;
    @NotNull
    private String password;
    @NotNull
    @Size(min = 11, max = 11)
    private String sPhone;
    @NotNull
    @Size(min = 18, max = 18)
    private String idCard;
    @NotNull
    private String classNo;
    @Valid
    private Class stuClass;
    //根据studentCourse表
    @Valid
    private List<Course> stuCourseList;
    //存储学生的考勤记录
    @Valid
    private List<ClockRecord> stuClockRecordList;
    //存储学生的请假记录
    @Valid
    private List<LeaveRecord> stuLeaveRecordList;
    //存储学生的申诉申请
    @Valid
    private List<StuAppeal> stuAppealList;
    @Valid
    private double attendTime;
    @Valid
    private double absentTime;

    @Override
    public String toString() {
        return "Student{" +
                "sNo='" + sNo + '\'' +
                ", sName='" + sName + '\'' +
                ", password='" + password + '\'' +
                ", sPhone='" + sPhone + '\'' +
                ", idCard='" + idCard + '\'' +
                ", classNo='" + classNo + '\'' +
                ", stuClass=" + stuClass +
                ", attendTime=" + attendTime +
                ", absentTime=" + absentTime +
                '}';
    }
}
