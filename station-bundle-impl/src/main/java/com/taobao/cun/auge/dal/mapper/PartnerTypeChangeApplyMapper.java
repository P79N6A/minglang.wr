package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.PartnerTypeChangeApply;
import com.taobao.cun.auge.dal.domain.PartnerTypeChangeApplyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PartnerTypeChangeApplyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_type_change_apply
     *
     * @mbggenerated Wed Dec 14 10:37:03 GMT+08:00 2016
     */
    int countByExample(PartnerTypeChangeApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_type_change_apply
     *
     * @mbggenerated Wed Dec 14 10:37:03 GMT+08:00 2016
     */
    int deleteByExample(PartnerTypeChangeApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_type_change_apply
     *
     * @mbggenerated Wed Dec 14 10:37:03 GMT+08:00 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_type_change_apply
     *
     * @mbggenerated Wed Dec 14 10:37:03 GMT+08:00 2016
     */
    int insert(PartnerTypeChangeApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_type_change_apply
     *
     * @mbggenerated Wed Dec 14 10:37:03 GMT+08:00 2016
     */
    int insertSelective(PartnerTypeChangeApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_type_change_apply
     *
     * @mbggenerated Wed Dec 14 10:37:03 GMT+08:00 2016
     */
    List<PartnerTypeChangeApply> selectByExample(PartnerTypeChangeApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_type_change_apply
     *
     * @mbggenerated Wed Dec 14 10:37:03 GMT+08:00 2016
     */
    PartnerTypeChangeApply selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_type_change_apply
     *
     * @mbggenerated Wed Dec 14 10:37:03 GMT+08:00 2016
     */
    int updateByExampleSelective(@Param("record") PartnerTypeChangeApply record, @Param("example") PartnerTypeChangeApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_type_change_apply
     *
     * @mbggenerated Wed Dec 14 10:37:03 GMT+08:00 2016
     */
    int updateByExample(@Param("record") PartnerTypeChangeApply record, @Param("example") PartnerTypeChangeApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_type_change_apply
     *
     * @mbggenerated Wed Dec 14 10:37:03 GMT+08:00 2016
     */
    int updateByPrimaryKeySelective(PartnerTypeChangeApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_type_change_apply
     *
     * @mbggenerated Wed Dec 14 10:37:03 GMT+08:00 2016
     */
    int updateByPrimaryKey(PartnerTypeChangeApply record);
}