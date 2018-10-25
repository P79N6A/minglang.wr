package com.taobao.cun.auge.station.transfer.ultimate;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.transfer.process.flow.TransferWorkflow;

/**
 * 提前发起交接
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class AdvanceUltimateTransferBo extends UltimateTransferBo{
	@Resource
	private TransferWorkflow countyTransferWorkflow;
	
	@Override
	protected void afterTransferProcess(Long countyStationId, String operator, Long opOrgId) {
		//终结县村交接流程
		countyTransferWorkflow.teminate(countyStationId);
	}
}
