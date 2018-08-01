package com.taobao.cun.auge.dal.mapper;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.CountyStationExample;
import org.apache.ibatis.annotations.Param;

public interface CountyStationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table county_station
     *
     * @mbggenerated
     */
    int countByExample(CountyStationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table county_station
     *
     * @mbggenerated
     */
    int deleteByExample(CountyStationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table county_station
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table county_station
     *
     * @mbggenerated
     */
    int insert(CountyStation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table county_station
     *
     * @mbggenerated
     */
    int insertSelective(CountyStation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table county_station
     *
     * @mbggenerated
     */
    List<CountyStation> selectByExample(CountyStationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table county_station
     *
     * @mbggenerated
     */
    CountyStation selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table county_station
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CountyStation record, @Param("example") CountyStationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table county_station
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CountyStation record, @Param("example") CountyStationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table county_station
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CountyStation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table county_station
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CountyStation record);
    
    List<CountyStation> getProvinceList(Map<String,Object> param);
    
    int countCountyStation(Map<String,Object> param);
    
    List<CountyStation> queryCountyStation(Map<String,Object> param);
    
    int countServicingStation(Long id);
}