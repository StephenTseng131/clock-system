package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Attendance {
    @NotNull
    private int courseNo;
    @NotNull
    private String tNo;
    @NotNull
    private Timestamp startTime;
    @NotNull
    @Future
    private Timestamp endTime;
    @NotNull
    @Size(min = 6, max = 6)
    private String code;
    @Valid
    private Course course;

}
