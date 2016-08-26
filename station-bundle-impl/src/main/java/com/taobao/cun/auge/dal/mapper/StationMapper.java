package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StationExample;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface StationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station
     *
     * @mbggenerated Fri Jul 15 12:43:49 CST 2016
     */
    int countByExample(StationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station
     *
     * @mbggenerated Fri Jul 15 12:43:49 CST 2016
     */
    int deleteByExample(StationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station
     *
     * @mbggenerated Fri Jul 15 12:43:49 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station
     *
     * @mbggenerated Fri Jul 15 12:43:49 CST 2016
     */
    int insert(Station record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station
     *
     * @mbggenerated Fri Jul 15 12:43:49 CST 2016
     */
    int insertSelective(Station record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station
     *
     * @mbggenerated Fri Jul 15 12:43:49 CST 2016
     */
    List<Station> selectByExample(StationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station
     *
     * @mbggenerated Fri Jul 15 12:43:49 CST 2016
     */
    Station selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station
     *
     * @mbggenerated Fri Jul 15 12:43:49 CST 2016
     */
    int updateByExampleSelective(@Param("record") Station record, @Param("example") StationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station
     *
     * @mbggenerated Fri Jul 15 12:43:49 CST 2016
     */
    int updateByExample(@Param("record") Station record, @Param("example") StationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station
     *
     * @mbggenerated Fri Jul 15 12:43:49 CST 2016
     */
    int updateByPrimaryKeySelective(Station record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station
     *
     * @mbggenerated Fri Jul 15 12:43:49 CST 2016
     */
    int updateByPrimaryKey(Station record);

}