package com.taobao.cun.auge.level.enterrule.setting;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationRuleDto;
import com.taobao.cun.auge.level.dto.TownLevelStationSetting;
import com.taobao.cun.auge.level.enterrule.setting.rule.SettingRuleParseFactory;


/**
 * 站点互斥规则，原则上是一镇一店，还需要考虑转型中的站点，如果已经签订了转型协议，那么需要算进来
 * 但不同级的镇又有一些差别
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class StationExclusiveRuleResolver {
	@Resource
	private SettingRuleParseFactory settingRuleParseFactory;
	
	/**
	 * 
	 * @param townLevelDto
	 * @param townLevelStationRuleDto
	 * @return
	 */
	public List<TownLevelStationSetting> resolve(TownLevelDto townLevelDto) {
		TownLevelStationRuleDto townLevelStationRuleDto = townLevelDto.getTownLevelStationRuleDto();
		Preconditions.checkNotNull(townLevelStationRuleDto, "规则配置不能为空");
		return settingRuleParseFactory.getSettingRuleParse(townLevelStationRuleDto.getRule()).parse(townLevelDto);
	}
}
