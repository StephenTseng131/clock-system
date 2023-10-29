package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Activity {
    @NotNull
    private int activityNo;
    @NotNull
    private String activityName;
    @Valid
    private String introduce;
    @NotNull
    @Max(4)
    @Min(0)
    private int type;
    @Valid
    private int credit;
    @NotNull
    private Timestamp startTime;
    @NotNull
    private Timestamp endTime;
    @Valid
    private String code;
    @Valid
    @Max(2)
    @Min(0)
    private int status;
    @NotNull
    private String societyId;
    @Valid
    private Society society;

    @Override
    public String toString() {
        return "Activity{" +
                "activityNo=" + activityNo +
                ", activityName='" + activityName + '\'' +
                ", type=" + type +
                ", credit=" + credit +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", code='" + code + '\'' +
                ", status=" + status +
                ", societyId='" + societyId + '\'' +
                ", society=" + society +
                '}';
    }
}
