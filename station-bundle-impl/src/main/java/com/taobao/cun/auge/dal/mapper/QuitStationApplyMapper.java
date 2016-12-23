package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.dal.domain.QuitStationApplyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface QuitStationApplyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quit_station_apply
     *
     * @mbggenerated Sat Jun 04 17:58:30 CST 2016
     */
    int countByExample(QuitStationApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quit_station_apply
     *
     * @mbggenerated Sat Jun 04 17:58:30 CST 2016
     */
    int deleteByExample(QuitStationApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quit_station_apply
     *
     * @mbggenerated Sat Jun 04 17:58:30 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quit_station_apply
     *
     * @mbggenerated Sat Jun 04 17:58:30 CST 2016
     */
    int insert(QuitStationApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quit_station_apply
     *
     * @mbggenerated Sat Jun 04 17:58:30 CST 2016
     */
    int insertSelective(QuitStationApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quit_station_apply
     *
     * @mbggenerated Sat Jun 04 17:58:30 CST 2016
     */
    List<QuitStationApply> selectByExample(QuitStationApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quit_station_apply
     *
     * @mbggenerated Sat Jun 04 17:58:30 CST 2016
     */
    QuitStationApply selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quit_station_apply
     *
     * @mbggenerated Sat Jun 04 17:58:30 CST 2016
     */
    int updateByExampleSelective(@Param("record") QuitStationApply record, @Param("example") QuitStationApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quit_station_apply
     *
     * @mbggenerated Sat Jun 04 17:58:30 CST 2016
     */
    int updateByExample(@Param("record") QuitStationApply record, @Param("example") QuitStationApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quit_station_apply
     *
     * @mbggenerated Sat Jun 04 17:58:30 CST 2016
     */
    int updateByPrimaryKeySelective(QuitStationApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table quit_station_apply
     *
     * @mbggenerated Sat Jun 04 17:58:30 CST 2016
     */
    int updateByPrimaryKey(QuitStationApply record);
}