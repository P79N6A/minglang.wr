package com.taobao.cun.auge.event.listener;

import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@EventSub(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT)
public class CuntaoFlowRecordListener implements EventListener{

	@Override
	public void onMessage(Event arg0) {
		// TODO Auto-generated method stub
		
	}

}
