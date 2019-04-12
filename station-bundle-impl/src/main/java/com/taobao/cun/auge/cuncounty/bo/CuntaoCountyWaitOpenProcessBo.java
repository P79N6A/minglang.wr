package com.taobao.cun.auge.cuncounty.bo;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyStateEnum;
import com.taobao.cun.auge.cuncounty.exception.IllegalCountyStateException;
import com.taobao.cun.crius.bpm.dto.StartProcessInstanceDto;
import com.taobao.cun.crius.bpm.enums.UserTypeEnum;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;

/**
 * 县服务中心待开业审批流程

 * @author chengyu.zhoucy
 *
 */
@Component
public class CuntaoCountyWaitOpenProcessBo {
	private static final String TASK_CODE = "countyWaitOpen";
	@Resource
	private CuntaoWorkFlowService cuntaoWorkFlowService;
	@Resource
	private CuntaoCountyBo cuntaoCountyBo;
	@Resource
	private CainiaoCountyRemoteBo cainiaoCountySyncBo;
	
	public void start(Long countyId, String operator) {
		CuntaoCountyDto cuntaoCountyDto = cuntaoCountyBo.getCuntaoCounty(countyId);
		if(isNeedAuditState(cuntaoCountyDto)) {
			Map<String, String> initData = Maps.newHashMap();
			initData.put("orgId", String.valueOf(cuntaoCountyDto.getOrgId()));
			initData.put("title", "[" + cuntaoCountyDto.getName() + "]申请待开业");
			initData.put("content", "[" + cuntaoCountyDto.getName() + "]申请待开业");
			StartProcessInstanceDto startDto = new StartProcessInstanceDto();
			startDto.setBusinessCode(TASK_CODE);
			startDto.setBusinessId(String.valueOf(cuntaoCountyDto.getId()));
			startDto.setApplierId(operator);
			startDto.setApplierUserType(UserTypeEnum.BUC);
			startDto.setBusinessName("[" + cuntaoCountyDto.getName() + "]申请待开业");
			startDto.setInitData(initData);
			ResultModel<Boolean> result = cuntaoWorkFlowService.startProcessInstance(startDto);
			if(!result.isSuccess()) {
				throw new RuntimeException("创建流程失败：" + result.getException().getMessage());
			}
			cuntaoCountyBo.updateState(countyId, CuntaoCountyStateEnum.WAIT_OPEN_AUDIT.getCode(), operator);
		}else {
			throw new IllegalCountyStateException("当前状态下不能发起开业待审批流程");
		}
	}

	public boolean isNeedAuditState(CuntaoCountyDto cuntaoCountyDto) {
		return cuntaoCountyDto.getState().getCode().equals(CuntaoCountyStateEnum.PLANNING.getCode()) || 
			cuntaoCountyDto.getState().getCode().equals(CuntaoCountyStateEnum.WAIT_OPEN_AUDIT_FAIL.getCode());
	}

	@Transactional(rollbackFor=Throwable.class)
	public void agree(Long countyId) {
		cuntaoCountyBo.startOperate(countyId);
		cainiaoCountySyncBo.createCainiaoCounty(countyId);
	}

	public void deny(Long countyId) {
		cuntaoCountyBo.updateState(countyId, CuntaoCountyStateEnum.WAIT_OPEN_AUDIT_FAIL.getCode(), null);
	}
}
