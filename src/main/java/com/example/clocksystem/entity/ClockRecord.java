package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ClockRecord {
    @NotNull
    private int clockNo;
    @NotNull
    @Past
    private Timestamp time;
    @NotNull
    int courseNo;
    @NotNull
    private String manageTNo;
    @NotNull
    private String courseTNo;
    @NotNull
    @Max(2)
    @Min(0)
    private int status;
    @NotNull
    private String sNo;
    @NotNull
    private int appealStatus;
    @Valid
    private Course course;
    @Valid
    private Student student;
}
