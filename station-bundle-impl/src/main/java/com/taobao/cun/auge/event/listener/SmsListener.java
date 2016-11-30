package com.taobao.cun.auge.event.listener;

import com.alibaba.buc.api.EnhancedUserQueryService;
import com.alibaba.buc.api.model.enhanced.EnhancedUser;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.event.WisdomCountyApplyEvent;
import com.taobao.cun.auge.station.enums.WisdomCountyStateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.enums.DingtalkTemplateEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

import java.util.Collections;
import java.util.Map;

@Component("smsListener")
@EventSub({EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, EventConstant.WISDOM_COUNTY_APPLY_EVENT})
public class SmsListener implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(SmsListener.class);

	private static final String SMS_SEND_TYPE = "station_change_sms_type";

	@Autowired
	PartnerBO partnerBO;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	AppResourceBO appResourceBO;

	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;

	@Autowired
	EnhancedUserQueryService enhancedUserQueryService;

	@Autowired
	DiamondConfiguredProperties diamondConfiguredProperties;

	@Override
	public void onMessage(Event event) {

		//智慧县域报名消息
		if (event.getValue() instanceof WisdomCountyApplyEvent){
			processWisdomCountyApplyEvent(event);
			return;
		}

		PartnerInstanceStateChangeEvent stateChangeEvent = (PartnerInstanceStateChangeEvent) event.getValue();

		logger.info("receive event." + JSON.toJSONString(stateChangeEvent));

		PartnerInstanceStateChangeEnum stateChangeEnum = stateChangeEvent.getStateChangeEnum();
		Long instanceId = stateChangeEvent.getPartnerInstanceId();
		Long taobaoUserId = stateChangeEvent.getTaobaoUserId();
		String operatorId = stateChangeEvent.getOperator();

		// 查询手机号码
		String mobile = findPartnerMobile(instanceId);
		// 查询短信模板
		DingtalkTemplateEnum dingTalkType = findSmsTemplate(stateChangeEnum);
		if (null == dingTalkType) {
			logger.info("没有找到钉钉模板.");
			return;
		}
		String content = appResourceBO.queryAppResourceValue(SMS_SEND_TYPE, dingTalkType.getCode());

		generalTaskSubmitService.submitSmsTask(taobaoUserId, mobile, operatorId, content);

		logger.info("Finished to handle event." + JSON.toJSONString(stateChangeEvent));
	}

	private void processWisdomCountyApplyEvent(Event event){
		WisdomCountyApplyEvent applyEvent = (WisdomCountyApplyEvent) event.getValue();
		logger.info("receive event." + JSON.toJSONString(applyEvent));
		String creator = applyEvent.getCreator();
		try {
			Map<String, EnhancedUser> users = enhancedUserQueryService.findUsers(Collections.singletonList(creator));
			if (users.get(creator) != null) {
				logger.info("users " + JSON.toJSONString(users.get(creator)));
				String mobile = users.get(creator).getCellphone();
				String name = users.get(creator).getLastName();
				StringBuilder content = new StringBuilder(name);
				WisdomCountyStateEnum type = applyEvent.getType();
				if (WisdomCountyStateEnum.AUDIT_PASS.equals(type)) {
					content.append(diamondConfiguredProperties.getPass());
					generalTaskSubmitService.submitSmsTask(Long.valueOf(creator), mobile, applyEvent.getOperator(), content.toString());
				} else if (WisdomCountyStateEnum.AUDIT_FAIL.equals(type)) {
					content.append(diamondConfiguredProperties.getFail());
					generalTaskSubmitService.submitSmsTask(Long.valueOf(creator), mobile, applyEvent.getOperator(), content.toString());
				} else if (WisdomCountyStateEnum.APPLY.equals(type)) {
					content.append(diamondConfiguredProperties.getApply());
					String[] phones = diamondConfiguredProperties.getMobile().split(",");
					for (String phone : phones) {
						generalTaskSubmitService.submitSmsTask(Long.valueOf(creator), phone, applyEvent.getOperator(), content.toString());
					}
				}
			} else {
				logger.info("can not query mobile by " + creator);
			}
		} catch (Exception e) {
			logger.error("can not query mobile by " + creator, e);
		}
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
		logger.warn("没有找到短信模板。stateChangeEnum=" + stateChangeEnum.getDescription());
		return null;
	}

	/**
	 * 查询合伙人手机号码
	 * 
	 * @param instanceId
	 * @return
	 */
	private String findPartnerMobile(Long instanceId) {
		try {
			PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);
			Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
			return partner.getMobile();
		} catch (AugeServiceException e) {
			logger.error("查询合伙人手机号码失败。instanceId=" + instanceId);
			return "";
		}
	}
}