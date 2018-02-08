package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import com.taobao.cun.auge.dal.domain.CuntaoUserRoleLog;
import com.taobao.cun.auge.dal.domain.CuntaoUserRoleLogExample;
import org.apache.ibatis.annotations.Param;

public interface CuntaoUserRoleLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_role_log
     *
     * @mbggenerated
     */
    int countByExample(CuntaoUserRoleLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_role_log
     *
     * @mbggenerated
     */
    int deleteByExample(CuntaoUserRoleLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_role_log
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_role_log
     *
     * @mbggenerated
     */
    int insert(CuntaoUserRoleLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_role_log
     *
     * @mbggenerated
     */
    int insertSelective(CuntaoUserRoleLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_role_log
     *
     * @mbggenerated
     */
    List<CuntaoUserRoleLog> selectByExample(CuntaoUserRoleLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_role_log
     *
     * @mbggenerated
     */
    CuntaoUserRoleLog selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_role_log
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CuntaoUserRoleLog record, @Param("example") CuntaoUserRoleLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_role_log
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CuntaoUserRoleLog record, @Param("example") CuntaoUserRoleLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_role_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CuntaoUserRoleLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_user_role_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CuntaoUserRoleLog record);
}