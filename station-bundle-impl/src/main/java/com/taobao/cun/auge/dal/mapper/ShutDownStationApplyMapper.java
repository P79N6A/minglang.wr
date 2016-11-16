package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.ShutDownStationApply;
import com.taobao.cun.auge.dal.domain.ShutDownStationApplyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ShutDownStationApplyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shut_down_station_apply
     *
     * @mbggenerated Thu Aug 25 15:34:43 GMT+08:00 2016
     */
    int countByExample(ShutDownStationApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shut_down_station_apply
     *
     * @mbggenerated Thu Aug 25 15:34:43 GMT+08:00 2016
     */
    int deleteByExample(ShutDownStationApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shut_down_station_apply
     *
     * @mbggenerated Thu Aug 25 15:34:43 GMT+08:00 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shut_down_station_apply
     *
     * @mbggenerated Thu Aug 25 15:34:43 GMT+08:00 2016
     */
    int insert(ShutDownStationApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shut_down_station_apply
     *
     * @mbggenerated Thu Aug 25 15:34:43 GMT+08:00 2016
     */
    int insertSelective(ShutDownStationApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shut_down_station_apply
     *
     * @mbggenerated Thu Aug 25 15:34:43 GMT+08:00 2016
     */
    List<ShutDownStationApply> selectByExample(ShutDownStationApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shut_down_station_apply
     *
     * @mbggenerated Thu Aug 25 15:34:43 GMT+08:00 2016
     */
    ShutDownStationApply selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shut_down_station_apply
     *
     * @mbggenerated Thu Aug 25 15:34:43 GMT+08:00 2016
     */
    int updateByExampleSelective(@Param("record") ShutDownStationApply record, @Param("example") ShutDownStationApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shut_down_station_apply
     *
     * @mbggenerated Thu Aug 25 15:34:43 GMT+08:00 2016
     */
    int updateByExample(@Param("record") ShutDownStationApply record, @Param("example") ShutDownStationApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shut_down_station_apply
     *
     * @mbggenerated Thu Aug 25 15:34:43 GMT+08:00 2016
     */
    int updateByPrimaryKeySelective(ShutDownStationApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shut_down_station_apply
     *
     * @mbggenerated Thu Aug 25 15:34:43 GMT+08:00 2016
     */
    int updateByPrimaryKey(ShutDownStationApply record);
}