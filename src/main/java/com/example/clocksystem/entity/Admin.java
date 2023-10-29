package com.example.clocksystem.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Admin {
    @NotNull
    private String id;
    @NotNull
    private String password;
}
