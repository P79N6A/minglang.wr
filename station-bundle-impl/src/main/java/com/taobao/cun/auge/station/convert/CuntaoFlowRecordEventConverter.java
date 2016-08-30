package com.taobao.cun.auge.station.convert;

import java.util.Date;

import com.taobao.cun.auge.event.domain.CuntaoFlowRecordEvent;
import com.taobao.cun.auge.station.dto.ProcessApproveResultDto;
import com.taobao.cun.auge.station.enums.CuntaoFlowRecordTargetTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;

public final class CuntaoFlowRecordEventConverter {

	private CuntaoFlowRecordEventConverter() {

	}

	public static CuntaoFlowRecordEvent convert(ProcessApproveResultDto approveResultDto) {
		Long stationApplyId = Long.valueOf(approveResultDto.getObjectId());
		String businessCode = approveResultDto.getBusinessCode();
		ProcessApproveResultEnum approveResult = approveResultDto.getResult();

		CuntaoFlowRecordEvent event = new CuntaoFlowRecordEvent();
		event.setOperateOpinion(approveResult.getCode());
		event.setOperateTime(new Date());
		event.setOperatorName("sys");
		event.setOperatorWorkid("sys");
		event.setRemarks(approveResultDto.getRemarks());
		event.setTargetId(stationApplyId);
		event.setTargetType(businessCode);
		event.setNodeTitle(approveResultDto.getBusinessCode());
		return event;
	}

	public static CuntaoFlowRecordEvent convert(Long stationApplyId, PartnerInstanceStateEnum state, String operatorId,
			String operatorName) {
		CuntaoFlowRecordEvent event = new CuntaoFlowRecordEvent();
		event.setOperateTime(new Date());
		event.setOperatorName(operatorName);
		event.setOperatorWorkid(operatorId);
		event.setTargetId(stationApplyId);
		event.setNodeTitle(state.getCode());
		event.setTargetType(CuntaoFlowRecordTargetTypeEnum.STATION_STATUS_CHANGE.getCode());

		return event;
	}
}
