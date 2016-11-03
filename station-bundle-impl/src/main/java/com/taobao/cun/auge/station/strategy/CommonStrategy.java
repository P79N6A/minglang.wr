package com.taobao.cun.auge.station.strategy;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.CloseStationApplyCloseReasonEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;

public class CommonStrategy {
	
	@Autowired
    PartnerInstanceQueryService partnerInstanceQueryService;
	
	@Autowired
	AppResourceBO appResourceBO;

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
	
	public Long findCloseApplyId(Long instanceId) {
		// 获取停业原因
		CloseStationApplyDto forcedCloseDto = partnerInstanceQueryService.getCloseStationApply(instanceId);
		if (null == forcedCloseDto) {
			return 0l;
		} 
		return forcedCloseDto.getId();
	}
	
	public Long findQuitApplyId(Long instanceId) {
		QuitStationApplyDto quitApply = partnerInstanceQueryService.getQuitStationApply(instanceId);
		if(null == quitApply){
			return 0l;
		}
		return quitApply.getId();
	}
	
	public String findApproveAclPermissionCode(ProcessBusinessEnum businessNum,PartnerInstanceTypeEnum typeEnum){
		return appResourceBO.queryAppResourceValue(businessNum.getCode(), typeEnum.getCode());
	}
}
