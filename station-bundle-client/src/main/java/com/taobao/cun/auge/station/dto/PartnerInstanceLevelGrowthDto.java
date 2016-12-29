package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;

public class PartnerInstanceLevelGrowthDto implements Serializable {

	private static final long serialVersionUID = -2876484522466697652L;

	/**
	 * 统计日期
	 */
	private String statDate;

	/**
	 * 统计开始日期
	 */
	private Date statStartDate;
	/**
	 * 统计截止日期
	 * 
	 */
	private Date statEndDate;

	/**
	 * 村点id
	 */
	private Long stationId;

	/**
	 * 分数
	 */
	private Double score;

	/**
	 * 月收入
	 */
	private Double monthlyIncome;
	/**
	 * 月收入得分
	 */
	private Double monthlyIncomeScore;
	/**
	 * 上次评定月收入得分
	 */
	private Double lastMonthlyIncomeScore;
	/**
	 * 月收入全国排名
	 */
	private Long monthlyIncomeRank;

	/**
	 * 1.0商品GMV
	 */
	private Double goods1Gmv;
	/**
	 * 1.0商品GMV得分
	 */
	private Double goods1GmvScore;
	/**
	 * 上次评定1.0商品GMV得分
	 */
	private Double lastGoods1GmvScore;
	/**
	 * 1.0商品GMV全国排名
	 */
	private Long goods1GmvRank;

	/**
	 * app收入占比
	 */
	private Double appIncomePercent;
	/**
	 * app收入占比得分
	 */
	private Double appIncomePercentScore;
	/**
	 * 上次评定app收入占比得分
	 */
	private Double lastAppIncomePercentScore;
	/**
	 * app收入占比排名
	 */
	private Long appIncomePercentRank;

	/**
	 * 购买村全国民数
	 */
	private Long buyVillagerCnt;
	/**
	 * 购买村民数得分
	 */
	private Double buyVillagerCntScore;
	/**
	 * 上次购买村民数得分
	 */
	private Double lastBuyVillagerCntScore;
	/**
	 * 购买村民数全国排名
	 */
	private Long buyVillagerCntRank;

	/**
	 * 人均购买次数
	 */
	private Double avgBuyTimes;
	/**
	 * 人均购买次数得分
	 */
	private Double avgBuyTimesScore;
	/**
	 * 上次评定人均购买次数得分
	 */
	private Double lastAvgBuyTimesScore;
	/**
	 * 人均购买次数全国排名
	 */
	private Long avgBuyTimesRank;

	/**
	 * 新增app绑定量
	 */
	private Long newAppBindingCnt;
	/**
	 * 新增app绑定量得分
	 */
	private Double newAppBindingCntScore;
	/**
	 * 上次评定新增app绑定量得分
	 */
	private Double lastNewAppBindingCntScore;
	/**
	 * 新增app绑定量全国排名
	 */
	private Long newAppBindingCntRank;

	/**
	 * 县排名
	 */
	private Long countyRank;
	/**
	 * 县合伙人总数
	 */
	private Long countyPartnerInstanceCnt;

	/**
	 * 全国排名
	 */
	private Long countryRank;
	/**
	 * 全国合伙人总数
	 */
	private Long countryPartnerInstanceCnt;

