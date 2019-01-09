package com.taobao.cun.auge.level.bo;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationRuleDto;

@Component
public class TownLevelStationEnterRuleBo {
	@Resource
	private TownLevelBo townLevelBo;
	
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
