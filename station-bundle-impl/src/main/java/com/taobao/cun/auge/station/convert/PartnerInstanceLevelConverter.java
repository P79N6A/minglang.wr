package com.taobao.cun.auge.station.convert;

import com.taobao.cun.auge.dal.domain.PartnerInstanceLevel;
import com.taobao.cun.auge.event.enums.PartnerInstanceLevelEnum;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;

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
		result.setNewAppBindingCntScore(level.getNewAppBindingCntScore());
		result.setNextEvaluateDate(level.getNextEvaluateDate());
		result.setPartnerInstanceId(level.getPartnerInstanceId());
		result.setPreLevel(PartnerInstanceLevelEnum.valueof(level.getPreLevel()));
		result.setScore(level.getScore());
		result.setStationId(level.getStationId());
		result.setTaobaoUserId(level.getTaobaoUserId());
		return result;
	}
}
