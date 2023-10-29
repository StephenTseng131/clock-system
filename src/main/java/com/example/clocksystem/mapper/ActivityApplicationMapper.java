package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.ActivityApplication;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ActivityApplicationMapper {
    //查询所有活动申请记录
    @Select("SELECT * FROM activityapplication")
    @Results(id = "ActivityApplicationMapperResultMapA", value = {@Result(column = "applicationNo", property = "applicationNo", id = true),
            @Result(column = "societyId", property = "societyId"),
            @Result(column = "societyId", property = "society", one = @One(select = "com.example.clocksystem.mapper.SocietyMapper.findSocietyBySocietyId", fetchType = FetchType.LAZY))})
    List<ActivityApplication> findAllActivityApplications();

    //根据申请编号查询申请记录
    @Select("SELECT * FROM activityapplication WHERE applicationNo = #{applicationNo}")
    @ResultMap(value = "ActivityApplicationMapperResultMapA")
    ActivityApplication findActivityApplicationByApplicationNo(@Param("applicationNo") int applicationNo);

    //根据活动名称查询申请记录
    @Select("SELECT * FROM activityapplication WHERE activityName = #{activityName}")
    @ResultMap(value = "ActivityApplicationMapperResultMapA")
    List<ActivityApplication> findActivityApplicationsByName(String activityName);

    //根据活动类型查找申请记录
    @Select("SELECT * FROM activityapplication WHERE type = #{type}")
    @ResultMap(value = "ActivityApplicationMapperResultMapA")
    List<ActivityApplication> findActivityApplicationsByType(@Param("type") int type);

    //根据学分查找申请记录
    @Select("SELECT * FROM activityapplication WHERE credit = #{credit}")
    @ResultMap(value = "ActivityApplicationMapperResultMapA")
    List<ActivityApplication> findActivityApplicationsByCredit(@Param("credit") int credit);

    //根据教务处审核状态查找申请记录
    @Select("SELECT * FROM activityapplication WHERE officestatus = #{officeStatus}")
    @ResultMap(value = "ActivityApplicationMapperResultMapA")
    List<ActivityApplication> findActivityApplicationsByOfficeStatus(@Param("officeStatus") int officeStatus);

    //根据申请记录审核状态查找申请记录
    @Select("SELECT * FROM activityapplication WHERE status = #{status}")
    @ResultMap(value = "ActivityApplicationMapperResultMapA")
    List<ActivityApplication> findActivityApplicationsByeStatus(@Param("status") int status);

    //根据社团ID查找活动申请记录
    @Select("SELECT * FROM activityapplication WHERE societyId = #{societyId}")
    @ResultMap(value = "ActivityApplicationMapperResultMapA")
    List<ActivityApplication> findActivityApplicationsBySocietyId(@Param("societyId") String societyId);

    //根据社团账号查找最后一条活动申请记录
    @Select("SELECT * FROM activityapplication WHERE societyId = #{societyId} ORDER BY applicationNo DESC LIMIT 1")
    @ResultMap(value = "ActivityApplicationMapperResultMapA")
    ActivityApplication findLastActivityApplicationBySocietyId(@Param("societyId") String societyId);

    //教务处查找教师审核通过的活动申请记录
    @Select("SELECT * FROM activityapplication WHERE applicationNo NOT IN (SELECT applicationNo FROM teacher_activityapplication WHERE status = 0 OR status = 2)")
    @ResultMap(value = "ActivityApplicationMapperResultMapA")
    List<ActivityApplication> findActivityApplicationsToOffice();

    //添加一条申请记录
    @Insert("INSERT INTO activityapplication (activityName, introduce, type, credit, startTime, endTime, officeStatus, status, societyId) VALUES (#{activityName}, #{introduce}, #{type}, #{credit}, #{startTime}, #{endTime}, #{officeStatus}, #{status}, #{societyId})")
    int addActivityApplication(ActivityApplication activityApplication);

    //更改教务处审批状态
    @Update("UPDATE activityapplication SET officeStatus = #{activityApplication.officeStatus} WHERE applicationNo = #{activityApplication.applicationNo}")
    int updateOfficeStatus(@Param("activityApplication") ActivityApplication activityApplication);

    //更改活动申请状态
    @Update("UPDATE activityapplication SET officestatus = #{activityApplication.officeStatus}, status = #{activityApplication.status} WHERE applicationNo = #{activityApplication.applicationNo}")
    int updateStatus(@Param("activityApplication") ActivityApplication activityApplication);

    //删除活动申请记录
    @Delete("DELETE FROM activityapplication WHERE applicationNo = #{applicationNo}")
    int deleteActivityApplication(int applicationNo);
}
