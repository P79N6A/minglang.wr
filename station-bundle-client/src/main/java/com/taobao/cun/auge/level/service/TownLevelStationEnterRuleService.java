package com.taobao.cun.auge.level.service;

import java.util.Optional;

import com.taobao.cun.auge.level.dto.TownLevelStationRuleDto;

/**
 * 市场分层站点入驻规则
 * 
 * @author chengyu.zhoucy
 *
 */
public interface TownLevelStationEnterRuleService {
	/**
	 * 获取镇站点入驻规则，如果返回空，则不能入驻任何站点
	 * 
	 * @param townCode
	 * @return
	 */
	Optional<TownLevelStationRuleDto> getTownLevelStationRule(String townCode);
}
