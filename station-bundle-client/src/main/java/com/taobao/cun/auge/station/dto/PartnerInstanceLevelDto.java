package com.taobao.cun.auge.station.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEvaluateTypeEnum;

public class PartnerInstanceLevelDto extends OperatorDto {

    /**
     * 
     */
    private static final long                    serialVersionUID = 4289981939113069738L;
    private Long                                 id;

    /**
     * 合伙人实例id(仅供内部使用)
     */
    private Long                                 partnerInstanceId;
    /**
     * 村点id
     */
    private Long                                 stationId;
    /**
     * 用户淘宝id
     */
    private Long                                 taobaoUserId;
    /**
     * 县组织id
     */
    private Long                                 countyOrgId;
    /**
     * 分数
     */
    private BigDecimal                           score;
    /**
     * 月收入
     */
    private BigDecimal                           monthlyIncome;
    /**
     * 月收入得分
     */
    private BigDecimal                           monthlyIncomeScore;
    /**
     * 1.0商品GMV
     */
    private BigDecimal                           goods1Gmv;
    /**
     * 1.0商品GMV得分
     */
    private BigDecimal                           goods1GmvScore;
    /**
     * app收入占比
     */
    private BigDecimal                           appIncomePercent;
    /**
     * app收入占比得分
     */
    private BigDecimal                           appIncomePercentScore;
    /**
     * 购买村民数
     */
    private BigDecimal                           buyVillagerCnt;
    /**
     * 购买村民数得分
     */
    private BigDecimal                           buyVillagerCntScore;
    /**
     * 人均购买次数
     */
    private BigDecimal                           avgBuyTimes;
    /**
     * 人均购买次数得分
     */
    private BigDecimal                           avgBuyTimesScore;
    /**
     * 新增app绑定量
     */
    private BigDecimal                           newAppBindingCnt;
    /**
     * 新增app绑定量得分
     */
    private BigDecimal                           newAppBindingCntScore;
    /**
     * 当前层级
     */
    private PartnerInstanceLevelEnum             currentLevel;
    /**
     * 评定人
     */
    private String                               evaluateBy;
    /**
     * 上次评定层级
     */
    private PartnerInstanceLevelEnum             preLevel;
    /**
     * 预授层级
     */
    private PartnerInstanceLevelEnum             expectedLevel;
    /**
     * 评定日期
     */
    private Date                                 evaluateDate;
    /**
     * 上次评定日期
     */
    private Date                                 lastEvaluateDate;
    /**
     * 下次评定日期
     */
    private Date                                 nextEvaluateDate;
    /**
     * 县排名
     */
    private Long                                 countyRank;
    /**
     * 县合伙人总数
     */
    private Long                                 countyPartnerInstanceCnt;
    /**
     * 全国排名
     */
    private Long                                 countryRank;
    /**
     * 全国合伙人总数
     */
    private Long                                 countryPartnerInstanceCnt;

    /**
     * 评定类型
     */
    private PartnerInstanceLevelEvaluateTypeEnum evaluateType;

    private String                               remark;
//----------------------------------------------------------新加指标 start-----------------------------------------------------//
    /**
     * 1.0商品gmv占比
     */
    private BigDecimal                           goods1GmvRatio;

    private BigDecimal                           goods1GmvRatioScore;

    /**
     * 活跃村民数:访问超过两次
     */
    private BigDecimal                           activeAppUserCnt;

    private BigDecimal                           activeAppUserCntScore;

    /**
     * 忠实村民数:近六个月访问超过六次
     */
    private BigDecimal                           loyaltyVillagerCnt;

    private BigDecimal                           loyaltyVillagerCntScore;

    /**
     * 复购率
     */
    private BigDecimal                           repurchaseRate;

    private BigDecimal                           repurchaseRateScore;

    /**
     * 商品访问数
     */
    private BigDecimal                           visitedProductCnt;

    private BigDecimal                           visitedProductCntScore;

    /**
     * 此次评级中前六个月中有几个月的月收入低于给定的最低值,评估月收入稳定性防止刷额
     */
    private String                           monthlyIncomeLastSixMonth;
//----------------------------------------------------------新加指标 end-----------------------------------------------------//

    
    public Long getPartnerInstanceId() {
        return partnerInstanceId;
    }

    public void setPartnerInstanceId(Long partnerInstanceId) {
        this.partnerInstanceId = partnerInstanceId;
    }

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

    public Long getCountyOrgId() {
        return countyOrgId;
    }

