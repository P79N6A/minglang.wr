package com.taobao.cun.auge.level.bo;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationSetting;
import com.taobao.cun.auge.level.enterrule.setting.StationExclusiveRuleResolver;

@Component
public class TownLevelStationEnterRuleBo {
	@Resource
	private TownLevelBo townLevelBo;
	@Resource
	private StationExclusiveRuleResolver stationExclusiveRuleResolver;
	
	public TownLevelStationSetting getTownLevelStationRule(String townCode) {
		TownLevelDto townLevelDto = townLevelBo.getTownLevelByTownCode(townCode);
		if(townLevelDto != null) {
			return stationExclusiveRuleResolver.resolve(townLevelDto);
		}else {//如果该镇数据缺失，则提示补充该地区的分层数据
			TownLevelStationSetting townLevelStationSetting = new TownLevelStationSetting();
			townLevelStationSetting = new TownLevelStationSetting();
			townLevelStationSetting.setAreaCode(townCode);
			townLevelStationSetting.setStationTypeCode("CLOSE");
			townLevelStationSetting.setStationTypeDesc("该镇分层数据缺失，请联系PD补充该镇分层数据");
			return townLevelStationSetting;
		}
	}

}
