package com.taobao.cun.auge.event.listener;

import java.util.Date;

import com.taobao.cun.auge.station.enums.WisdomCountyStateEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.dal.domain.CuntaoFlowRecord;
import com.taobao.cun.auge.event.ChangeTPEvent;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.PartnerChildMaxNumChangeEvent;
import com.taobao.cun.auge.event.PartnerInstanceLevelChangeEvent;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.PartnerInstanceTypeChangeEvent;
import com.taobao.cun.auge.event.StationStatusChangeEvent;
import com.taobao.cun.auge.event.WisdomCountyApplyEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum;
import com.taobao.cun.auge.event.enums.StationStatusChangeEnum;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.auge.station.bo.CuntaoFlowRecordBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceLevelConverter;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.CloseStationApplyCloseReasonEnum;
import com.taobao.cun.auge.station.enums.CuntaoFlowRecordTargetTypeEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("cuntaoFlowRecordListener")
@EventSub({ EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, EventConstant.PARTNER_INSTANCE_TYPE_CHANGE_EVENT,
		EventConstant.PARTNER_CHILD_MAX_NUM_CHANGE_EVENT, EventConstant.PARTNER_INSTANCE_LEVEL_CHANGE_EVENT,
		EventConstant.CHANGE_TP_EVENT, EventConstant.WISDOM_COUNTY_APPLY_EVENT,EventConstant.CUNTAO_STATION_STATUS_CHANGED_EVENT})
