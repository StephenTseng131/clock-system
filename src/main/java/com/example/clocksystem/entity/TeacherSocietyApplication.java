package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeacherSocietyApplication {
    @NotNull
    private int applicationNo;
    @Valid
    private String tNo;
    @NotNull
    @Max(2)
    @Min(0)
    private int status;
    @Valid
    private Teacher teacher;
    @Valid
    private SocietyApplication societyApplication;

    @Override
    public String toString() {
        return "TeacherSocietyApplication{" +
                "applicationNo=" + applicationNo +
                ", tNo='" + tNo + '\'' +
                ", status=" + status +
                ", societyApplication=" + societyApplication +
                '}';
    }
}
