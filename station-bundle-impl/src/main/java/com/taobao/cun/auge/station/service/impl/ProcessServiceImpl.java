package com.taobao.cun.auge.station.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.StartProcessDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.ProcessService;
import com.taobao.cun.crius.bpm.dto.CuntaoProcessInstance;
import com.taobao.cun.crius.bpm.enums.UserTypeEnum;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("processService")
@HSFProvider(serviceInterface = ProcessService.class)
public class ProcessServiceImpl implements ProcessService {

	private static final Logger logger = LoggerFactory.getLogger(ProcessService.class);

	@Autowired
	private CuntaoWorkFlowService cuntaoWorkFlowService;

	@Autowired
	StationBO stationBO;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	/**
	 * 启动停业、退出流程审批流程
	 */
	@Override
	public void startApproveProcess(StartProcessDto startProcessDto) {
		String businessCode = startProcessDto.getBusinessCode();
		Long businessId = startProcessDto.getBusinessId();
		String remarks = startProcessDto.getRemarks();

		String applierId = startProcessDto.getOperator();
		Long applierOrgId = startProcessDto.getOperatorOrgId();
		OperatorTypeEnum operatorType = startProcessDto.getOperatorType();

		// // 创建退出村点任务流程
		Map<String, String> initData = new HashMap<String, String>();
		initData.put("orgId", String.valueOf(applierOrgId));
		initData.put("remarks", remarks);
		ResultModel<CuntaoProcessInstance> rm = cuntaoWorkFlowService.startProcessInstance(businessCode,
				String.valueOf(businessId), applierId, UserTypeEnum.valueof(operatorType.getCode()), initData);
		if (!rm.isSuccess()) {
			logger.error("启动审批流程失败。businessCode=" + businessCode + " businessId =" + businessId + "applier = "
					+ applierId + " applierOrgId = " + applierOrgId + " remarks = " + remarks, rm.getException());
			throw new AugeServiceException("启动流程失败。StartProcessDto = " + JSON.toJSONString(startProcessDto),
					rm.getException());
		}
	}
}
