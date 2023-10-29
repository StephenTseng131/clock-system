package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.ClockRecord;
import com.example.clocksystem.entity.Student;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;


import java.util.List;

@Mapper
public interface StudentMapper {
    // 按sNo查询学生
    @Select("SELECT * FROM student WHERE sNo = #{sNo}")
    @Results({@Result(column = "sNo", property = "sNo", id = true),
            @Result(column = "classNo", property = "classNo"),
            @Result(column = "classNo", property = "stuClass", one = @One(select = "com.example.clocksystem.mapper.ClassMapper.findByClassNo", fetchType = FetchType.LAZY)),
            @Result(column = "sNo", property = "stuCourseList", many = @Many(select = "com.example.clocksystem.mapper.StudentCourseMapper.findBySno", fetchType = FetchType.LAZY)),
            @Result(column = "sNo", property = "stuLeaveRecordList", many = @Many(select = "com.example.clocksystem.mapper.LeaveRecordMapper.findBySno", fetchType = FetchType.LAZY)),
            @Result(column = "sNo", property = "stuClockRecordList", many = @Many(select = "com.example.clocksystem.mapper.ClockRecordMapper.findBySno", fetchType = FetchType.LAZY)),
            @Result(column = "sNo", property = "stuAppealList", many = @Many(select = "com.example.clocksystem.mapper.StuAppealMapper.findBySno", fetchType = FetchType.LAZY)),
            @Result(column = "sNo", property = "attendTime", one = @One(select = "com.example.clocksystem.mapper.ClockRecordMapper.countStudentAttendTime", fetchType = FetchType.LAZY)),
            @Result(column = "sNo", property = "absentTime", one = @One(select = "com.example.clocksystem.mapper.ClockRecordMapper.countStudentAbsenceTime", fetchType = FetchType.LAZY))})
    Student findBySno(@Param("sNo") String sNo);

    // 按classNo查询学生及其考勤记录
    @Select("SELECT * FROM student WHERE classNo = #{classNo}")
    @Results({@Result(column = "sNo", property = "sNo", id = true),
            @Result(column = "sNo", property = "stuClockRecordList", many = @Many(select = "com.example.clocksystem.mapper.ClockRecordMapper.findBySno", fetchType = FetchType.LAZY)),
            @Result(column = "sNo", property = "attendTime", one = @One(select = "com.example.clocksystem.mapper.ClockRecordMapper.countStudentAttendTime", fetchType = FetchType.LAZY)),
            @Result(column = "sNo", property = "absentTime", one = @One(select = "com.example.clocksystem.mapper.ClockRecordMapper.countStudentAbsenceTime", fetchType = FetchType.LAZY))})
    List<Student> findByClassNo(@Param("classNo") String classNo);

    // 查询所有学生
    @Select("SELECT * FROM student")
    @Results(id = "StudentMapperResultMapA", value = {@Result(column = "sNo", property = "sNo", id = true),
            @Result(column = "classNo", property = "classNo"),
            @Result(column = "classNo", property = "stuClass", one = @One(select = "com.example.clocksystem.mapper.ClassMapper.findByClassNo", fetchType = FetchType.LAZY))})
    List<Student> findAllStudents();

    //按课号查找学生
    @Select("SELECT * FROM student WHERE sNo IN (SELECT sNo FROM student_course WHERE courseNo = #{courseNo})")
    @Results({@Result(column = "courseNo", property = "courseNo"),
            @Result(column = "sNo", property = "sNo", id = true),
            @Result(column = "classNo", property = "classNo"),
            @Result(column = "classNo", property = "stuClass", one = @One(select = "com.example.clocksystem.mapper.ClassMapper.findByClassNo", fetchType = FetchType.LAZY))})
    List<Student> findByCourseNo(int courseNo);

    //根据班级号查找最后一个学生
    @Select("SELECT * FROM student WHERE classNo = #{classNo} ORDER BY sno DESC LIMIT 1")
    @ResultMap(value = "StudentMapperResultMapA")
    Student findLastStudentByClassNo(@Param("classNo") String classNo);

    //按班级统计学生数量
    @Select("SELECT count(*) number FROM student WHERE classNo = #{classNo}")
    int countClassStudentNumberByClassNo(@Param("classNo") String classNo);

    // 添加学生
    @Insert("INSERT INTO student (sNo, sName, classNo, password, sPhone, idCard) VALUES (#{sNo}, #{sName}, #{classNo}, #{password}, #{sPhone}, #{idCard})")
    int addStudent(Student student);

    // 删除学生
    @Delete("DELETE FROM student WHERE sNo = #{sNo}")
    int deleteBySno(@Param("sNo") String sNo);

    // 按学号更新学生
    @Update("UPDATE student SET sName = #{student.sName}, classNo = #{student.classNo}, password = #{student.password}, sPhone = #{student.sPhone}, idCard = #{student.idCard} WHERE sNo = #{student.sNo}")
    int updateStudent(@Param("student") Student student);

    //找出课程缺勤名单
    @Select("SELECT * FROM student WHERE sNo IN (SELECT DISTINCT sNo FROM clockrecord WHERE courseNo = #{courseNo} AND status = 1)")
    List<Student> findAbsentStudentsByCourseNo(@Param("courseNo") int courseNo);

    //找出课程出勤名单
    @Select("SELECT * FROM student WHERE sNo IN (SELECT DISTINCT sNo FROM clockrecord WHERE courseNo = #{courseNo} AND sNo NOT IN (SELECT sNo FROM clockrecord WHERE courseNo = #{courseNo} AND (status = 1 OR status = 2)))")
    List<Student> findAttendStudentsByCourseNo(@Param("courseNo") int courseNo);

    //按班级号找出缺勤过的学生
    @Select("SELECT * FROM student WHERE sNo IN (SELECT DISTINCT sNo FROM clockrecord WHERE status = 1 AND sNo IN (SELECT sNo FROM student WHERE classNo = #{classNo}))")
    List<Student> findAbsentStudentsByClassNo(@Param("classNo") String classNo);

    //按班级号找出全勤的学生
    @Select("SELECT * FROM student WHERE classNo = #{classNo} AND sNo NOT IN (SELECT DISTINCT sNo FROM clockrecord WHERE (status = 1 OR status = 2) AND sNo IN (SELECT sNo FROM student WHERE classNo = #{classNo}))")
    List<Student> findAttendStudentsByClassNo(@Param("classNo") String classNo);
}
