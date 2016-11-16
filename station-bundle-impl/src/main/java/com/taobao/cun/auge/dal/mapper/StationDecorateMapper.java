package com.taobao.cun.auge.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.taobao.cun.auge.dal.domain.StationDecorate;
import com.taobao.cun.auge.dal.domain.StationDecorateExample;

public interface StationDecorateMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Tue Aug 09 16:50:31 CST 2016
     */
    int countByExample(StationDecorateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Tue Aug 09 16:50:31 CST 2016
     */
    int deleteByExample(StationDecorateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Tue Aug 09 16:50:31 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Tue Aug 09 16:50:31 CST 2016
     */
    int insert(StationDecorate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Tue Aug 09 16:50:31 CST 2016
     */
    int insertSelective(StationDecorate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Tue Aug 09 16:50:31 CST 2016
     */
    List<StationDecorate> selectByExample(StationDecorateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Tue Aug 09 16:50:31 CST 2016
     */
    StationDecorate selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Tue Aug 09 16:50:31 CST 2016
     */
    int updateByExampleSelective(@Param("record") StationDecorate record, @Param("example") StationDecorateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Tue Aug 09 16:50:31 CST 2016
     */
    int updateByExample(@Param("record") StationDecorate record, @Param("example") StationDecorateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Tue Aug 09 16:50:31 CST 2016
     */
    int updateByPrimaryKeySelective(StationDecorate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Tue Aug 09 16:50:31 CST 2016
     */
    int updateByPrimaryKey(StationDecorate record);
    
    int invalidOldDecorateRecord(Map<String,String> param);
}