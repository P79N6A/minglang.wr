package com.taobao.cun.auge.station.transfer.ultimate;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.transfer.process.flow.TransferWorkflow;

/**
 * N + 75这天会自动转交
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class AutoUltimateTransferBo extends UltimateTransferBo{
	@Resource
	private TransferWorkflow advanceTransferWorkflow;
	@Resource
	private TransferWorkflow countyTransferWorkflow;
	
	@Override
	protected void afterTransferProcess(Long countyStationId, String operator, Long opOrgId) {
		//终结县村交接流程
		countyTransferWorkflow.teminate(countyStationId);
		//终结提前交接流程
		advanceTransferWorkflow.teminate(countyStationId);
	}
}
