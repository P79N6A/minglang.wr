package com.taobao.cun.auge.event.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.common.lang.StringUtil;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.enums.DingtalkTemplateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;
@Component("smsListener")
@EventSub(EventConstant.CUNTAO_STATION_STATUS_CHANGED_EVENT)
public class SmsListener implements EventListener {
	
	@Autowired
	PartnerBO partnerBO;

	@Override
	public void onMessage(Event event) {
		Map<String, Object> map = event.getContent();

		StationStatusEnum newStatus = (StationStatusEnum) map.get("newStatus");
		StationStatusEnum oldStatus = (StationStatusEnum) map.get("oldStatus");
		Long taobaoUserId = (Long) map.get("taobaoUserId");
		
		String mobile = findPartnerMobile(taobaoUserId);

		// 由停业中，变更为已停业，去标,发短信
		if (StationStatusEnum.CLOSED.equals(newStatus) && StationStatusEnum.CLOSING.equals(oldStatus)) {
			sms(mobile, DingtalkTemplateEnum.NODE_LEAVE);
		}
	}

	/**
	 * 查询合伙人手机号码
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	private String findPartnerMobile(Long taobaoUserId) {
		try {
			Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
			return partner.getMobile();
		} catch (AugeServiceException e) {
			//FIXME FHH
			
			return "";
		}
	}

	private void sms(String mobile, DingtalkTemplateEnum templateEnum) {
		if(StringUtil.isEmpty(mobile)){
			return;
		}
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
