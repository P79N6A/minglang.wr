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
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelProcessDto;
import com.taobao.cun.auge.station.dto.StartProcessDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.ProcessService;
import com.taobao.cun.crius.bpm.dto.CuntaoProcessInstance;
import com.taobao.cun.crius.bpm.enums.UserTypeEnum;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.util.CalendarUtil;

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
		ProcessBusinessEnum business = startProcessDto.getBusiness();
		Long businessId = startProcessDto.getBusinessId();

		String applierId = startProcessDto.getOperator();
		Long applierOrgId = startProcessDto.getOperatorOrgId();
		OperatorTypeEnum operatorType = startProcessDto.getOperatorType();

		// 创建退出村点任务流程
		Map<String, String> initData = new HashMap<String, String>(FeatureUtil.toMap(startProcessDto.getJsonParams()));
		initData.put("orgId", String.valueOf(applierOrgId));
		if (StringUtil.isNotBlank(startProcessDto.getBusinessName())) {
			initData.put("taskName", "(" + startProcessDto.getBusinessName() + ")" + business.getDesc());
		}
		
		ResultModel<CuntaoProcessInstance> rm = cuntaoWorkFlowService.startProcessInstance(business.getCode(),
				String.valueOf(businessId), applierId, UserTypeEnum.valueof(operatorType.getCode()), initData);
		if (!rm.isSuccess()) {
			 logger.error("启动审批流程失败。StartProcessDto = " + JSON.toJSONString(startProcessDto), rm.getException());
			throw new AugeServiceException("启动流程失败。StartProcessDto = " + JSON.toJSONString(startProcessDto),
					rm.getException());
		}
	}
		
	
	public void startLevelApproveProcess(PartnerInstanceLevelProcessDto levelProcessDto){
		String businessCode = levelProcessDto.getBusinessCode();
		Long businessId = levelProcessDto.getBusinessId();

		String applierId = String.valueOf(levelProcessDto.getEmployeeId());
		Long applierOrgId = levelProcessDto.getCountyOrgId();
		OperatorTypeEnum operatorType = OperatorTypeEnum.BUC;

		//  创建村点任务流程
		Map<String, String> initData = new HashMap<String, String>();
		initData.put("orgId", String.valueOf(applierOrgId));
		initData.put("countyStationName", levelProcessDto.getCountyStationName());
		initData.put("applyTime", CalendarUtil.formatDate(levelProcessDto.getApplyTime(),CalendarUtil.TIME_PATTERN));
		initData.put("partnerTaobaoUserId", String.valueOf(levelProcessDto.getPartnerTaobaoUserId()));
		initData.put("partnerName", levelProcessDto.getPartnerName());
		initData.put("currentLevel", levelProcessDto.getCurrentLevel().getLevel().toString());
		initData.put("currentLevelDesc", levelProcessDto.getCurrentLevel().getDescription());
		initData.put("expectedLevel", levelProcessDto.getExpectedLevel().getLevel().toString());
		initData.put("expectedLevelDesc", levelProcessDto.getExpectedLevel().getDescription());
		initData.put("score", levelProcessDto.getScore().toString());
		initData.put("monthlyIncome", levelProcessDto.getMonthlyIncome().toString());
		initData.put("stationId", String.valueOf(levelProcessDto.getStationId()));
		initData.put("stationName", levelProcessDto.getStationName());
		initData.put("employeeName", levelProcessDto.getEmployeeName());
		initData.put("employeeId", levelProcessDto.getEmployeeId());
		initData.put("evaluateInfo", levelProcessDto.getEvaluateInfo());

		
		ResultModel<CuntaoProcessInstance> rm = cuntaoWorkFlowService.startProcessInstance(businessCode,
				String.valueOf(businessId), applierId, UserTypeEnum.valueof(operatorType.getCode()), initData);
		if (!rm.isSuccess()) {
			logger.error("启动审批流程失败。param=" + JSON.toJSONString(levelProcessDto) , rm.getException());
			throw new AugeServiceException("启动流程失败。param = " + JSON.toJSONString(levelProcessDto),
					rm.getException());
		}
	}
}
