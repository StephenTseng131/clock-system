package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.StudentActivity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface StudentActivityMapper {
    //查找所有学生活动记录
    @Select("SELECT * FROM student_activity")
    @Results(id = "StudentActivityMapperResultMapA", value = {@Result(column = "activityNo", property = "activityNo"),
            @Result(column = "sNo", property = "sNo"),
            @Result(column = "activityNo", property = "activity", one = @One(select = "com.example.clocksystem.mapper.ActivityMapper.findActivityByNo", fetchType = FetchType.LAZY)),
            @Result(column = "sNo", property = "student", one = @One(select = "com.example.clocksystem.mapper.StudentMapper.findBySno", fetchType = FetchType.LAZY))})
    List<StudentActivity> findStudentActivities();

    //根据活动编号查找学生活动记录
    @Select("SELECT * FROM student_activity WHERE activityNo = #{activityNo}")
    @ResultMap(value = "StudentActivityMapperResultMapA")
    List<StudentActivity> findStudentActivitiesByActivityNo(@Param("activityNo") int activityNo);

    //根据学号查找学生活动记录
    @Select("SELECT * FROM student_activity WHERE sNo = #{sNo}")
    @ResultMap(value = "StudentActivityMapperResultMapA")
    List<StudentActivity> findStudentActivitiesBySno(@Param("sNo") String sNo);

    //根据活动编号和学号查找活动记录
    @Select("SELECT * FROM student_activity WHERE activityNo = #{activityNo} AND sNo = #{sNo}")
    @ResultMap(value = "StudentActivityMapperResultMapA")
    StudentActivity findStudentActivityByActivityNoAndSno(@Param("activityNo") int activityNo, @Param("sNo") String sNo);

    //根据活动编号和签到状态查找活动记录
    @Select("SELECT * FROM student_activity WHERE activityNo = #{activityNo} AND status = #{status}")
    @ResultMap(value = "StudentActivityMapperResultMapA")
    List<StudentActivity> findStudentActivitiesByActivityNoAndStatus(@Param("activityNo") int activityNo, @Param("status") String status);

    //添加一条学生活动记录
    @Insert("INSERT INTO student_activity (activityNo, sNo, status) VALUES (#{activityNo}, #{sNo}, #{status})")
    int addStudentActivity(StudentActivity studentActivity);

    //更新学生活动记录状态
    @Update("UPDATE student_activity SET status = #{studentActivity.status} WHERE activityNo = #{studentActivity.activityNo} AND sNo = #{studentActivity.sNo}")
    int updateStudentActivity(@Param("studentActivity") StudentActivity studentActivity);

    //删除学生活动记录
    @Delete("DELETE FROM student_activity WHERE activityNo = #{activityNo}")
    int deleteStudentActivities(@Param("activityNo") int activityNo);

    //删除学生活动记录
    @Delete("DELETE FROM student_activity WHERE activityNo = #{activityNo} AND sNo = #{sNo}")
    int deleteStudentActivityByActivityNoAndSno(@Param("activityNo") int activityNo, @Param("sNo") String sNo);
}
