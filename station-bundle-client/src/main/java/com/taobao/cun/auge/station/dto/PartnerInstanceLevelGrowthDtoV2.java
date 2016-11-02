package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class PartnerInstanceLevelGrowthDtoV2 implements Serializable {

    private static final long serialVersionUID = -8157263646689962620L;
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
    private BigDecimal        goods1GmvRatio;
    private Long              goods1GmvRatioRank;
    private BigDecimal        goods1GmvRatioScore;
    private BigDecimal        lastGoods1GmvRatioScore;

    /**
     * app活跃村民使用数
     */
    private BigDecimal        activeAppUserCnt;
    private Long              activeAppUserCntRank;
    private BigDecimal        activeAppUserCntScore;
    private BigDecimal        lastActiveAppUserCntScore;

    /**
     * 忠实村民数
     */
    private BigDecimal        loyaltyVillagerCnt;
    private Long              loyaltyVillagerCntRank;
    private BigDecimal        loyaltyVillagerCntScore;
    private BigDecimal        lastLoyaltyVillagerCntScore;

    /**
     * 复购率
     */
    private BigDecimal        repurchaseRate;
    private Long              repurchaseRateRank;
    private BigDecimal        repurchaseRateScore;
    private BigDecimal        lastRepurchaseRateScore;

    /**
     * 访问商品数
     */
    private BigDecimal        visitedProductCnt;
    private Long              visitedProductCntRank;
    private BigDecimal        visitedProductCntScore;
    private BigDecimal        lastVisitedProductCntScore;

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

    public BigDecimal getGoods1GmvRatio() {
        return goods1GmvRatio;
    }

    public void setGoods1GmvRatio(BigDecimal goods1GmvRatio) {
        this.goods1GmvRatio = goods1GmvRatio;
    }

    public BigDecimal getGoods1GmvRatioScore() {
        return goods1GmvRatioScore;
    }

    public void setGoods1GmvRatioScore(BigDecimal goods1GmvRatioScore) {
        this.goods1GmvRatioScore = goods1GmvRatioScore;
    }

    public BigDecimal getLastGoods1GmvRatioScore() {
        return lastGoods1GmvRatioScore;
    }

    public void setLastGoods1GmvRatioScore(BigDecimal lastGoods1GmvRatioScore) {
        this.lastGoods1GmvRatioScore = lastGoods1GmvRatioScore;
    }

    public BigDecimal getActiveAppUserCnt() {
        return activeAppUserCnt;
    }

    public void setActiveAppUserCnt(BigDecimal activeAppUserCnt) {
        this.activeAppUserCnt = activeAppUserCnt;
    }

    public Long getActiveAppUserCntRank() {
        return activeAppUserCntRank;
    }

    public void setActiveAppUserCntRank(Long activeAppUserCntRank) {
        this.activeAppUserCntRank = activeAppUserCntRank;
    }

    public BigDecimal getActiveAppUserCntScore() {
        return activeAppUserCntScore;
    }

    public void setActiveAppUserCntScore(BigDecimal activeAppUserCntScore) {
        this.activeAppUserCntScore = activeAppUserCntScore;
    }

    public BigDecimal getLastActiveAppUserCntScore() {
        return lastActiveAppUserCntScore;
    }

    public void setLastActiveAppUserCntScore(BigDecimal lastActiveAppUserCntScore) {
        this.lastActiveAppUserCntScore = lastActiveAppUserCntScore;
    }

    public BigDecimal getLoyaltyVillagerCnt() {
        return loyaltyVillagerCnt;
    }

    public void setLoyaltyVillagerCnt(BigDecimal loyaltyVillagerCnt) {
        this.loyaltyVillagerCnt = loyaltyVillagerCnt;
    }

    public Long getLoyaltyVillagerCntRank() {
        return loyaltyVillagerCntRank;
    }

    public void setLoyaltyVillagerCntRank(Long loyaltyVillagerCntRank) {
        this.loyaltyVillagerCntRank = loyaltyVillagerCntRank;
    }

    public BigDecimal getLoyaltyVillagerCntScore() {
        return loyaltyVillagerCntScore;
    }

    public void setLoyaltyVillagerCntScore(BigDecimal loyaltyVillagerCntScore) {
        this.loyaltyVillagerCntScore = loyaltyVillagerCntScore;
    }

    public BigDecimal getLastLoyaltyVillagerCntScore() {
        return lastLoyaltyVillagerCntScore;
    }

    public void setLastLoyaltyVillagerCntScore(BigDecimal lastLoyaltyVillagerCntScore) {
        this.lastLoyaltyVillagerCntScore = lastLoyaltyVillagerCntScore;
    }

    public BigDecimal getRepurchaseRate() {
        return repurchaseRate;
    }

    public void setRepurchaseRate(BigDecimal repurchaseRate) {
        this.repurchaseRate = repurchaseRate;
    }

    public Long getRepurchaseRateRank() {
        return repurchaseRateRank;
    }

    public void setRepurchaseRateRank(Long repurchaseRateRank) {
        this.repurchaseRateRank = repurchaseRateRank;
    }

    public BigDecimal getRepurchaseRateScore() {
        return repurchaseRateScore;
    }

    public void setRepurchaseRateScore(BigDecimal repurchaseRateScore) {
        this.repurchaseRateScore = repurchaseRateScore;
    }

    public BigDecimal getLastRepurchaseRateScore() {
        return lastRepurchaseRateScore;
    }

    public void setLastRepurchaseRateScore(BigDecimal lastRepurchaseRateScore) {
        this.lastRepurchaseRateScore = lastRepurchaseRateScore;
    }

    public BigDecimal getVisitedProductCnt() {
        return visitedProductCnt;
    }

    public void setVisitedProductCnt(BigDecimal visitedProductCnt) {
        this.visitedProductCnt = visitedProductCnt;
    }

    public Long getVisitedProductCntRank() {
        return visitedProductCntRank;
    }

    public void setVisitedProductCntRank(Long visitedProductCntRank) {
        this.visitedProductCntRank = visitedProductCntRank;
    }

    public BigDecimal getVisitedProductCntScore() {
        return visitedProductCntScore;
    }

    public void setVisitedProductCntScore(BigDecimal visitedProductCntScore) {
        this.visitedProductCntScore = visitedProductCntScore;
    }

    public BigDecimal getLastVisitedProductCntScore() {
        return lastVisitedProductCntScore;
    }

    public void setLastVisitedProductCntScore(BigDecimal lastVisitedProductCntScore) {
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

}
