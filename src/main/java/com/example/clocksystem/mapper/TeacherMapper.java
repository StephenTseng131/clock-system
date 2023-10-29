package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.Teacher;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface TeacherMapper {
    //查询所有教师
    @Select("SELECT * FROM teacher")
    @ResultMap(value = "TeacherMapperResultMapA")
    List<Teacher> findAll();

    // 按tNo查询教师
    @Select("SELECT * FROM teacher WHERE tNo = #{tNo}")
    @Results(id = "TeacherMapperResultMapB", value = {@Result(column = "tNo", property = "tNo", id = true),
            @Result(column = "departmentNo", property = "departmentNo"),
            @Result(column = "departmentNo", property = "department", one = @One(select = "com.example.clocksystem.mapper.DepartmentMapper.findDepartmentByNo", fetchType = FetchType.LAZY)),
            @Result(column = "tNo", property = "teacherCourseList", many = @Many(select = "com.example.clocksystem.mapper.TeacherCourseMapper.findTeacherCouresByTno", fetchType = FetchType.LAZY)),
            @Result(column = "tNo", property = "teacherLeaveRecordList", many = @Many(select = "com.example.clocksystem.mapper.TeacherLeaveRecordMapper.findLeaveRecordByTno", fetchType = FetchType.LAZY)),
            @Result(column = "tNo", property = "teacherStuAppealList", many = @Many(select = "com.example.clocksystem.mapper.TeacherStuAppealMapper.findStuAppealByTno", fetchType = FetchType.LAZY)),
            @Result(column = "tNo", property = "teacherAttendanceList", many = @Many(select = "com.example.clocksystem.mapper.AttendanceMapper.findByTNo", fetchType = FetchType.LAZY))})
    Teacher findByTno(@Param("tNo") String tNo);

    // 按roleName查询教师
    @Select("SELECT * FROM teacher WHERE roleName = #{roleName}")
    @ResultMap(value = "TeacherMapperResultMapA")
    List<Teacher> findByRoleName(@Param("roleName") String roleName);

    //按学院编号查询教师
    @Select("SELECT * FROM teacher WHERE departmentNo = #{departmentNo}")
    @Results(id = "TeacherMapperResultMapA", value = {@Result(column = "tNo", property = "tNo", id = true),
            @Result(column = "departmentNo", property = "departmentNo"),
            @Result(column = "departmentNo", property = "department", one = @One(select = "com.example.clocksystem.mapper.DepartmentMapper.findDepartmentByNo"))})
    List<Teacher> findTeachersByDepartmentNo(@Param("departmentNo") String departmentNo);

    //按学院号查找最后一个老师
    @Select("SELECT * FROM teacher WHERE departmentNo = #{departmentNo} ORDER BY tNo DESC LIMIT 1")
    @ResultMap(value = "TeacherMapperResultMapA")
    Teacher findLastTeacherByDepartmentNo(@Param("departmentNo") String departmentNo);

    // 添加教师
    @Insert("INSERT INTO teacher (tNo, tName, roleName, password, tPhone, departmentNo) VALUES (#{tNo}, #{tName}, #{roleName}, #{password}, #{tPhone}, #{departmentNo})")
    int addTeacher(Teacher teacher);

    // 按tNo删除教师
    @Delete("DELETE FROM teacher WHERE tNo = #{tNo}")
    int deleteByTno(@Param("tNo") String tNo);

    // 按tNo更新教师
    @Update("UPDATE teacher SET tName = #{teacher.tName}, roleName = #{teacher.roleName}, password = #{teacher.password}, tPhone = #{teacher.tPhone} WHERE tNo = #{teacher.tNo}")
    int updateTeacher(@Param("teacher") Teacher teacher);
}
