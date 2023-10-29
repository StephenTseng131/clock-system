package com.example.clocksystem.utils;

import java.util.Random;

public class CreateClassCode {
    public static String generateRandomCourseCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        Random random = new Random();
        StringBuilder courseCode = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(0,61);
            courseCode.append(characters.charAt(index));
        }
        return courseCode.toString();
    }
}
