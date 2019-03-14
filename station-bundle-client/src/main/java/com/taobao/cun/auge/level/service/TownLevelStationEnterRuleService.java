package com.taobao.cun.auge.level.service;

import java.util.List;

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
	List<TownLevelStationSetting> getTownLevelStationRules(String townCode);
}
