package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.Admin;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminMapper {
    @Select("SELECT * FROM admin WHERE id = #{id} ORDER BY id ASC")
    Admin findById(@Param("id") String id);

    @Select("SELECT * FROM admin ORDER BY id ASC")
    List<Admin> findAll();

    @Insert("INSERT INTO admin (id, password) VALUES (#{admin.id}, #{admin.password})")
    int addAdmin(@Param("admin") Admin admin);

    @Delete("DELETE FROM admin WHERE id = #{id}")
    int deleteAdmin(@Param("id") String id);

    @Update("UPDATE admin SET id = #{admin.id}, password = #{admin.password} WHERE id = #{admin.id}")
    int updateAdmin(@Param("admin") Admin admin);
}
