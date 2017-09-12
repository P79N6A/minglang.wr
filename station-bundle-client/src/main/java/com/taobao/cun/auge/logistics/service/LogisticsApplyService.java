package com.taobao.cun.auge.logistics.service;

import java.util.List;

import com.taobao.cun.auge.logistics.dto.LogisticsApplyRequest;
import com.taobao.cun.auge.logistics.dto.LogisticsStationApplyDTO;

public interface LogisticsApplyService {

	/**
	 * 申请物流站点
	 * @param request
	 * @return
	 */
	public Boolean applyLogisticStation(LogisticsApplyRequest request);
	
	/**
	 * 获取待审核的物流申请单
	 * @param stationId
	 * @return
	 */
	public List<LogisticsStationApplyDTO> getLogisticsStationApply(List<Long> stationIds, Long orgId);
	
	public List<LogisticsStationApplyDTO> getLogisticsStationApply(List<Long> stationIds, List<Long> orgIds);
	
	public Boolean auditLogistcsStationApply(Long applyId, Boolean isPass,String remark);
	
	public Boolean finishLogisticStationApply(Long logisticsStationId, Boolean isPass);
	
	
	public LogisticsStationApplyDTO getLogisticsStationApply(Long applyId);
	
	
	
}
