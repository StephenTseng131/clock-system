package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Class {
    @NotNull
    private String classNo;
    @NotNull
    private String className;
    @NotNull
    private String tNo;
    @Valid
    private double number;
    @Valid
    private double absence;
    @Valid
    private Teacher teacher;
}
