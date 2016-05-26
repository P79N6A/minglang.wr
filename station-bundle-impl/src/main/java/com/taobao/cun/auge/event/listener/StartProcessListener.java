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
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessTypeEnum;
import com.taobao.cun.auge.station.enums.TaskBusinessTypeEnum;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.chronus.dto.GeneralTaskDto;
import com.taobao.cun.chronus.service.TaskExecuteService;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("startProcessListener")
@EventSub(EventConstant.CUNTAO_STATION_STATUS_CHANGED_EVENT)
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
		// 可能是小二，也可能是TP商淘宝账号
		String operatorId = stateChangeEvent.getOperator();
		OperatorTypeEnum operatorType = stateChangeEvent.getOperatorType();
		Long operatorOrgId = stateChangeEvent.getOperatorOrgId();
		Long instanceId = stateChangeEvent.getPartnerInstanceId();
		PartnerInstanceTypeEnum partnerType = stateChangeEvent.getPartnerType();
		String remark = stateChangeEvent.getRemark();

		// FIXME FHH 流程暂时为迁移，还是使用stationapplyId关联流程实例
		Long stationApplyId = partnerInstanceBO.findStationApplyId(instanceId);

		ProcessBusinessEnum business = findBusinessType(stateChangeEnum, partnerType);
		createStartApproveProcessTask(business, stationApplyId, operatorId, operatorType, operatorOrgId, remark);
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
	private void createStartApproveProcessTask(ProcessBusinessEnum business, Long stationApplyId, String applierId,
			OperatorTypeEnum operatorType, Long applierOrgId, String remarks) {
		try {

			StartProcessDto startProcessDto = new StartProcessDto();

			startProcessDto.setRemarks(remarks);
			startProcessDto.setApplierOrgId(applierOrgId);
			startProcessDto.setBusinessId(stationApplyId);
			startProcessDto.setBusinessCode(business.getCode());
			startProcessDto.setApplierId(applierId);
			// 启动流程
			GeneralTaskDto startProcessTask = new GeneralTaskDto();
			startProcessTask.setBusinessNo(String.valueOf(stationApplyId));
			startProcessTask.setBusinessStepNo(1l);
			startProcessTask.setBusinessType(TaskBusinessTypeEnum.STATION_QUITE_CONFIRM.getCode());
			startProcessTask.setBusinessStepDesc("启动审批流程");
			startProcessTask.setBeanName("processService");
			startProcessTask.setMethodName("startApproveProcess");
			startProcessTask.setOperator(applierId);
			startProcessTask.setParameter(startProcessDto);

			// 提交任务
			taskExecuteService.submitTask(startProcessTask);
		} catch (Exception e) {
			logger.error("创建启动流程任务失败。stationApplyId = " + stationApplyId + " business=" + business.getCode()
					+ " applierId=" + applierId + " operatorType=" + operatorType.getCode(), e);
		}
	}
}
