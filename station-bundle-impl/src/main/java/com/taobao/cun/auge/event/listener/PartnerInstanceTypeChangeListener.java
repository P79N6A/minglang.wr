package com.taobao.cun.auge.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.PartnerInstanceTypeChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum;
import com.taobao.cun.auge.station.dto.PartnerInstanceExtDto;
import com.taobao.cun.auge.station.service.PartnerInstanceExtService;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("partnerInstanceTypeChangeListener")
@EventSub({ StationBundleEventConstant.PARTNER_INSTANCE_TYPE_CHANGE_EVENT })
public class PartnerInstanceTypeChangeListener implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceTypeChangeListener.class);
	
	@Autowired
	PartnerInstanceExtService partnerInstanceExtService;
	
	@Override
	public void onMessage(Event event) {
		PartnerInstanceTypeChangeEvent typeChangeEvent = (PartnerInstanceTypeChangeEvent) event.getValue();

		logger.info("receive event." + JSON.toJSONString(typeChangeEvent));

		PartnerInstanceTypeChangeEnum typeChangeEnum = typeChangeEvent.getTypeChangeEnum();
		Long instanceId = typeChangeEvent.getPartnerInstanceId();
		//降级
		if (PartnerInstanceTypeChangeEnum.TP_DEGREE_2_TPA.equals(typeChangeEnum)) {
			PartnerInstanceExtDto instanceExtDto = new PartnerInstanceExtDto ();
			instanceExtDto.setInstanceId(instanceId);
			instanceExtDto.setMaxChildNum(0);
			instanceExtDto.copyOperatorDto(typeChangeEvent);
			
			partnerInstanceExtService.savePartnerExtInfo(instanceExtDto);
		}
	}
}
