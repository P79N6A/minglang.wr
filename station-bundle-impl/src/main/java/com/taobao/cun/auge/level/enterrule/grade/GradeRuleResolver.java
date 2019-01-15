package com.taobao.cun.auge.level.enterrule.grade;

import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationRuleDto;

/**
 * 层级准入规则
 * 
 * @author chengyu.zhoucy
 *
 */
public interface GradeRuleResolver {
	/**
	 * 获取配置规则
	 * @param townLevelDto
	 * @return
	 */
	TownLevelStationRuleDto getTownLevelStationRule(TownLevelDto townLevelDto);
}
