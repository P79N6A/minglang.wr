package com.taobao.cun.auge.station.transfer.ultimate.handle;

import javax.annotation.Priority;
import javax.annotation.Resource;

import com.taobao.cun.auge.annotation.Tag;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.station.transfer.process.flow.TransferWorkflow;
import org.springframework.stereotype.Component;

/**
 * 县村提前交接流程需要被取消
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
@Priority(2)
@Tag({HandlerGroup.AUTO})
public class AdvanceTransferWorkflowUltimateTransferHandler implements UltimateTransferHandler {
	@Resource
	private TransferWorkflow advanceTransferWorkflow;
	
	@Override
	public void transfer(CountyStation countyStation, String operator, Long opOrgId) {
		advanceTransferWorkflow.teminate(countyStation.getId());
	}
}
