package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

/**
 * 最近一天各个指标的数据:如最近一天的收入,最近一天的app活跃用户数等 类PartnerInstanceLevelGrowthTrendDtoV2.java的实现描述：趋势数据模型:统计的是最近一天的数据,不是月均数据.
 * 
 * @author xujianhui 2016年11月4日 下午2:51:02
 */
public class PartnerInstanceLevelGrowthTrendDtoV2 implements Serializable {

    private static final long serialVersionUID = 462091570592784309L;
    /**
     * 日期
     */
    private String            statDate;
    /**
     * 最近一天日收入
     */
    private Double            lastDayIncome;
    /**
     * 最近一天的1.0商品GMV占比
     */
    private Double            lastDayGoods1GmvRatio;

    /**
     * 最近一天app活跃用户数
     */
    private Double            lastDayActiveAppUserCnt;

    /**
     * 最近一天忠实村民数
     */
    private Double            lastDayLoyaltyVillagerCnt;

    /**
     * 最近一天复购率
     */
    private Double            lastDayRepurchaseRate;

    /**
     * 最近一天访问商品数
     */
    private Double            lastDayVisitedProductCnt;

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public Double getLastDayIncome() {
        return lastDayIncome;
    }

    public void setLastDayIncome(Double lastDayIncome) {
        this.lastDayIncome = lastDayIncome;
    }

    public Double getLastDayGoods1GmvRatio() {
        return lastDayGoods1GmvRatio;
    }

    public void setLastDayGoods1GmvRatio(Double lastDayGoods1GmvRatio) {
        this.lastDayGoods1GmvRatio = lastDayGoods1GmvRatio;
    }

    public Double getLastDayActiveAppUserCnt() {
        return lastDayActiveAppUserCnt;
    }

    public void setLastDayActiveAppUserCnt(Double lastDayActiveAppUserCnt) {
        this.lastDayActiveAppUserCnt = lastDayActiveAppUserCnt;
    }

    public Double getLastDayLoyaltyVillagerCnt() {
        return lastDayLoyaltyVillagerCnt;
    }

    public void setLastDayLoyaltyVillagerCnt(Double lastDayLoyaltyVillagerCnt) {
        this.lastDayLoyaltyVillagerCnt = lastDayLoyaltyVillagerCnt;
    }

    public Double getLastDayRepurchaseRate() {
        return lastDayRepurchaseRate;
    }

    public void setLastDayRepurchaseRate(Double lastDayRepurchaseRate) {
        this.lastDayRepurchaseRate = lastDayRepurchaseRate;
    }

    public Double getLastDayVisitedProductCnt() {
        return lastDayVisitedProductCnt;
    }

    public void setLastDayVisitedProductCnt(Double lastDayVisitedProductCnt) {
        this.lastDayVisitedProductCnt = lastDayVisitedProductCnt;
    }

}
