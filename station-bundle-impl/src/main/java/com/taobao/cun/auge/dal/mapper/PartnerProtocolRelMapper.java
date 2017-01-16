package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.PartnerProtocolRel;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PartnerProtocolRelMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_protocol_rel
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int countByExample(PartnerProtocolRelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_protocol_rel
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int deleteByExample(PartnerProtocolRelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_protocol_rel
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_protocol_rel
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int insert(PartnerProtocolRel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_protocol_rel
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int insertSelective(PartnerProtocolRel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_protocol_rel
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    List<PartnerProtocolRel> selectByExample(PartnerProtocolRelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_protocol_rel
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    PartnerProtocolRel selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_protocol_rel
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int updateByExampleSelective(@Param("record") PartnerProtocolRel record, @Param("example") PartnerProtocolRelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_protocol_rel
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int updateByExample(@Param("record") PartnerProtocolRel record, @Param("example") PartnerProtocolRelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_protocol_rel
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int updateByPrimaryKeySelective(PartnerProtocolRel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_protocol_rel
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int updateByPrimaryKey(PartnerProtocolRel record);
}