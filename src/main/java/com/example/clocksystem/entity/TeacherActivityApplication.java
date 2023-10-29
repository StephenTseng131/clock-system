package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TeacherActivityApplication {
    @NotNull
    private int applicationNo;
    @NotNull
    private String tNo;
    @NotNull
    @Max(2)
    @Min(0)
    private int status;
    @Valid
    private ActivityApplication activityApplication;
    @Valid
    private Teacher teacher;
}

