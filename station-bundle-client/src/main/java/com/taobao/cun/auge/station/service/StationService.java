package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.ShutDownStationApplyDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;

public interface StationService {
	
	/**
	 * 处理撤点审批结果
	 * 
	 * @param stationId
	 * @param approveResult
	 */
	public void auditQuitStation(Long stationId,ProcessApproveResultEnum approveResult);
	
	/**
	 * 申请撤点
	 * 
	 * @param quitDto
	 */
	public void applyShutDownStationByManager(ShutDownStationApplyDto shutDownDto) ;
	
	/**
     * 修改服务站信息
     * 
     * @param stationDto
     */
    public void updateStationInfo(StationDto stationDto);
}
