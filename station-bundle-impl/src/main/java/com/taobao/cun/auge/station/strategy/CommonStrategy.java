package com.taobao.cun.auge.station.strategy;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.CloseStationApplyCloseReasonEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.station.sync.StationApplySyncBO;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommonStrategy implements PartnerInstanceStrategy{
	
	@Autowired
    PartnerInstanceQueryService partnerInstanceQueryService;
	
	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;

	@Autowired
	StationApplySyncBO stationApplySyncBO;

	public void syncStationApply(Long partnerInstanceId,SyncStationApplyEnum type){
		stationApplySyncBO.updateStationApply(partnerInstanceId, type);
	}

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
	}
}
