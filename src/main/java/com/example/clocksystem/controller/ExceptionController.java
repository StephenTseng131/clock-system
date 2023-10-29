package com.example.clocksystem.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(Exception.class)
    public String doException(Exception e, Model model) {
        String errorInfo;
        if (e instanceof ArithmeticException) {
            errorInfo = "算术异常";
        } else if (e instanceof ArrayIndexOutOfBoundsException) {
            errorInfo = "数组下标越界异常";
        } else if (e instanceof SQLException) {
            errorInfo = "数据库访问异常";
        } else {
            errorInfo = "未知异常";
        }
        model.addAttribute("errorInfo", errorInfo);
        return "/error/404";
    }
}
