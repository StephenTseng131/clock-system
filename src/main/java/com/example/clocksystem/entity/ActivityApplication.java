package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ActivityApplication {
    @NotNull
    private int applicationNo;
    @NotNull
    private String activityName;
    @Valid
    private String introduce;
    @NotNull
    @Max(4)
    @Min(0)
    private int type;
    @NotNull
    private int credit;
    @NotNull
    private Timestamp startTime;
    @NotNull
    private Timestamp endTime;
    @NotNull
    @Max(2)
    @Min(0)
    private int officeStatus;
    @NotNull
    @Max(2)
    @Min(0)
    private int status;
    @NotNull
    private String societyId;
    @Valid
    private Society society;
}
