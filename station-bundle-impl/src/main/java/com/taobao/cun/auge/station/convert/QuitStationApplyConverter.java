package com.taobao.cun.auge.station.convert;

import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.AssertUseStateEnum;

public final class QuitStationApplyConverter {

	private QuitStationApplyConverter() {

	}

	public static QuitStationApply convert(QuitStationApplyDto quitDto, PartnerStationRel instance,	String operatorName) {
		QuitStationApply quitStationApply = new QuitStationApply();

		quitStationApply.setPartnerInstanceId(instance.getId());
		quitStationApply.setStationApplyId(instance.getStationApplyId());

		quitStationApply.setRevocationAppFormFileName(quitDto.getRevocationAppFormFileName());
		quitStationApply.setOtherDescription(quitDto.getOtherDescription());
		if (null != quitDto.getAssertUseState()) {
			quitStationApply.setAssetType(quitDto.getAssertUseState().getCode());
		}
		quitStationApply.setLoanHasClose(quitDto.isLoanHasClose() ? "y" : "n");
		quitStationApply.setState("FINISHED");
		quitStationApply.setSubmittedPeopleName(operatorName);
		quitStationApply.setIsQuitStation(quitDto.getIsQuitStation() ? "y" : "n");
		return quitStationApply;
	}
	
	
	public static QuitStationApplyDto tQuitStationApplyDto(QuitStationApply quitStationApply) {
		if (quitStationApply == null) {
			return null;
		}

		QuitStationApplyDto quitStationApplyDto = new QuitStationApplyDto();

		quitStationApplyDto.setApprovalFileName(quitStationApply.getApprovalFileName());
		quitStationApplyDto.setAssertUseState(AssertUseStateEnum.valueof(quitStationApply.getAssetType()));
		quitStationApplyDto.setInstanceId(quitStationApply.getPartnerInstanceId());
		quitStationApplyDto.setIsQuitStation("y".equals(quitStationApply.getIsQuitStation()) ? Boolean.TRUE:Boolean.FALSE);
		quitStationApplyDto.setLoanHasClose("y".equals(quitStationApply.getLoanHasClose()) ? Boolean.TRUE:Boolean.FALSE);
		quitStationApplyDto.setLoanProveFileName(quitStationApply.getLoanProveFileName());
		quitStationApplyDto.setOtherDescription(quitStationApply.getOtherDescription());
		quitStationApplyDto.setRevocationAppFormFileName(quitStationApply.getRevocationAppFormFileName());
		quitStationApplyDto.setSubmittedPeopleName(quitStationApply.getSubmittedPeopleName());
		return quitStationApplyDto;
	}

}
