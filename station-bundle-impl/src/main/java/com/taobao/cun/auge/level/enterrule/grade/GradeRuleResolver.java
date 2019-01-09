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
	 * 解析镇域准入规则，返回允许开的站点类型
	 * 
	 * @param townLevelDto
	 * @return
	 */
	TownLevelStationRuleDto resolve(TownLevelDto townLevelDto);
}
