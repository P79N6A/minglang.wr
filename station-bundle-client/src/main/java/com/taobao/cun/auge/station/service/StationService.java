package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.ShutDownStationApplyDto;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface StationService {
	
	/**
	 * 处理撤点审批结果
	 * 
	 * @param stationId
	 * @param approveResult
	 * @throws AugeServiceException
	 */
	public void auditQuitStation(Long stationId,ProcessApproveResultEnum approveResult) throws AugeServiceException;
	
	/**
	 * 申请撤点
	 * 
	 * @param quitDto
	 * @throws AugeServiceException
	 */
	public void applyShutDownStationByManager(ShutDownStationApplyDto quitDto) throws AugeServiceException;
}
