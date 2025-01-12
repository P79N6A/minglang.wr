package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.CuntaoCountyWhitename;
import com.taobao.cun.auge.dal.domain.CuntaoCountyWhitenameExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CuntaoCountyWhitenameMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_whitename
     *
     * @mbggenerated
     */
    int countByExample(CuntaoCountyWhitenameExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_whitename
     *
     * @mbggenerated
     */
    int deleteByExample(CuntaoCountyWhitenameExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_whitename
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_whitename
     *
     * @mbggenerated
     */
    int insert(CuntaoCountyWhitename record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_whitename
     *
     * @mbggenerated
     */
    int insertSelective(CuntaoCountyWhitename record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_whitename
     *
     * @mbggenerated
     */
    List<CuntaoCountyWhitename> selectByExample(CuntaoCountyWhitenameExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_whitename
     *
     * @mbggenerated
     */
    CuntaoCountyWhitename selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_whitename
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CuntaoCountyWhitename record, @Param("example") CuntaoCountyWhitenameExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_whitename
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CuntaoCountyWhitename record, @Param("example") CuntaoCountyWhitenameExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_whitename
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CuntaoCountyWhitename record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county_whitename
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CuntaoCountyWhitename record);
}