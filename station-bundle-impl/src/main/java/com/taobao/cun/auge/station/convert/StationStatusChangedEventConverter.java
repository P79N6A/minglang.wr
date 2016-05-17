package com.taobao.cun.auge.station.convert;

import java.util.Date;

import com.taobao.cun.auge.event.domain.StationStatusChangedEvent;
import com.taobao.cun.auge.station.enums.StationStatusEnum;

public final class StationStatusChangedEventConverter {

	private StationStatusChangedEventConverter() {

	}

	public static StationStatusChangedEvent convert(Long stationId, StationStatusEnum status, String operatorId,
			String operatorName) {
		
		StationStatusChangedEvent event = new StationStatusChangedEvent();
		
		event.setChangedTime(new Date());
		event.setOperatorId(operatorId);
		event.setStationId(stationId);
		event.setStatus(status);

		return event;
	}
}
