package com.taobao.cun.auge.station.transfer.process.flow;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.taobao.cun.auge.dal.domain.CountyStationTransferJob;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.transfer.TransferException;
import com.taobao.cun.auge.station.transfer.TransferJobBo;
import com.taobao.cun.crius.bpm.dto.StartProcessInstanceDto;
import com.taobao.cun.crius.bpm.enums.UserTypeEnum;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;

/**
 * 交接工作流
 * 
 * @author chengyu.zhoucy
 *
 */
public abstract class AbstractTransferWorkflow implements TransferWorkflow {
	@Resource
	protected TransferJobBo transferJobBo;
	@Resource
    protected CuntaoWorkFlowService cuntaoWorkFlowService;
	@Resource
	protected CountyStationBO countyStationBO;

	protected void startWorkflow(String taskCode, CountyStationTransferJob countyStationTransferJob, Map<String, String> initData) throws TransferException {
		StartProcessInstanceDto startDto = new StartProcessInstanceDto();
		startDto.setBusinessCode(taskCode);
		startDto.setBusinessId(String.valueOf(countyStationTransferJob.getId()));
		startDto.setApplierId(countyStationTransferJob.getCreator());
		startDto.setApplierUserType(UserTypeEnum.BUC);
		startDto.setInitData(initData);
		ResultModel<Boolean> result = cuntaoWorkFlowService.startProcessInstance(startDto);
		if(!result.isSuccess()) {
			throw new TransferException("创建流程失败：" + result.getException().getMessage());
		}
	}
	
	protected void teminateFlow(List<CountyStationTransferJob> countyStationTransferJobs, String businessCode) {
		for(CountyStationTransferJob countyStationTransferJob : countyStationTransferJobs) {
			cuntaoWorkFlowService.teminateProcessInstance(String.valueOf(countyStationTransferJob.getId()), businessCode, countyStationTransferJob.getCreator());
			transferJobBo.updateTransferJobState(countyStationTransferJob.getId(), "CANCEL");
		}
	}

}
