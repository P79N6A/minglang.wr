package com.taobao.cun.auge.cuncounty.bo;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyStateEnum;
import com.taobao.cun.crius.bpm.dto.StartProcessInstanceDto;
import com.taobao.cun.crius.bpm.enums.UserTypeEnum;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;

@Component
public class CuntaoCountyWaitOpenProcessBo {
	private static final String TASK_CODE = "countyWaitOpen";
	@Resource
	private CuntaoWorkFlowService cuntaoWorkFlowService;
	@Resource
	private CuntaoCountyBo cuntaoCountyBo;
	
	public void start(Long countyId, String operator) {
		CuntaoCountyDto cuntaoCountyDto = cuntaoCountyBo.getCuntaoCounty(countyId);
		if(checkState(cuntaoCountyDto)) {
			StartProcessInstanceDto startDto = new StartProcessInstanceDto();
			startDto.setBusinessCode(TASK_CODE);
			startDto.setBusinessId(String.valueOf(cuntaoCountyDto.getId()));
			startDto.setApplierId(operator);
			startDto.setApplierUserType(UserTypeEnum.BUC);
			startDto.setBusinessName("[" + cuntaoCountyDto.getName() + "]申请待开业");
			ResultModel<Boolean> result = cuntaoWorkFlowService.startProcessInstance(startDto);
			if(!result.isSuccess()) {
				throw new RuntimeException("创建流程失败：" + result.getException().getMessage());
			}
			cuntaoCountyBo.updateState(countyId, CuntaoCountyStateEnum.WAIT_OPEN_AUDIT.getCode(), operator);
		}else {
			throw new RuntimeException("当前状态下不能发起开业待审批流程");
		}
	}

	private boolean checkState(CuntaoCountyDto cuntaoCountyDto) {
		return cuntaoCountyDto.getState().getCode().equals(CuntaoCountyStateEnum.PLANNING.getCode()) || 
			cuntaoCountyDto.getState().getCode().equals(CuntaoCountyStateEnum.WAIT_OPEN_AUDIT_FAIL.getCode());
	}

	public void agree(Long countyId) {
		cuntaoCountyBo.updateState(countyId, CuntaoCountyStateEnum.WAIT_OPEN.getCode(), null);
	}

	public void deny(Long countyId) {
		cuntaoCountyBo.updateState(countyId, CuntaoCountyStateEnum.WAIT_OPEN_AUDIT_FAIL.getCode(), null);
	}
}
