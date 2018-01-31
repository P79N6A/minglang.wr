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
     * 服务站物流能力信息维护
     * 
     * @param stationDto
     */
    public void applyLogisticAbility(StationDto stationDto);
    
    /**
     * 根据站点模式获取站点名称后缀
     * 
     * @param stationDto
     */
    public String getStationNameSuffix(Long stationId,String key);
    
    /**
     * 村站名称是否符合标准
     * 
     * @param instanceId、stationDto
     */
    public boolean getStationInfoValidateRule(Long instanceId,StationDto station);
}
