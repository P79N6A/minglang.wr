package com.taobao.cun.auge.station.convert;

import java.util.Date;

import com.taobao.cun.auge.event.domain.StationStatusChangedEvent;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.StationStatusEnum;

public final class StationStatusChangedEventConverter {

	private StationStatusChangedEventConverter(){
		
	}
	
	public static StationStatusChangedEvent convert(StationStatusEnum oldStatus, StationStatusEnum newStatus,PartnerInstanceDto partnerInstanceDto,String operatorId){
		StationStatusChangedEvent event = new StationStatusChangedEvent();

		event.setTaobaoUserId(partnerInstanceDto.getPartnerDto().getTaobaoUserId());
		event.setPartnerType(partnerInstanceDto.getType());
		event.setChangedTime(new Date());
		event.setOperatorId(operatorId);
		event.setStationId(partnerInstanceDto.getStationDto().getId());
		event.setNewStatus(newStatus);
		event.setOldStatus(oldStatus);

		
		return event;
	}
}
