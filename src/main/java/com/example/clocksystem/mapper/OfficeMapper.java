package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.Office;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OfficeMapper {
    @Select("SELECT * FROM office WHERE officeId = #{officeId}")
    Office findOfficeByOfficeId(@Param("officeId") String officeId);

    @Insert("INSERT INTO office (officeId, password) VALUES (#{office.officeId}, #{office.password})")
    int addOffice(@Param("office") Office office);

    @Update("UPDATE office SET password = #{office.password} WHERE officeId = #{office.officeId}")
    int updateOffice(@Param("office") Office office);

    @Delete("DELETE FROM office WHERE officeId = #{officeId}")
    int deleteOffice(@Param("officeId") String officeId);
}
