package com.taobao.cun.auge.level.bo;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationSetting;
import com.taobao.cun.auge.level.upgraderule.UpgradeRuleParser;
import com.taobao.cun.auge.level.upgraderule.UpgradeRuleParserFactory;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;

@Component
public class TownLevelStationUpgradeRuleBo {
	@Resource
	private PartnerInstanceQueryService partnerInstanceQueryService;
	@Resource
	private TownLevelBo townLevelBo;
	@Resource
	private UpgradeRuleParserFactory upgradeRuleParserFactory;
	
	public List<TownLevelStationSetting> getTownLevelStationRule(long stationId) {
		PartnerInstanceDto partnerInstanceDto = partnerInstanceQueryService.getCurrentPartnerInstanceByStationId(stationId);
		StationDto stationDto = partnerInstanceDto.getStationDto();
		TownLevelDto townLevelDto = townLevelBo.getTownLevelByTownCode(stationDto.getAddress().getTown());
		UpgradeRuleParser upgradeRuleParser = upgradeRuleParserFactory.getUpgradeRuleParser(townLevelDto.getTownLevelStationRuleDto().getUpgradeRule());
		return upgradeRuleParser.parse(townLevelDto, stationId);
	}
}
