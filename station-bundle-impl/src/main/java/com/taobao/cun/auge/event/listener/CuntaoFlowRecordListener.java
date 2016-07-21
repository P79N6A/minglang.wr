package com.taobao.cun.auge.event.listener;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.common.category.util.StringUtil;
import com.taobao.cun.auge.dal.domain.CuntaoFlowRecord;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.PartnerChildMaxNumChangeEvent;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.PartnerInstanceTypeChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.auge.station.bo.CuntaoFlowRecordBO;
import com.taobao.cun.auge.station.enums.CuntaoFlowRecordTargetTypeEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("cuntaoFlowRecordListener")
@EventSub({ EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, EventConstant.PARTNER_INSTANCE_TYPE_CHANGE_EVENT,EventConstant.PARTNER_CHILD_MAX_NUM_CHANGE_EVENT })
public class CuntaoFlowRecordListener implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(CuntaoFlowRecordListener.class);

	@Autowired
	CuntaoFlowRecordBO cuntaoFlowRecordBO;

	@Autowired
	Emp360Adapter emp360Adapter;

	@Autowired
	UicReadAdapter uicReadAdapter;

	@Override
	public void onMessage(Event event) {
		if (event.getValue() instanceof PartnerInstanceStateChangeEvent) {
			processStateChangeEvent(event);
		} else if (event.getValue() instanceof PartnerInstanceTypeChangeEvent) {
			processTypeChangeEvent(event);
		}else if (event.getValue() instanceof PartnerChildMaxNumChangeEvent) {
			processChildMaxNumChangeEvent(event);
		}

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
		cuntaoFlowRecord.setRemarks(buildTypeChangeRecordContent(typeChangeEvent));
		cuntaoFlowRecordBO.addRecord(cuntaoFlowRecord);

		logger.info("Finished to handle event." + JSON.toJSONString(typeChangeEvent));
	}

	private String buildTypeChangeRecordContent(PartnerInstanceTypeChangeEvent event) {
		if (PartnerInstanceTypeChangeEnum.TP_DEGREE_2_TPA.equals(event.getTypeChangeEnum())) {
			return PartnerInstanceTypeChangeEnum.TP_DEGREE_2_TPA.getDescription();
		}
		return null;
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
			return stateChangeEvent.getRemark();
		} else if (PartnerInstanceStateChangeEnum.CLOSING_REFUSED.equals(stateChangeEnum)) {
			return "";
		} else if (PartnerInstanceStateChangeEnum.CLOSED.equals(stateChangeEnum)) {
			return "";
		} else if (PartnerInstanceStateChangeEnum.START_QUITTING.equals(stateChangeEnum)) {
			return "";
		} else if (PartnerInstanceStateChangeEnum.QUITTING_REFUSED.equals(stateChangeEnum)) {
			return "";
		} else if (PartnerInstanceStateChangeEnum.QUIT.equals(stateChangeEnum)) {
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
		}
		return operator;
	}
	
	private void processChildMaxNumChangeEvent(Event event) {
		PartnerChildMaxNumChangeEvent changEvent = (PartnerChildMaxNumChangeEvent) event.getValue();

		logger.info("receive event." + JSON.toJSONString(changEvent));

		String bizMonth = changEvent.getBizMonth();
		Integer childMaxNum = changEvent.getChildMaxNum();
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
		if (StringUtil.isEmpty(bizMonth)) {
			cuntaoFlowRecord.setRemarks(buildOperatorName + "修改子成员最大配额为" + (null != childMaxNum ? childMaxNum : 0));
		} else {
			cuntaoFlowRecord
					.setRemarks("根据淘帮手在" + bizMonth + "业绩，修改子成员最大配额为" + (null != childMaxNum ? childMaxNum : 0));
		}
		cuntaoFlowRecordBO.addRecord(cuntaoFlowRecord);

		logger.info("Finished to handle event." + JSON.toJSONString(changEvent));
	}
}
