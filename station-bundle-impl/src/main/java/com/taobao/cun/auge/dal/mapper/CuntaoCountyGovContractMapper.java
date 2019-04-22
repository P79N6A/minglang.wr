package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.CuntaoCountyGovContract;
import com.taobao.cun.auge.dal.domain.CuntaoCountyGovContractExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CuntaoCountyGovContractMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_gov_contract
     *
     * @mbggenerated
     */
    int countByExample(CuntaoCountyGovContractExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_gov_contract
     *
     * @mbggenerated
     */
    int deleteByExample(CuntaoCountyGovContractExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_gov_contract
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_gov_contract
     *
     * @mbggenerated
     */
    int insert(CuntaoCountyGovContract record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_gov_contract
     *
     * @mbggenerated
     */
    int insertSelective(CuntaoCountyGovContract record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_gov_contract
     *
     * @mbggenerated
     */
    List<CuntaoCountyGovContract> selectByExample(CuntaoCountyGovContractExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_gov_contract
     *
     * @mbggenerated
     */
    CuntaoCountyGovContract selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_gov_contract
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CuntaoCountyGovContract record, @Param("example") CuntaoCountyGovContractExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_gov_contract
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CuntaoCountyGovContract record, @Param("example") CuntaoCountyGovContractExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_gov_contract
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CuntaoCountyGovContract record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_gov_contract
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CuntaoCountyGovContract record);
}