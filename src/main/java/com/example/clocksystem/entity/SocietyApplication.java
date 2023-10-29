package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import javax.persistence.Lob;
import java.util.Arrays;

@Data
public class SocietyApplication {
    @NotNull
    private int applicationNo;
    @NotNull
    private String societyName;
    @NotNull
    @Max(1)
    @Min(0)
    private int type;
    @Lob
    private byte[] picture;
    @Valid
    private String introduce;
    @Valid
    private String reason;
    @NotNull
    @Max(2)
    @Min(0)
    private int officeStatus;
    @Valid
    @Max(2)
    @Min(0)
    private int status;
    @Valid
    private String societyId;
    @NotNull
    private String sNo;
    @Valid
    private String tNo;
    @Valid
    private Society society;
    @Valid
    private Student student;
    @Valid
    private Teacher teacher;

    @Override
    public String toString() {
        return "SocietyApplication{" +
                "applicationNo=" + applicationNo +
                ", societyName='" + societyName + '\'' +
                ", type=" + type +
                ", picture=" + Arrays.toString(picture) +
                ", introduce='" + introduce + '\'' +
                ", reason='" + reason + '\'' +
                ", officeStatus=" + officeStatus +
                ", status=" + status +
                ", societyId='" + societyId + '\'' +
                ", sNo='" + sNo + '\'' +
                ", tNo='" + tNo + '\'' +
                ", student=" + student +
                ", teacher=" + teacher +
                '}';
    }
}
