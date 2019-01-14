package com.taobao.cun.auge.level.bo;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.utils.BeanCopy;
import com.taobao.cun.auge.level.dto.TownLevelStationSetting;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.StationBizTypeEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.recruit.partner.enums.PartnerApplyConfirmIntentionEnum;

@Component
public class TownLevelStationUpgradeRuleBo {
	private final static List<String> UPGRADE_ORDER = Lists.newArrayList("TPS_ELEC", "TP_ELEC", "TP_YOUPIN", "STATION"); 
	@Resource
	private PartnerInstanceQueryService partnerInstanceQueryService;
	@Resource
	private TownLevelStationEnterRuleBo townLevelStationEnterRuleBo;
	
	public TownLevelStationSetting getTownLevelStationRule(long stationId) {
		PartnerInstanceDto partnerInstanceDto = partnerInstanceQueryService.getCurrentPartnerInstanceByStationId(stationId);
		StationDto stationDto = partnerInstanceDto.getStationDto();
		TownLevelStationSetting townLevelStationSetting = townLevelStationEnterRuleBo.getTownLevelStationRule(stationDto.getAddress().getTown());
		
		if(!townLevelStationSetting.getStationTypeCode().equals("CLOSE")) {
			if(!isValidUpgradeType(townLevelStationSetting, partnerInstanceDto.getId())) {
				return invalidTownLevelStationSetting(townLevelStationSetting);
			}
		}
		return townLevelStationSetting;
	}

	private TownLevelStationSetting invalidTownLevelStationSetting(TownLevelStationSetting townLevelStationSetting) {
		TownLevelStationSetting invalid = BeanCopy.copy(TownLevelStationSetting.class, townLevelStationSetting);
		invalid.setStationTypeCode("CLOSE");
		invalid.setStationTypeDesc("不允许升级");
		return invalid;
	}

	private boolean isValidUpgradeType(TownLevelStationSetting townLevelStationSetting, Long instanceId) {
		StationBizTypeEnum stationBizTypeEnum = partnerInstanceQueryService.getBizTypeByInstanceId(instanceId);
		String code = stationBizTypeEnum.getCode();
		String targetCode = null;
		if(StationBizTypeEnum.TPS_ELEC.getCode().equals(code)) {
			targetCode = PartnerApplyConfirmIntentionEnum.TPS_ELEC.getCode();
		}else if(StationBizTypeEnum.YOUPIN_ELEC.getCode().equals(code)) {
			targetCode = PartnerApplyConfirmIntentionEnum.TP_ELEC.getCode();
		}else if(StationBizTypeEnum.YOUPIN.getCode().equals(code)) {
			targetCode = PartnerApplyConfirmIntentionEnum.TP_YOUPIN.getCode();
		}else {
			targetCode = "STATION";
		}
		
		int currentIdx = UPGRADE_ORDER.indexOf(targetCode);
		int upgradeIdx = UPGRADE_ORDER.indexOf(townLevelStationSetting.getStationTypeCode());
		
		return upgradeIdx < currentIdx;
	}
}
