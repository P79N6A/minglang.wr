package com.taobao.cun.auge.event;

import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventDispatcher;
import com.taobao.cun.crius.event.client.EventListener;

@EventSub("a-event")
public class AEventListener implements EventListener {

	@Override
	public void onMessage(Event event) {
		System.out.println("a:" + event.getContent().toString() + event.getEventMeta().toString());
		EventDispatcher.getInstance().dispatch("b-event", "收到了");
	}

}
