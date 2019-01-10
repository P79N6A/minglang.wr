package com.taobao.cun.auge.level.service;

import com.taobao.cun.auge.level.dto.TownLevelStationSetting;

/**
 * 市场分层站点入驻规则
 * 
 * @author chengyu.zhoucy
 *
 */
public interface TownLevelStationEnterRuleService {
	/**
	 * 获取镇站点入驻规则
	 * 
	 * @param townCode
	 * @return
	 */
	TownLevelStationSetting getTownLevelStationRule(String townCode);
}
