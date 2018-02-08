package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.cun.auge.dal.domain.AssetExample;
import org.apache.ibatis.annotations.Param;

public interface AssetMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset
     *
     * @mbggenerated
     */
    int countByExample(AssetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset
     *
     * @mbggenerated
     */
    int deleteByExample(AssetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset
     *
     * @mbggenerated
     */
    int insert(Asset record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset
     *
     * @mbggenerated
     */
    int insertSelective(Asset record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset
     *
     * @mbggenerated
     */
    List<Asset> selectByExample(AssetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset
     *
     * @mbggenerated
     */
    Asset selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") Asset record, @Param("example") AssetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") Asset record, @Param("example") AssetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Asset record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Asset record);
}