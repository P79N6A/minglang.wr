package com.taobao.cun.auge.station.transfer.process.flow;

import java.util.List;

import javax.annotation.Resource;

import com.taobao.cun.auge.dal.domain.CountyStationTransferJob;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.transfer.TransferJobBo;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;

public abstract class AbstractTransferWorkflow implements TransferWorkflow {
	@Resource
	protected TransferJobBo transferJobBo;
	@Resource
    protected CuntaoWorkFlowService cuntaoWorkFlowService;
	@Resource
	protected CountyStationBO countyStationBO;

	public void teminateFlow(List<CountyStationTransferJob> countyStationTransferJobs, String businessCode) {
		for(CountyStationTransferJob countyStationTransferJob : countyStationTransferJobs) {
			cuntaoWorkFlowService.teminateProcessInstance(String.valueOf(countyStationTransferJob.getId()), businessCode, countyStationTransferJob.getCreator());
			transferJobBo.updateTransferJobState(countyStationTransferJob.getId(), "CANCEL");
		}
	}

}
