package com.taobao.cun.auge.station.util;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.PartnerInstanceTypeChangeEvent;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum;
import com.taobao.cun.auge.station.dto.StationDto;

public final class PartnerInstanceEventUtil {
	private PartnerInstanceEventUtil() {

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
	
	public static String buildAddressInfo(StationDto oldStation,StationDto newStation){
		StringBuffer sb = new StringBuffer();
		sb.append(oldStation.getAddress().getProvinceDetail());
		sb.append(" | ");
		sb.append(oldStation.getAddress().getCityDetail());
		sb.append(" | ");
		sb.append(oldStation.getAddress().getCountyDetail());
		sb.append(" | ");
		sb.append(oldStation.getAddress().getTownDetail());
		sb.append(" | ");
		sb.append(oldStation.getAddress().getVillageDetail());
		sb.append(" | ");
		sb.append(oldStation.getAddress().getAddressDetail());
		sb.append(" ====> ");
		sb.append(newStation.getAddress().getProvinceDetail());
		sb.append(" | ");
		sb.append(newStation.getAddress().getCityDetail());
		sb.append(" | ");
		sb.append(newStation.getAddress().getCountyDetail());
		sb.append(" | ");
		sb.append(newStation.getAddress().getTownDetail());
		sb.append(" | ");
		sb.append(newStation.getAddress().getVillageDetail());
		sb.append(" | ");
		sb.append(newStation.getAddress().getAddressDetail());
		return sb.toString();
	}
	
	public static String buildLngLatInfo(StationDto oldStation,StationDto newStation){
		StringBuffer sb = new StringBuffer();
		sb.append(oldStation.getAddress().getProvinceDetail());
		sb.append(" Lng: ");
		sb.append(oldStation.getAddress().getLng());
		sb.append(" Lat: ");
		sb.append(oldStation.getAddress().getLat());
		sb.append(" ====> ");
		sb.append(" Lng: ");
		sb.append(newStation.getAddress().getLng());
		sb.append(" Lat: ");
		sb.append(newStation.getAddress().getLat());
		return sb.toString();
	}
}
