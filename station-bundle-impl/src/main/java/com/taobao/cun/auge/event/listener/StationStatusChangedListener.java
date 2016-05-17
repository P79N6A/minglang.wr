package com.taobao.cun.auge.event.listener;

import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@EventSub("station-status-changed-event")
public class StationStatusChangedListener implements EventListener {

	@Override
	public void onMessage(Event arg0) {
		// TODO Auto-generated method stub

	}

}
