package com.taobao.cun.auge.station.transfer.ultimate.handle;

import javax.annotation.Priority;
import javax.annotation.Resource;

import com.taobao.cun.auge.annotation.Tag;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.station.transfer.process.flow.TransferWorkflow;
import org.springframework.stereotype.Component;

/**
 * 县村交接流程需要被取消
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
@Priority(1)
@Tag({HandlerGroup.AUTO, HandlerGroup.ADVANCE})
public class CountyTransferWorkflowUltimateTransferHandler implements UltimateTransferHandler {
	@Resource
	private TransferWorkflow countyTransferWorkflow;
	
	@Override
	public void transfer(CountyStation countyStation, String operator, Long opOrgId) {
		countyTransferWorkflow.teminate(countyStation.getId());
	}
}
