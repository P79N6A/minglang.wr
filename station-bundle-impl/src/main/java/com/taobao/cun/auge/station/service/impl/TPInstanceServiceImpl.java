package com.taobao.cun.auge.station.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import com.taobao.cun.auge.station.condition.ForcedCloseCondition;
import com.taobao.cun.auge.station.condition.TPInstanceCondition;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.auge.station.service.TPInstanceService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
@HSFProvider(serviceInterface = TPInstanceService.class)
public class TPInstanceServiceImpl implements TPInstanceService {

	@Override
	public Long tempPartnerStation(TPInstanceCondition condition)
			throws AugeServiceException {
		if (1==1) {
			throw new AugeServiceException(StationExceptionEnum.STATION_NUM_IS_DUPLICATE.getCode(),StationExceptionEnum.STATION_NUM_IS_DUPLICATE.getDesc());
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long submitPartnerStation(TPInstanceCondition condition)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long submitTempPartnerStation(TPInstanceCondition condition)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updatePartnerStation(TPInstanceCondition condition)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deletePartnerStation(Long instanceId)
			throws AugeServiceException {
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
	public boolean quitApplyByPartner(Long taobaoUserId)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean approvePartnerQuitApply(Long partnerInstanceId,
			String employeeId, boolean isAgree) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean applyForcedClose(ForcedCloseCondition forcedCloseCondition,
			String employeeId) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean quitApplyByEmployee(Long partnerInstanceId, String employeeId)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PartnerInstanceDto findSafedStationPartner(Long partnerStationId)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
