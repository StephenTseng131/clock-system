package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeacherLeaveRecord {
    @NotNull
    private int leaveNo;
    @NotNull
    private String tNo;
    @NotNull
    @Max(3)
    @Min(0)
    private int status;
    @Valid
    private LeaveRecord leaveRecord;
}
