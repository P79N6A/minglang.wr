package com.taobao.cun.auge.station.strategy;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.CloseStationApplyCloseReasonEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommonStrategy implements PartnerInstanceStrategy{
	
	@Autowired
    PartnerInstanceQueryService partnerInstanceQueryService;
	
	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;

	@Autowired
	private DiamondConfiguredProperties diamondConfiguredProperties;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

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
			return 0L;
		} 
		return forcedCloseDto.getId();
	}
	
	public Long findQuitApplyId(Long instanceId) {
		QuitStationApplyDto quitApply = partnerInstanceQueryService.getQuitStationApply(instanceId);
		if(null == quitApply){
			return 0L;
		}
		return quitApply.getId();
	}
	
	@Override
	public void partnerClosing(Long instanceId, OperatorDto operatorDto) {
	}
	
	@Override
	public void autoClosing(Long instanceId, OperatorDto operatorDto){
		
	}
	
	@Override
	public void closed(Long instanceId, Long taobaoUserId,String taobaoNick, PartnerInstanceTypeEnum typeEnum,OperatorDto operatorDto){
		generalTaskSubmitService.submitRemoveUserTagTasks(taobaoUserId, taobaoNick, typeEnum, operatorDto.getOperator(),instanceId);
		generalTaskSubmitService.submitClosedCainiaoStation(instanceId, operatorDto.getOperator());

		PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);
		generalTaskSubmitService.submitClosedUmTask(instance.getStationId(),operatorDto);
	}

	@Override
	public void quited(Long instanceId, OperatorDto operatorDto){
		PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);
		generalTaskSubmitService.submitClosedUmTask(instance.getStationId(),operatorDto);
	}
}
