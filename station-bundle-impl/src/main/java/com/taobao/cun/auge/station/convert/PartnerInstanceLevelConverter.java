package com.taobao.cun.auge.station.convert;

import com.taobao.cun.auge.dal.domain.PartnerInstanceLevel;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum;

/**
 * Created by jingxiao.gjx on 2016/7/29.
 */
public class PartnerInstanceLevelConverter {

	private PartnerInstanceLevelConverter() {
	}

	public static PartnerInstanceLevelDto toPartnerInstanceLevelDto(PartnerInstanceLevel level) {
		if (level == null) {
			return null;
		}

		PartnerInstanceLevelDto result = new PartnerInstanceLevelDto();
		result.setId(level.getId());
		result.setAppIncomePercent(level.getAppIncomePercent());
		result.setAppIncomePercentScore(level.getAppIncomePercentScore());
		result.setAvgBuyTimes(level.getAvgBuyTimes());
		result.setAvgBuyTimesScore(level.getAvgBuyTimesScore());
		result.setBuyVillagerCnt(level.getBuyVillagerCnt());
		result.setBuyVillagerCntScore(level.getBuyVillagerCntScore());
		result.setCountryPartnerInstanceCnt(level.getCountryPartnerInstanceCnt());
		result.setCountryRank(level.getCountryRank());
		result.setCountyOrgId(level.getCountyOrgId());
		result.setCountyPartnerInstanceCnt(level.getCountyPartnerInstanceCnt());
		result.setCountyRank(level.getCountyRank());
		result.setCurrentLevel(PartnerInstanceLevelEnum.valueof(level.getCurrentLevel()));
		result.setEvaluateBy(level.getEvaluateBy());
		result.setEvaluateDate(level.getEvaluateDate());
		result.setExpectedLevel(PartnerInstanceLevelEnum.valueof(level.getExpectedLevel()));
		result.setGoods1Gmv(level.getGoods1Gmv());
		result.setGoods1GmvScore(level.getGoods1GmvScore());
		result.setLastEvaluateDate(level.getLastEvaluateDate());
		result.setMonthlyIncome(level.getMonthlyIncome());
		result.setMonthlyIncomeScore(level.getMonthlyIncomeScore());
		result.setNewAppBindingCnt(level.getNewAppBindingCnt());
		result.setNewAppBindingCntScore(level.getNewAppBindingCntScore());
		result.setNextEvaluateDate(level.getNextEvaluateDate());
		result.setPartnerInstanceId(level.getPartnerInstanceId());
		result.setPreLevel(PartnerInstanceLevelEnum.valueof(level.getPreLevel()));
		result.setScore(level.getScore());
		result.setStationId(level.getStationId());
		result.setTaobaoUserId(level.getTaobaoUserId());
		result.setGoods1GmvRatio(level.getGoods1GmvRatio());
		result.setGoods1GmvRatioScore(level.getGoods1GmvRatioScore());
		result.setActiveAppUserCnt(level.getActiveAppUserCnt());
		result.setActiveAppUserCntScore(level.getActiveAppUserCntScore());
		result.setLoyaltyVillagerCnt(level.getLoyaltyVillagerCnt());
		result.setLoyaltyVillagerCntScore(level.getLoyaltyVillagerCntScore());
		result.setVisitedProductCnt(level.getVisitedProductCnt());
		result.setVisitedProductCntScore(level.getVisitedProductCntScore());
		result.setRepurchaseRate(level.getRepurchaseRate());
		result.setRepurchaseRateScore(level.getRepurchaseRateScore());
		result.setMonthlyIncomeLastSixMonth(level.getMonthlyIncomeLastSixMonth());
		return result;
	}

	public static PartnerInstanceLevel toPartnerInstanceLevel(PartnerInstanceLevelDto level) {
		if (level == null) {
			return null;
		}
		PartnerInstanceLevel result = new PartnerInstanceLevel();
		result.setId(level.getId());
		result.setAppIncomePercent(level.getAppIncomePercent());
		result.setAppIncomePercentScore(level.getAppIncomePercentScore());
		result.setAvgBuyTimes(level.getAvgBuyTimes());
		result.setAvgBuyTimesScore(level.getAvgBuyTimesScore());
		result.setBuyVillagerCnt(level.getBuyVillagerCnt());
		result.setBuyVillagerCntScore(level.getBuyVillagerCntScore());
		result.setCountryPartnerInstanceCnt(level.getCountryPartnerInstanceCnt());
		result.setCountryRank(level.getCountryRank());
		result.setCountyOrgId(level.getCountyOrgId());
		result.setCountyPartnerInstanceCnt(level.getCountyPartnerInstanceCnt());
		result.setCountyRank(level.getCountyRank());
		result.setCurrentLevel(levelToString(level.getCurrentLevel()));
		result.setEvaluateBy(level.getEvaluateBy());
		result.setEvaluateDate(level.getEvaluateDate());
		result.setExpectedLevel(levelToString(level.getExpectedLevel()));
		result.setGoods1Gmv(level.getGoods1Gmv());
		result.setGoods1GmvScore(level.getGoods1GmvScore());
		result.setLastEvaluateDate(level.getLastEvaluateDate());
		result.setMonthlyIncome(level.getMonthlyIncome());
		result.setMonthlyIncomeScore(level.getMonthlyIncomeScore());
		result.setNewAppBindingCnt(level.getNewAppBindingCnt());
		result.setNewAppBindingCntScore(level.getNewAppBindingCntScore());
		result.setNextEvaluateDate(level.getNextEvaluateDate());
		result.setPartnerInstanceId(level.getPartnerInstanceId());
		result.setPreLevel(levelToString(level.getPreLevel()));
		result.setScore(level.getScore());
		result.setStationId(level.getStationId());
		result.setTaobaoUserId(level.getTaobaoUserId());
		result.setRemark(level.getRemark());
		result.setGoods1GmvRatio(level.getGoods1GmvRatio());
        result.setGoods1GmvRatioScore(level.getGoods1GmvRatioScore());
        result.setActiveAppUserCnt(level.getActiveAppUserCnt());
        result.setActiveAppUserCntScore(level.getActiveAppUserCntScore());
        result.setLoyaltyVillagerCnt(level.getLoyaltyVillagerCnt());
        result.setLoyaltyVillagerCntScore(level.getLoyaltyVillagerCntScore());
        result.setVisitedProductCnt(level.getVisitedProductCnt());
        result.setVisitedProductCntScore(level.getVisitedProductCntScore());
        result.setRepurchaseRate(level.getRepurchaseRate());
        result.setRepurchaseRateScore(level.getRepurchaseRateScore());
        result.setMonthlyIncomeLastSixMonth(level.getMonthlyIncomeLastSixMonth());
		return result;
	}

	public static String levelToString(PartnerInstanceLevelEnum preLevel) {
		return preLevel == null ? null : preLevel.getLevel().name();
	}

	public static PartnerInstanceLevelDto toPartnerInstanceLevelDtoWithoutId(PartnerInstanceLevel level) {
		PartnerInstanceLevelDto dto = toPartnerInstanceLevelDto(level);
		if (null != dto) {
			dto.setPartnerInstanceId(null);
		}
		return dto;
	}

}
