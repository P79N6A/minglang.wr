package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taobao.cun.auge.dal.domain.CuntaoUserOrg;
import com.taobao.cun.auge.dal.domain.CuntaoUserOrgExample;

public interface CuntaoUserOrgMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_org
     *
     * @mbggenerated
     */
    int countByExample(CuntaoUserOrgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_org
     *
     * @mbggenerated
     */
    int deleteByExample(CuntaoUserOrgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_org
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_org
     *
     * @mbggenerated
     */
    int insert(CuntaoUserOrg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_org
     *
     * @mbggenerated
     */
    int insertSelective(CuntaoUserOrg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_org
     *
     * @mbggenerated
     */
    List<CuntaoUserOrg> selectByExample(CuntaoUserOrgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_org
     *
     * @mbggenerated
     */
    CuntaoUserOrg selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_org
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CuntaoUserOrg record, @Param("example") CuntaoUserOrgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_org
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CuntaoUserOrg record, @Param("example") CuntaoUserOrgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_org
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CuntaoUserOrg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_org
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CuntaoUserOrg record);
    
    
}