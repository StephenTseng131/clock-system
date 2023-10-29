package com.example.clocksystem.mapper;

import org.apache.ibatis.annotations.*;
import com.example.clocksystem.entity.Class;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ClassMapper {
    @Select("SELECT * FROM class")
    @Results({@Result(column = "classNo", property = "classNo", id = true),
            @Result(column = "tNo", property = "tNo"),
            @Result(column = "tNo", property = "teacher", one = @One(select = "com.example.clocksystem.mapper.TeacherMapper.findByTno", fetchType = FetchType.LAZY)),
            @Result(column = "classNo", property = "number", one = @One(select = "com.example.clocksystem.mapper.StudentMapper.countClassStudentNumberByClassNo", fetchType = FetchType.LAZY))})
    List<Class> findAll();

    @Select("SELECT * FROM class WHERE classNo = #{classNo}")
    @Results({@Result(column = "classNo", property = "classNo", id = true),
            @Result(column = "tNo", property = "tNo"),
            @Result(column = "tNo", property = "teacher", one = @One(select = "com.example.clocksystem.mapper.TeacherMapper.findByTno", fetchType = FetchType.LAZY))})
    Class findByClassNo(@Param("classNo") String classNo);

    @Select("SELECT * FROM class WHERE tNo = #{tNo}")
    @Results({@Result(column = "tNo", property = "tNo"),
            @Result(column = "classNo", property = "classNo", id = true),
            @Result(column = "classNo", property = "number", one = @One(select = "com.example.clocksystem.mapper.StudentMapper.countClassStudentNumberByClassNo", fetchType = FetchType.LAZY)),
            @Result(column = "classNo", property = "absence", one = @One(select = "com.example.clocksystem.mapper.ClockRecordMapper.countAbsenceNumber", fetchType = FetchType.LAZY))})
    List<Class> findByTNo(@Param("tNo") String tNo);

    //根据年份最后两位查询该年份的最后一个班级
    @Select("SELECT * FROM class WHERE classNo LIKE CONCAT(#{year}, '%') ORDER BY classNo DESC LIMIT 1")
    Class findLastClassByYear(@Param("year") String year);

    @Insert("INSERT INTO class (classNo, tNo, className) VALUES (#{partment.classNo}, #{partment.tNo}, #{partment.className})")
    int addClass(@Param("partment") Class partment);

    @Delete("DELETE FROM class WHERE classNo = #{classNo}")
    int deleteClass(@Param("classNo") String classNo);

    @Update("UPDATE class SET tNo = #{partment.tNo}, className = #{partment.className} WHERE classNo = #{partment.classNo}")
    int updateClass(@Param("partment") Class partment);
}
