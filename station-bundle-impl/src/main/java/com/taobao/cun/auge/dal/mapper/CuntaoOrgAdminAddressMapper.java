package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import com.taobao.cun.auge.dal.domain.CuntaoOrgAdminAddress;
import com.taobao.cun.auge.dal.domain.CuntaoOrgAdminAddressExample;
import org.apache.ibatis.annotations.Param;

public interface CuntaoOrgAdminAddressMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_org_admin_address
     *
     * @mbggenerated
     */
    int countByExample(CuntaoOrgAdminAddressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_org_admin_address
     *
     * @mbggenerated
     */
    int deleteByExample(CuntaoOrgAdminAddressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_org_admin_address
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_org_admin_address
     *
     * @mbggenerated
     */
    int insert(CuntaoOrgAdminAddress record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_org_admin_address
     *
     * @mbggenerated
     */
    int insertSelective(CuntaoOrgAdminAddress record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_org_admin_address
     *
     * @mbggenerated
     */
    List<CuntaoOrgAdminAddress> selectByExample(CuntaoOrgAdminAddressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_org_admin_address
     *
     * @mbggenerated
     */
    CuntaoOrgAdminAddress selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_org_admin_address
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CuntaoOrgAdminAddress record, @Param("example") CuntaoOrgAdminAddressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_org_admin_address
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CuntaoOrgAdminAddress record, @Param("example") CuntaoOrgAdminAddressExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_org_admin_address
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CuntaoOrgAdminAddress record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_org_admin_address
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CuntaoOrgAdminAddress record);
}