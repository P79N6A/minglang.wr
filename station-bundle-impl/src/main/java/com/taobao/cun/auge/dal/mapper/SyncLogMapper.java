package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import com.taobao.cun.auge.dal.domain.SyncLog;
import com.taobao.cun.auge.dal.domain.SyncLogExample;
import org.apache.ibatis.annotations.Param;

public interface SyncLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sync_log
     *
     * @mbggenerated
     */
    int countByExample(SyncLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sync_log
     *
     * @mbggenerated
     */
    int deleteByExample(SyncLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sync_log
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sync_log
     *
     * @mbggenerated
     */
    int insert(SyncLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sync_log
     *
     * @mbggenerated
     */
    int insertSelective(SyncLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sync_log
     *
     * @mbggenerated
     */
    List<SyncLog> selectByExampleWithBLOBs(SyncLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sync_log
     *
     * @mbggenerated
     */
    List<SyncLog> selectByExample(SyncLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sync_log
     *
     * @mbggenerated
     */
    SyncLog selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sync_log
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") SyncLog record, @Param("example") SyncLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sync_log
     *
     * @mbggenerated
     */
    int updateByExampleWithBLOBs(@Param("record") SyncLog record, @Param("example") SyncLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sync_log
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") SyncLog record, @Param("example") SyncLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sync_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SyncLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sync_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(SyncLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sync_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SyncLog record);

	void clearAll();
}