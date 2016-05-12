package com.taobao.cun.auge.station.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import com.taobao.cun.auge.station.condition.ForcedCloseCondition;
import com.taobao.cun.auge.station.condition.TPInstanceCondition;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.service.TPInstanceService;

public class TPInstanceServiceImpl implements TPInstanceService {

	@Override
	public Long tempPartnerStation(TPInstanceCondition condition) {		
		return null;
	}

	@Override
	public Long submitPartnerStation(TPInstanceCondition condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long submitTempPartnerStation(TPInstanceCondition condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updatePartnerStation(TPInstanceCondition condition) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deletePartnerStation(Long instanceId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean signSettledProtocol(Long taobaoUserId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean signManageProtocol(Long taobaoUserId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean freezeBond(Long taobaoUserId, BigDecimal frozenMoney) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean openStation(Long partnerInstanceId, Date openDate,
			boolean isImme, String employeeId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean quitApplyByPartner(Long taobaoUserId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean approvePartnerQuitApply(Long partnerInstanceId,
			String employeeId, boolean isAgree) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean applyForcedClose(ForcedCloseCondition forcedCloseCondition,
			String employeeId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean quitApplyByEmployee(Long partnerInstanceId, String employeeId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PartnerInstanceDto findSafedStationPartner(Long partnerStationId) {
		// TODO Auto-generated method stub
		return null;
	}

}
