package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.BizActionLog;
import com.taobao.cun.auge.dal.domain.BizActionLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BizActionLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_action_log
     *
     * @mbggenerated
     */
    int countByExample(BizActionLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_action_log
     *
     * @mbggenerated
     */
    int deleteByExample(BizActionLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_action_log
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_action_log
     *
     * @mbggenerated
     */
    int insert(BizActionLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_action_log
     *
     * @mbggenerated
     */
    int insertSelective(BizActionLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_action_log
     *
     * @mbggenerated
     */
    List<BizActionLog> selectByExample(BizActionLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_action_log
     *
     * @mbggenerated
     */
    BizActionLog selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_action_log
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") BizActionLog record, @Param("example") BizActionLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_action_log
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") BizActionLog record, @Param("example") BizActionLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_action_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(BizActionLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_action_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(BizActionLog record);
}