package com.taobao.cun.auge.station.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.StationApplySyncEvent;
import com.taobao.cun.auge.station.sync.StationApplySyncBO;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("stationApplySyncListener")
@EventSub(EventConstant.CUNTAO_STATION_APPLY_SYNC_EVENT)
public class StationApplySyncListener implements EventListener {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private StationApplySyncBO syncStationApplyBO;

	@Override
	public void onMessage(Event event) {
		StationApplySyncEvent syncEvent = (StationApplySyncEvent) event.getValue();
		logger.info("sync back to station_apply: {}", JSON.toJSONString(syncEvent));
		switch (syncEvent.getSyncType()) {
		case ADD:
			syncStationApplyBO.addStationApply(syncEvent.getObjectId());
			break;
		default:
			syncStationApplyBO.updateStationApply(syncEvent.getObjectId(), syncEvent.getSyncType());
		}

	}

}