	public String getStatDate() {
		return statDate;
	}

	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}

	public Date getStatStartDate() {
		return statStartDate;
	}

	public void setStatStartDate(Date statStartDate) {
		this.statStartDate = statStartDate;
	}

	public Date getStatEndDate() {
		return statEndDate;
	}

	public void setStatEndDate(Date statEndDate) {
		this.statEndDate = statEndDate;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Double getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(Double monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public Double getMonthlyIncomeScore() {
		return monthlyIncomeScore;
	}

	public void setMonthlyIncomeScore(Double monthlyIncomeScore) {
		this.monthlyIncomeScore = monthlyIncomeScore;
	}

	public Double getLastMonthlyIncomeScore() {
		return lastMonthlyIncomeScore;
	}

	public void setLastMonthlyIncomeScore(Double lastMonthlyIncomeScore) {
		this.lastMonthlyIncomeScore = lastMonthlyIncomeScore;
	}

	public Long getMonthlyIncomeRank() {
		return monthlyIncomeRank;
	}

	public void setMonthlyIncomeRank(Long monthlyIncomeRank) {
		this.monthlyIncomeRank = monthlyIncomeRank;
	}

	public Double getGoods1Gmv() {
		return goods1Gmv;
	}

	public void setGoods1Gmv(Double goods1Gmv) {
		this.goods1Gmv = goods1Gmv;
	}

	public Double getGoods1GmvScore() {
		return goods1GmvScore;
	}

	public void setGoods1GmvScore(Double goods1GmvScore) {
		this.goods1GmvScore = goods1GmvScore;
	}

	public Double getLastGoods1GmvScore() {
		return lastGoods1GmvScore;
	}

	public void setLastGoods1GmvScore(Double lastGoods1GmvScore) {
		this.lastGoods1GmvScore = lastGoods1GmvScore;
	}

	public Long getGoods1GmvRank() {
		return goods1GmvRank;
	}

	public void setGoods1GmvRank(Long goods1GmvRank) {
		this.goods1GmvRank = goods1GmvRank;
	}

	public Double getAppIncomePercent() {
		return appIncomePercent;
	}

	public void setAppIncomePercent(Double appIncomePercent) {
		this.appIncomePercent = appIncomePercent;
	}

	public Double getAppIncomePercentScore() {
		return appIncomePercentScore;
	}

	public void setAppIncomePercentScore(Double appIncomePercentScore) {
		this.appIncomePercentScore = appIncomePercentScore;
	}

	public Double getLastAppIncomePercentScore() {
		return lastAppIncomePercentScore;
	}

	public void setLastAppIncomePercentScore(Double lastAppIncomePercentScore) {
		this.lastAppIncomePercentScore = lastAppIncomePercentScore;
	}

	public Long getAppIncomePercentRank() {
		return appIncomePercentRank;
	}

	public void setAppIncomePercentRank(Long appIncomePercentRank) {
		this.appIncomePercentRank = appIncomePercentRank;
	}

	public Long getBuyVillagerCnt() {
		return buyVillagerCnt;
	}

	public void setBuyVillagerCnt(Long buyVillagerCnt) {
		this.buyVillagerCnt = buyVillagerCnt;
	}

	public Double getBuyVillagerCntScore() {
		return buyVillagerCntScore;
	}

	public void setBuyVillagerCntScore(Double buyVillagerCntScore) {
		this.buyVillagerCntScore = buyVillagerCntScore;
	}

	public Double getLastBuyVillagerCntScore() {
		return lastBuyVillagerCntScore;
	}

	public void setLastBuyVillagerCntScore(Double lastBuyVillagerCntScore) {
		this.lastBuyVillagerCntScore = lastBuyVillagerCntScore;
	}

	public Long getBuyVillagerCntRank() {
		return buyVillagerCntRank;
	}

	public void setBuyVillagerCntRank(Long buyVillagerCntRank) {
		this.buyVillagerCntRank = buyVillagerCntRank;
	}

	public Double getAvgBuyTimes() {
		return avgBuyTimes;
	}

	public void setAvgBuyTimes(Double avgBuyTimes) {
		this.avgBuyTimes = avgBuyTimes;
	}

	public Double getAvgBuyTimesScore() {
		return avgBuyTimesScore;
	}

	public void setAvgBuyTimesScore(Double avgBuyTimesScore) {
		this.avgBuyTimesScore = avgBuyTimesScore;
	}

	public Double getLastAvgBuyTimesScore() {
		return lastAvgBuyTimesScore;
	}

	public void setLastAvgBuyTimesScore(Double lastAvgBuyTimesScore) {
		this.lastAvgBuyTimesScore = lastAvgBuyTimesScore;
	}

	public Long getAvgBuyTimesRank() {
		return avgBuyTimesRank;
	}

	public void setAvgBuyTimesRank(Long avgBuyTimesRank) {
		this.avgBuyTimesRank = avgBuyTimesRank;
	}

	public Long getNewAppBindingCnt() {
		return newAppBindingCnt;
	}

	public void setNewAppBindingCnt(Long newAppBindingCnt) {
		this.newAppBindingCnt = newAppBindingCnt;
	}

	public Double getNewAppBindingCntScore() {
		return newAppBindingCntScore;
	}

	public void setNewAppBindingCntScore(Double newAppBindingCntScore) {
		this.newAppBindingCntScore = newAppBindingCntScore;
	}

	public Double getLastNewAppBindingCntScore() {
		return lastNewAppBindingCntScore;
	}

	public void setLastNewAppBindingCntScore(Double lastNewAppBindingCntScore) {
		this.lastNewAppBindingCntScore = lastNewAppBindingCntScore;
	}

	public Long getNewAppBindingCntRank() {
		return newAppBindingCntRank;
	}

	public void setNewAppBindingCntRank(Long newAppBindingCntRank) {
		this.newAppBindingCntRank = newAppBindingCntRank;
	}

	public Long getCountyRank() {
		return countyRank;
	}

	public void setCountyRank(Long countyRank) {
		this.countyRank = countyRank;
	}

	public Long getCountyPartnerInstanceCnt() {
		return countyPartnerInstanceCnt;
	}

	public void setCountyPartnerInstanceCnt(Long countyPartnerInstanceCnt) {
		this.countyPartnerInstanceCnt = countyPartnerInstanceCnt;
	}

	public Long getCountryRank() {
		return countryRank;
	}

	public void setCountryRank(Long countryRank) {
		this.countryRank = countryRank;
	}

	public Long getCountryPartnerInstanceCnt() {
		return countryPartnerInstanceCnt;
	}

	public void setCountryPartnerInstanceCnt(Long countryPartnerInstanceCnt) {
		this.countryPartnerInstanceCnt = countryPartnerInstanceCnt;
	}

}
