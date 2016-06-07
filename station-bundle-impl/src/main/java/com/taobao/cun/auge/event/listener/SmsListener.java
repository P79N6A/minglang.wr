package com.taobao.cun.auge.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.common.lang.StringUtil;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.msg.dto.SmsSendDto;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.enums.DingtalkTemplateEnum;
import com.taobao.cun.auge.station.enums.TaskBusinessTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.chronus.dto.GeneralTaskDto;
import com.taobao.cun.chronus.service.TaskExecuteService;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("smsListener")
@EventSub(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT)
public class SmsListener implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(SmsListener.class);

	private static final String SMS_SEND_TYPE = "station_change_sms_type";

	@Autowired
	PartnerBO partnerBO;
	@Autowired
	PartnerInstanceBO partnerInstanceBO;

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

		String partnerMobile = findPartnerMobile(taobaoUserId);
		sms(taobaoUserId, partnerMobile, findSmsTemplate(stateChangeEnum), operatorId);
	}

	private DingtalkTemplateEnum findSmsTemplate(PartnerInstanceStateChangeEnum stateChangeEnum) {
		// 正式提交后，入驻中，发短信
		if (PartnerInstanceStateChangeEnum.START_SETTLING.equals(stateChangeEnum)) {
			return DingtalkTemplateEnum.NODE_COMMIT;
		} // 正式提交后，装修中，发短信
		else if (PartnerInstanceStateChangeEnum.START_DECORATING.equals(stateChangeEnum)) {
			return DingtalkTemplateEnum.NODE_RECV;
		} // 正式提交后，服务中，发短信
		else if (PartnerInstanceStateChangeEnum.START_SERVICING.equals(stateChangeEnum)) {
			return DingtalkTemplateEnum.NODE_OPEN;
		} // 申请停业,发短信
		else if (PartnerInstanceStateChangeEnum.START_CLOSING.equals(stateChangeEnum)) {
			return DingtalkTemplateEnum.NODE_LEAVE_APPLY;
		} // 由停业中，变更为已停业,发短信
		else if (PartnerInstanceStateChangeEnum.CLOSED.equals(stateChangeEnum)) {
			return DingtalkTemplateEnum.NODE_LEAVE;
		}
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
			PartnerStationRel instance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
			Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
			return partner.getMobile();
		} catch (AugeServiceException e) {
			logger.error("查询合伙人手机号码失败。taobaoUserId=" + taobaoUserId);
			return "";
		}
	}

	/**
	 * 发短信
	 * 
	 * @param taobaoUserId
	 * @param mobile
	 * @param dingTalkType
	 * @param operatorId
	 */
	private void sms(Long taobaoUserId, String mobile, DingtalkTemplateEnum dingTalkType, String operatorId) {
		if (StringUtil.isEmpty(mobile)) {
			logger.error("合伙人手机号码为空。taobaoUserId=" + taobaoUserId);
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
			logger.error("Failed to send sms. dingTalkType.getCode()=" + dingTalkType.getCode() + " taobaouserid = " + taobaoUserId, e);
		}
	}

}