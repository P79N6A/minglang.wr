package com.taobao.cun.auge.flowRecord.convert;

import com.taobao.cun.auge.dal.domain.CuntaoFlowRecord;
import com.taobao.cun.auge.flowRecord.dto.CuntaoFlowRecordDto;
import com.taobao.cun.auge.flowRecord.enums.CuntaoFlowRecordTargetTypeEnum;

public final class CuntaoFlowRecordEventConverter {

	private CuntaoFlowRecordEventConverter() {

	}
	
	public static CuntaoFlowRecordDto toCuntaoFlowRecordDto(CuntaoFlowRecord flowRecord) {
		if (null == flowRecord) {
			return null;
		}
		
		CuntaoFlowRecordDto recordDto = new CuntaoFlowRecordDto();
		
		recordDto.setFlowId(flowRecord.getFlowId());
		recordDto.setNodeTitle(flowRecord.getNodeTitle());
		recordDto.setOperateOpinion(flowRecord.getOperateOpinion());
		recordDto.setOperateTime(flowRecord.getOperateTime());
		recordDto.setOperatorName(flowRecord.getOperatorName());
		recordDto.setOperatorWorkid(flowRecord.getOperatorWorkid());
		recordDto.setRemarks(flowRecord.getRemarks());
		recordDto.setTargetId(flowRecord.getTargetId());
		recordDto.setTargetType(CuntaoFlowRecordTargetTypeEnum.valueof(flowRecord.getTargetType()));
		
		return recordDto;
	}

}
