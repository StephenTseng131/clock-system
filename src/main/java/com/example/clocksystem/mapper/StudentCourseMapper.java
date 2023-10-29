package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.Course;
import com.example.clocksystem.entity.StudentCourse;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface StudentCourseMapper {
    // 按sNo查询课程
    @Select("SELECT course.courseNo, courseName,tNo, course.week, course.startTime, course.endTime FROM student_course, course WHERE course.courseNo = student_course.courseNo AND sNo = #{sNo}")
    @Results({@Result(column = "courseNo", property = "courseNo", id = true),
            @Result(column = "tNo", property = "tNo"),
            @Result(column = "tNo", property = "teacher", one = @One(select = "com.example.clocksystem.mapper.TeacherMapper.findByTno", fetchType = FetchType.LAZY)),
            @Result(column = "courseNo", property = "teacherCourse", one = @One(select = "com.example.clocksystem.mapper.TeacherCourseMapper.findByCourseNo", fetchType = FetchType.LAZY))})
    List<Course> findBySno(@Param("sNo") String sNo);

    // 按courseNo查询课程
    @Select("SELECT * FROM student_course WHERE courseNo = #{courseNo}")
    List<StudentCourse> findByCourseNo(@Param("courseNo") int courseNo);

    // 添加学生课程
    @Insert("INSERT INTO student_course (sNo, courseNo) VALUES (#{sNo}, #{courseNo})")
    int addStudentCourse(StudentCourse studentCourse);

    // 删除学生课程
    @Delete("DELETE FROM student_course WHERE sNo = #{sNo} AND courseNo = #{courseNo}")
    int deleteBySnoAndCourseNo(@Param("sNo") String sNo, @Param("courseNo") int courseNo);

    //统计课程的人数
    @Select("SELECT COUNT(*) number FROM student_course WHERE courseNo = #{courseNo}")
    int countCourseStudentNumber(@Param("courseNo") int courseNo);
}
