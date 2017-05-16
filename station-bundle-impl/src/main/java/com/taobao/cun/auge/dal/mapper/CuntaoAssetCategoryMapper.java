package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.CuntaoAssetCategory;
import com.taobao.cun.auge.dal.domain.CuntaoAssetCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CuntaoAssetCategoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset_category
     *
     * @mbggenerated
     */
    int countByExample(CuntaoAssetCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset_category
     *
     * @mbggenerated
     */
    int deleteByExample(CuntaoAssetCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset_category
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset_category
     *
     * @mbggenerated
     */
    int insert(CuntaoAssetCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset_category
     *
     * @mbggenerated
     */
    int insertSelective(CuntaoAssetCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset_category
     *
     * @mbggenerated
     */
    List<CuntaoAssetCategory> selectByExample(CuntaoAssetCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset_category
     *
     * @mbggenerated
     */
    CuntaoAssetCategory selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset_category
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CuntaoAssetCategory record, @Param("example") CuntaoAssetCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset_category
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CuntaoAssetCategory record, @Param("example") CuntaoAssetCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset_category
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CuntaoAssetCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cuntao_asset_category
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CuntaoAssetCategory record);
}