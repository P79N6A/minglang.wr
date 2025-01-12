package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.dal.domain.FenceEntityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FenceEntityMapper {
	/**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fence_entity
     *
     * @mbggenerated
     */
    int countByExample(FenceEntityExample example);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fence_entity
     *
     * @mbggenerated
     */
    int deleteByExample(FenceEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fence_entity
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fence_entity
     *
     * @mbggenerated
     */
    int insert(FenceEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fence_entity
     *
     * @mbggenerated
     */
    int insertSelective(FenceEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fence_entity
     *
     * @mbggenerated
     */
    List<FenceEntity> selectByExampleWithBLOBs(FenceEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fence_entity
     *
     * @mbggenerated
     */
    List<FenceEntity> selectByExample(FenceEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fence_entity
     *
     * @mbggenerated
     */
    FenceEntity selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fence_entity
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") FenceEntity record, @Param("example") FenceEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fence_entity
     *
     * @mbggenerated
     */
    int updateByExampleWithBLOBs(@Param("record") FenceEntity record, @Param("example") FenceEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fence_entity
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") FenceEntity record, @Param("example") FenceEntityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fence_entity
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(FenceEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fence_entity
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(FenceEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fence_entity
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(FenceEntity record);
}