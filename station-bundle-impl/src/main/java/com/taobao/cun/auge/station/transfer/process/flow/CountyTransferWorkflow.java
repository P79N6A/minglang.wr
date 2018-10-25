package com.taobao.cun.auge.station.transfer.process.flow;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.CountyStationTransferJob;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.ExtDeptOrgClient;
import com.taobao.cun.auge.station.transfer.TransferException;
import com.taobao.cun.auge.station.transfer.TransferItemBo;
import com.taobao.cun.auge.station.transfer.state.StationTransferStateMgrBo;

/**
 * 交接工作流
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CountyTransferWorkflow extends AbstractTransferWorkflow{
	@Resource
	private TransferItemBo transferItemBo;
	@Resource
	private ExtDeptOrgClient extDeptOrgClient;
	@Resource
	private StationTransferStateMgrBo stationTransferStateMgrBo;
	
	private static final String TASK_CODE = "extCountyTransfer";
	
	@Override
	public void start(CountyStationTransferJob countyStationTransferJob) throws TransferException{
		Map<String, String> initData = Maps.newHashMap();
		initData.put("targetTeamOrgId", String.valueOf(countyStationTransferJob.getTargetTeamOrgId()));
		CountyStation countyStation = countyStationBO.getCountyStationById(countyStationTransferJob.getCountyStationId());
		initData.put("orgId", String.valueOf(countyStation.getOrgId()));
		//拓展队
		Optional<CuntaoOrgDto> optional = extDeptOrgClient.getExtTeamByCountyOrg(countyStation.getOrgId());
		if(optional.isPresent()) {
			initData.put("extTeamOrgId", String.valueOf(optional.get().getId()));
		}else {
			throw new TransferException("找不到该县域的拓展队");
		}
		
		startWorkflow(TASK_CODE, countyStationTransferJob, initData);
	}

	@Override
	public void teminate(Long countyId) {
		List<CountyStationTransferJob> countyStationTransferJobs = transferJobBo.getCountyStationTransferJobs(countyId, "advance", "NEW");
		teminateFlow(countyStationTransferJobs, TASK_CODE);
		for(CountyStationTransferJob countyStationTransferJob : countyStationTransferJobs) {
			transferItemBo.updateStateByJobId(countyStationTransferJob.getId(), "CANCEL");
			//更新站点的转交状态为WAITING
			List<String> stationIdList = Splitter.on(',').splitToList(countyStationTransferJob.getStationIdList());
			
			List<Long> stationIds = Lists.newArrayList();
			for(String stationId : stationIdList) {
				stationIds.add(Long.valueOf(stationId));
			}
			stationTransferStateMgrBo.cancelTransfer(stationIds);
		}
	}
}
