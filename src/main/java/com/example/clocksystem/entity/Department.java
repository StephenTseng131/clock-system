package com.example.clocksystem.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Department {
    @NotNull
    private String departmentNo;
    @NotNull
    private String departmentName;
}
