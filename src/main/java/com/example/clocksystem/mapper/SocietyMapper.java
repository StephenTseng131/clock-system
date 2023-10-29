package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.Society;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface SocietyMapper {
    //查找所有社团
    @Select("SELECT * FROM society")
    @Results(id = "SocietyMapperResultMapA", value = {@Result(column = "societyId", property = "societyId", id = true),
            @Result(column = "sNo", property = "sNo"),
            @Result(column = "tNo", property = "tNo"),
            @Result(column = "sNo", property = "student", one = @One(select = "com.example.clocksystem.mapper.StudentMapper.findBySno", fetchType = FetchType.LAZY)),
            @Result(column = "tNo", property = "teacher", one = @One(select = "com.example.clocksystem.mapper.TeacherMapper.findByTno", fetchType = FetchType.LAZY))})
    List<Society> findAllSocieties();

    //根据社团ID查找社团记录
    @Select("SELECT * FROM society WHERE societyId = #{societyId}")
    @ResultMap(value = "SocietyMapperResultMapA")
    Society findSocietyBySocietyId(@Param("societyId") String societyId);

    //根据社团名称查找社团记录
    @Select("SELECT * FROM society WHERE societyName = #{societyName}")
    @ResultMap(value = "SocietyMapperResultMapA")
    List<Society> findSocietiesBySocietyName(@Param("societyName") String societyName);

    //根据创始人学号查找社团记录
    @Select("SELECT * FROM society WHERE sNo = #{sNo}")
    @ResultMap(value = "SocietyMapperResultMapA")
    List<Society> findSocietiesBySno(@Param("sNo") String sNo);

    //根据指导老师教师号查找社团记录
    @Select("SELECT * FROM society WHERE tNo = #{tNo}")
    @ResultMap(value = "SocietyMapperResultMapA")
    List<Society> findSocietiesByTno(@Param("tNo") String tNo);

    //增加一条社团记录
    @Insert("INSERT INTO society (societyId, password, societyName, picture, introduce, sNo, tNo) VALUES (#{society.societyId}, #{society.password}, #{society.societyName}, #{society.picture}, #{society.introduce}, #{society.sNo}, #{society.tNo})")
    int addSociety(@Param("society") Society society);

    //更新社团账号的密码
    @Update("UPDATE society SET password = #{society.password} WHERE societyId = #{society.societyId}")
    int updateSocietyPassword(@Param("society") Society society);

    //更新社团信息
    @Update("UPDATE society SET societyName = #{society.societyName}, picture = #{society.picture}, introduce = #{society.introduce}, tNo = #{society.tNo} WHERE societyId = #{society.societyId}")
    int updateSocietyInfo(@Param("society") Society society);

    //删除一条社团记录
    @Delete("DELETE FROM society WHERE societyId = #{societyId}")
    int deleteSociety(@Param("societyId") String societyId);
}
