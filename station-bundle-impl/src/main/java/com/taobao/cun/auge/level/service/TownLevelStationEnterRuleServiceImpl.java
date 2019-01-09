package com.taobao.cun.auge.level.service;

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
	public TownLevelStationRuleDto getTownLevelStationRule(String townCode) {
		TownLevelDto townLevelDto = townLevelBo.getTownLevelByTownCode(townCode);
		TownLevelStationRuleDto townLevelStationRuleDto = null;
		if(townLevelDto != null) {
			townLevelStationRuleDto = townLevelDto.getTownLevelStationRuleDto();
		}else {//如果该镇数据缺失，则提示补充该地区的分层数据
			townLevelStationRuleDto = new TownLevelStationRuleDto();
			townLevelStationRuleDto.setAreaCode(townCode);
			townLevelStationRuleDto.setStationTypeCode("CLOSE");
			townLevelStationRuleDto.setStationTypeDesc("该镇分层数据缺失，请联系PD补充该镇分层数据");
		}
		return townLevelStationRuleDto;
	}

}
