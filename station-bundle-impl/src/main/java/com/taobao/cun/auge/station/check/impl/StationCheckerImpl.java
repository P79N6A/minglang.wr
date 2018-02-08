package com.taobao.cun.auge.station.check.impl;

import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.check.StationChecker;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("stationChecker")
public class StationCheckerImpl implements StationChecker{
	
	private static final Logger logger = LoggerFactory.getLogger(StationChecker.class);
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Override
	public void checkShutdownApply(Long stationId) {
		// 校验村点上所有人是否都是退出待解冻、已退出的状态
		boolean isAllPartnerQuit = partnerInstanceBO.isAllPartnerQuit(stationId);
		if (!isAllPartnerQuit) {
			logger.warn("存在非退出，或者退出待解冻的合伙人，不可以撤点");
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"存在非退出，或者退出待解冻的合伙人，不可以撤点");
		}
	}
}
