package com.taobao.cun.auge.station.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.event.listener.StartProcessListener;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.StartProcessDto;
import com.taobao.cun.auge.station.service.ProcessService;
import com.taobao.cun.crius.bpm.dto.CuntaoProcessInstance;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;

public class ProcessServiceImpl implements ProcessService {

	private static final Logger logger = LoggerFactory.getLogger(StartProcessListener.class);

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
		 String applierId = startProcessDto.getApplierId();
		 Long parentOrgId =startProcessDto.getParentOrgId();
		 String remarks =startProcessDto.getRemarks();
		
		
		// // 创建退出村点任务流程
		Map<String, String> initData = new HashMap<String, String>();
		initData.put("orgId", String.valueOf(parentOrgId));
		initData.put("remarks", remarks);
		ResultModel<CuntaoProcessInstance> rm = cuntaoWorkFlowService.startProcessInstance(businessCode,
				String.valueOf(businessId), applierId, initData);
		if (!rm.isSuccess()) {
			logger.error("启动审批流程失败。businessCode=" + businessCode + " businessId =" + businessId + "applier = " + applierId
					+ " parentOrgId = " + parentOrgId + " remarks = " + remarks, rm.getException());
		}
	}
}
