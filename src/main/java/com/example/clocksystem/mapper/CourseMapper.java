package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.Course;
import com.example.clocksystem.entity.TimeFrame;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface CourseMapper {
    //查询所有课程
    @Select("SELECT * FROM course")
    @ResultMap(value = "CourseResultMapA")
    List<Course> findAllCourses();

    // 按courseNo查询课程
    @Select("SELECT * FROM course WHERE courseNo = #{courseNo}")
    @Results(id = "CourseResultMapA", value = {@Result(column = "courseNo", property = "courseNo", id = true),
            @Result(column = "tNo", property = "tNo"),
            @Result(column = "tNo", property = "teacher", one = @One(select = "com.example.clocksystem.mapper.TeacherMapper.findByTno", fetchType = FetchType.LAZY)),
            @Result(column = "courseNo", property = "teacherCourse", one = @One(select = "com.example.clocksystem.mapper.TeacherCourseMapper.findByCourseNo", fetchType = FetchType.LAZY)),
            @Result(column = "courseNo", property = "number", one = @One(select = "com.example.clocksystem.mapper.StudentCourseMapper.countCourseStudentNumber", fetchType = FetchType.LAZY)),
            @Result(column = "courseNo", property = "absence", one = @One(select = "com.example.clocksystem.mapper.ClockRecordMapper.countCourseAbsenceNumber", fetchType = FetchType.LAZY))})
    Course findByCourseNo(@Param("courseNo") int courseNo);

    //按照教师号查询课程
    @Select("SELECT * FROM course WHERE tNo = #{tNo}")
    @Results({@Result(column = "courseNo", property = "courseNo", id = true),
            @Result(column = "tNo", property = "tNo"),
            @Result(column = "courseNo", property = "teacherCourse", one = @One(select = "com.example.clocksystem.mapper.TeacherCourseMapper.findByCourseNo", fetchType = FetchType.LAZY)),
            @Result(column = "number", property = "number", one = @One(select = "com.example.clocksystem.mapper.StudentCourseMapper.countCourseStudentNumber", fetchType = FetchType.LAZY)),
            @Result(column = "absence", property = "absence", one = @One(select = "com.example.clocksystem.mapper.ClockRecordMapper.countCourseAbsenceNumber", fetchType = FetchType.LAZY))})
    List<Course> findCoursesByTno(@Param("tNo") String tNo);

    //根据教师号和课程名称查询课程
    @Select("SELECT * FROM course WHERE courseName = #{courseName} AND tNo = #{tNo}")
    @ResultMap(value = "CourseResultMapA")
    List<Course> findByCourseNameAndTno(@Param("courseName") String courseName, @Param("tNo") String tNo);

    //根据课程名称、教师号、上课时间查询课程
    @Select("SELECT * FROM course WHERE courseName = #{courseName} AND tNo = #{tNo} AND week = #{week} AND startTime = #{startTime}")
    @ResultMap(value = "CourseResultMapA")
    Course findByCourseNameAndTnoAndWeekAndStartTime(@Param("courseName") String courseName, @Param("tNo") String tNo, @Param("week") int week, @Param("startTime") Timestamp startTime);

    // 添加课程
    @Insert("INSERT INTO course (courseName, tNo, week, startTime, endTime) VALUES (#{courseName}, #{tNo}, #{week}, #{startTime}, #{endTime})")
    int addCourse(Course course);

    // 删除课程
    @Delete("DELETE FROM course WHERE courseNo = #{courseNo}")
    int deleteByCourseNo(@Param("courseNo") int courseNo);

    //更新课程
    @Update("UPDATE course SET courseName = #{course.courseName} AND tNo = #{course.tNo} AND week = #{course.week} AND startTime = #{course.startTime} AND endTime = #{course.endTime} WHERE courseNo = #{course.courseNo}")
    int updateCourse(@Param("course") Course course);

    //查找课程时间表
    @Select("SELECT * FROM timeframe")
    List<TimeFrame> findAllTimeFrames();

    //根据节次查找时间
    @Select("SELECT * FROM timeframe WHERE timeNo = #{timeNo}")
    TimeFrame findTimeFrameByTimeNo(@Param("timeNo") int timeNo);
}
