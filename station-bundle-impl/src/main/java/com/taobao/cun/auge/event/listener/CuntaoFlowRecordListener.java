package com.taobao.cun.auge.event.listener;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CuntaoFlowRecord;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.auge.station.bo.CuntaoFlowRecordBO;
import com.taobao.cun.auge.station.enums.CuntaoFlowRecordTargetTypeEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("cuntaoFlowRecordListener")
@EventSub(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT)
public class CuntaoFlowRecordListener implements EventListener {

	@Autowired
	CuntaoFlowRecordBO cuntaoFlowRecordBO;

	@Autowired
	Emp360Adapter emp360Adapter;

	@Autowired
	UicReadAdapter uicReadAdapter;

	@Override
	public void onMessage(Event event) {
		PartnerInstanceStateChangeEvent stateChangeEvent = (PartnerInstanceStateChangeEvent) event.getValue();
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
			return uicReadAdapter.findTaobaoName(operator);
		}
		return operator;
	}
}
