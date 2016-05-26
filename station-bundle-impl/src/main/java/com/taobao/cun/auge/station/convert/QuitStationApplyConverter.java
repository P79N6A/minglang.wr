package com.taobao.cun.auge.station.convert;

import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.station.dto.QuitDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public final class QuitStationApplyConverter {
	
	private QuitStationApplyConverter(){
		
	}
	
	public static QuitStationApply convert(QuitDto quitDto, PartnerStationRel instance) throws AugeServiceException {
		QuitStationApply quitStationApply = new QuitStationApply();
		
		quitStationApply.setPartnerInstanceId(instance.getId());
		quitStationApply.setStationApplyId(instance.getStationApplyId());
		
		quitStationApply.setRevocationAppFormFileName(quitDto.getRevocationAppFormFileName());
		quitStationApply.setOtherDescription(quitDto.getOtherDescription());
		quitStationApply.setAssetType(quitDto.getAssertUseState().getCode());
		quitStationApply.setLoanHasClose(quitDto.getLoanHasClose());
		// FIXME FHH 枚举
		quitStationApply.setState("FINISHED");
		quitStationApply.setSubmittedPeopleName(quitDto.getOperatorName());
		return quitStationApply;
	}

}
