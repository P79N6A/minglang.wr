package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import com.taobao.cun.auge.dal.domain.CuntaoAsset;
import com.taobao.cun.auge.dal.domain.CuntaoAssetExample;
import org.apache.ibatis.annotations.Param;

public interface CuntaoAssetMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset
     *
     * @mbggenerated
     */
    int countByExample(CuntaoAssetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset
     *
     * @mbggenerated
     */
    int deleteByExample(CuntaoAssetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset
     *
     * @mbggenerated
     */
    int insert(CuntaoAsset record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset
     *
     * @mbggenerated
     */
    int insertSelective(CuntaoAsset record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset
     *
     * @mbggenerated
     */
    List<CuntaoAsset> selectByExample(CuntaoAssetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset
     *
     * @mbggenerated
     */
    CuntaoAsset selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CuntaoAsset record, @Param("example") CuntaoAssetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CuntaoAsset record, @Param("example") CuntaoAssetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CuntaoAsset record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CuntaoAsset record);
}