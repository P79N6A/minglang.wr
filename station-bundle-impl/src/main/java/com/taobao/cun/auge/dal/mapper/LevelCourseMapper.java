package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import com.taobao.cun.auge.dal.domain.LevelCourse;
import com.taobao.cun.auge.dal.domain.LevelCourseExample;
import org.apache.ibatis.annotations.Param;

public interface LevelCourseMapper {
    
    List<LevelCourse> groupCoursesByTag(int topN);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table level_course
     *
     * @mbggenerated Fri Sep 30 13:52:22 CST 2016
     */
    int countByExample(LevelCourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table level_course
     *
     * @mbggenerated Fri Sep 30 13:52:22 CST 2016
     */
    int deleteByExample(LevelCourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table level_course
     *
     * @mbggenerated Fri Sep 30 13:52:22 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table level_course
     *
     * @mbggenerated Fri Sep 30 13:52:22 CST 2016
     */
    int insert(LevelCourse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table level_course
     *
     * @mbggenerated Fri Sep 30 13:52:22 CST 2016
     */
    int insertSelective(LevelCourse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table level_course
     *
     * @mbggenerated Fri Sep 30 13:52:22 CST 2016
     */
    List<LevelCourse> selectByExample(LevelCourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table level_course
     *
     * @mbggenerated Fri Sep 30 13:52:22 CST 2016
     */
    LevelCourse selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table level_course
     *
     * @mbggenerated Fri Sep 30 13:52:22 CST 2016
     */
    int updateByExampleSelective(@Param("record") LevelCourse record, @Param("example") LevelCourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table level_course
     *
     * @mbggenerated Fri Sep 30 13:52:22 CST 2016
     */
    int updateByExample(@Param("record") LevelCourse record, @Param("example") LevelCourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table level_course
     *
     * @mbggenerated Fri Sep 30 13:52:22 CST 2016
     */
    int updateByPrimaryKeySelective(LevelCourse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table level_course
     *
     * @mbggenerated Fri Sep 30 13:52:22 CST 2016
     */
    int updateByPrimaryKey(LevelCourse record);
}