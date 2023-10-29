package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeacherCourse {
    @NotNull
    private String courseClass;
    @NotNull
    private String tNo;
    @NotNull
    private int courseNo;
    @Valid
    private Course course;

    @Override
    public String toString() {
        return "TeacherCourse{" +
                "courseClass='" + courseClass + '\'' +
                ", tNo='" + tNo + '\'' +
                ", courseNo=" + courseNo +
                '}';
    }
}
