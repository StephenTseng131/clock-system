package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.Attendance;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface AttendanceMapper {
    @Select("SELECT * FROM attendance WHERE courseNo = #{courseNo} ORDER BY courseNo ASC")
    List<Attendance> findByCourseNo(@Param("courseNo") int courseNo);

    @Select("SELECT * FROM attendance WHERE startTime = #{startTime}")
    List<Attendance> findByTime(@Param("startTime") Timestamp startTime);

    @Select("SELECT * FROM attendance WHERE tNo = #{tNo} ORDER BY tNo ASC")
    @Results({@Result(column = "tNo", property = "tNo"),
            @Result(column = "courseNo", property = "courseNo"),
            @Result(column = "courseNo", property = "course", one = @One(select = "com.example.clocksystem.mapper.CourseMapper.findByCourseNo", fetchType = FetchType.LAZY))})
    List<Attendance> findByTNo(@Param("tNo") String tNo);

    @Select("SELECT * FROM attendance WHERE courseNo = #{courseNo} AND tNo = #{tNo}")
    List<Attendance> findByCourseNoAndTno(@Param("courseNo") int courseNo, @Param("tNo") String tNo);

    @Select("SELECT * FROM attendance WHERE courseNo = #{courseNo} AND tNo = #{tNo} AND startTime = #{startTime} ORDER BY courseNo ASC, tNo ASC")
    Attendance findByCourseNoAndTnoAndTime(@Param("courseNo") int courseNo, @Param("tNo") String tNo, @Param("startTime") Timestamp startTime);

    @Select("SELECT * FROM attendance WHERE tNo = #{tNo} AND startTime = #{startTime}")
    List<Attendance> findByTNoAndTime(@Param("tNo") String tNo, @Param("startTime") Timestamp startTime);

    //按教师号查询返回最后一条发起打卡记录
    @Select("SELECT * FROM attendance WHERE tNo = #{tNo} ORDER BY startTime DESC LIMIT 1")
    Attendance findLastAttendanceByTno(@Param("tNo") String tNo);

    //按课程班查询返回最后 一条发起打卡记录
    @Select("SELECT * FROM attendance WHERE courseNo = (SELECT courseNo FROM teacher_course WHERE courseClass = #{courseClass}) ORDER BY startTime DESC LIMIT 1")
    Attendance findLastAttendanceByCourseClass(@Param("courseClass") String courseClass);

    //按照课程号查询返回最后一条发起打卡记录
    @Select("SELECT * FROM attendance WHERE courseNo = #{courseNo} ORDER BY startTime DESC LIMIT 1")
    Attendance findLastAttendanceByCourseNo(@Param("courseNo") int courseNo);

    @Insert("INSERT INTO attendance (courseNo, tNo, startTime, endTime, code) VALUES (#{attendance.courseNo}, #{attendance.tNo}, #{attendance.startTime}, #{attendance.endTime}, #{attendance.code})")
    int addAttendance(@Param("attendance") Attendance attendance);

    @Update("UPDATE attendance SET tNo = #{attendance.tNo},startTime = #{attendance.startTime}, endTime = #{endTime}, code = #{attendance.code} WHERE courseNo = #{attendance.courseNo} AND startTime = #{attendance.startTime}")
    int updateAttendanceByCourseNoAndTime(@Param("attendance") Attendance attendance);

    @Delete("DELETE FROM attendance WHERE courseNo = #{courseNo} AND startTime = #{startTime}")
    int deleteAttendanceByCourseNoAndTime(@Param("courseNo") int courseNo, @Param("startTime") Timestamp startTime);
}
