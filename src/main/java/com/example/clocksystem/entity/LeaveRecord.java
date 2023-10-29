package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class LeaveRecord {
    @NotNull
    private int leaveNo;
    @NotNull
    @Future
    private Timestamp startTime;
    @NotNull
    @Future
    private Timestamp endTime;
    private String reason;
    @NotNull
    @Max(3)
    @Min(0)
    private int status;
    @NotNull
    private String sNo;
    @Valid
    private Student student;
}
