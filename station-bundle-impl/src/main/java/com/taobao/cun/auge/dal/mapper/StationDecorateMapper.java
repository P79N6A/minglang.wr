package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.StationDecorate;
import com.taobao.cun.auge.dal.domain.StationDecorateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StationDecorateMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Wed Jul 20 14:30:47 CST 2016
     */
    int countByExample(StationDecorateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Wed Jul 20 14:30:47 CST 2016
     */
    int deleteByExample(StationDecorateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Wed Jul 20 14:30:47 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Wed Jul 20 14:30:47 CST 2016
     */
    int insert(StationDecorate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Wed Jul 20 14:30:47 CST 2016
     */
    int insertSelective(StationDecorate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Wed Jul 20 14:30:47 CST 2016
     */
    List<StationDecorate> selectByExample(StationDecorateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Wed Jul 20 14:30:47 CST 2016
     */
    StationDecorate selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Wed Jul 20 14:30:47 CST 2016
     */
    int updateByExampleSelective(@Param("record") StationDecorate record, @Param("example") StationDecorateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Wed Jul 20 14:30:47 CST 2016
     */
    int updateByExample(@Param("record") StationDecorate record, @Param("example") StationDecorateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Wed Jul 20 14:30:47 CST 2016
     */
    int updateByPrimaryKeySelective(StationDecorate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table station_decorate
     *
     * @mbggenerated Wed Jul 20 14:30:47 CST 2016
     */
    int updateByPrimaryKey(StationDecorate record);
}