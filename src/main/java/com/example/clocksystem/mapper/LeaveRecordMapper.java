package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.LeaveRecord;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface LeaveRecordMapper {
    // 按leaveNo查询请假记录
    @Select("SELECT * FROM leaverecord WHERE leaveNo = #{leaveNo}")
    @Results(id = "leaveRecordResultMapA", value = {@Result(column = "sNo", property = "sNo"),
            @Result(column = "sNo", property = "student", one = @One(select = "com.example.clocksystem.mapper.StudentMapper.findBySno", fetchType = FetchType.LAZY))})
    LeaveRecord findByLeaveNo(@Param("leaveNo") int leaveNo);

    // 按sNo查询请假记录
    @Select("SELECT * FROM leaverecord WHERE sNo = #{sNo}")
    @ResultMap(value = "leaveRecordResultMapA")
    List<LeaveRecord> findBySno(@Param("sNo") String sNo);

    // 按sNo和startTime查询请假记录
    @Select("SELECT * FROM leaverecord WHERE sNo = #{sNo} AND startTime = #{startTime}")
    @ResultMap(value = "leaveRecordResultMapA")
    LeaveRecord findBySnoAndTime(@Param("sNo") String sNo, @Param("startTime") Timestamp startTime);

    // 按status查询请假记录
    @Select("SELECT * FROM leaverecord WHERE status = #{status}")
    List<LeaveRecord> findByStatus(@Param("status") int status);

    // 按sNo和status查询请假记录
    @Select("SELECT * FROM leaverecord WHERE sNo = #{sNo} AND status = #{status}")
    List<LeaveRecord> findBySnoAndStatus(@Param("sNo") String sNo, @Param("status") int status);

    // 按sNo、startTime和status查询请假记录
    @Select("SELECT * FROM leaverecord WHERE sNo = #{sNo} AND startTime = #{startTime} AND status = #{status}")
    List<LeaveRecord> findBySnoTimeAndStatus(@Param("sNo") String sNo, @Param("startTime") Timestamp startTime, @Param("status") int status);

    // 按startTime和status查询请假记录
    @Select("SELECT * FROM leaverecord WHERE startTime = #{startTime} AND status = #{status}")
    List<LeaveRecord> findByTimeAndStatus(@Param("startTime") Timestamp startTime, @Param("status") int status);

    // 按sNo查询请假审批通过的请假记录
    @Select("SELECT * FROM leaverecord WHERE sNo = #{sNo} AND status = 1")
    @ResultMap(value = "leaveRecordResultMapA")
    List<LeaveRecord> findApprovedLeaveRecordsBySno(@Param("sNo") String sNo);

    // 添加请假记录
    @Insert("INSERT INTO leaverecord (sNo, startTime, endTime, status, reason) VALUES (#{sNo}, #{startTime}, #{endTime}, #{status}, #{reason})")
    int addLeaveRecord(LeaveRecord leaveRecord);

    // 删除请假记录
    @Delete("DELETE FROM leaverecord WHERE leaveNo = #{leaveNo}")
    int deleteByLeaveNo(@Param("leaveNo") int leaveNo);

    // 更新请假记录
    @Update("UPDATE leaverecord SET status = #{leaveRecord.status} WHERE leaveNo = #{leaveRecord.leaveNo}")
    int updateStatus(@Param("leaveRecord") LeaveRecord leaveRecord);
}
