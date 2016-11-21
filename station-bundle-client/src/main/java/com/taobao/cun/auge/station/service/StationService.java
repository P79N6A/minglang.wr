package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.ShutDownStationApplyDto;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeSystemException;

public interface StationService {
	
	/**
	 * 处理撤点审批结果
	 * 
	 * @param stationId
	 * @param approveResult
	 */
	public void auditQuitStation(Long stationId,ProcessApproveResultEnum approveResult)throws AugeBusinessException,AugeSystemException;
	
	/**
	 * 申请撤点
	 * 
	 * @param quitDto
	 */
	public void applyShutDownStationByManager(ShutDownStationApplyDto shutDownDto) throws AugeBusinessException,AugeSystemException;
}
