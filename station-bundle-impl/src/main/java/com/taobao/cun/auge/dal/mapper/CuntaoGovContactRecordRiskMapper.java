package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.CuntaoGovContactRecordRisk;
import com.taobao.cun.auge.dal.domain.CuntaoGovContactRecordRiskExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CuntaoGovContactRecordRiskMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_gov_contact_record_risk
     *
     * @mbggenerated
     */
    int countByExample(CuntaoGovContactRecordRiskExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_gov_contact_record_risk
     *
     * @mbggenerated
     */
    int deleteByExample(CuntaoGovContactRecordRiskExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_gov_contact_record_risk
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_gov_contact_record_risk
     *
     * @mbggenerated
     */
    int insert(CuntaoGovContactRecordRisk record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_gov_contact_record_risk
     *
     * @mbggenerated
     */
    int insertSelective(CuntaoGovContactRecordRisk record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_gov_contact_record_risk
     *
     * @mbggenerated
     */
    List<CuntaoGovContactRecordRisk> selectByExample(CuntaoGovContactRecordRiskExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_gov_contact_record_risk
     *
     * @mbggenerated
     */
    CuntaoGovContactRecordRisk selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_gov_contact_record_risk
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CuntaoGovContactRecordRisk record, @Param("example") CuntaoGovContactRecordRiskExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_gov_contact_record_risk
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CuntaoGovContactRecordRisk record, @Param("example") CuntaoGovContactRecordRiskExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_gov_contact_record_risk
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CuntaoGovContactRecordRisk record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_gov_contact_record_risk
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CuntaoGovContactRecordRisk record);
}