package com.taobao.cun.auge.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("removeUserTagListener")
@EventSub(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT)
public class RemoveUserTagListener implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(RemoveUserTagListener.class);

	@Autowired
	PartnerBO partnerBO;

	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;

	@Override
	public void onMessage(Event event) {
		PartnerInstanceStateChangeEvent stateChangeEvent = (PartnerInstanceStateChangeEvent) event.getValue();

		PartnerInstanceStateChangeEnum stateChangeEnum = stateChangeEvent.getStateChangeEnum();
		String operatorId = stateChangeEvent.getOperator();
		Long taobaoUserId = stateChangeEvent.getTaobaoUserId();
		Long instanceId = stateChangeEvent.getPartnerInstanceId();
		String taobaoNick = stateChangeEvent.getTaobaoNick();
		PartnerInstanceTypeEnum partnerType = stateChangeEvent.getPartnerType();

		// 已停业，去标
		if (PartnerInstanceStateChangeEnum.CLOSED.equals(stateChangeEnum)) {
			generalTaskSubmitService.submitRemoveUserTagTasks(taobaoUserId, taobaoNick, partnerType, operatorId);
		} 
	}

}
