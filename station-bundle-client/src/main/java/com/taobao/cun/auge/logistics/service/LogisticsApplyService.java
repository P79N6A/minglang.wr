package com.taobao.cun.auge.logistics.service;

import java.util.List;

import com.taobao.cun.auge.logistics.dto.LogisticsApplyRequest;
import com.taobao.cun.auge.logistics.dto.LogisticsStationApplyDTO;
import com.taobao.cun.crius.common.resultmodel.ResultModel;

public interface LogisticsApplyService {

	/**
	 * 申请物流站点
	 * @param request
	 * @return
	 */
	public ResultModel<Boolean> applyLogisticStation(LogisticsApplyRequest request);
	
	/**
	 * 获取待审核的物流申请单
	 * @param stationId
	 * @return
	 */
	public ResultModel<List<LogisticsStationApplyDTO>> getLogisticsStationApply(List<Long> stationIds, Long orgId);
	
	public ResultModel<List<LogisticsStationApplyDTO>> getLogisticsStationApply(List<Long> stationIds, List<Long> orgIds);
	
	public ResultModel<Boolean> auditLogistcsStationApply(Long applyId, Boolean isPass,String remark);
	
	public ResultModel<Boolean> finishLogisticStationApply(Long logisticsStationId, Boolean isPass);
	
	
	public ResultModel<LogisticsStationApplyDTO> getLogisticsStationApply(Long applyId);
	
	
	
}
