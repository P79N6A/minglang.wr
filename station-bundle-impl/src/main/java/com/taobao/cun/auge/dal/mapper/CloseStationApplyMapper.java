package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import com.taobao.cun.auge.dal.domain.CloseStationApply;
import com.taobao.cun.auge.dal.domain.CloseStationApplyExample;
import org.apache.ibatis.annotations.Param;

public interface CloseStationApplyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table close_station_apply
     *
     * @mbggenerated Fri Sep 09 15:36:41 GMT+08:00 2016
     */
    int countByExample(CloseStationApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table close_station_apply
     *
     * @mbggenerated Fri Sep 09 15:36:41 GMT+08:00 2016
     */
    int deleteByExample(CloseStationApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table close_station_apply
     *
     * @mbggenerated Fri Sep 09 15:36:41 GMT+08:00 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table close_station_apply
     *
     * @mbggenerated Fri Sep 09 15:36:41 GMT+08:00 2016
     */
    int insert(CloseStationApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table close_station_apply
     *
     * @mbggenerated Fri Sep 09 15:36:41 GMT+08:00 2016
     */
    int insertSelective(CloseStationApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table close_station_apply
     *
     * @mbggenerated Fri Sep 09 15:36:41 GMT+08:00 2016
     */
    List<CloseStationApply> selectByExample(CloseStationApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table close_station_apply
     *
     * @mbggenerated Fri Sep 09 15:36:41 GMT+08:00 2016
     */
    CloseStationApply selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table close_station_apply
     *
     * @mbggenerated Fri Sep 09 15:36:41 GMT+08:00 2016
     */
    int updateByExampleSelective(@Param("record") CloseStationApply record, @Param("example") CloseStationApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table close_station_apply
     *
     * @mbggenerated Fri Sep 09 15:36:41 GMT+08:00 2016
     */
    int updateByExample(@Param("record") CloseStationApply record, @Param("example") CloseStationApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table close_station_apply
     *
     * @mbggenerated Fri Sep 09 15:36:41 GMT+08:00 2016
     */
    int updateByPrimaryKeySelective(CloseStationApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table close_station_apply
     *
     * @mbggenerated Fri Sep 09 15:36:41 GMT+08:00 2016
     */
    int updateByPrimaryKey(CloseStationApply record);
}