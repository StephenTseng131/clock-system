package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.StuAppeal;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface StuAppealMapper {
    // 按appealNo查询申诉记录
    @Select("SELECT * FROM stuappeal WHERE appealNo = #{appealNo}")
    @Results({@Result(column = "appealNo", property = "appealNo"),
            @Result(column = "sNo", property = "sNo"),
            @Result(column = "clockNo", property = "clockNo"),
            @Result(column = "sNo", property = "student", one = @One(select = "com.example.clocksystem.mapper.StudentMapper.findBySno", fetchType = FetchType.LAZY)),
            @Result(column = "clockNo", property = "clockRecord", one = @One(select = "com.example.clocksystem.mapper.ClockRecordMapper.findByClockNo", fetchType = FetchType.LAZY))})
    StuAppeal findByAppealNo(@Param("appealNo") int appealNo);

    // 按sNo查询申诉记录
    @Select("SELECT * FROM stuappeal WHERE sNo = #{sNo}")
    List<StuAppeal> findBySno(@Param("sNo") String sNo);

    // 按sNo和status查询申诉记录
    @Select("SELECT * FROM stuappeal WHERE sNo = #{sNo} AND status = #{status}")
    List<StuAppeal> findBySnoAndStatus(@Param("sNo") String sNo, @Param("status") int status);

    // 按status查询申诉记录
    @Select("SELECT * FROM stuappeal WHERE status = #{status}")
    List<StuAppeal> findByStatus(@Param("status") int status);

    //根据sNo和clockNo查询申述记录
    @Select("SELECT * FROM stuappeal WHERE sNo = #{sNo} AND clockNo = #{clockNo}")
    @Results({@Result(column = "appealNo", property = "appealNo"),
            @Result(column = "sNo", property = "sNo"),
            @Result(column = "sNo", property = "student", one = @One(select = "com.example.clocksystem.mapper.StudentMapper.findBySno", fetchType = FetchType.LAZY)),
            @Result(column = "clockNo", property = "clockRecord", one = @One(select = "com.example.clocksystem.mapper.ClockRecordMapper.findByClockNo", fetchType = FetchType.LAZY))})
    StuAppeal findBySnoAndClockNo(@Param("sNo") String sNo, @Param("clockNo") int clockNo);

    // 添加申诉记录
    @Insert("INSERT INTO stuappeal (sNo, status, reason, clockNo) VALUES (#{sNo}, #{status}, #{reason}, #{clockNo})")
    int addStuAppeal(StuAppeal stuAppeal);

    // 按appealNo删除申诉记录
    @Delete("DELETE FROM stuappeal WHERE appealNo = #{appealNo}")
    int deleteByAppealNo(@Param("appealNo") int appealNo);

    // 按appealNo更新申诉记录
    @Update("UPDATE stuappeal SET status = #{stuAppeal.status} WHERE appealNo = #{stuAppeal.appealNo}")
    int updateStatus(@Param("stuAppeal") StuAppeal stuAppeal);
}
