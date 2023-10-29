package com.example.clocksystem.utils;

import com.example.clocksystem.entity.Course;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * TimeConvert
 *
 * @author 94548
 * @date 2023/9/10
 */
public class TimeConvert {

    /**
     * 时间类型转换
     *
     * @param time 时间
     * @return Timestamp类型的时间数据
     */
    public static Timestamp timeConvert(Long time) {
        Date date = new Date(time);
        int year = date.getYear() + 1900; // 年份需要加1900
        int month = date.getMonth() + 1; // 月份从0开始，所以需要加1
        int day = date.getDate();
        int hours = date.getHours();
        int minutes = date.getMinutes();
        int seconds = date.getSeconds();
        return Timestamp.valueOf(year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds);
    }

    /**
     * 时间类型转换1
     *
     * @param time 时间
     * @return Timestamp类型的时间数据
     */
    public static Timestamp timeConvert1(String time) {
        String[] split = time.split("T");
        return Timestamp.valueOf(split[0] + " " + split[1] + ":00");
    }

    /**
     * 查找教师号
     * @param startTime 请假起始时间
     * @param endTime 请假截止时间
     * @param courseList 学生课程列表
     * @return 教师号集合
     */
    public static Set<String> findTeacherNo(Timestamp startTime, Timestamp endTime, List<Course> courseList) {
        // 星期集合
        Set<Integer> weeks = new HashSet<>();
        // 教师号集合
        Set<String> teacherSet = new HashSet<>();
        Date startDate = new Date(startTime.getTime());
        Date endDate = new Date(endTime.getTime());
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        // 星期集合
        for (LocalDate date = start.plusDays(1); date.isBefore(end); date = date.plusDays(1)) {
            if (!date.isEqual(start) && !date.isEqual(end)) {
                weeks.add(weekConvert(date.getDayOfWeek()));
            }
        }
        // 教师集合
        for (Course course : courseList) {
            if (weeks.contains(course.getWeek())) {
                teacherSet.add(course.getTNo());
            }

            if (weekConvert(start.getDayOfWeek()) == course.getWeek() || weekConvert(end.getDayOfWeek()) == course.getWeek()) {
                if (course.getStartTime().after(startTime) && course.getStartTime().before(endTime)) {
                    teacherSet.add(course.getTNo());
                }
            }
        }
        return teacherSet;
    }

    /**
     * 通过传递的dayofweek转换为星期
     *
     * @param dayOfWeek 星期信息
     * @return 星期几
     */
    public static int weekConvert(DayOfWeek dayOfWeek) {
        if (dayOfWeek == DayOfWeek.MONDAY) {
            return 1;
        } else if (dayOfWeek == DayOfWeek.TUESDAY) {
            return 2;
        } else if (dayOfWeek == DayOfWeek.WEDNESDAY) {
            return 3;
        } else if (dayOfWeek == DayOfWeek.THURSDAY) {
            return 4;
        } else if (dayOfWeek == DayOfWeek.FRIDAY) {
            return 5;
        } else if (dayOfWeek == DayOfWeek.SATURDAY) {
            return 6;
        } else {
            return 7;
        }
    }

}
