package com.taobao.cun.auge.station.convert;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.StationStatusChangeEvent;
import com.taobao.cun.auge.event.enums.StationStatusChangeEnum;

public final class StationEventConverter {

	private StationEventConverter() {

	}

	public static StationStatusChangeEvent convert(StationStatusChangeEnum statusChangeEnum, Station station,
			OperatorDto operator) {
		StationStatusChangeEvent event = new StationStatusChangeEvent();

		event.setStationId(station.getId());
		event.setStationName(station.getName());
		
		event.setStatusChangeEnum(statusChangeEnum);
		event.copyOperatorDto(operator);

		return event;
	}
}
