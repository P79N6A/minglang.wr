package com.taobao.cun.auge.level.upgraderule;

import javax.annotation.Resource;

import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.StationBizTypeEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.recruit.partner.enums.PartnerApplyConfirmIntentionEnum;

public abstract class AbstractUpgradeRuleParser implements UpgradeRuleParser {
	@Resource
	private PartnerInstanceQueryService partnerInstanceQueryService;
	
	/**
	 * 由于市场分层准入的入驻类型定义在PartnerApplyConfirmIntentionEnum中，与站点实际类型StationBizTypeEnum还不一致，
	 * 所以需要转换成一致的code，最后统一转换成PartnerApplyConfirmIntentionEnum中定义的类型
	 * @param instanceId
	 * @return
	 */
	protected PartnerApplyConfirmIntentionEnum typeConvert(Long stationId) {
		PartnerInstanceDto partnerInstanceDto = partnerInstanceQueryService.getCurrentPartnerInstanceByStationId(stationId);
		StationBizTypeEnum stationBizTypeEnum = partnerInstanceQueryService.getBizTypeByInstanceId(partnerInstanceDto.getId());
		String code = stationBizTypeEnum.getCode();
		if(StationBizTypeEnum.TPS_ELEC.getCode().equals(code)) {
			return PartnerApplyConfirmIntentionEnum.TPS_ELEC;
		}else if(StationBizTypeEnum.YOUPIN_ELEC.getCode().equals(code)) {
			return PartnerApplyConfirmIntentionEnum.TP_ELEC;
		}else if(StationBizTypeEnum.YOUPIN.getCode().equals(code)) {
			return PartnerApplyConfirmIntentionEnum.TP_YOUPIN;
		}else {
			return new PartnerApplyConfirmIntentionEnum("STATION", "农村淘宝服务站");
		}
	}
	
	/**
	 * 判断是否为优品服务站
	 * @param type
	 * @return
	 */
	protected boolean isTPYOUPIN(PartnerApplyConfirmIntentionEnum type) {
		return PartnerApplyConfirmIntentionEnum.TP_YOUPIN.equals(type);
	}
	
	/**
	 * 判断是否为农村淘宝服务站
	 * @param type
	 * @return
	 */
	protected boolean isStation(PartnerApplyConfirmIntentionEnum type) {
		return "STATION".equals(type.getCode());
	}
	
	/**
	 * 判断是否为电器合作店
	 * @param type
	 * @return
	 */
	protected boolean isTPELEC(PartnerApplyConfirmIntentionEnum type) {
		return PartnerApplyConfirmIntentionEnum.TP_ELEC.equals(type);
	}
	
	/**
	 * 判断是否为电器体验店
	 * @param type
	 * @return
	 */
	protected boolean isTPSELEC(PartnerApplyConfirmIntentionEnum type) {
		return PartnerApplyConfirmIntentionEnum.TPS_ELEC.equals(type);
	}
}