    public void setCountyOrgId(Long countyOrgId) {
        this.countyOrgId = countyOrgId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getMonthlyIncomeScore() {
        return monthlyIncomeScore;
    }

    public void setMonthlyIncomeScore(BigDecimal monthlyIncomeScore) {
        this.monthlyIncomeScore = monthlyIncomeScore;
    }

    public BigDecimal getGoods1Gmv() {
        return goods1Gmv;
    }

    public void setGoods1Gmv(BigDecimal goods1Gmv) {
        this.goods1Gmv = goods1Gmv;
    }

    public BigDecimal getGoods1GmvScore() {
        return goods1GmvScore;
    }

    public void setGoods1GmvScore(BigDecimal goods1GmvScore) {
        this.goods1GmvScore = goods1GmvScore;
    }

    public BigDecimal getAppIncomePercent() {
        return appIncomePercent;
    }

    public void setAppIncomePercent(BigDecimal appIncomePercent) {
        this.appIncomePercent = appIncomePercent;
    }

    public BigDecimal getAppIncomePercentScore() {
        return appIncomePercentScore;
    }

    public void setAppIncomePercentScore(BigDecimal appIncomePercentScore) {
        this.appIncomePercentScore = appIncomePercentScore;
    }

    public BigDecimal getBuyVillagerCnt() {
        return buyVillagerCnt;
    }

    public void setBuyVillagerCnt(BigDecimal buyVillagerCnt) {
        this.buyVillagerCnt = buyVillagerCnt;
    }

    public BigDecimal getBuyVillagerCntScore() {
        return buyVillagerCntScore;
    }

    public void setBuyVillagerCntScore(BigDecimal buyVillagerCntScore) {
        this.buyVillagerCntScore = buyVillagerCntScore;
    }

    public BigDecimal getAvgBuyTimes() {
        return avgBuyTimes;
    }

    public void setAvgBuyTimes(BigDecimal avgBuyTimes) {
        this.avgBuyTimes = avgBuyTimes;
    }

    public BigDecimal getAvgBuyTimesScore() {
        return avgBuyTimesScore;
    }

    public void setAvgBuyTimesScore(BigDecimal avgBuyTimesScore) {
        this.avgBuyTimesScore = avgBuyTimesScore;
    }

    public BigDecimal getNewAppBindingCntScore() {
        return newAppBindingCntScore;
    }

    public void setNewAppBindingCntScore(BigDecimal newAppBindingCntScore) {
        this.newAppBindingCntScore = newAppBindingCntScore;
    }

    public PartnerInstanceLevelEnum getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(PartnerInstanceLevelEnum currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getEvaluateBy() {
        return evaluateBy;
    }

    public void setEvaluateBy(String evaluateBy) {
        this.evaluateBy = evaluateBy;
    }

    public PartnerInstanceLevelEnum getPreLevel() {
        return preLevel;
    }

    public void setPreLevel(PartnerInstanceLevelEnum preLevel) {
        this.preLevel = preLevel;
    }

    public PartnerInstanceLevelEnum getExpectedLevel() {
        return expectedLevel;
    }

    public void setExpectedLevel(PartnerInstanceLevelEnum expectedLevel) {
        this.expectedLevel = expectedLevel;
    }

    public Date getEvaluateDate() {
        return evaluateDate;
    }

    public void setEvaluateDate(Date evaluateDate) {
        this.evaluateDate = evaluateDate;
    }

    public Date getLastEvaluateDate() {
        return lastEvaluateDate;
    }

    public void setLastEvaluateDate(Date lastEvaluateDate) {
        this.lastEvaluateDate = lastEvaluateDate;
    }

    public Date getNextEvaluateDate() {
        return nextEvaluateDate;
    }

    public void setNextEvaluateDate(Date nextEvaluateDate) {
        this.nextEvaluateDate = nextEvaluateDate;
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

    public BigDecimal getNewAppBindingCnt() {
        return newAppBindingCnt;
    }

    public void setNewAppBindingCnt(BigDecimal newAppBindingCnt) {
        this.newAppBindingCnt = newAppBindingCnt;
    }

    public PartnerInstanceLevelEvaluateTypeEnum getEvaluateType() {
        return evaluateType;
    }

    public void setEvaluateType(PartnerInstanceLevelEvaluateTypeEnum evaluateType) {
        this.evaluateType = evaluateType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getActiveAppUserCnt() {
        return activeAppUserCnt;
    }

    public void setActiveAppUserCnt(BigDecimal activeAppUserCnt) {
        this.activeAppUserCnt = activeAppUserCnt;
    }

    public BigDecimal getActiveAppUserCntScore() {
        return activeAppUserCntScore;
    }

    public void setActiveAppUserCntScore(BigDecimal activeAppUserCntScore) {
        this.activeAppUserCntScore = activeAppUserCntScore;
    }

    public BigDecimal getLoyaltyVillagerCnt() {
        return loyaltyVillagerCnt;
    }

    public void setLoyaltyVillagerCnt(BigDecimal loyaltyVillagerCnt) {
        this.loyaltyVillagerCnt = loyaltyVillagerCnt;
    }

    public BigDecimal getLoyaltyVillagerCntScore() {
        return loyaltyVillagerCntScore;
    }

    public void setLoyaltyVillagerCntScore(BigDecimal loyaltyVillagerCntScore) {
        this.loyaltyVillagerCntScore = loyaltyVillagerCntScore;
    }

    public BigDecimal getRepurchaseRate() {
        return repurchaseRate;
    }

    public void setRepurchaseRate(BigDecimal repurchaseRate) {
        this.repurchaseRate = repurchaseRate;
    }

    public BigDecimal getRepurchaseRateScore() {
        return repurchaseRateScore;
    }

    public void setRepurchaseRateScore(BigDecimal repurchaseRateScore) {
        this.repurchaseRateScore = repurchaseRateScore;
    }

    public BigDecimal getVisitedProductCnt() {
        return visitedProductCnt;
    }

    public void setVisitedProductCnt(BigDecimal visitedProductCnt) {
        this.visitedProductCnt = visitedProductCnt;
    }

    public BigDecimal getVisitedProductCntScore() {
        return visitedProductCntScore;
    }

    public void setVisitedProductCntScore(BigDecimal visitedProductCntScore) {
        this.visitedProductCntScore = visitedProductCntScore;
    }
    
    public String getMonthlyIncomeLastSixMonth() {
        return monthlyIncomeLastSixMonth;
    }

    public void setMonthlyIncomeLastSixMonth(String monthlyIncomeLastSixMonth) {
        this.monthlyIncomeLastSixMonth = monthlyIncomeLastSixMonth;
    }

}
