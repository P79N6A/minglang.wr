package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.Protocol;
import com.taobao.cun.auge.dal.domain.ProtocolExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProtocolMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int countByExample(ProtocolExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int deleteByExample(ProtocolExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int insert(Protocol record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int insertSelective(Protocol record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    List<Protocol> selectByExample(ProtocolExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    Protocol selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int updateByExampleSelective(@Param("record") Protocol record, @Param("example") ProtocolExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int updateByExample(@Param("record") Protocol record, @Param("example") ProtocolExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int updateByPrimaryKeySelective(Protocol record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbggenerated Mon May 30 17:14:20 CST 2016
     */
    int updateByPrimaryKey(Protocol record);
}