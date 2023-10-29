package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.SocietyApplication;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface SocietyApplicationMapper {
    //查找所有申请记录
    @Select("SELECT * FROM societyapplication")
    @Results(id = "SocietyApplicationMapperResultMapA", value = {@Result(column = "applicationNo", property = "applicationNo", id = true),
            @Result(column = "societyId", property = "societyId"),
            @Result(column = "sNo", property = "sNo"),
            @Result(column = "tNo", property = "tNo"),
            @Result(column = "societyId", property = "society", one = @One(select = "com.example.clocksystem.mapper.SocietyMapper.findSocietyBySocietyId", fetchType = FetchType.LAZY)),
            @Result(column = "sNo", property = "student", one = @One(select = "com.example.clocksystem.mapper.StudentMapper.findBySno", fetchType = FetchType.LAZY)),
            @Result(column = "tNo", property = "teacher", one = @One(select = "com.example.clocksystem.mapper.TeacherMapper.findByTno", fetchType = FetchType.LAZY))})
    List<SocietyApplication> findAllSocietiesApplications();

    //按申请编号查询申请记录
    @Select("SELECT * FROM societyapplication WHERE applicationNo = #{applicationNo}")
    @ResultMap(value = "SocietyApplicationMapperResultMapA")
    SocietyApplication findSocietyApplicationByApplicationNo(@Param("applicationNo") int applicationNo);

    //按社团名称查询申请记录
    @Select("SELECT * FROM societyapplication WHERE societyName = #{societyName}")
    @ResultMap(value = "SocietyApplicationMapperResultMapA")
    List<SocietyApplication> findSocietyApplicationsBySocietyName(@Param("societyName") String societyName);

    //根据教务处审核状态查找记录
    @Select("SELECT * FROM societyapplication WHERE officestatus = #{officeStatus}")
    @ResultMap(value = "SocietyApplicationMapperResultMapA")
    List<SocietyApplication> findSocietyApplicationsByOfficeStatus(@Param("officeStatus") int officeStatus);

    //根据申请记录审核状态查找申请记录
    @Select("SELECT * FROM societyapplication WHERE status = #{status}")
    @ResultMap(value = "SocietyApplicationMapperResultMapA")
    List<SocietyApplication> findSocietyApplicationsByStatus(@Param("status") int status);

    //根据社团ID查找申请记录
    @Select("SELECT * FROM societyapplication WHERE societyId = #{societyId}")
    @ResultMap(value = "SocietyApplicationMapperResultMapA")
    List<SocietyApplication> findSocietyApplicationsBySocietyId(@Param("societyId") String societyId);

    //按申请人学号查询记录
    @Select("SELECT * FROM societyapplication WHERE sNo = #{sNo} AND type = 0")
    @ResultMap(value = "SocietyApplicationMapperResultMapA")
    List<SocietyApplication> findSocietyApplicationsBySno(@Param("sNo") String sNo);

    //按指导老师教师号查询记录
    @Select("SELECT * FROM societyapplication WHERE tNo = #{tNo}")
    @ResultMap(value = "SocietyApplicationMapperResultMapA")
    List<SocietyApplication> findSocietyApplicationsByTno(@Param("tNo") String tNo);

    //按申请人学号查找最后一条申请记录
    @Select("SELECT * FROM societyapplication WHERE sNo = #{sNo} ORDER BY applicationNo DESC LIMIT 1")
    @ResultMap(value = "SocietyApplicationMapperResultMapA")
    SocietyApplication findLastSocietyApplicationBySno(@Param("sNo") String sNo);

    //根据社团ID查找最后一条申请记录
    @Select("SELECT * FROM societyapplication WHERE societyId = #{societyId} ORDER BY applicationNo DESC LIMIT 1")
    @ResultMap(value = "SocietyApplicationMapperResultMapA")
    SocietyApplication findLastSocietyApplicationBySocietyId(@Param("societyId") String societyId);

    //给教务处显示教师审核通过的社团审批记录
    @Select("SELECT * FROM societyapplication WHERE applicationNo NOT IN (SELECT applicationNo FROM teacher_societyapplication WHERE status = 2 OR status = 0)")
    @ResultMap(value = "SocietyApplicationMapperResultMapA")
    List<SocietyApplication> findSocietyApplicationsToOffice();

    //添加一条申请记录
    @Insert("INSERT INTO societyapplication (societyName,type, picture, introduce, reason, officestatus, status, societyId, sNo, tNo) VALUES (#{societyApplication.societyName}, #{societyApplication.type}, #{societyApplication.picture}, #{societyApplication.introduce}, #{societyApplication.reason}, #{societyApplication.officeStatus}, #{societyApplication.status}, #{societyApplication.societyId}, #{societyApplication.sNo}, #{societyApplication.tNo})")
    int addSocietyApplication(@Param("societyApplication") SocietyApplication societyApplication);

    //更新教务处审核状态
    @Update("UPDATE societyApplication SET officeStatus = #{societyApplication.officeStatus} WHERE applicationNo = #{societyApplication.applicationNo}")
    int updateOfficeApplicationStatus(@Param("societyApplication") SocietyApplication societyApplication);

    //更新申请审核状态
    @Update("UPDATE societyapplication SET officestatus = #{societyApplication.officeStatus}, status = #{societyApplication.status} WHERE applicationNo = #{societyApplication.applicationNo}")
    int updateStatus(@Param("societyApplication") SocietyApplication societyApplication);

    //审核通过后记录社团账号
    @Update("UPDATE societyapplication SET societyId = #{societyApplication.societyId} WHERE applicationNo = #{societyApplication.applicationNo}")
    int updateSetSocietyId(@Param("societyApplication") SocietyApplication societyApplication);

    //删除申请记录
    @Delete("DELETE FROM societyapplication WHERE applicationNo = #{applicationNo}")
    int deleteSocietyApplication(@Param("applicationNo") int applicationNo);
}
