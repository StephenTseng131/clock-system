package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.TeacherSocietyApplication;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface TeacherSocietyApplicationMapper {
    //查找所有教师审核记录
    @Select("SELECT * FROM teacher_societyapplication")
    @Results(id = "TeacherSocietyApplicationMapperResultMapA", value = {@Result(column = "applicationNo", property = "applicationNo"),
            @Result(column = "tNo", property = "tNo"),
            @Result(column = "applicationNo", property = "societyApplication", one = @One(select = "com.example.clocksystem.mapper.SocietyApplicationMapper.findSocietyApplicationByApplicationNo", fetchType = FetchType.LAZY)),
            @Result(column = "tNo", property = "teacher", one = @One(select = "com.example.clocksystem.mapper.TeacherMapper.findByTno", fetchType = FetchType.LAZY))})
    List<TeacherSocietyApplication> findAllTeacherSocietyApplications();

    //根据申请编号查找
    @Select("SELECT * FROM teacher_societyapplication WHERE applicationNo = #{applicationNo}")
    @ResultMap(value = "TeacherSocietyApplicationMapperResultMapA")
    List<TeacherSocietyApplication> findTeacherSocietyApplicationsByApplicationNo(@Param("applicationNo") int applicationNo);

    //根据教师号查找教师审核记录
    @Select("SELECT * FROM teacher_societyapplication WHERE tNo = #{tNo}")
    @ResultMap(value = "TeacherSocietyApplicationMapperResultMapA")
    List<TeacherSocietyApplication> findTeacherSocietyApplicationsByTno(String tNo);

    //根据申请编号和教师号查询教师审核记录
    @Select("SELECT * FROM teacher_societyapplication WHERE applicationNo = #{applicationNo} AND tNo = #{tNo}")
    @ResultMap(value = "TeacherSocietyApplicationMapperResultMapA")
    TeacherSocietyApplication findTeacherSocietyApplicationsByApplicationNoAndTno(@Param("applicationNo") int applicationNo, @Param("tNo") String tNo);

    //根据教师号和审批状态查询教师审核记录
    @Select("SELECT * FROM teacher_societyapplication WHERE tNo = #{tNo} AND status = #{status}")
    @ResultMap(value = "TeacherSocietyApplicationMapperResultMapA")
    List<TeacherSocietyApplication> findfindTeacherSocietyApplicationByTnoAndStatus(@Param("tNo") String tNo, @Param("status") int status);

    //添加一条教师审核记录
    @Insert("INSERT INTO teacher_societyapplication (applicationNo, tNo, status) VALUES (#{teacherSocietyApplication.applicationNo}, #{teacherSocietyApplication.tNo}, #{teacherSocietyApplication.status})")
    int addTeacherSocietyApplication(@Param("teacherSocietyApplication") TeacherSocietyApplication teacherSocietyApplication);

    //更新教师审核状态
    @Update("UPDATE teacher_societyapplication SET status = #{teacherSocietyApplication.status} WHERE applicationNo = #{teacherSocietyApplication.applicationNo} AND tNo = #{teacherSocietyApplication.tNo}")
    int updateStatus(@Param("teacherSocietyApplication") TeacherSocietyApplication teacherSocietyApplication);

    //删除教师审核记录
    @Delete("DELETE FROM teacher_societyapplication WHERE applicationNo = #{applicationNo}")
    int deleteTeacherSocietyApplication(@Param("applicationNo") int applicationNo);
}
