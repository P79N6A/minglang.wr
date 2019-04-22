package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.CuntaoCounty;
import com.taobao.cun.auge.dal.domain.CuntaoCountyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CuntaoCountyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county
     *
     * @mbggenerated
     */
    int countByExample(CuntaoCountyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county
     *
     * @mbggenerated
     */
    int deleteByExample(CuntaoCountyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county
     *
     * @mbggenerated
     */
    int insert(CuntaoCounty record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county
     *
     * @mbggenerated
     */
    int insertSelective(CuntaoCounty record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county
     *
     * @mbggenerated
     */
    List<CuntaoCounty> selectByExample(CuntaoCountyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county
     *
     * @mbggenerated
     */
    CuntaoCounty selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CuntaoCounty record, @Param("example") CuntaoCountyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CuntaoCounty record, @Param("example") CuntaoCountyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CuntaoCounty record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_county
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CuntaoCounty record);
}