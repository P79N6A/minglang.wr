package com.taobao.cun.auge.station.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.ProtocolBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.condition.ForcedCloseCondition;
import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTargetBizTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.auge.station.service.PatnerInstanceService;

public class PatnerInstanceServiceImpl implements PatnerInstanceService {

	private static final Logger logger = LoggerFactory.getLogger(PatnerInstanceService.class);

	@Autowired
	ProtocolBO protocolBO;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	PartnerInstanceHandler partnerInstanceHandler;
	
	@Autowired
	StationBO stationBO;

	@Override
	public Long addTemp(PartnerInstanceCondition condition) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long updateTemp(PartnerInstanceCondition condition) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long addSubmit(PartnerInstanceCondition condition) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long updateSubmit(PartnerInstanceCondition condition) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(PartnerInstanceCondition condition) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Long instanceId) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void signSettledProtocol(Long taobaoUserId) throws AugeServiceException {
		try {
			Long instanceId = partnerInstanceBO.findPartnerInstanceId(taobaoUserId, PartnerInstanceStateEnum.SETTLING);
			protocolBO.signProtocol(taobaoUserId, ProtocolTypeEnum.SETTLE_PRO, instanceId,
					ProtocolTargetBizTypeEnum.PARTNER_INSTANCE);
		} catch (Exception e) {
			logger.error(StationExceptionEnum.SIGN_SETTLE_PROTOCOL_FAIL.getDesc(), e);
			throw new AugeServiceException(StationExceptionEnum.SIGN_SETTLE_PROTOCOL_FAIL);
		}
	}

	@Override
	public void signManageProtocol(Long taobaoUserId) throws AugeServiceException {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean freezeBond(Long taobaoUserId, BigDecimal frozenMoney) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean openStation(Long partnerInstanceId, Date openDate, boolean isImme, String employeeId)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean applyCloseByPartner(Long taobaoUserId) throws AugeServiceException {
		try {
			Long partnerInstanceId = partnerInstanceBO.findPartnerInstanceId(taobaoUserId,PartnerInstanceStateEnum.SERVICING);
			if(partnerInstanceId == null) {
				throw new AugeServiceException(PartnerExceptionEnum.NO_RECORD);
			}
			partnerInstanceBO.changeState(partnerInstanceId, PartnerInstanceStateEnum.SERVICING, PartnerInstanceStateEnum.CLOSING, String.valueOf(taobaoUserId));
			return false;
		} catch (Exception e) {
			logger.error("applyCloseByPartner.error.param:"+taobaoUserId,e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	@Override
	public boolean confirmClose(Long partnerInstanceId, String employeeId, boolean isAgree)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean applyCloseByEmployee(ForcedCloseCondition forcedCloseCondition, String employeeId)
			throws AugeServiceException {
		try {
			Long instanceId = forcedCloseCondition.getInstanceId();
			PartnerStationRel partnerStationRel = partnerInstanceBO.findPartnerInstanceById(instanceId);

			// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
			partnerInstanceHandler.validateExistServiceChildren(
					PartnerInstanceTypeEnum.valueof(partnerStationRel.getType()), instanceId);

			// 合伙人实例停业中
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.SERVICING,
					PartnerInstanceStateEnum.CLOSING, employeeId);

			// 村点停业中
			stationBO.changeState(instanceId, StationStatusEnum.SERVICING, StationStatusEnum.CLOSING, employeeId);

			// 通过事件，定时钟，启动停业流程
			return true;

		} catch (Exception e) {
			logger.error(StationExceptionEnum.SIGN_SETTLE_PROTOCOL_FAIL.getDesc(), e);
			throw new AugeServiceException(StationExceptionEnum.SIGN_SETTLE_PROTOCOL_FAIL);
		}
	}

	@Override
	public boolean auditClose(ForcedCloseCondition forcedCloseCondition, String employeeId)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean applyQuitByEmployee(Long partnerInstanceId, String employeeId) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean auditQuit(ForcedCloseCondition forcedCloseCondition, String employeeId) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long applySettle(PartnerInstanceCondition condition, PartnerInstanceTypeEnum partnerInstanceTypeEnum)
			throws AugeServiceException {
		return partnerInstanceHandler.handleApplySettle(condition, partnerInstanceTypeEnum);
	}

}
