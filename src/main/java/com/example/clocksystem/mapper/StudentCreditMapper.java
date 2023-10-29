package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.StudentCredit;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface StudentCreditMapper {
    //查找所有学生学分记录
    @Select("SELECT * FROM student_credit")
    @Results({@Result(column = "sNo", property = "sNo"),
            @Result(column = "sNo", property = "student", one = @One(select = "com.example.clocksystem.mapper.StudentMapper.findBySno", fetchType = FetchType.LAZY))})
    List<StudentCredit> findAllStudentCredits();

    //根据学号查找学生学分记录
    @Select("SELECT * FROM student_credit WHERE sNo = #{sNo}")
    StudentCredit findStudentCreditBySno(@Param("sNo") String sNo);

    //添加学生学分记录
    @Insert("INSERT INTO student_credit (sNo) VALUES (#{sNo})")
    int addStudentCredit(StudentCredit studentCredit);

    //更新德育学分
    @Update("UPDATE student_credit SET moral = moral + #{moral} WHERE sNo = #{sNo}")
    int updateMoral(@Param("moral") int moral, @Param("sNo") String sNo);

    //更新智育学分
    @Update("UPDATE student_credit SET intellectual = intellectual + #{intellectual} WHERE sNo = #{sNo}")
    int updateIntellectual(@Param("intellectual") int intellectual, @Param("sNo") String sNo);

    //更新体育学分
    @Update("UPDATE student_credit SET physical = physical + #{physical} WHERE sNo = #{sNo}")
    int updatePhysical(@Param("physical") int physical, @Param("sNo") String sNo);

    //更新美育学分
    @Update("UPDATE student_credit SET aesthetic = aesthetic + #{aesthetic} WHERE sNo = #{sNo}")
    int updateAesthetic(@Param("aesthetic") int aesthetic, @Param("sNo") String sNo);

    //更新劳育学分
    @Update("UPDATE student_credit SET labor = labor + #{labor} WHERE sNo = #{sNo}")
    int updateLabor(@Param("labor") int labor, @Param("sNo") String sNo);

    //更新学分总分
    @Update("UPDATE student_credit SET total = moral + intellectual + physical + aesthetic + labor WHERE sNo = #{sNo}")
    int updateTotal(@Param("sNo") String sNo);

    //删除学生学分记录
    @Delete("DELETE FROM student_credit WHERE sNo = #{sNo}")
    int deleteStudentCredit(@Param("sNo") String sNo);
}
