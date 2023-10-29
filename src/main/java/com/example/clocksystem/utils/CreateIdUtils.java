package com.example.clocksystem.utils;

public class CreateIdUtils {
    /**
     * 自动生成教师号
     *
     * @param inputString 最后一名学院教师的教师号
     * @return 生成的教师号
     */
    public static String createTno(String inputString) {
        String lastThreeDigits = inputString.substring(inputString.length() - 3); // 获取最后三位
        int number = Integer.parseInt(lastThreeDigits);
        number++;
        if (number > 999) {
            return "error";
        }
        return inputString.substring(0, inputString.length() - 3) + String.format("%03d", number);
    }

    /**
     * 自动生成学号
     *
     * @param inputString 最后一名学生的学号
     * @return 生成的学号
     */
    public static String createSno(String inputString) {
        String lastTwoDigits = inputString.substring(inputString.length() - 2); // 获取最后两位
        int number = Integer.parseInt(lastTwoDigits);
        number++;
        if (number > 99) {
            return "error";
        }
        return inputString.substring(0, inputString.length() - 2) + String.format("%02d", number);
    }

    /**
     * 自动生成班级号
     * @param inputString 该年级最后一个班级的班级号
     * @return 生成的班级号
     */
    public static String createClassNo(String inputString) {
        String lastTwoDigits = inputString.substring(inputString.length() - 2); // 获取最后两位
        int number = Integer.parseInt(lastTwoDigits);
        number++;
        if (number > 99) {
            return "error";
        }
        return inputString.substring(0, inputString.length() - 2) + String.format("%02d", number);
    }
}
