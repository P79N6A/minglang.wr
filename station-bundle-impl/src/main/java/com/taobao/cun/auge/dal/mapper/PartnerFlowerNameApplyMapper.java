package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.PartnerFlowerNameApply;
import com.taobao.cun.auge.dal.domain.PartnerFlowerNameApplyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PartnerFlowerNameApplyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_flower_name_apply
     *
     * @mbggenerated
     */
    int countByExample(PartnerFlowerNameApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_flower_name_apply
     *
     * @mbggenerated
     */
    int deleteByExample(PartnerFlowerNameApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_flower_name_apply
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_flower_name_apply
     *
     * @mbggenerated
     */
    int insert(PartnerFlowerNameApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_flower_name_apply
     *
     * @mbggenerated
     */
    int insertSelective(PartnerFlowerNameApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_flower_name_apply
     *
     * @mbggenerated
     */
    List<PartnerFlowerNameApply> selectByExample(PartnerFlowerNameApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_flower_name_apply
     *
     * @mbggenerated
     */
    PartnerFlowerNameApply selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_flower_name_apply
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") PartnerFlowerNameApply record, @Param("example") PartnerFlowerNameApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_flower_name_apply
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") PartnerFlowerNameApply record, @Param("example") PartnerFlowerNameApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_flower_name_apply
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(PartnerFlowerNameApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_flower_name_apply
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(PartnerFlowerNameApply record);
}