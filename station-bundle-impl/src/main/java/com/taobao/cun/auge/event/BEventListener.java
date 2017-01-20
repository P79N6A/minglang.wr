package com.taobao.cun.auge.event;

import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@EventSub("b-event")
public class BEventListener implements EventListener {

	@Override
	public void onMessage(Event event) {
		System.out.println("b:" + event.getValue());
	}

}
