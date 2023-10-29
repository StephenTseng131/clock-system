package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import javax.persistence.Lob;

@Data
public class Society {
    @NotNull
    private String societyId;
    @NotNull
    private String password;
    @NotNull
    private String societyName;
    @Lob
    private byte[] picture;
    @Valid
    private String introduce;
    @Valid
    private String sNo;
    @Valid
    private String tNo;
    @Valid
    private Student student;
    @Valid
    private Teacher teacher;
}
