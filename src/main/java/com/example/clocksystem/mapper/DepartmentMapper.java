package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.Department;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    @Select("SELECT * FROM department")
    List<Department> findAllDepartments();

    @Select("SELECT * FROM department WHERE departmentNo = #{departmentNo}")
    Department findDepartmentByNo(@Param("departmentNo") String departmentNo);

    @Select("SELECT * FROM department WHERE departmentName = #{departmentName}")
    List<Department> findDepartmentsByName(@Param("departmentName") String departmentName);

    @Insert("INSERT INTO department (departmentNo, departmentName) VALUES (#{departmentNo}, #{departmentName})")
    int addDepartment(Department department);

    @Update("UPDATE department SET departmentName = #{department.departmentName} WHERE departmentNo = #{department.departmentNo}")
    int updateDepartment(@Param("department") Department department);

    @Delete("DELETE FROM department WHERE departmentNo = #{departmentNo}")
    int deleteDepartment(@Param("departmentNo") String departmentNo);
}
