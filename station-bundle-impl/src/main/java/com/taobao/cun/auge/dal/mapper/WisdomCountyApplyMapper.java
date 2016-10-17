package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.WisdomCountyApply;
import com.taobao.cun.auge.dal.domain.WisdomCountyApplyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WisdomCountyApplyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wisdom_county_apply
     *
     * @mbggenerated Mon Oct 17 21:56:57 CST 2016
     */
    int countByExample(WisdomCountyApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wisdom_county_apply
     *
     * @mbggenerated Mon Oct 17 21:56:57 CST 2016
     */
    int deleteByExample(WisdomCountyApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wisdom_county_apply
     *
     * @mbggenerated Mon Oct 17 21:56:57 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wisdom_county_apply
     *
     * @mbggenerated Mon Oct 17 21:56:57 CST 2016
     */
    int insert(WisdomCountyApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wisdom_county_apply
     *
     * @mbggenerated Mon Oct 17 21:56:57 CST 2016
     */
    int insertSelective(WisdomCountyApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wisdom_county_apply
     *
     * @mbggenerated Mon Oct 17 21:56:57 CST 2016
     */
    List<WisdomCountyApply> selectByExample(WisdomCountyApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wisdom_county_apply
     *
     * @mbggenerated Mon Oct 17 21:56:57 CST 2016
     */
    WisdomCountyApply selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wisdom_county_apply
     *
     * @mbggenerated Mon Oct 17 21:56:57 CST 2016
     */
    int updateByExampleSelective(@Param("record") WisdomCountyApply record, @Param("example") WisdomCountyApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wisdom_county_apply
     *
     * @mbggenerated Mon Oct 17 21:56:57 CST 2016
     */
    int updateByExample(@Param("record") WisdomCountyApply record, @Param("example") WisdomCountyApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wisdom_county_apply
     *
     * @mbggenerated Mon Oct 17 21:56:57 CST 2016
     */
    int updateByPrimaryKeySelective(WisdomCountyApply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wisdom_county_apply
     *
     * @mbggenerated Mon Oct 17 21:56:57 CST 2016
     */
    int updateByPrimaryKey(WisdomCountyApply record);
}