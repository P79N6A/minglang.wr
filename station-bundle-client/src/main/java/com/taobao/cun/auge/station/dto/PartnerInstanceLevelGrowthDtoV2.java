package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;

public class PartnerInstanceLevelGrowthDtoV2 implements Serializable {

    private static final long serialVersionUID = -8157263646689962620L;

    /**
     * 统计日期
     */
    private String            statDate;

    /**
     * 统计开始日期
     */
    private Date              statStartDate;
    /**
     * 统计截止日期
     */
    private Date              statEndDate;

    /**
     * 村点id
     */
    private Long              stationId;

    /**
     * 分数
     */
    private Double            score;

    /**
     * 月收入
     */
    private Double            monthlyIncome;
    /**
     * 月收入得分
     */
    private Double            monthlyIncomeScore;
    /**
     * 上次评定月收入得分
     */
    private Double            lastMonthlyIncomeScore;
    /**
     * 月收入全国排名
     */
    private Long              monthlyIncomeRank;

    /**
     * 1.0商品GMV占比全国排名
     */
    private Double            goods1GmvRatio;
    private Long              goods1GmvRatioRank;
    private Double            goods1GmvRatioScore;
    private Double            lastGoods1GmvRatioScore;

    /**
     * app活跃村民使用数
     */
    private Double              activeAppUserCnt;
    private Long              activeAppUserCntRank;
    private Double            activeAppUserCntScore;
    private Double            lastActiveAppUserCntScore;

    /**
     * 忠实村民数
     */
    private Long              loyaltyVillagerCnt;
    private Long              loyaltyVillagerCntRank;
    private Double            loyaltyVillagerCntScore;
    private Double            lastLoyaltyVillagerCntScore;

    /**
     * 复购率
     */
    private Double            repurchaseRate;
    private Long              repurchaseRateRank;
    private Double            repurchaseRateScore;
    private Double            lastRepurchaseRateScore;

    /**
     * 访问商品数
     */
    private Long              visitedProductCnt;
    private Long              visitedProductCntRank;
    private Double            visitedProductCntScore;
    private Double            lastVisitedProductCntScore;

    /**
     * 县排名
     */
    private Long              countyRank;
    /**
     * 县合伙人总数
     */
    private Long              countyPartnerInstanceCnt;

    /**
     * 全国排名
     */
    private Long              countryRank;
    /**
     * 全国合伙人总数
     */
    private Long              countryPartnerInstanceCnt;

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

    public Long getGoods1GmvRatioRank() {
        return goods1GmvRatioRank;
    }

    public void setGoods1GmvRatioRank(Long goods1GmvRatioRank) {
        this.goods1GmvRatioRank = goods1GmvRatioRank;
    }

    public Double getGoods1GmvRatio() {
        return goods1GmvRatio;
    }

    public void setGoods1GmvRatio(Double goods1GmvRatio) {
        this.goods1GmvRatio = goods1GmvRatio;
    }

    public Double getGoods1GmvRatioScore() {
        return goods1GmvRatioScore;
    }

    public void setGoods1GmvRatioScore(Double goods1GmvRatioScore) {
        this.goods1GmvRatioScore = goods1GmvRatioScore;
    }

    public Double getLastGoods1GmvRatioScore() {
        return lastGoods1GmvRatioScore;
    }

    public void setLastGoods1GmvRatioScore(Double lastGoods1GmvRatioScore) {
        this.lastGoods1GmvRatioScore = lastGoods1GmvRatioScore;
    }

    public Long getActiveAppUserCntRank() {
        return activeAppUserCntRank;
    }

    public void setActiveAppUserCntRank(Long activeAppUserCntRank) {
        this.activeAppUserCntRank = activeAppUserCntRank;
    }

    public Double getActiveAppUserCntScore() {
        return activeAppUserCntScore;
    }

    public void setActiveAppUserCntScore(Double activeAppUserCntScore) {
        this.activeAppUserCntScore = activeAppUserCntScore;
    }

    public Double getLastActiveAppUserCntScore() {
        return lastActiveAppUserCntScore;
    }

    public void setLastActiveAppUserCntScore(Double lastActiveAppUserCntScore) {
        this.lastActiveAppUserCntScore = lastActiveAppUserCntScore;
    }

    public Long getLoyaltyVillagerCntRank() {
        return loyaltyVillagerCntRank;
    }

    public void setLoyaltyVillagerCntRank(Long loyaltyVillagerCntRank) {
        this.loyaltyVillagerCntRank = loyaltyVillagerCntRank;
    }

    public Double getLoyaltyVillagerCntScore() {
        return loyaltyVillagerCntScore;
    }

    public void setLoyaltyVillagerCntScore(Double loyaltyVillagerCntScore) {
        this.loyaltyVillagerCntScore = loyaltyVillagerCntScore;
    }

    public Double getLastLoyaltyVillagerCntScore() {
        return lastLoyaltyVillagerCntScore;
    }

    public void setLastLoyaltyVillagerCntScore(Double lastLoyaltyVillagerCntScore) {
        this.lastLoyaltyVillagerCntScore = lastLoyaltyVillagerCntScore;
    }

    public Double getRepurchaseRate() {
        return repurchaseRate;
    }

    public void setRepurchaseRate(Double repurchaseRate) {
        this.repurchaseRate = repurchaseRate;
    }

    public Long getRepurchaseRateRank() {
        return repurchaseRateRank;
    }

    public void setRepurchaseRateRank(Long repurchaseRateRank) {
        this.repurchaseRateRank = repurchaseRateRank;
    }

    public Double getRepurchaseRateScore() {
        return repurchaseRateScore;
    }

    public void setRepurchaseRateScore(Double repurchaseRateScore) {
        this.repurchaseRateScore = repurchaseRateScore;
    }

    public Double getLastRepurchaseRateScore() {
        return lastRepurchaseRateScore;
    }

    public void setLastRepurchaseRateScore(Double lastRepurchaseRateScore) {
        this.lastRepurchaseRateScore = lastRepurchaseRateScore;
    }

    public Long getVisitedProductCntRank() {
        return visitedProductCntRank;
    }

    public void setVisitedProductCntRank(Long visitedProductCntRank) {
        this.visitedProductCntRank = visitedProductCntRank;
    }

    public Double getVisitedProductCntScore() {
        return visitedProductCntScore;
    }

    public void setVisitedProductCntScore(Double visitedProductCntScore) {
        this.visitedProductCntScore = visitedProductCntScore;
    }

    public Double getLastVisitedProductCntScore() {
        return lastVisitedProductCntScore;
    }

    public void setLastVisitedProductCntScore(Double lastVisitedProductCntScore) {
        this.lastVisitedProductCntScore = lastVisitedProductCntScore;
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

    public Double getActiveAppUserCnt() {
        return activeAppUserCnt;
    }

    public void setActiveAppUserCnt(Double activeAppUserCnt) {
        this.activeAppUserCnt = activeAppUserCnt;
    }

    public Long getLoyaltyVillagerCnt() {
        return loyaltyVillagerCnt;
    }

    public void setLoyaltyVillagerCnt(Long loyaltyVillagerCnt) {
        this.loyaltyVillagerCnt = loyaltyVillagerCnt;
    }

    public Long getVisitedProductCnt() {
        return visitedProductCnt;
    }

    public void setVisitedProductCnt(Long visitedProductCnt) {
        this.visitedProductCnt = visitedProductCnt;
    }

}
