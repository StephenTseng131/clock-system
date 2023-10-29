package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.Course;
import com.example.clocksystem.entity.TeacherCourse;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface TeacherCourseMapper {
    //查找所有教师课程班记录
    @Select("SELECT * FROM teacher_course")
    @Results({@Result(column = "tNo", property = "tNo"),
            @Result(column = "courseNo", property = "courseNo", id = true),
            @Result(column = "courseNo", property = "teacherCourse", one = @One(select = "com.example.clocksystem.mapper.TeacherCourseMapper.findByCourseNo", fetchType = FetchType.LAZY))})
    List<TeacherCourse> findAllTeacherCourses();

    // 按tNo和courseNo查询课程
    @Select("SELECT * FROM teacher_course WHERE tNo = #{tNo} AND courseNo = #{courseNo}")
    @Results({@Result(column = "courseNo", property = "courseNo"),
            @Result(column = "tNo", property = "tNo")})
    TeacherCourse findByTnoAndCourseNo(@Param("tNo") String tNo, @Param("courseNo") int courseNo);

    // 按tNo查询课程班级
    @Select("SELECT * FROM teacher_course WHERE tNo = #{tNo}")
    List<TeacherCourse> findCourseClassByTno(@Param("tNo") String tNo);

    //按照教师号查询课程
    @Select("SELECT * FROM course WHERE courseNo IN (SELECT courseNo FROM teacher_course WHERE tNo = #{tNo})")
    @Results({@Result(column = "tNo", property = "tNo"),
            @Result(column = "courseNo", property = "courseNo", id = true),
            @Result(column = "courseNo", property = "teacherCourse", one = @One(select = "com.example.clocksystem.mapper.TeacherCourseMapper.findByCourseNo", fetchType = FetchType.LAZY))})
    List<Course> findTeacherCouresByTno(@Param("tNo") String tNo);

    // 按courseNo查询课程
    @Select("SELECT * FROM teacher_course WHERE courseNo = #{courseNo}")
    @Results({@Result(column = "courseNo", property = "courseNo"),
            @Result(column = "tNo", property = "tNo")})
    TeacherCourse findByCourseNo(@Param("courseNo") int courseNo);

    //按照课程班级码courseClass查询教师课程
    @Select("SELECT * FROM teacher_course WHERE courseClass = #{courseClass}")
    @Results({@Result(column = "tNo", property = "tNo"),
            @Result(column = "courseNo", property = "courseNo"),
            @Result(column = "courseClass", property = "courseClass", id = true),
            @Result(column = "courseNo", property = "course", one = @One(select = "com.example.clocksystem.mapper.CourseMapper.findByCourseNo", fetchType = FetchType.LAZY))})
    TeacherCourse findByCourseClass(String courseClass);

    // 添加课程
    @Insert("INSERT INTO teacher_course (tNo, courseNo, courseClass) VALUES (#{tNo}, #{courseNo}, #{courseClass})")
    int addTeacherCourse(TeacherCourse teacherCourse);

    // 删除课程
    @Delete("DELETE FROM teacher_course WHERE courseClass = #{courseClass}")
    int deleteByTnoAndCourseNo(String courseClass);

    // 更新课程
    @Update("UPDATE teacher_course SET courseNo = #{teacherCourse.courseNo} WHERE courseClass = #{teacherCourse.courseClass} ")
    int updateTeacherCourse(@Param("teacherCourse") TeacherCourse teacherCourse);
}
