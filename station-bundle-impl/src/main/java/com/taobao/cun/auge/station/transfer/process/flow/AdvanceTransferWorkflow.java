package com.taobao.cun.auge.station.transfer.process.flow;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.CountyStationTransferJob;
import com.taobao.cun.auge.station.transfer.TransferException;
import com.taobao.cun.crius.bpm.dto.StartProcessInstanceDto;
import com.taobao.cun.crius.bpm.enums.UserTypeEnum;
import com.taobao.cun.crius.common.resultmodel.ResultModel;

/**
 * 交接工作流
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class AdvanceTransferWorkflow extends AbstractTransferWorkflow{
	private static final String TASK_CODE = "extAdvanceTransfer";
	
	@Override
	public void start(CountyStationTransferJob countyStationTransferJob) throws TransferException{
		StartProcessInstanceDto startDto = new StartProcessInstanceDto();

		startDto.setBusinessCode(TASK_CODE);
		startDto.setBusinessId(String.valueOf(countyStationTransferJob.getId()));
		startDto.setApplierId(countyStationTransferJob.getCreator());
		startDto.setApplierUserType(UserTypeEnum.BUC);
		Map<String, String> initData = Maps.newHashMap();
		CountyStation countyStation = countyStationBO.getCountyStationById(countyStationTransferJob.getCountyStationId());
		initData.put("orgId", String.valueOf(countyStation.getOrgId()));
		startDto.setInitData(initData);
		
		ResultModel<Boolean> result = cuntaoWorkFlowService.startProcessInstance(startDto);
		if(!result.isSuccess()) {
			throw new TransferException("创建流程失败：" + result.getException().getMessage());
		}
	}

	@Override
	public void teminate(Long countyId) {
		List<CountyStationTransferJob> countyStationTransferJobs = transferJobBo.getCountyStationTransferJobs(countyId, "advance", "NEW");
		teminateFlow(countyStationTransferJobs, TASK_CODE);
	}
}
