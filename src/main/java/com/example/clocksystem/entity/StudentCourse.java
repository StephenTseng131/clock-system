package com.example.clocksystem.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentCourse {
    @NotNull
    private String sNo;
    @NotNull
    private int courseNo;
}
