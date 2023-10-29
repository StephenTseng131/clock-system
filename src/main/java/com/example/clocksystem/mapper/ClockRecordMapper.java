package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.ClockRecord;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ClockRecordMapper {
    // 按clockNo查询考勤记录
    @Select("SELECT * FROM clockrecord WHERE clockNo = #{clockNo}")
    @Results({@Result(column = "clockNo", property = "clockNo", id = true),
            @Result(column = "manage_tNo", property = "manageTNo"),
            @Result(column = "course_tNo", property = "courseTNo"),
            @Result(column = "courseNo", property = "course", one = @One(select = "com.example.clocksystem.mapper.CourseMapper.findByCourseNo", fetchType = FetchType.LAZY)),
            @Result(column = "sNo", property = "student", one = @One(select = "com.example.clocksystem.mapper.StudentMapper.findBySno", fetchType = FetchType.LAZY))})
    ClockRecord findByClockNo(@Param("clockNo") int clockNo);

    // 按sNo查询考勤记录
    @Select("SELECT * FROM clockrecord WHERE sNo = #{sNo} ORDER BY sNo ASC")
    @Results(id = "clockRecordResultMapA", value = {@Result(column = "manage_tNo", property = "manageTNo"),
            @Result(column = "course_tNo", property = "courseTNo"),
            @Result(column = "courseNo", property = "course", one = @One(select = "com.example.clocksystem.mapper.CourseMapper.findByCourseNo", fetchType = FetchType.LAZY)),
            @Result(column = "sNo", property = "student", one = @One(select = "com.example.clocksystem.mapper.StudentMapper.findBySno", fetchType = FetchType.LAZY))})
    List<ClockRecord> findBySno(@Param("sNo") String sNo);

    // 按courseNo查询考勤记录
    @Select("SELECT * FROM clockrecord WHERE courseNo = #{courseNo}")
    List<ClockRecord> findByCourseNo(@Param("courseNo") int courseNo);

    // 按manage_tNo查询考勤记录
    @Select("SELECT * FROM clockrecord WHERE manage_tNo = #{manageTNo}")
    @ResultMap(value = "clockRecordResultMapA")
    List<ClockRecord> findByManageTno(@Param("manageTNo") String manageTNo);

    // 按course_tNo查询考勤记录
    @Select("SELECT * FROM clockrecord WHERE course_tNo = #{courseTNo}")
    @ResultMap(value = "clockRecordResultMapA")
    List<ClockRecord> findByCourseTno(@Param("courseTNo") String courseTNo);

    //按教师号查询考勤记录
    @Select("SELECT * FROM clockrecord WHERE manage_tNo = #{tNo} OR course_tNo = #{tNo}")
    @ResultMap(value = "clockRecordResultMapA")
    List<ClockRecord> findClockRecordsByTno(@Param("tNo") String tNo);

    // 按sNo和time查询考勤记录
    @Select("SELECT * FROM clockrecord WHERE sNo = #{sNo} AND time = #{time}")
    List<ClockRecord> findBySnoAndTime(@Param("sNo") String sNo, @Param("time") Timestamp time);

    // 按time和courseNo查询考勤记录
    @Select("SELECT * FROM clockrecord WHERE time = #{time} AND courseNo = #{courseNo}")
    List<ClockRecord> findByTimeAndCourseNo(@Param("time") Timestamp time, @Param("courseNo") int courseNo);

    // 按time和manage_tNO查询考勤记录
    @Select("SELECT * FROM clockrecord WHERE time = #{time} AND manage_tNo = #{manageTNo}")
    List<ClockRecord> findByTimeAndManageTno(@Param("time") Timestamp time, @Param("manageTNo") String manageTNo);

    // 按sNo和status查询考勤记录
    @Select("SELECT * FROM clockrecord WHERE sNo = #{sNo} AND status = #{status}")
    @ResultMap(value = "clockRecordResultMapA")
    List<ClockRecord> findBySnoAndStatus(@Param("sNo") String sNo, @Param("status") int status);

    // 按sNo、courseNo和status查询考勤记录
    @Select("SELECT * FROM clockrecord WHERE sNo = #{sNo} AND courseNo = #{courseNo} AND status = #{status}")
    List<ClockRecord> findBySnoCourseNoAndStatus(@Param("sNo") String sNo, @Param("courseNo") int courseNo, @Param("status") int status);

    // 按manage_tNo、course和status查询考勤记录
    @Select("SELECT * FROM clockrecord WHERE manage_tNo = #{manageTNo} AND courseNo = #{courseNo} AND status = #{status}")
    List<ClockRecord> findByManageTnoCourseAndStatus(@Param("manageTNo") String manageTNo, @Param("courseNo") int courseNo, @Param("status") int status);

    // 按manage_tNo和status查询考勤记录
    @Select("SELECT * FROM clockrecord WHERE manage_tNo = #{manageTNo} AND status = #{status}")
    List<ClockRecord> findByManageTnoAndStatus(@Param("manageTNo") String manageTNo, @Param("status") int status);

    // 按course_tNo、course和status查询考勤记录
    @Select("SELECT * FROM clockrecord WHERE course_tNo = #{courseTNo} AND courseNo = #{courseNo} AND status = #{status}")
    List<ClockRecord> findByCourseTnoCourseAndStatus(@Param("courseTNo") String courseTNo, @Param("courseNo") int courseNo, @Param("status") int status);

    // 按course_tNo和status查询考勤记录
    @Select("SELECT * FROM clockrecord WHERE course_tNo = #{courseTNo} AND status = #{status}")
    List<ClockRecord> findByCourseTnoAndStatus(@Param("courseTNo") String courseTNo, @Param("status") int status);

    // 按status查询考勤记录
    @Select("SELECT * FROM clockrecord WHERE status = #{status}")
    List<ClockRecord> findByStatus(@Param("status") int status);

    // 添加考勤记录
    @Insert("INSERT INTO clockrecord (sNo, courseNo, manage_tNo, course_tNo, time, status, appealStatus) VALUES (#{sNo}, #{courseNo}, #{manageTNo}, #{courseTNo}, #{time}, #{status}, #{appealStatus})")
    int addClockRecord(ClockRecord clockRecord);

    // 按clockNo删除考勤记录
    @Delete("DELETE FROM clockrecord WHERE clockNo = #{clockNo}")
    int deleteByClockNo(@Param("clockNo") int clockNo);

    // 按clockNo更新考勤记录的status
    @Update("UPDATE clockrecord SET status = #{clockRecord.status} WHERE clockNo = #{clockRecord.clockNo}")
    int updateStatusByClockNo(@Param("clockRecord") ClockRecord clockRecord);

    //按clockNo更新考勤记录的appealStatus
    @Update("UPDATE clockrecord SET appealStatus = #{clockRecord.appealStatus} WHERE clockNo = #{clockRecord.clockNo}")
    int updateAppealStatusByClockNo(@Param("clockRecord") ClockRecord clockRecord);

    //按辅导员管理的行政班级查询考勤记录
    @Select("SELECT * FROM clockrecord WHERE sNo IN (SELECT sNo FROM student WHERE classNo = #{classNo})")
    @Results(id = "clockRecordResultMapB", value = {@Result(column = "classNo", property = "classNo"),
            @Result(column = "sNo", property = "sNo"),
            @Result(column = "clockNo", property = "clockNo", id = true),
            @Result(column = "manage_tNo", property = "manageTNo"),
            @Result(column = "course_tNo", property = "courseTNo"),
            @Result(column = "courseNo", property = "courseNo"),
            @Result(column = "courseNo", property = "course", one = @One(select = "com.example.clocksystem.mapper.CourseMapper.findByCourseNo", fetchType = FetchType.LAZY))})
    List<ClockRecord> findByClassNo(String classNo);

    //按课程班级查询学生的考勤记录
    @Select("SELECT * FROM clockrecord WHERE sNo IN (SELECT sNo FROM student_course WHERE courseNo IN (SELECT courseNo FROM teacher_course WHERE courseClass = #{courseClass}))")
    @ResultMap(value = "clockRecordResultMapB")
    List<ClockRecord> findByCourseClass(String courseClass);

    //按照学号查询返回最后一条考勤记录
    @Select("SELECT * FROM clockrecord WHERE sNo = #{sNo} ORDER BY clockNo DESC LIMIT 1")
    ClockRecord findLastClockRecordBySno(@Param("sNo") String sNo);

    //统计缺勤人数
    @Select("SELECT COUNT(DISTINCT sNo) absence FROM clockrecord WHERE sNo IN (SELECT sNo FROM student WHERE classNo = #{classNo}) AND status = 1")
    int countAbsenceNumber(@Param("classNo") int classNo);

    //统计课程缺勤人数
    @Select("SELECT COUNT(DISTINCT sNo) absence FROM clockrecord WHERE courseNo = #{courseNo} AND status = 1")
    int countCourseAbsenceNumber(@Param("courseNo") int courseNo);

    //统计学生的出勤次数
    @Select("SELECT COUNT(*) attendTime FROM clockrecord WHERE sNo = #{sNo} AND status = 0")
    int countStudentAttendTime(@Param("sNo") String sNo);

    //统计学生的缺勤次数
    @Select("SELECT COUNT(*) absentTime FROM clockrecord WHERE sNo = #{sNo} AND status = 1")
    int countStudentAbsenceTime(@Param("sNo") String sNo);
}
