package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.TeacherLeaveRecord;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface TeacherLeaveRecordMapper {
    // 按leaveNo查询请假审批
    @Select("SELECT * FROM teacher_leaverecord WHERE leaveNo = #{leaveNo}")
    @Results({@Result(column = "leaveNo", property = "leaveNo"),
            @Result(column = "tNo", property = "tNo"),
            @Result(column = "leaveNo", property = "leaveRecord", one = @One(select = "com.example.clocksystem.mapper.LeaveRecordMapper.findByLeaveNo", fetchType = FetchType.LAZY))})
    List<TeacherLeaveRecord> findByLeaveNo(@Param("leaveNo") int leaveNo);

    // 按tNo查询请假审批
    @Select("SELECT * FROM teacher_leaverecord WHERE tNo = #{tNo}")
    List<TeacherLeaveRecord> findByTno(@Param("tNo") String tNo);

    //按教师号查询请假审批详情
    @Select("SELECT * FROM teacher_leaverecord WHERE tNo = #{tNo}")
    @Results({@Result(column = "sNo", property = "sNo"),
            @Result(column = "leaveNo", property = "leaveNo"),
            @Result(column = "tNo", property = "tNo"),
            @Result(column = "leaveNo", property = "leaveRecord", one = @One(select = "com.example.clocksystem.mapper.LeaveRecordMapper.findByLeaveNo", fetchType = FetchType.LAZY))})
    List<TeacherLeaveRecord> findLeaveRecordByTno(@Param("tNo") String tNo);

    // 按leaveNo和tNo查询请假审批
    @Select("SELECT * FROM teacher_leaverecord WHERE leaveNo = #{leaveNo} AND tNo = #{tNo}")
    TeacherLeaveRecord findByLeaveNoAndTno(@Param("leaveNo") int leaveNo, @Param("tNo") String tNo);

    // 按tNo和status查询请假审批
    @Select("SELECT * FROM teacher_leaverecord WHERE tNo = #{tNo} AND status = #{status}")
    List<TeacherLeaveRecord> findByTnoAndStatus(@Param("tNo") String tNo, @Param("status") int status);

    // 按leaveNo和status查询请假审批
    @Select("SELECT * FROM teacher_leaverecord WHERE leaveNo = #{leaveNo} AND status = #{status}")
    TeacherLeaveRecord findByLeaveNoAndStatus(@Param("leaveNo") int leaveNo, @Param("status") int status);

    // 添加请假审批
    @Insert("INSERT INTO teacher_leaverecord (leaveNo, tNo, status) VALUES (#{leaveNo}, #{tNo}, #{status})")
    int addTeacherLeave(TeacherLeaveRecord teacherLeaveRecord);

    // 删除请假审批
    @Delete("DELETE FROM teacher_leaverecord WHERE leaveNo = #{leaveNo}")
    int deleteByLeaveNo(@Param("leaveNo") int leaveNo);

    // 更新请假审批
    @Update("UPDATE teacher_leaverecord SET status = #{teacherLeaveRecord.status} WHERE leaveNo = #{teacherLeaveRecord.leaveNo} AND tNo = #{teacherLeaveRecord.tNo}")
    int updateLeaveStatus(@Param("teacherLeaveRecord") TeacherLeaveRecord teacherLeaveRecord);
}
