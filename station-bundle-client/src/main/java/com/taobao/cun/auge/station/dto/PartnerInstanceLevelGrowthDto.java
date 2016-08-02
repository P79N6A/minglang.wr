package com.taobao.cun.auge.station.dto;

public class PartnerInstanceLevelGrowthDto {
	/**
	 * 村点id
	 */
	private Long stationId;
	/**
	 * 用户淘宝id
	 */
	private Long taobaoUserId;
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
	 * app收入占比排名
	 */
	private Long appIncomePercentRank;

	/**
	 * 购买村全国民数
	 */
	private Double buyVillagerCnt;
	/**
	 * 购买村民数得分
	 */
	private Double buyVillagerCntScore;
	/**
	 * 购买村民数全国排名
	 */
	private Double buyVillagerCntRank;

	/**
	 * 人均购买次数
	 */
	private Double avgBuyTimes;
	/**
	 * 人均购买次数得分
	 */
	private Double avgBuyTimesScore;
	/**
	 * 人均购买次数全国排名
	 */
	private Double avgBuyTimesRank;

	/**
	 * 新增app绑定量
	 */
	private Double newAppBindingCnt;
	/**
	 * 新增app绑定量得分
	 */
	private Double newAppBindingCntScore;
	/**
	 * 新增app绑定量全国排名
	 */
	private Double newAppBindingCntRank;

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

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
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

	public Long getAppIncomePercentRank() {
		return appIncomePercentRank;
	}

	public void setAppIncomePercentRank(Long appIncomePercentRank) {
		this.appIncomePercentRank = appIncomePercentRank;
	}

	public Double getBuyVillagerCnt() {
		return buyVillagerCnt;
	}

	public void setBuyVillagerCnt(Double buyVillagerCnt) {
		this.buyVillagerCnt = buyVillagerCnt;
	}

	public Double getBuyVillagerCntScore() {
		return buyVillagerCntScore;
	}

	public void setBuyVillagerCntScore(Double buyVillagerCntScore) {
		this.buyVillagerCntScore = buyVillagerCntScore;
	}

	public Double getBuyVillagerCntRank() {
		return buyVillagerCntRank;
	}

	public void setBuyVillagerCntRank(Double buyVillagerCntRank) {
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

	public Double getAvgBuyTimesRank() {
		return avgBuyTimesRank;
	}

	public void setAvgBuyTimesRank(Double avgBuyTimesRank) {
		this.avgBuyTimesRank = avgBuyTimesRank;
	}

	public Double getNewAppBindingCnt() {
		return newAppBindingCnt;
	}

	public void setNewAppBindingCnt(Double newAppBindingCnt) {
		this.newAppBindingCnt = newAppBindingCnt;
	}

	public Double getNewAppBindingCntScore() {
		return newAppBindingCntScore;
	}

	public void setNewAppBindingCntScore(Double newAppBindingCntScore) {
		this.newAppBindingCntScore = newAppBindingCntScore;
	}

	public Double getNewAppBindingCntRank() {
		return newAppBindingCntRank;
	}

	public void setNewAppBindingCntRank(Double newAppBindingCntRank) {
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
