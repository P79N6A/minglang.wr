package com.taobao.cun.auge.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.common.lang.StringUtil;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.msg.dto.SmsSendDto;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.enums.DingtalkTemplateEnum;
import com.taobao.cun.auge.station.enums.TaskBusinessTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.chronus.dto.GeneralTaskDto;
import com.taobao.cun.chronus.service.TaskExecuteService;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("smsListener")
@EventSub(EventConstant.CUNTAO_STATION_STATUS_CHANGED_EVENT)
public class SmsListener implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(SmsListener.class);

	private static final String SMS_SEND_TYPE = "station_change_sms_type";

	@Autowired
	PartnerBO partnerBO;

	@Autowired
	TaskExecuteService taskExecuteService;

	@Autowired
	AppResourceBO appResourceBO;

	@Override
	public void onMessage(Event event) {
		PartnerInstanceStateChangeEvent stateChangeEvent = (PartnerInstanceStateChangeEvent) event.getValue();

		PartnerInstanceStateChangeEnum stateChangeEnum = stateChangeEvent.getStateChangeEnum();

		Long taobaoUserId = stateChangeEvent.getTaobaoUserId();
		String operatorId = stateChangeEvent.getOperator();

		// 由停业中，变更为已停业，去标,发短信
		String partnerMobile = findPartnerMobile(taobaoUserId);
		sms(taobaoUserId, partnerMobile, findSmsTemplate(stateChangeEnum), operatorId);
	}

	private DingtalkTemplateEnum findSmsTemplate(PartnerInstanceStateChangeEnum stateChangeEnum) {
		// 已停业
		if (PartnerInstanceStateChangeEnum.CLOSED.equals(stateChangeEnum)) {
			return DingtalkTemplateEnum.NODE_LEAVE;
		}
		// else if(){
		//
		// }

		throw new IllegalArgumentException("没有找到短信模板。stateChangeEnum=" + stateChangeEnum.getDescription());
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
			logger.error("查询合伙人手机号码失败。taobaoUserId=" + taobaoUserId);
			return "";
		}
	}

	private void sms(Long taobaoUserId, String mobile, DingtalkTemplateEnum dingTalkType, String operatorId) {
		if (StringUtil.isEmpty(mobile)) {
			return;
		}

		try {
			String content = appResourceBO.queryAppResourceValue(SMS_SEND_TYPE, dingTalkType.getCode());

			SmsSendDto smsDto = new SmsSendDto();

			smsDto.setContent(content);
			smsDto.setMobilelist(new String[] { mobile });
			smsDto.setOperator(operatorId);

			GeneralTaskDto task = new GeneralTaskDto();

			task.setBusinessNo(String.valueOf(taobaoUserId));
			task.setBeanName("messageService");
			task.setMethodName("sendSmsMsg");
			task.setBusinessStepNo(1l);
			task.setBusinessType(TaskBusinessTypeEnum.PARTNER_SMS.getCode());
			task.setBusinessStepDesc("发短信");
			task.setOperator(operatorId);
			task.setParameter(smsDto);
			taskExecuteService.submitTask(task);
		} catch (Exception e) {
			logger.error("dingtalkHelper execute error!" + " dingTalkType.getCode():" + dingTalkType.getCode());
		}
	}

}
