package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import com.taobao.cun.auge.dal.domain.DwiCtStationTpaIncomeM;
import com.taobao.cun.auge.dal.domain.DwiCtStationTpaIncomeMExample;
import org.apache.ibatis.annotations.Param;

public interface DwiCtStationTpaIncomeMMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated
     */
    int countByExample(DwiCtStationTpaIncomeMExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated
     */
    int deleteByExample(DwiCtStationTpaIncomeMExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated
     */
    int insert(DwiCtStationTpaIncomeM record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated
     */
    int insertSelective(DwiCtStationTpaIncomeM record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated
     */
    List<DwiCtStationTpaIncomeM> selectByExample(DwiCtStationTpaIncomeMExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated
     */
    DwiCtStationTpaIncomeM selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DwiCtStationTpaIncomeM record, @Param("example") DwiCtStationTpaIncomeMExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DwiCtStationTpaIncomeM record, @Param("example") DwiCtStationTpaIncomeMExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DwiCtStationTpaIncomeM record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DwiCtStationTpaIncomeM record);
}