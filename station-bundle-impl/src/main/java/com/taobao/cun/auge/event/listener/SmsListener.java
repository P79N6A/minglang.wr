package com.taobao.cun.auge.event.listener;

import java.util.Map;

import com.taobao.cun.auge.station.enums.DingtalkTemplateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.client.EventListener;

public class SmsListener implements EventListener {

	@Override
	public void onMessage(Event event) {

		Map<String, Object> map = event.getContent();

		StationStatusEnum newStatus = (StationStatusEnum) map.get("newStatus");
		StationStatusEnum oldStatus = (StationStatusEnum) map.get("oldStatus");

		String mobile = "";

		// 由停业中，变更为已停业，去标,发短信
		if (StationStatusEnum.CLOSED.equals(newStatus) && StationStatusEnum.CLOSING.equals(oldStatus)) {
			sms(mobile, DingtalkTemplateEnum.NODE_LEAVE);
		}
	}

	private void sms(String mobile, DingtalkTemplateEnum templateEnum) {
		// try {
		// String content =
		// appResourceQueryBo.appResValueFromTair(SMS_SEND_TYPE,
		// dingTalkType.getCode());
		// PartnerSmsDto sms = new PartnerSmsDto();
		// sms.setContent(content);
		// messageCenterServiceBo.sendSmsTest(sms, new String[]{mobileNum});
		// } catch (Exception e) {
		// log.error("dingtalkHelper execute error!" + "
		// ：dingTalkType.getCode():" + dingTalkType.getCode());
		// }
	}

}
