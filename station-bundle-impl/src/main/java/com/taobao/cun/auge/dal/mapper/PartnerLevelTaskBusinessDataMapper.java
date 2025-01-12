package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import com.taobao.cun.auge.dal.domain.PartnerLevelTaskBusinessData;
import com.taobao.cun.auge.dal.domain.PartnerLevelTaskBusinessDataExample;
import org.apache.ibatis.annotations.Param;

public interface PartnerLevelTaskBusinessDataMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    int countByExample(PartnerLevelTaskBusinessDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    int deleteByExample(PartnerLevelTaskBusinessDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    int insert(PartnerLevelTaskBusinessData record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    int insertSelective(PartnerLevelTaskBusinessData record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    List<PartnerLevelTaskBusinessData> selectByExample(PartnerLevelTaskBusinessDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    PartnerLevelTaskBusinessData selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") PartnerLevelTaskBusinessData record, @Param("example") PartnerLevelTaskBusinessDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") PartnerLevelTaskBusinessData record, @Param("example") PartnerLevelTaskBusinessDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(PartnerLevelTaskBusinessData record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(PartnerLevelTaskBusinessData record);
}