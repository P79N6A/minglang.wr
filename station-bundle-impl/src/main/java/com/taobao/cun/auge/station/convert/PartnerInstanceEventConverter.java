package com.taobao.cun.auge.station.convert;

import java.util.Date;

import com.taobao.cun.auge.event.domain.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;

/**
 * 合伙人实例状态转化事件
 * @author quanzhu.wangqz
 *
 */
public final class PartnerInstanceEventConverter {
	
	private PartnerInstanceEventConverter() {
	}
	
	public static PartnerInstanceStateChangeEvent convert(PartnerInstanceStateEnum prePartnerInstanceState,
			PartnerInstanceStateEnum curPartnerInstanceState,
			PartnerInstanceDto partnerInstanceDto) {
		PartnerInstanceStateChangeEvent event = new PartnerInstanceStateChangeEvent();
		event.setStationId(partnerInstanceDto.getStationDto().getId());
		event.setTaobaoUserId(partnerInstanceDto.getPartnerDto().getTaobaoUserId());
		event.setTaobaoNick(partnerInstanceDto.getPartnerDto().getTaobaoNick());
		event.setPartnerInstanceState(curPartnerInstanceState.getCode());
		event.setOwnOrgId(partnerInstanceDto.getStationDto().getApplyOrg());
		event.setPartnerType(partnerInstanceDto.getType().getCode());
		event.setExecDate(com.taobao.cun.auge.common.utils.DateUtil.formatTime(new Date()));
		if (partnerInstanceDto.getPartnerLifecycleDto() != null) {
			event.setLifecycleState(partnerInstanceDto.getPartnerLifecycleDto().getCurrentStep().getCode());
		}
		event.setPrePartnerInstanceState(prePartnerInstanceState.getCode());
		
		return event;
	}
}
