package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StuAppeal {
    @NotNull
    private int appealNo;
    private String reason;
    @NotNull
    @Max(3)
    @Min(0)
    private int status;
    @NotNull
    private String sNo;
    @NotNull
    private int clockNo;
    @Valid
    private Student student;
    @Valid
    private ClockRecord clockRecord;
}
