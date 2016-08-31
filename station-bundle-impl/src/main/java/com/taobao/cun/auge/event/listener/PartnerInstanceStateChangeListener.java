package com.taobao.cun.auge.event.listener;

import com.taobao.cun.auge.station.bo.PartnerApplyBO;
import com.taobao.cun.auge.station.dto.PartnerApplyDto;
import com.taobao.cun.auge.station.enums.PartnerApplyStateEnum;
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
import com.taobao.cun.auge.station.constant.PartnerInstanceExtConstant;
import com.taobao.cun.auge.station.dto.PartnerInstanceExtDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceCloseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerInstanceExtService;
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
	
	@Autowired
	PartnerInstanceExtService partnerInstanceExtService;

	@Autowired
	PartnerApplyBO partnerApplyBO;

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
			generalTaskSubmitService.submitApproveProcessTask(business, instanceId, stateChangeEvent);

			// 已停业，去标
		} else if (PartnerInstanceStateChangeEnum.CLOSED.equals(stateChangeEnum)) {
			generalTaskSubmitService.submitRemoveUserTagTasks(taobaoUserId, taobaoNick, partnerType, operatorId);
			// 退出
		} else if (PartnerInstanceStateChangeEnum.START_QUITTING.equals(stateChangeEnum)) {
			ProcessBusinessEnum business = ProcessBusinessEnum.stationQuitRecord;
			generalTaskSubmitService.submitApproveProcessTask(business, instanceId, stateChangeEvent);
			//服务中
		}else if(PartnerInstanceStateChangeEnum.START_SERVICING.equals(stateChangeEnum)){
			PartnerInstanceExtDto instanceExtDto = new PartnerInstanceExtDto();
			
			instanceExtDto.setInstanceId(instanceId);
			instanceExtDto.setMaxChildNum(PartnerInstanceExtConstant.DEFAULT_MAX_CHILD_NUM);
			instanceExtDto.copyOperatorDto(stateChangeEvent);
			
			partnerInstanceExtService.savePartnerExtInfo(instanceExtDto);
		} else if (PartnerInstanceStateChangeEnum.QUIT.equals(stateChangeEnum)){
			//将生成合伙人按钮打开
			if (PartnerInstanceTypeEnum.TP.equals(partnerType)){
				PartnerApplyDto partnerApplyDto = new PartnerApplyDto();
				partnerApplyDto.setTaobaoUserId(instance.getTaobaoUserId());
				partnerApplyDto.setState(PartnerApplyStateEnum.STATE_APPLY_SUCC);
				partnerApplyDto.setOperator(stateChangeEvent.getOperator());
				partnerApplyBO.restartPartnerApplyByUserId(partnerApplyDto);
			}
		}
		logger.info("Finished to handle event." + JSON.toJSONString(stateChangeEvent));
	}
}
