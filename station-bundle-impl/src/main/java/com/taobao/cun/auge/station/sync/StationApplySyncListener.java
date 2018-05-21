package com.taobao.cun.auge.station.sync;

import com.alibaba.fastjson.JSON;

import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.event.StationApplySyncEvent;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("stationApplySyncListener")
@EventSub(StationBundleEventConstant.CUNTAO_STATION_APPLY_SYNC_EVENT)
public class StationApplySyncListener implements EventListener {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private StationApplySyncBO syncStationApplyBO;

	@Autowired
	private DiamondConfiguredProperties diamondConfiguredProperties;

	@Override
	public void onMessage(Event event) {
		String isSync = diamondConfiguredProperties.getIsSync();
		if(!"y".equals(isSync)){
			return;
		}
		
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
