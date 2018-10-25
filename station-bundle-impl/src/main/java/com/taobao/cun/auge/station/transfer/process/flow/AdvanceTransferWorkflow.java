package com.taobao.cun.auge.station.transfer.process.flow;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.CountyStationTransferJob;
import com.taobao.cun.auge.station.transfer.TransferException;

/**
 * 提前交接工作流
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class AdvanceTransferWorkflow extends AbstractTransferWorkflow{
	private static final String TASK_CODE = "extAdvanceTransfer";
	
	@Override
	public void start(CountyStationTransferJob countyStationTransferJob) throws TransferException{
		Map<String, String> initData = Maps.newHashMap();
		//设置发起组织
		CountyStation countyStation = countyStationBO.getCountyStationById(countyStationTransferJob.getCountyStationId());
		initData.put("orgId", String.valueOf(countyStation.getOrgId()));
		startWorkflow(TASK_CODE, countyStationTransferJob, initData);
	}

	@Override
	public void teminate(Long countyId) {
		List<CountyStationTransferJob> countyStationTransferJobs = transferJobBo.getCountyStationTransferJobs(countyId, "advance", "NEW");
		teminateFlow(countyStationTransferJobs, TASK_CODE);
	}
}
