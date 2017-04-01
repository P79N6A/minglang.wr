package com.taobao.cun.auge.station.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.common.utils.FeatureUtil;
import com.taobao.cun.auge.incentive.IncentiveAuditFlowService;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelProcessDto;
import com.taobao.cun.auge.station.dto.StartProcessDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.ProcessService;
import com.taobao.cun.auge.station.service.interfaces.LevelAuditFlowService;
import com.taobao.cun.crius.bpm.dto.StartProcessInstanceDto;
import com.taobao.cun.crius.bpm.enums.UserTypeEnum;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("processService")
@HSFProvider(serviceInterface = ProcessService.class, clientTimeout=15000)
public class ProcessServiceImpl implements ProcessService {

	private static final Logger logger = LoggerFactory.getLogger(ProcessService.class);

	@Autowired
	private CuntaoWorkFlowService cuntaoWorkFlowService;

	@Autowired
	LevelAuditFlowService levelAuditFlowService;

	@Autowired
	IncentiveAuditFlowService incentiveAuditFlowService;

	/**
	 * 启动停业、退出流程审批流程
	 */
	@Override
	public void startApproveProcess(StartProcessDto startProcessDto) {
		ProcessBusinessEnum business = startProcessDto.getBusiness();
		String businessCode = business.getCode();
		Long businessId = startProcessDto.getBusinessId();
		String applierId = startProcessDto.getOperator();
		OperatorTypeEnum operatorType = startProcessDto.getOperatorType();

		// 创建退出村点任务流程
		Map<String, String> initData = new HashMap<String, String>(FeatureUtil.toMap(startProcessDto.getJsonParams()));
		Long orgId = null != startProcessDto.getBusinessOrgId() ? startProcessDto.getBusinessOrgId()
				: startProcessDto.getOperatorOrgId();
		initData.put("orgId", String.valueOf(orgId));

		StartProcessInstanceDto startDto = new StartProcessInstanceDto();

		startDto.setBusinessCode(businessCode);
		startDto.setBusinessId(String.valueOf(businessId));

		if (StringUtil.isNotBlank(startProcessDto.getBusinessName())) {
			startDto.setBusinessName("(" + startProcessDto.getBusinessName() + ")" + business.getDesc());
		}

		startDto.setApplierId(applierId);
		startDto.setApplierUserType(UserTypeEnum.valueof(operatorType.getCode()));
		startDto.setInitData(initData);

		ResultModel<Boolean> rm = cuntaoWorkFlowService.startProcessInstance(startDto);
		if (!rm.isSuccess()) {
			logger.error("启动审批流程失败。StartProcessDto = " + JSON.toJSONString(startProcessDto), rm.getException());
			throw new AugeServiceException("启动流程失败。StartProcessDto = " + JSON.toJSONString(startProcessDto),
					rm.getException());
		}
	}

	@Override
	public void startLevelApproveProcess(PartnerInstanceLevelProcessDto levelProcessDto){
	    levelAuditFlowService.startApproveProcess(levelProcessDto);
	}

	@Override
	public void startIncentiveProgramAuditProcess(StartProcessDto startProcessDto) {
		incentiveAuditFlowService.startProcess(startProcessDto);
	}
}
