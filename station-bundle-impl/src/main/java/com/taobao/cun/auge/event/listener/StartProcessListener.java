package com.taobao.cun.auge.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceCloseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessTypeEnum;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("startProcessListener")
@EventSub(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT)
public class StartProcessListener implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(StartProcessListener.class);

	@Autowired
	PartnerInstanceHandler partnerInstanceHandler;

	@Autowired
	StationBO stationBO;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;

	@Override
	public void onMessage(Event event) {
		PartnerInstanceStateChangeEvent stateChangeEvent = (PartnerInstanceStateChangeEvent) event.getValue();

		PartnerInstanceStateChangeEnum stateChangeEnum = stateChangeEvent.getStateChangeEnum();
		Long instanceId = stateChangeEvent.getPartnerInstanceId();
		PartnerInstanceTypeEnum partnerType = stateChangeEvent.getPartnerType();

		// FIXME FHH 流程暂时为迁移，还是使用stationapplyId关联流程实例
		PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);
		
		//合伙人申请停业，不需要流程
		if(PartnerInstanceCloseTypeEnum.PARTNER_QUIT.getCode().equals(instance.getCloseType())){
			return;
		}

		ProcessBusinessEnum business = findBusinessType(stateChangeEnum, partnerType);
		if (null == business) {
			return;
		}
		generalTaskSubmitService.submitApproveProcessTask(business, instance.getStationApplyId(), stateChangeEvent);
	}

	/**
	 * 
	 * @param changedState
	 * @param partnerType
	 * @return
	 */
	private ProcessBusinessEnum findBusinessType(PartnerInstanceStateChangeEnum changedState,
			PartnerInstanceTypeEnum partnerType) {
		// 停业申请
		if (PartnerInstanceStateChangeEnum.START_CLOSING.equals(changedState)) {
			return partnerInstanceHandler.findProcessBusiness(partnerType, ProcessTypeEnum.CLOSING_PRO);
			// 退出申请
		} else if (PartnerInstanceStateChangeEnum.START_QUITTING.equals(changedState)) {
			return partnerInstanceHandler.findProcessBusiness(partnerType, ProcessTypeEnum.QUITING_PRO);
		}
		String msg = "没有找到相应的流程businessCode.changedState=" + changedState.getDescription() + " partnerType="
				+ partnerType.getCode();
		logger.warn(msg);
		return null;
	}


}
