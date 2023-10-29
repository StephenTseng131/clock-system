package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.TeacherActivityApplication;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface TeacherActivityApplicationMapper {
    //查询所有教师审批记录
    @Select("SELECT * FROM teacher_activityapplication")
    @Results(id = "TeacherActivityApplicationMapperResultMapA", value = {@Result(column = "applicationNo", property = "applicationNo"),
            @Result(column = "tNo", property = "tNo"),
            @Result(column = "applicationNo", property = "activityApplication", one = @One(select = "com.example.clocksystem.mapper.ActivityApplicationMapper.findActivityApplicationByApplicationNo", fetchType = FetchType.LAZY)),
            @Result(column = "tNo", property = "teacher", one = @One(select = "com.example.clocksystem.mapper.TeacherMapper.findByTno", fetchType = FetchType.LAZY))})
    List<TeacherActivityApplication> findAllTeacherActivityApplications();

    //根据申请编号查询教师审批记录
    @Select("SELECT * FROM teacher_activityapplication WHERE applicationNo = #{applicationNo}")
    @ResultMap(value = "TeacherActivityApplicationMapperResultMapA")
    TeacherActivityApplication findTeacherActivityApplicationByApplicationNo(@Param("applicationNo") int applicationNo);

    //根据教师号查询教师审批记录
    @Select("SELECT * FROM teacher_activityapplication WHERE tNo = #{tNo}")
    @ResultMap(value = "TeacherActivityApplicationMapperResultMapA")
    List<TeacherActivityApplication> findTeacherActivityApplicationsByTno(@Param("tNo") String tNo);

    //根据申请编号和教师号查询教师审批记录
    @Select("SELECT * FROM teacher_activityapplication WHERE applicationNo = #{applicationNo} AND tNo = #{tNo}")
    @ResultMap(value = "TeacherActivityApplicationMapperResultMapA")
    TeacherActivityApplication findTeacherActivityApplicationByApplicationNoAndTno(@Param("applicationNo") int applicationNo, @Param("tNo") String tNo);

    //根据教师号和审批状态查询教师审核记录
    @Select("SELECT * FROM teacher_societyapplication WHERE tNo = #{tNo} AND status = #{status}")
    @ResultMap(value = "TeacherActivityApplicationMapperResultMapA")
    List<TeacherActivityApplication> findTeacherActivityApplicationsByTnoAndStatus(@Param("tNo") String tNo, @Param("status") int status);

    //添加一条教师审批记录
    @Insert("INSERT INTO teacher_activityapplication (applicationNo, tNo, status) VALUES (#{applicationNo}, #{tNo}, #{status})")
    int addTeacherActivityApplication(TeacherActivityApplication teacherActivityApplication);

    //更新教师审批状态
    @Update("UPDATE teacher_activityapplication SET status = #{teacherActivityApplication.status} WHERE applicationNo = #{teacherActivityApplication.applicationNo} AND tNo = #{teacherActivityApplication.tNo}")
    int updateTeacherActivityApplication(@Param("teacherActivityApplication") TeacherActivityApplication teacherActivityApplication);

    //删除教师审批记录
    @Delete("DELETE FROM teacher_activityapplication WHERE applicationNo = #{applicationNo}")
    int deleteTeacherActivityApplication(@Param("applicationNo") int applicationNo);
}
