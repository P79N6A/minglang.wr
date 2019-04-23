package com.taobao.cun.auge.level.stationrule;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationRuleDto;

@Component
public class GradeRuleFacade {
	@Resource
	private GradeRuleResolverFactory gradeRuleResolverFactory;
	
	public TownLevelStationRuleDto getTownLevelStationRule(TownLevelDto townLevelDto) {
		return gradeRuleResolverFactory.getGradeRuleResolver(townLevelDto.getLevel()).getTownLevelStationRule(townLevelDto);
	}
}
