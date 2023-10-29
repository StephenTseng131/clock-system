package com.example.clocksystem.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Office {
    @NotNull
    private String officeId;
    @NotNull
    private String password;
}
