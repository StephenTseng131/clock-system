package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.TeacherStuAppeal;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface TeacherStuAppealMapper {
    // 按appealNo查询申诉审批
    @Select("SELECT * FROM teacher_stuappeal WHERE appealNo = #{appealNo}")
    List<TeacherStuAppeal> findByAppealNo(@Param("appealNo") int appealNo);

    // 按tNo查询申诉审批
    @Select("SELECT * FROM teacher_stuappeal WHERE tNo = #{tNo}")
    List<TeacherStuAppeal> findByTno(@Param("tNo") String tNo);

    //按教师号查询申诉详情
    @Select("SELECT * FROM teacher_stuappeal WHERE tNo = #{tNo}")
    @Results({@Result(column = "appealNo", property = "appealNo"),
            @Result(column = "tNo", property = "tNo"),
            @Result(column = "appealNo", property = "stuAppeal", one = @One(select = "com.example.clocksystem.mapper.StuAppealMapper.findByAppealNo", fetchType = FetchType.LAZY))})
    List<TeacherStuAppeal> findStuAppealByTno(@Param("tNo") String tNo);

    // 按tNo和appealNo查询申诉审批
    @Select("SELECT * FROM teacher_stuappeal WHERE tNo = #{tNo} AND appealNo = #{appealNo}")
    TeacherStuAppeal findByTnoAndAppealNo(@Param("tNo") String tNo, @Param("appealNo") int appealNo);

    // 按status查询申诉审批
    @Select("SELECT * FROM teacher_stuappeal WHERE status = #{status}")
    List<TeacherStuAppeal> findByStatus(@Param("status") int status);

    // 按tNo和status查询申诉审批
    @Select("SELECT * FROM teacher_stuappeal WHERE tNo = #{tNo} AND status = #{status}")
    List<TeacherStuAppeal> findByTnoAndStatus(@Param("tNo") String tNo, @Param("status") int status);

    // 添加申诉审批
    @Insert("INSERT INTO teacher_stuappeal (appealNo, tNo, status) VALUES (#{appealNo}, #{tNo}, #{status})")
    int addTeacherStuAppeal(TeacherStuAppeal teacherStuAppeal);

    // 更新申诉审批
    @Update("UPDATE teacher_stuappeal SET status = #{teacherStuAppeal.status} WHERE appealNo = #{teacherStuAppeal.appealNo} AND tNo = #{teacherStuAppeal.tNo}")
    int updateAppealStatus(@Param("teacherStuAppeal") TeacherStuAppeal teacherStuAppeal);
}
