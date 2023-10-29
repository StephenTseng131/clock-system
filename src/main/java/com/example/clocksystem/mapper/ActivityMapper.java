package com.example.clocksystem.mapper;

import com.example.clocksystem.entity.Activity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ActivityMapper {
    //查询所有活动记录
    @Select("SELECT * FROM activity")
    @ResultMap(value = "ActivityMapperResultMapA")
    List<Activity> findAllActivities();

    //根据活动编号查询活动记录
    @Select("SELECT * FROM activity WHERE activityNo = #{activityNo}")
    @Results(id = "ActivityMapperResultMapA", value = {@Result(column = "activityNo", property = "activityNo", id = true),
            @Result(column = "societyId", property = "societyId"),
            @Result(column = "societyId", property = "society", one = @One(select = "com.example.clocksystem.mapper.SocietyMapper.findSocietyBySocietyId", fetchType = FetchType.LAZY))})
    Activity findActivityByNo(int activityNo);

    //根据活动名称查询活动记录
    @Select("SELECT * FROM activity WHERE activityName = #{activityName}")
    @ResultMap(value = "ActivityMapperResultMapA")
    List<Activity> findActivityByName(@Param("activityName") String activityName);

    //根据活动类型查询活动记录
    @Select("SELECT * FROM activity WHERE type = #{type}")
    @ResultMap(value = "ActivityMapperResultMapA")
    List<Activity> findActivitiesByType(@Param("type") int type);

    //根据活动的学分查询活动记录
    @Select("SELECT * FROM activity WHERE credit = #{credit}")
    @ResultMap(value = "ActivityMapperResultMapA")
    List<Activity> findActivitiesByCredit(@Param("credit") int credit);

    //根据社团ID查询活动记录
    @Select("SELECT * FROM activity WHERE societyId = #{societyId}")
    @ResultMap(value = "ActivityMapperResultMapA")
    List<Activity> findActivitiesBySocietyId(@Param("societyId") String societyId);

    //根据学生学号查询活动记录
    @Select("SELECT * FROM activity WHERE activityNo IN (SELECT activityNo FROM student_activity WHERE sNo = #{sNo})")
    @ResultMap(value = "ActivityMapperResultMapA")
    List<Activity> findActivitiesBySno(@Param("sNo") String sNo);

    //按学号和当前时间查找这个学生未参加过的、有效时间的活动
    @Select("SELECT * FROM activity WHERE endTime > #{now} AND activityNo NOT IN (SELECT activityNo FROM student_activity WHERE sNo = #{sNo})")
    @ResultMap(value = "ActivityMapperResultMapA")
    List<Activity> findActivitiesToStudent(@Param("sNo") String sNo, @Param("now") Timestamp now);

    //按时间范围筛选学生参加过且签到状态为1的活动
    @Select("SELECT * FROM activity WHERE endTime > #{head} AND endTime < #{tail} AND activityNo IN (SELECT activityNo FROM student_activity WHERE status = 1 AND sNo = #{sNo})")
    List<Activity> findActivitiesBySnoAndTimeScopeAndStatus(String sNo, Timestamp head, Timestamp tail);

    //添加一条活动记录
    @Insert("INSERT INTO activity (activityName, introduce, type, credit, startTime, endTime, societyId) VALUES (#{activityName}, #{introduce}, #{type}, #{credit}, #{startTime}, #{endTime}, #{societyId})")
    int addActivity(Activity activity);

    //设置活动的签到码
    @Update("UPDATE activity SET code = #{activity.code} WHERE activityNo = #{activity.activityNo}")
    int updateActivityCode(@Param("activity") Activity activity);

    //更改活动时间
    @Update("UPDATE activity SET startTime = #{activity.startTime}, endTime = #{activity.endTime} WHERE activityNo = #{activity.activityNo}")
    int updateActivityTime(@Param("activity") Activity activity);

    //删除活动
    @Delete("DELETE FROM activity WHERE activityNo = #{activityNo}")
    int deleteActivity(@Param("activityNo") int activityNo);
}
