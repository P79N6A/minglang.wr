package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class PartnerInstanceLevelGrowthTrendDto implements Serializable {

	private static final long serialVersionUID = 462091570592784309L;
	/**
	 * 日期
	 */
	private String statDate;
	/**
	 * 日收入
	 */
	private Double dailyIncome;
	/**
	 * 1.0商品GMV
	 */
	private Double dailyGoods1Gmv;

	/**
	 * app收入占比
	 */
	private Double dailyAppIncomePercent;

	/**
	 * 购买村全国民数
	 */
	private Long dailyBuyVillagerCnt;

	/**
	 * 人均购买次数
	 */
	private Double dailyAvgBuyTimes;

	/**
	 * 新增app绑定量
	 */
	private Long dailyNewAppBindingCnt;

	public String getStatDate() {
		return statDate;
	}

	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}

	public Double getDailyIncome() {
		return dailyIncome;
	}

	public void setDailyIncome(Double dailyIncome) {
		this.dailyIncome = dailyIncome;
	}

	public Double getDailyGoods1Gmv() {
		return dailyGoods1Gmv;
	}

	public void setDailyGoods1Gmv(Double dailyGoods1Gmv) {
		this.dailyGoods1Gmv = dailyGoods1Gmv;
	}

	public Double getDailyAppIncomePercent() {
		return dailyAppIncomePercent;
	}

	public void setDailyAppIncomePercent(Double dailyAppIncomePercent) {
		this.dailyAppIncomePercent = dailyAppIncomePercent;
	}

	public Long getDailyBuyVillagerCnt() {
		return dailyBuyVillagerCnt;
	}

	public void setDailyBuyVillagerCnt(Long dailyBuyVillagerCnt) {
		this.dailyBuyVillagerCnt = dailyBuyVillagerCnt;
	}

	public Double getDailyAvgBuyTimes() {
		return dailyAvgBuyTimes;
	}

	public void setDailyAvgBuyTimes(Double dailyAvgBuyTimes) {
		this.dailyAvgBuyTimes = dailyAvgBuyTimes;
	}

	public Long getDailyNewAppBindingCnt() {
		return dailyNewAppBindingCnt;
	}

	public void setDailyNewAppBindingCnt(Long dailyNewAppBindingCnt) {
		this.dailyNewAppBindingCnt = dailyNewAppBindingCnt;
	}

}
