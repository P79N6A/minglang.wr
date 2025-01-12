package com.taobao.cun.auge.event.listener;

import com.alibaba.fastjson.JSON;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.event.PartnerInstanceTypeChangeEvent;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.PartnerChildMaxNumUpdateDto;
import com.taobao.cun.auge.station.enums.PartnerMaxChildNumChangeReasonEnum;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerInstanceExtService;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("partnerInstanceTypeChangeListener")
@EventSub({ StationBundleEventConstant.PARTNER_INSTANCE_TYPE_CHANGE_EVENT })
public class PartnerInstanceTypeChangeListener implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceTypeChangeListener.class);

	@Autowired
	PartnerInstanceExtService partnerInstanceExtService;
	
	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	PartnerBO partnerBO;

	@Override
	public void onMessage(Event event) {
		PartnerInstanceTypeChangeEvent typeChangeEvent = (PartnerInstanceTypeChangeEvent) event.getValue();

		logger.info("receive event." + JSON.toJSONString(typeChangeEvent));

		PartnerInstanceTypeChangeEnum typeChangeEnum = typeChangeEvent.getTypeChangeEnum();
		Long instanceId = typeChangeEvent.getPartnerInstanceId();
		// 降级
		if (PartnerInstanceTypeChangeEnum.TP_DEGREE_2_TPA.equals(typeChangeEnum)) {
			PartnerChildMaxNumUpdateDto updateDto = new PartnerChildMaxNumUpdateDto();
			updateDto.setInstanceId(instanceId);
			updateDto.setMaxChildNum(0);
			updateDto.setReason(PartnerMaxChildNumChangeReasonEnum.TP_DEGREE_2_TPA);
			updateDto.copyOperatorDto(typeChangeEvent);

			partnerInstanceExtService.updatePartnerMaxChildNum(updateDto);
		}else if(PartnerInstanceTypeChangeEnum.TPA_UPGRADE_2_TP.equals(typeChangeEnum)){
			//淘帮手升级
			PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);
			Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
			
			//淘帮手升级为合伙人，去淘帮手标
			generalTaskSubmitService.submitRemoveUserTagTasks(partner.getTaobaoUserId(), partner.getTaobaoNick(), typeChangeEnum.getPrePartnerInstanceType(), typeChangeEvent.getOperator(), instanceId);
		}else if(PartnerInstanceTypeChangeEnum.CANCEL_TPA_UPGRADE_2_TP.equals(typeChangeEnum)){
			//淘帮手撤销升级为合伙人，打标淘帮手标
			generalTaskSubmitService.submitAddUserTagTasks(instanceId, typeChangeEvent.getOperator());
		}
	}
}
