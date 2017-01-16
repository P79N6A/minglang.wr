package com.taobao.cun.auge.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.taobao.cun.auge.dal.domain.PeixunPurchase;
import com.taobao.cun.auge.dal.domain.PeixunPurchaseExample;
import com.taobao.cun.auge.station.dto.PeixunPurchaseDto;

public interface PeixunPurchaseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table peixun_purchase
     *
     * @mbggenerated Mon Nov 21 16:20:21 CST 2016
     */
    int countByExample(PeixunPurchaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table peixun_purchase
     *
     * @mbggenerated Mon Nov 21 16:20:21 CST 2016
     */
    int deleteByExample(PeixunPurchaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table peixun_purchase
     *
     * @mbggenerated Mon Nov 21 16:20:21 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table peixun_purchase
     *
     * @mbggenerated Mon Nov 21 16:20:21 CST 2016
     */
    int insert(PeixunPurchase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table peixun_purchase
     *
     * @mbggenerated Mon Nov 21 16:20:21 CST 2016
     */
    int insertSelective(PeixunPurchase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table peixun_purchase
     *
     * @mbggenerated Mon Nov 21 16:20:21 CST 2016
     */
    List<PeixunPurchase> selectByExample(PeixunPurchaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table peixun_purchase
     *
     * @mbggenerated Mon Nov 21 16:20:21 CST 2016
     */
    PeixunPurchase selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table peixun_purchase
     *
     * @mbggenerated Mon Nov 21 16:20:21 CST 2016
     */
    int updateByExampleSelective(@Param("record") PeixunPurchase record, @Param("example") PeixunPurchaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table peixun_purchase
     *
     * @mbggenerated Mon Nov 21 16:20:21 CST 2016
     */
    int updateByExample(@Param("record") PeixunPurchase record, @Param("example") PeixunPurchaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table peixun_purchase
     *
     * @mbggenerated Mon Nov 21 16:20:21 CST 2016
     */
    int updateByPrimaryKeySelective(PeixunPurchase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table peixun_purchase
     *
     * @mbggenerated Mon Nov 21 16:20:21 CST 2016
     */
    int updateByPrimaryKey(PeixunPurchase record);
    
    List<PeixunPurchaseDto> queryListByCondition(Map<String,Object> param);
    int queryPurchaseListCount(Map<String,Object> param);

}