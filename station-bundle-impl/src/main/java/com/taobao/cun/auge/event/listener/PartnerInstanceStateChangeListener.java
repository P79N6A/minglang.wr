package com.taobao.cun.auge.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceCloseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("partnerInstanceStateChangeListener")
@EventSub(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT)
public class PartnerInstanceStateChangeListener implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceStateChangeListener.class);

	@Autowired
	PartnerInstanceHandler partnerInstanceHandler;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	PartnerBO partnerBO;

	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;

	@Override
	public void onMessage(Event event) {
		PartnerInstanceStateChangeEvent stateChangeEvent = (PartnerInstanceStateChangeEvent) event.getValue();

		logger.info("receive event." + JSON.toJSONString(stateChangeEvent));

		PartnerInstanceStateChangeEnum stateChangeEnum = stateChangeEvent.getStateChangeEnum();
		Long instanceId = stateChangeEvent.getPartnerInstanceId();

		logger.info("instance Id." + instanceId);

		PartnerInstanceTypeEnum partnerType = stateChangeEvent.getPartnerType();
		Long taobaoUserId = stateChangeEvent.getTaobaoUserId();
		String taobaoNick = stateChangeEvent.getTaobaoNick();
		String operatorId = stateChangeEvent.getOperator();

		PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);

		logger.info("partner instance." + JSON.toJSONString(instance));
		if (PartnerInstanceStateChangeEnum.START_CLOSING.equals(stateChangeEnum)
				&& PartnerInstanceCloseTypeEnum.WORKER_QUIT.getCode().equals(instance.getCloseType())) {
			ProcessBusinessEnum business = ProcessBusinessEnum.stationForcedClosure;
			// FIXME FHH 流程暂时为迁移，还是使用stationapplyId关联流程实例
			generalTaskSubmitService.submitApproveProcessTask(business, instance.getStationApplyId(), stateChangeEvent);

			// 已停业，去标
		} else if (PartnerInstanceStateChangeEnum.CLOSED.equals(stateChangeEnum)) {
			generalTaskSubmitService.submitRemoveUserTagTasks(taobaoUserId, taobaoNick, partnerType, operatorId);
			// 退出
		} else if (PartnerInstanceStateChangeEnum.START_QUITTING.equals(stateChangeEnum)) {
			ProcessBusinessEnum business = ProcessBusinessEnum.stationQuitRecord;
			// FIXME FHH 流程暂时为迁移，还是使用stationapplyId关联流程实例
			generalTaskSubmitService.submitApproveProcessTask(business, instance.getStationApplyId(), stateChangeEvent);
		}

		logger.info("Finished to handle event." + JSON.toJSONString(stateChangeEvent));
	}
}
