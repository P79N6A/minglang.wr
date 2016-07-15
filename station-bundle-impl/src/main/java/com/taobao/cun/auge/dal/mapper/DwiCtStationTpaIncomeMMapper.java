package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.DwiCtStationTpaIncomeM;
import com.taobao.cun.auge.dal.domain.DwiCtStationTpaIncomeMExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DwiCtStationTpaIncomeMMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated Fri Jul 15 15:50:59 GMT+08:00 2016
     */
    int countByExample(DwiCtStationTpaIncomeMExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated Fri Jul 15 15:50:59 GMT+08:00 2016
     */
    int deleteByExample(DwiCtStationTpaIncomeMExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated Fri Jul 15 15:50:59 GMT+08:00 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated Fri Jul 15 15:50:59 GMT+08:00 2016
     */
    int insert(DwiCtStationTpaIncomeM record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated Fri Jul 15 15:50:59 GMT+08:00 2016
     */
    int insertSelective(DwiCtStationTpaIncomeM record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated Fri Jul 15 15:50:59 GMT+08:00 2016
     */
    List<DwiCtStationTpaIncomeM> selectByExample(DwiCtStationTpaIncomeMExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated Fri Jul 15 15:50:59 GMT+08:00 2016
     */
    DwiCtStationTpaIncomeM selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated Fri Jul 15 15:50:59 GMT+08:00 2016
     */
    int updateByExampleSelective(@Param("record") DwiCtStationTpaIncomeM record, @Param("example") DwiCtStationTpaIncomeMExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated Fri Jul 15 15:50:59 GMT+08:00 2016
     */
    int updateByExample(@Param("record") DwiCtStationTpaIncomeM record, @Param("example") DwiCtStationTpaIncomeMExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated Fri Jul 15 15:50:59 GMT+08:00 2016
     */
    int updateByPrimaryKeySelective(DwiCtStationTpaIncomeM record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dwi_ct_station_tpa_income_m
     *
     * @mbggenerated Fri Jul 15 15:50:59 GMT+08:00 2016
     */
    int updateByPrimaryKey(DwiCtStationTpaIncomeM record);
}