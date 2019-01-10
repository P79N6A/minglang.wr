package com.taobao.cun.auge.level.service;

import com.taobao.cun.auge.level.dto.TownLevelStationSetting;

/**
 * 市场分层站点转型升级规则
 * 
 * @author chengyu.zhoucy
 *
 */
public interface TownLevelStationUpgradeRuleService {
	/**
	 * 获取站点转型升级规则
	 * 
	 * @param stationId
	 * @return
	 */
	TownLevelStationSetting getTownLevelStationRule(long stationId);
}
