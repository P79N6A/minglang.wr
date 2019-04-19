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
	/**
	 * 定义一个升级顺序，只允许从后面的类型升级到前面的类型
	 */
	private final static List<String> UPGRADE_ORDER = Lists.newArrayList(/*"TPS_ELEC", */"TP_ELEC", "TP_YOUPIN", "STATION"); 
	@Resource
	private PartnerInstanceQueryService partnerInstanceQueryService;
	@Resource
	private TownLevelStationEnterRuleBo townLevelStationEnterRuleBo;
	
	public List<TownLevelStationSetting> getTownLevelStationRule(long stationId) {
		PartnerInstanceDto partnerInstanceDto = partnerInstanceQueryService.getCurrentPartnerInstanceByStationId(stationId);
		StationDto stationDto = partnerInstanceDto.getStationDto();
		return filter(townLevelStationEnterRuleBo.getTownLevelStationRules(stationDto.getAddress().getTown()), partnerInstanceDto.getId());
	}
	
	/**
	 * 将准入规则计算出来的允许入驻的类型做过滤
	 * 
	 * 如果是CLOSE的，那么是不允许入驻
	 * 如果是允许入驻TPS_ELEC的镇，由于目前不支持，也不让升级
	 * 接下来就是看一下当前站点的类型是不是符合升级顺序
	 */
	private List<TownLevelStationSetting> filter(List<TownLevelStationSetting> townLevelStationSettings, Long instanceId){
		List<TownLevelStationSetting> result = Lists.newArrayList();
		for(TownLevelStationSetting rule : townLevelStationSettings) {
			if(rule.getStationTypeCode().equals("CLOSE")) { //如果是CLOSE，则不能开点
				return Lists.newArrayList(rule);
			}
			if(!rule.getStationTypeCode().equals("TPS_ELEC")) {//目前不允许升级到体验店，过滤掉
				if(isValidUpgradeType(rule, instanceId)) {
					result.add(rule);
				}
			}
		}
		
		if(result.isEmpty()) {
			result.add(invalidTownLevelStationSetting(townLevelStationSettings.get(0)));
		}
		return result;
	}

	private TownLevelStationSetting invalidTownLevelStationSetting(TownLevelStationSetting townLevelStationSetting) {
		TownLevelStationSetting invalid = BeanCopy.copy(TownLevelStationSetting.class, townLevelStationSetting);
		invalid.setStationTypeCode("CLOSE");
		invalid.setStationTypeDesc("不允许升级");
		return invalid;
	}

	private boolean isValidUpgradeType(TownLevelStationSetting townLevelStationSetting, Long instanceId) {
		String targetCode = typeConvert(instanceId);
		//
		int currentIdx = UPGRADE_ORDER.indexOf(targetCode);
		int upgradeIdx = UPGRADE_ORDER.indexOf(townLevelStationSetting.getStationTypeCode());
		
		return upgradeIdx < currentIdx;
	}

	/**
	 * 由于市场分层准入的入驻类型定义在PartnerApplyConfirmIntentionEnum中，与站点实际类型StationBizTypeEnum还不一致，
	 * 所以需要转换成一致的code，最后统一转换成PartnerApplyConfirmIntentionEnum中定义的类型
	 * @param instanceId
	 * @return
	 */
	private String typeConvert(Long instanceId) {
		StationBizTypeEnum stationBizTypeEnum = partnerInstanceQueryService.getBizTypeByInstanceId(instanceId);
		String code = stationBizTypeEnum.getCode();
		if(StationBizTypeEnum.TPS_ELEC.getCode().equals(code)) {
			return PartnerApplyConfirmIntentionEnum.TPS_ELEC.getCode();
		}else if(StationBizTypeEnum.YOUPIN_ELEC.getCode().equals(code)) {
			return PartnerApplyConfirmIntentionEnum.TP_ELEC.getCode();
		}else if(StationBizTypeEnum.YOUPIN.getCode().equals(code)) {
			return PartnerApplyConfirmIntentionEnum.TP_YOUPIN.getCode();
		}else {
			return "STATION";
		}
	}
}
