package com.taobao.cun.auge.level.bo;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationSetting;
import com.taobao.cun.auge.level.enterrule.setting.StationExclusiveRuleResolver;

@Component
public class TownLevelStationEnterRuleBo {
	@Resource
	private TownLevelBo townLevelBo;
	@Resource
	private StationExclusiveRuleResolver stationExclusiveRuleResolver;
	@Value("${townlevel.notown.tip}")
	private String noTownTip;
	
	public List<TownLevelStationSetting> getTownLevelStationRules(String townCode) {
		TownLevelDto townLevelDto = townLevelBo.getTownLevelByTownCode(townCode);
		if(townLevelDto != null) {
			return stationExclusiveRuleResolver.resolve(townLevelDto);
		}else {//如果该镇数据缺失，则提示补充该地区的分层数据
			TownLevelStationSetting townLevelStationSetting = new TownLevelStationSetting();
			townLevelStationSetting = new TownLevelStationSetting();
			townLevelStationSetting.setAreaCode(townCode);
			townLevelStationSetting.setStationTypeCode("CLOSE");
			townLevelStationSetting.setStationTypeDesc(noTownTip);
			return Lists.newArrayList(townLevelStationSetting);
		}
	}

}
