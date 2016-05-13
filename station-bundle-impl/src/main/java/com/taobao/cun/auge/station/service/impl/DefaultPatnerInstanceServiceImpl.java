package com.taobao.cun.auge.station.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import com.taobao.cun.auge.station.condition.ForcedCloseCondition;
import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.PatnerInstanceService;

public class DefaultPatnerInstanceServiceImpl implements PatnerInstanceService {

	@Override
	public Long addTemp(PartnerInstanceCondition condition)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long updateTemp(PartnerInstanceCondition condition)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long addSubmit(PartnerInstanceCondition condition)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long updateSubmit(PartnerInstanceCondition condition)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(PartnerInstanceCondition condition)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Long instanceId) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean signSettledProtocol(Long taobaoUserId)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean signManageProtocol(Long taobaoUserId)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean freezeBond(Long taobaoUserId, BigDecimal frozenMoney)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean openStation(Long partnerInstanceId, Date openDate,
			boolean isImme, String employeeId) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean applyCloseByPartner(Long taobaoUserId)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean confirmClose(Long partnerInstanceId, String employeeId,
			boolean isAgree) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean applyCloseByEmployee(
			ForcedCloseCondition forcedCloseCondition, String employeeId)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean auditClose(ForcedCloseCondition forcedCloseCondition,
			String employeeId) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean applyQuitByEmployee(Long partnerInstanceId, String employeeId)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean auditQuit(ForcedCloseCondition forcedCloseCondition,
			String employeeId) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PartnerInstanceDto querySafedInfo(Long partnerInstanceId)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PartnerInstanceDto queryInfo(Long partnerInstanceId)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