public class CuntaoFlowRecordListener implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(CuntaoFlowRecordListener.class);

	@Autowired
	CuntaoFlowRecordBO cuntaoFlowRecordBO;

	@Autowired
	Emp360Adapter emp360Adapter;

	@Autowired
	UicReadAdapter uicReadAdapter;
	
	@Autowired
	PartnerInstanceQueryService partnerInstanceQueryService;

	@Override
	public void onMessage(Event event) {
		if (event.getValue() instanceof PartnerInstanceStateChangeEvent) {
			processStateChangeEvent(event);
		} else if (event.getValue() instanceof PartnerInstanceTypeChangeEvent) {
			processTypeChangeEvent(event);
		} else if (event.getValue() instanceof PartnerChildMaxNumChangeEvent) {
			processChildMaxNumChangeEvent(event);
		} else if (event.getValue() instanceof PartnerInstanceLevelChangeEvent) {
			processLevelChangeEvent(event);
		} else if (event.getValue() instanceof ChangeTPEvent){
			processChangeTPEvent(event);
		} else if (event.getValue() instanceof WisdomCountyApplyEvent) {
			processWisdomCountyApplyEvent(event);
		}else if (event.getValue() instanceof StationStatusChangeEvent) {
		    processStationStatusChangeEvent(event);
	    }

	}

	private void processChangeTPEvent(Event event) {
		ChangeTPEvent changeTPEvent = (ChangeTPEvent) event.getValue();
		String operator = changeTPEvent.getOperator();
		OperatorTypeEnum operatorTypeEnum = changeTPEvent.getOperatorType();
		String buildOperatorName = buildOperatorName(operator, operatorTypeEnum);
		CuntaoFlowRecord cuntaoFlowRecord = new CuntaoFlowRecord();
		cuntaoFlowRecord.setTargetId(changeTPEvent.getStationId());
		cuntaoFlowRecord.setTargetType(CuntaoFlowRecordTargetTypeEnum.STATION.getCode());
		cuntaoFlowRecord.setNodeTitle("淘帮手合伙人变更");
		cuntaoFlowRecord.setOperatorName(buildOperatorName);
		cuntaoFlowRecord.setOperatorWorkid(operator);
		cuntaoFlowRecord.setOperateTime(new Date());
		cuntaoFlowRecordBO.addRecord(cuntaoFlowRecord);
		logger.info("Finished to handle event." + JSON.toJSONString(changeTPEvent));
	}

	private void processWisdomCountyApplyEvent(Event event){
		WisdomCountyApplyEvent applyEvent = (WisdomCountyApplyEvent) event.getValue();
		if (WisdomCountyStateEnum.AUDIT_PASS.equals(applyEvent.getType()) || WisdomCountyStateEnum.AUDIT_FAIL.equals(applyEvent.getType())) {
			logger.info("receive event." + JSON.toJSONString(applyEvent));
			String operator = applyEvent.getOperator();
			OperatorTypeEnum operatorType = applyEvent.getOperatorType();
			String buildOperatorName = buildOperatorName(operator, operatorType);
			CuntaoFlowRecord cuntaoFlowRecord = new CuntaoFlowRecord();
			cuntaoFlowRecord.setTargetId(applyEvent.getApplyId());
			cuntaoFlowRecord.setTargetType(CuntaoFlowRecordTargetTypeEnum.WISDOM_COUNTY_APPLY.getCode());
			cuntaoFlowRecord.setNodeTitle("智慧县域报名审核");
			cuntaoFlowRecord.setOperatorName(buildOperatorName);
			cuntaoFlowRecord.setOperatorWorkid(operator);
			cuntaoFlowRecord.setOperateTime(new Date());
			cuntaoFlowRecord.setRemarks(applyEvent.getRemark());
			cuntaoFlowRecord.setOperateOpinion(applyEvent.getOpinion());
			cuntaoFlowRecordBO.addRecord(cuntaoFlowRecord);
			logger.info("Finished to handle event." + JSON.toJSONString(applyEvent));
		}
	}

	private void processLevelChangeEvent(Event event) {
		PartnerInstanceLevelChangeEvent levelChangeEvent = (PartnerInstanceLevelChangeEvent) event.getValue();

		logger.info("receive event." + JSON.toJSONString(levelChangeEvent));

		String operator = levelChangeEvent.getOperator();
		OperatorTypeEnum operatorType = levelChangeEvent.getOperatorType();

		String buildOperatorName = buildOperatorName(operator, operatorType);

		CuntaoFlowRecord cuntaoFlowRecord = new CuntaoFlowRecord();

		cuntaoFlowRecord.setTargetId(levelChangeEvent.getPartnerInstanceId());
		cuntaoFlowRecord.setTargetType(CuntaoFlowRecordTargetTypeEnum.PARTNER_INSTANCE.getCode());
		cuntaoFlowRecord.setNodeTitle("合伙人层级变更");
		cuntaoFlowRecord.setOperatorName(buildOperatorName);
		cuntaoFlowRecord.setOperatorWorkid(operator);
		cuntaoFlowRecord.setOperateTime(new Date());
		cuntaoFlowRecord.setRemarks(buildLevelChangeRecordContent(levelChangeEvent));

		cuntaoFlowRecordBO.addRecord(cuntaoFlowRecord);

		logger.info("Finished to handle event." + JSON.toJSONString(levelChangeEvent));
	}

	private String buildLevelChangeRecordContent(PartnerInstanceLevelChangeEvent levelChangeEvent) {
		StringBuilder sb = new StringBuilder();
		sb.append("层级: ");
		sb.append(PartnerInstanceLevelConverter.levelToString(levelChangeEvent.getCurrentLevel()));
		sb.append(", 评定人: ");
		sb.append(levelChangeEvent.getEvaluateBy());
		sb.append(", 评定类型: ");
		sb.append(null == levelChangeEvent.getEvaluateType() ? "-" : levelChangeEvent.getEvaluateType().getDesc());
		sb.append(", 上次评定层级: ");
		sb.append(PartnerInstanceLevelConverter.levelToString(levelChangeEvent.getPreLevel()));
		if (null != levelChangeEvent.getExpectedLevel()) {
			sb.append(", 预授层级: ");
			sb.append(PartnerInstanceLevelConverter.levelToString(levelChangeEvent.getExpectedLevel()));
		}
		if (StringUtils.isNotBlank(levelChangeEvent.getRemark())) {
			sb.append(", 备注: ");
			sb.append(levelChangeEvent.getRemark());
		}
		return sb.toString();
	}

	private void processTypeChangeEvent(Event event) {
		PartnerInstanceTypeChangeEvent typeChangeEvent = (PartnerInstanceTypeChangeEvent) event.getValue();

		logger.info("receive event." + JSON.toJSONString(typeChangeEvent));

		PartnerInstanceTypeChangeEnum typeChangeEnum = typeChangeEvent.getTypeChangeEnum();
		Long stationId = typeChangeEvent.getStationId();
		String operator = typeChangeEvent.getOperator();
		OperatorTypeEnum operatorType = typeChangeEvent.getOperatorType();

		String buildOperatorName = buildOperatorName(operator, operatorType);

		CuntaoFlowRecord cuntaoFlowRecord = new CuntaoFlowRecord();

		cuntaoFlowRecord.setTargetId(stationId);
		cuntaoFlowRecord.setTargetType(CuntaoFlowRecordTargetTypeEnum.STATION.getCode());
		cuntaoFlowRecord.setNodeTitle(typeChangeEnum.getDescription());
		cuntaoFlowRecord.setOperatorName(buildOperatorName);
		cuntaoFlowRecord.setOperatorWorkid(operator);
		cuntaoFlowRecord.setOperateTime(new Date());
		cuntaoFlowRecord.setRemarks(typeChangeEnum.getDescription());
		cuntaoFlowRecordBO.addRecord(cuntaoFlowRecord);

		logger.info("Finished to handle event." + JSON.toJSONString(typeChangeEvent));
	}

	private void processStateChangeEvent(Event event) {
		PartnerInstanceStateChangeEvent stateChangeEvent = (PartnerInstanceStateChangeEvent) event.getValue();

		logger.info("receive event." + JSON.toJSONString(stateChangeEvent));

		PartnerInstanceStateChangeEnum stateChangeEnum = stateChangeEvent.getStateChangeEnum();
		Long stationId = stateChangeEvent.getStationId();
		String operator = stateChangeEvent.getOperator();
		OperatorTypeEnum operatorType = stateChangeEvent.getOperatorType();

		String buildOperatorName = buildOperatorName(operator, operatorType);

		CuntaoFlowRecord cuntaoFlowRecord = new CuntaoFlowRecord();

		cuntaoFlowRecord.setTargetId(stationId);
		cuntaoFlowRecord.setTargetType(CuntaoFlowRecordTargetTypeEnum.STATION.getCode());
		cuntaoFlowRecord.setNodeTitle(stateChangeEnum.getDescription());
		cuntaoFlowRecord.setOperatorName(buildOperatorName);
		cuntaoFlowRecord.setOperatorWorkid(operator);
		cuntaoFlowRecord.setOperateTime(new Date());
		cuntaoFlowRecord.setRemarks(buildRecordContent(stateChangeEvent));
		cuntaoFlowRecordBO.addRecord(cuntaoFlowRecord);

		logger.info("Finished to handle event." + JSON.toJSONString(stateChangeEvent));
	}

	private String buildRecordContent(PartnerInstanceStateChangeEvent stateChangeEvent) {
		PartnerInstanceStateChangeEnum stateChangeEnum = stateChangeEvent.getStateChangeEnum();

		if (PartnerInstanceStateChangeEnum.START_SETTLING.equals(stateChangeEnum)) {
			return "";
		} else if (PartnerInstanceStateChangeEnum.SETTLING_REFUSED.equals(stateChangeEnum)) {
			return "";
		} else if (PartnerInstanceStateChangeEnum.START_DECORATING.equals(stateChangeEnum)) {
			return "";
		} else if (PartnerInstanceStateChangeEnum.START_SERVICING.equals(stateChangeEnum)) {
			return "";
		} else if (PartnerInstanceStateChangeEnum.START_CLOSING.equals(stateChangeEnum)) {
			return findCloseReason(stateChangeEvent.getPartnerInstanceId());
		} else if (PartnerInstanceStateChangeEnum.CLOSING_REFUSED.equals(stateChangeEnum)) {
			return "";
		} else if (PartnerInstanceStateChangeEnum.CLOSED.equals(stateChangeEnum)) {
			return "";
		} else if (PartnerInstanceStateChangeEnum.START_QUITTING.equals(stateChangeEnum)) {
			return findQuitReason(stateChangeEvent.getPartnerInstanceId());
		} else if (PartnerInstanceStateChangeEnum.QUITTING_REFUSED.equals(stateChangeEnum)) {
			return "";
		} else if (PartnerInstanceStateChangeEnum.QUIT.equals(stateChangeEnum)) {
			return "";
		} else if (PartnerInstanceStateChangeEnum.CLOSE_TO_SERVICE.equals(stateChangeEnum)) {
			return "";
		}
		return "";
	}

	private String buildOperatorName(String operator, OperatorTypeEnum type) {
		// 小二工号
		if (OperatorTypeEnum.BUC.equals(type)) {
			return emp360Adapter.getName(operator);
		} else if (OperatorTypeEnum.HAVANA.equals(type)) {
			return uicReadAdapter.getFullName(Long.parseLong(operator));
		}else if (OperatorTypeEnum.SYSTEM.equals(type)) {
			return OperatorTypeEnum.SYSTEM.getDesc();
		}
		return operator;
	}

	private void processChildMaxNumChangeEvent(Event event) {
		PartnerChildMaxNumChangeEvent changEvent = (PartnerChildMaxNumChangeEvent) event.getValue();

		logger.info("receive event." + JSON.toJSONString(changEvent));

		Long instanceId = changEvent.getPartnerInstanceId();

		String operator = changEvent.getOperator();
		OperatorTypeEnum operatorType = changEvent.getOperatorType();

		String buildOperatorName = buildOperatorName(operator, operatorType);

		CuntaoFlowRecord cuntaoFlowRecord = new CuntaoFlowRecord();

		cuntaoFlowRecord.setTargetId(instanceId);
		cuntaoFlowRecord.setTargetType(CuntaoFlowRecordTargetTypeEnum.PARTNER_INSTANCE.getCode());
		cuntaoFlowRecord.setNodeTitle("子成员最大配额变更");
		cuntaoFlowRecord.setOperatorName(buildOperatorName);
		cuntaoFlowRecord.setOperatorWorkid(operator);
		cuntaoFlowRecord.setOperateTime(new Date());
		cuntaoFlowRecord.setRemarks(changEvent.getReason());
		
		cuntaoFlowRecordBO.addRecord(cuntaoFlowRecord);

		logger.info("Finished to handle event." + JSON.toJSONString(changEvent));
	}
	
	private void processStationStatusChangeEvent(Event event) {
		StationStatusChangeEvent statusChangeEvent = (StationStatusChangeEvent) event.getValue();

		logger.info("receive event." + JSON.toJSONString(statusChangeEvent));

		StationStatusChangeEnum statusChangeEnum = statusChangeEvent.getStatusChangeEnum();
		Long stationId = statusChangeEvent.getStationId();
		String operator = statusChangeEvent.getOperator();
		OperatorTypeEnum operatorType = statusChangeEvent.getOperatorType();

		String buildOperatorName = buildOperatorName(operator, operatorType);

		CuntaoFlowRecord cuntaoFlowRecord = new CuntaoFlowRecord();

		cuntaoFlowRecord.setTargetId(stationId);
		cuntaoFlowRecord.setTargetType(CuntaoFlowRecordTargetTypeEnum.STATION.getCode());
		cuntaoFlowRecord.setNodeTitle(statusChangeEnum.getDescription());
		cuntaoFlowRecord.setOperatorName(buildOperatorName);
		cuntaoFlowRecord.setOperatorWorkid(operator);
		cuntaoFlowRecord.setOperateTime(new Date());
		cuntaoFlowRecord.setRemarks("");
		cuntaoFlowRecordBO.addRecord(cuntaoFlowRecord);

		logger.info("Finished to handle event." + JSON.toJSONString(statusChangeEvent));
	}
	
	// 获取停业原因
	private String findCloseReason(Long instanceId) {
		try {
			CloseStationApplyDto forcedCloseDto = partnerInstanceQueryService.getCloseStationApply(instanceId);
			if (CloseStationApplyCloseReasonEnum.OTHER.equals(forcedCloseDto.getCloseReason())) {
				return forcedCloseDto.getOtherReason();
			} else {
				return null != forcedCloseDto.getCloseReason() ? forcedCloseDto.getCloseReason().getDesc() : "";
			}
		} catch (Exception e) {
			logger.error("查询停业原因失败。instanceId=" + instanceId, e);
			return "";
		}
	}
	
	// 获取退出原因
	private String findQuitReason(Long instanceId) {
		try {
			QuitStationApplyDto quitStationApplyDto = partnerInstanceQueryService.getQuitStationApply(instanceId);
			return quitStationApplyDto.getOtherDescription();
		} catch (Exception e) {
			logger.error("查询退出原因失败。instanceId=" + instanceId, e);
			return "";
		}
	}
}
