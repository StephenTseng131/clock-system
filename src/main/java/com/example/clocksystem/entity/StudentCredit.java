package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentCredit {
    @NotNull
    private String sNo;
    @NotNull
    private int moral;
    @NotNull
    private int intellectual;
    @NotNull
    private int physical;
    @NotNull
    private int aesthetic;
    @NotNull
    private int labor;
    @NotNull
    private int total;
    @Valid
    private Student student;
}
