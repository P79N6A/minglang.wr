package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PartnerMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner
     *
     * @mbggenerated Tue Jul 12 21:25:03 CST 2016
     */
    int countByExample(PartnerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner
     *
     * @mbggenerated Tue Jul 12 21:25:03 CST 2016
     */
    int deleteByExample(PartnerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner
     *
     * @mbggenerated Tue Jul 12 21:25:03 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner
     *
     * @mbggenerated Tue Jul 12 21:25:03 CST 2016
     */
    int insert(Partner record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner
     *
     * @mbggenerated Tue Jul 12 21:25:03 CST 2016
     */
    int insertSelective(Partner record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner
     *
     * @mbggenerated Tue Jul 12 21:25:03 CST 2016
     */
    List<Partner> selectByExample(PartnerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner
     *
     * @mbggenerated Tue Jul 12 21:25:03 CST 2016
     */
    Partner selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner
     *
     * @mbggenerated Tue Jul 12 21:25:03 CST 2016
     */
    int updateByExampleSelective(@Param("record") Partner record, @Param("example") PartnerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner
     *
     * @mbggenerated Tue Jul 12 21:25:03 CST 2016
     */
    int updateByExample(@Param("record") Partner record, @Param("example") PartnerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner
     *
     * @mbggenerated Tue Jul 12 21:25:03 CST 2016
     */
    int updateByPrimaryKeySelective(Partner record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner
     *
     * @mbggenerated Tue Jul 12 21:25:03 CST 2016
     */
    int updateByPrimaryKey(Partner record);
}