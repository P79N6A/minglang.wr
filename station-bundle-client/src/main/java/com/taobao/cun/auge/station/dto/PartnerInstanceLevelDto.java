package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.taobao.cun.auge.event.enums.PartnerInstanceLevelEnum;

public class PartnerInstanceLevelDto implements Serializable {
	
	
	
	  private Long partnerInstanceId;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.station_id
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private Long stationId;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.taobao_user_id
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private Long taobaoUserId;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.county_org_id
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private Long countyOrgId;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.score
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private BigDecimal score;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.monthly_income
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private BigDecimal monthlyIncome;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.monthly_income_score
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private BigDecimal monthlyIncomeScore;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.goods_1_gmv
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private BigDecimal goods1Gmv;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.goods_1_gmv_score
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private BigDecimal goods1GmvScore;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.app_income_percent
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private BigDecimal appIncomePercent;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.app_income_percent_score
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private BigDecimal appIncomePercentScore;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.buy_villager_cnt
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private BigDecimal buyVillagerCnt;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.buy_villager_cnt_score
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private BigDecimal buyVillagerCntScore;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.avg_buy_times
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private BigDecimal avgBuyTimes;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.avg_buy_times_score
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private BigDecimal avgBuyTimesScore;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.new_app_binding_cnt
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private BigDecimal newAppBindingCnt;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.new_app_binding_cnt_score
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private BigDecimal newAppBindingCntScore;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.current_level
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private PartnerInstanceLevelEnum currentLevel;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.evaluate_by
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private String evaluateBy;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.pre_level
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private PartnerInstanceLevelEnum preLevel;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.expected_level
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private PartnerInstanceLevelEnum expectedLevel;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.evaluate_date
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private Date evaluateDate;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.last_evaluate_date
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private Date lastEvaluateDate;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.next_evaluate_date
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private Date nextEvaluateDate;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.county_rank
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private Long countyRank;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.county_partner_instance_cnt
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private Long countyPartnerInstanceCnt;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.country_rank
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private Long countryRank;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column partner_instance_level.country_partner_instance_cnt
	     *
	     * @mbggenerated Wed Jul 27 19:29:36 CST 2016
	     */
	    private Long countryPartnerInstanceCnt;


}
