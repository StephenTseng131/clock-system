package com.example.clocksystem.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentActivity {
    @NotNull
    private int activityNo;
    @NotNull
    private String sNo;
    @Valid
    @Max(1)
    @Min(0)
    private int status;
    @Valid
    private Activity activity;
    @Valid
    private Student student;

    @Override
    public String toString() {
        return "StudentActivity{" +
                "activityNo=" + activityNo +
                ", sNo='" + sNo + '\'' +
                ", status=" + status +
                ", activity=" + activity +
                '}';
    }
}
