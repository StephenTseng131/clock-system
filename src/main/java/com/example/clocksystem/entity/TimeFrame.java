package com.example.clocksystem.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class TimeFrame {
    @NotNull
    private int timeNo;
    @NotNull
    private Timestamp startTime;
    @NotNull
    private Timestamp endtime;
}
