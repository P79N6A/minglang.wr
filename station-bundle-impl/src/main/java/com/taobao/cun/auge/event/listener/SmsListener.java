package com.taobao.cun.auge.event.listener;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.buc.api.EnhancedUserQueryService;
import com.alibaba.buc.api.model.enhanced.EnhancedUser;
import com.alibaba.fastjson.JSON;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.configuration.TpaGmvCheckConfiguration;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.PartnerInstanceTypeChangeEvent;
import com.taobao.cun.auge.event.WisdomCountyApplyEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.enums.DingtalkTemplateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceCloseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.WisdomCountyStateEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("smsListener")
@EventSub({EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, EventConstant.WISDOM_COUNTY_APPLY_EVENT,EventConstant.PARTNER_INSTANCE_TYPE_CHANGE_EVENT})
public class SmsListener implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(SmsListener.class);

	private static final String SMS_SEND_TYPE = "station_change_sms_type";

	@Autowired
	PartnerBO partnerBO;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	AppResourceService appResourceService;

	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;

	@Autowired
	EnhancedUserQueryService enhancedUserQueryService;

	@Autowired
	DiamondConfiguredProperties diamondConfiguredProperties;
	
	@Autowired
	TpaGmvCheckConfiguration tpaGmvCheckConfiguration;
	
	@Override
	public void onMessage(Event event) {
		//智慧县域报名消息
		if (event.getValue() instanceof WisdomCountyApplyEvent){
			processWisdomCountyApplyEvent(event);
		}else if (event.getValue() instanceof PartnerInstanceTypeChangeEvent){
			//升降级事件
			processTypeChangeEvent((PartnerInstanceTypeChangeEvent)event.getValue());
		}else if (event.getValue() instanceof PartnerInstanceStateChangeEvent){
			//实例状态变更事件
			processPartnerInstanceStateChangeEvent((PartnerInstanceStateChangeEvent)event.getValue());
		}
	}
	
	private void processPartnerInstanceStateChangeEvent(PartnerInstanceStateChangeEvent stateChangeEvent){
		logger.info("receive event." + JSON.toJSONString(stateChangeEvent));
		smsPartner(stateChangeEvent);
		smsParentPartner(stateChangeEvent);
		logger.info("Finished to handle event." + JSON.toJSONString(stateChangeEvent));
	}
	
	private void smsPartner(PartnerInstanceStateChangeEvent stateChangeEvent){
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
		String content = appResourceService.queryAppResourceValue(SMS_SEND_TYPE, dingTalkType.getCode());

		generalTaskSubmitService.submitSmsTask(taobaoUserId, mobile, operatorId, content);
	}
	
	private void smsParentPartner(PartnerInstanceStateChangeEvent stateChangeEvent) {
		PartnerInstanceStateChangeEnum stateChangeEnum = stateChangeEvent.getStateChangeEnum();
		Long instanceId = stateChangeEvent.getPartnerInstanceId();
		String operatorId = stateChangeEvent.getOperator();
		PartnerInstanceTypeEnum partnerType = stateChangeEvent.getPartnerType();

		// 淘帮手自动停业
		if (PartnerInstanceStateChangeEnum.START_CLOSING.equals(stateChangeEnum)
				&& PartnerInstanceTypeEnum.TPA.equals(partnerType)) {
			// 淘帮手实例
			PartnerStationRel tpaInstance = partnerInstanceBO.findPartnerInstanceById(instanceId);

			if (PartnerInstanceCloseTypeEnum.SYSTEM_QUIT.getCode().equals(tpaInstance.getCloseType())) {
				// 父站点id
				Long parentStationId = tpaInstance.getParentStationId();
				// 合伙人实例
				PartnerStationRel tpInstance = partnerInstanceBO.findPartnerInstanceByStationId(parentStationId);
				
				// 查询合伙人手机号码
				Partner tpPartner = partnerBO.getPartnerById(tpInstance.getPartnerId());
				String tpMobile = tpPartner.getMobile();
				// 合伙人淘宝账号
				Long tpTaobaoUserId = tpPartner.getTaobaoUserId();
				
				// tpa partner
				Partner tpaPartner = partnerBO.getPartnerById(tpaInstance.getPartnerId());
				
				String content = appResourceService.queryAppResourceValue(SMS_SEND_TYPE,	DingtalkTemplateEnum.TPA_AUTO_CLOSE_SMS_2_TP.getCode());
				// 替换淘帮手姓名和减少淘帮手名额
				content = String.format(content, tpaPartner.getName(),tpaGmvCheckConfiguration.getReduceTpaNum4AutoClose());
				
				generalTaskSubmitService.submitSmsTask(tpTaobaoUserId, tpMobile, operatorId, content);
			}
		}
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
                    logger.info("phones " + JSON.toJSONString(phones));
					for (String phone : phones) {
						generalTaskSubmitService.submitSmsTask(Long.valueOf(creator), phone, applyEvent.getOperator(), content.toString());
					}
				}
			} else {
				logger.info("can not query mobile by " + creator);
			}
		} catch (Exception e) {
			logger.error("query mobile error by " + creator, e);
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
			PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);
			Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
			return partner.getMobile();
	}
	
	/**
	 * 升级事件处理器
	 * 
	 * @param event
	 */
	private void processTypeChangeEvent(PartnerInstanceTypeChangeEvent event) {
		PartnerInstanceTypeChangeEnum typeChangeEnum = event.getTypeChangeEnum();
		// 淘帮手升级为合伙人
		if (PartnerInstanceTypeChangeEnum.TPA_UPGRADE_2_TP.equals(typeChangeEnum)) {
			// 淘帮手实例
			PartnerStationRel tpaInstance = partnerInstanceBO.findPartnerInstanceById(event.getPartnerInstanceId());
			// 父站点id
			Long parentStationId = tpaInstance.getParentStationId();
			// 合伙人实例
			PartnerStationRel tpInstance = partnerInstanceBO.findPartnerInstanceByStationId(parentStationId);
			// 查询合伙人手机号码
			Partner tpPartner = partnerBO.getPartnerById(tpInstance.getPartnerId());
			String tpMobile = tpPartner.getMobile();
			// 合伙人淘宝账号
			Long tpTaobaoUserId = tpInstance.getTaobaoUserId();
			
			//tpa partner
			Partner tpaPartner = partnerBO.getPartnerById(tpaInstance.getPartnerId());

			String operatorId = event.getOperator();
			String content = appResourceService.queryAppResourceValue(SMS_SEND_TYPE,	DingtalkTemplateEnum.TPA_UPGRADE_2_TP.getCode());
			//替换淘帮手姓名和奖励淘帮手名额
			content = String.format(content, tpaPartner.getName(),	tpaGmvCheckConfiguration.getRewardTpaNum4TpaUpgrade());

			generalTaskSubmitService.submitSmsTask(tpTaobaoUserId, tpMobile, operatorId, content);
		}
	}
}