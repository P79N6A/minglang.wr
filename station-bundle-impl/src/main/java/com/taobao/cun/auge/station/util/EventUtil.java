package com.taobao.cun.auge.station.util;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.PartnerInstanceTypeChangeEvent;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum;

public final class EventUtil {
	private EventUtil() {

	}

	// 发出撤销升级事件
	public static void dispatchCancelUpgradeEvent(Long instanceId,Long stationId, OperatorDto operatorDto) {
		PartnerInstanceTypeChangeEvent event = new PartnerInstanceTypeChangeEvent();

		event.setPartnerInstanceId(instanceId);
		event.setStationId(stationId);
		event.setTypeChangeEnum(PartnerInstanceTypeChangeEnum.CANCEL_TPA_UPGRADE_2_TP);
		event.copyOperatorDto(operatorDto);

		EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_TYPE_CHANGE_EVENT, event);
	}
	
	//发出升级事件
	public static void dispatchUpgradeEvent(PartnerStationRel tpaInstance,OperatorDto operatorDto) {
		PartnerInstanceTypeChangeEvent event = new PartnerInstanceTypeChangeEvent();
		
		event.setPartnerInstanceId(tpaInstance.getId());
		event.setStationId(tpaInstance.getStationId());
		event.setTypeChangeEnum(PartnerInstanceTypeChangeEnum.TPA_UPGRADE_2_TP);
		event.copyOperatorDto(operatorDto);
		
		EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_TYPE_CHANGE_EVENT, event);
	}
}
