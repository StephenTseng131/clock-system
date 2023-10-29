package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Course {
    @NotNull
    private int courseNo;
    @NotNull
    private String courseName;
    @NotNull
    private String tNo;
    @NotNull
    @Max(7)
    @Min(1)
    private int week;
    @NotNull
    private Timestamp startTime;
    @NotNull
    private Timestamp endTime;
    @Valid
    private Teacher teacher;
    @Valid
    private TeacherCourse teacherCourse;
    @Valid
    private double number;
    @Valid
    private double absence;

    @Override
    public String toString() {
        return "Course{" +
                "courseNo=" + courseNo +
                ", courseName='" + courseName + '\'' +
                ", tNo='" + tNo + '\'' +
                ", week=" + week +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", teacherCourse=" + teacherCourse +
                ", number=" + number +
                ", absence=" + absence +
                '}';
    }
}
