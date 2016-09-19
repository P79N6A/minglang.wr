package com.taobao.cun.auge.station.strategy;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.enums.CloseStationApplyCloseReasonEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;

public class CommonStrategy {
	
	@Autowired
    PartnerInstanceQueryService partnerInstanceQueryService;

	public String findCloseReason(Long instanceId) {
		// 获取停业原因
		CloseStationApplyDto forcedCloseDto = partnerInstanceQueryService.getCloseStationApply(instanceId);
		String remark;
		if (null == forcedCloseDto) {
			remark = "";
		} else {
			remark = CloseStationApplyCloseReasonEnum.OTHER.equals(forcedCloseDto.getCloseReason())
					? forcedCloseDto.getOtherReason() : forcedCloseDto.getCloseReason().getDesc();
		}
		return remark;
	}
}
