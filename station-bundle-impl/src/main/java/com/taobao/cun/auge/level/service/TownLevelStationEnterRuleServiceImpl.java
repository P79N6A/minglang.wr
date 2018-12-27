package com.taobao.cun.auge.level.service;

import java.util.Optional;

import javax.annotation.Resource;

import com.taobao.cun.auge.level.bo.TownLevelBo;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationRuleDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface= TownLevelStationEnterRuleService.class)
public class TownLevelStationEnterRuleServiceImpl implements TownLevelStationEnterRuleService {
	@Resource
	private TownLevelBo townLevelBo;
	
	@Override
	public Optional<TownLevelStationRuleDto> getTownLevelStationRule(String townCode) {
		TownLevelDto townLevelDto = townLevelBo.getTownLevelByTownCode(townCode);
		TownLevelStationRuleDto townLevelStationRuleDto = null;
		if(townLevelDto != null) {
			townLevelStationRuleDto = townLevelDto.getTownLevelStationRuleDto();
		}
		//如果是C，则返回空，不能开点
		if(townLevelStationRuleDto != null) {
			if(townLevelStationRuleDto.getLevel().equalsIgnoreCase("C")) {
				townLevelStationRuleDto = null;
			}
		}
		return Optional.ofNullable(townLevelStationRuleDto);
	}

}
