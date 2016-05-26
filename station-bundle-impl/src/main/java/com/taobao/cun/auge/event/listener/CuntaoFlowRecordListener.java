package com.taobao.cun.auge.event.listener;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.station.enums.DingtalkTemplateEnum;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("cuntaoFlowRecordListener")
@EventSub(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT)
public class CuntaoFlowRecordListener implements EventListener{

	@Override
	public void onMessage(Event event) {
		PartnerInstanceStateChangeEvent stateChangeEvent = (PartnerInstanceStateChangeEvent) event.getValue();
		PartnerInstanceStateChangeEnum stateChangeEnum = stateChangeEvent.getStateChangeEnum();
		Long stationId = stateChangeEvent.getStationId();
		
		
//		// 正式提交后，入驻中，发短信
//				if (PartnerInstanceStateChangeEnum.START_SETTLING.equals(stateChangeEnum)) {
//					return DingtalkTemplateEnum.NODE_COMMIT;
//				} // 正式提交后，装修中，发短信
//				else if (PartnerInstanceStateChangeEnum.START_DECORATING.equals(stateChangeEnum)) {
//					return DingtalkTemplateEnum.NODE_RECV;
//				} // 正式提交后，服务中，发短信
//				else if (PartnerInstanceStateChangeEnum.START_SERVICING.equals(stateChangeEnum)) {
//					return DingtalkTemplateEnum.NODE_OPEN;
//				} // 申请停业,发短信
//				else if (PartnerInstanceStateChangeEnum.START_CLOSING.equals(stateChangeEnum)) {
//					return DingtalkTemplateEnum.NODE_LEAVE_APPLY;
//				} // 由停业中，变更为已停业,发短信
//				else if (PartnerInstanceStateChangeEnum.CLOSED.equals(stateChangeEnum)) {
//					return DingtalkTemplateEnum.NODE_LEAVE;
//				}
	}

}
