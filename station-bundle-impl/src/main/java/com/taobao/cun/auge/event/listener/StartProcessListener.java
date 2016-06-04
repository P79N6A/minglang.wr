package com.taobao.cun.auge.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.StartProcessDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessTypeEnum;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.chronus.dto.GeneralTaskDto;
import com.taobao.cun.chronus.service.TaskExecuteService;
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
	TaskExecuteService taskExecuteService;

	@Override
	public void onMessage(Event event) {
		PartnerInstanceStateChangeEvent stateChangeEvent = (PartnerInstanceStateChangeEvent) event.getValue();

		PartnerInstanceStateChangeEnum stateChangeEnum = stateChangeEvent.getStateChangeEnum();
		Long instanceId = stateChangeEvent.getPartnerInstanceId();
		PartnerInstanceTypeEnum partnerType = stateChangeEvent.getPartnerType();

		// FIXME FHH 流程暂时为迁移，还是使用stationapplyId关联流程实例
		Long stationApplyId = partnerInstanceBO.findStationApplyId(instanceId);

		ProcessBusinessEnum business = findBusinessType(stateChangeEnum, partnerType);
		createStartApproveProcessTask(business, stationApplyId,stateChangeEvent);
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
		throw new IllegalArgumentException(msg);
	}
	
	/**
	 * 启动停业、退出流程审批流程
	 * 
	 * @param business
	 *            业务类型
	 * @param stationApplyId
	 *            业务主键
	 * @param applierId
	 *            申请人
	 * @param applierOrgId
	 *            申请人orgid
	 * @param remarks
	 *            备注
	 */
	private void createStartApproveProcessTask(ProcessBusinessEnum business, Long stationApplyId, PartnerInstanceStateChangeEvent stateChangeEvent) {
		try {

			StartProcessDto startProcessDto = new StartProcessDto();

			startProcessDto.setRemarks(stateChangeEvent.getRemark());
			startProcessDto.setBusinessId(stationApplyId);
			startProcessDto.setBusinessCode(business.getCode());
			startProcessDto.copyOperatorDto(stateChangeEvent);
			// 启动流程
			GeneralTaskDto startProcessTask = new GeneralTaskDto();
			startProcessTask.setBusinessNo(String.valueOf(stationApplyId));
			startProcessTask.setBusinessStepNo(1l);
			startProcessTask.setBusinessType(business.getCode());
			startProcessTask.setBusinessStepDesc(business.getDesc());
			startProcessTask.setBeanName("processServiceImpl");
			startProcessTask.setMethodName("startApproveProcess");
			startProcessTask.setOperator(stateChangeEvent.getOperator());
			startProcessTask.setParameter(startProcessDto);

			// 提交任务
			taskExecuteService.submitTask(startProcessTask);
		} catch (Exception e) {
			logger.error("创建启动流程任务失败。stationApplyId = " + stationApplyId + " business=" + business.getCode()
					+ " applierId=" + stateChangeEvent.getOperator() + " operatorType=" + stateChangeEvent.getOperatorType().getCode(), e);
		}
	}
}
