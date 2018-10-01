package com.taobao.cun.auge.station.transfer.process;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.CountyStationTransferJob;
import com.taobao.cun.auge.station.bo.CountyStationBO;
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
@Component
public class TransferWorkflow {
	@Resource
    private CuntaoWorkFlowService  cuntaoWorkFlowService;
	@Resource
	private CountyStationBO countyStationBO;
	
	private static final String TASK_CODE = "extCountyTransfer";
	
	public void start(CountyStationTransferJob countyStationTransferJob) {
		StartProcessInstanceDto startDto = new StartProcessInstanceDto();

		startDto.setBusinessCode(TASK_CODE);
		startDto.setBusinessId(String.valueOf(countyStationTransferJob.getId()));
		startDto.setApplierId(countyStationTransferJob.getCreator());
		startDto.setApplierUserType(UserTypeEnum.BUC);
		Map<String, String> initData = Maps.newHashMap();
		initData.put("targetTeamOrgId", String.valueOf(countyStationTransferJob.getTargetTeamOrgId()));
		
		CountyStation countyStation = countyStationBO.getCountyStationById(countyStationTransferJob.getCountyStationId());
		initData.put("orgId", String.valueOf(countyStation.getOrgId()));
		startDto.setInitData(initData);
		
		ResultModel<Boolean> result = cuntaoWorkFlowService.startProcessInstance(startDto);
		if(!result.isSuccess()) {
			throw new RuntimeException(result.getException());
		}
	}
}
