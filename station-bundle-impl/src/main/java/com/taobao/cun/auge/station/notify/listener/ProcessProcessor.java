package com.taobao.cun.auge.station.notify.listener;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.station.bo.CloseStationApplyBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.sync.StationApplySyncBO;
import com.taobao.cun.crius.event.client.EventDispatcher;

@Component("processProcessor")
public class ProcessProcessor {

	private static final Logger logger = LoggerFactory.getLogger(ProcessProcessor.class);

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	StationBO stationBO;

	@Autowired
	PartnerBO partnerBO;

	@Autowired
	QuitStationApplyBO quitStationApplyBO;

	@Autowired
	CloseStationApplyBO closeStationApplyBO;

	@Autowired
	PartnerInstanceHandler partnerInstanceHandler;

	@Autowired
	PartnerLifecycleBO partnerLifecycleBO;
	@Autowired
	StationApplySyncBO stationApplySyncBO;
	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;

	/**
	 * 处理停业审批结果
	 * 
	 * @param stationApplyId
	 * @param approveResult
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void monitorCloseApprove(Long stationApplyId, ProcessApproveResultEnum approveResult) throws Exception {
		PartnerStationRel partnerStationRel = partnerInstanceBO.getPartnerStationRelByStationApplyId(stationApplyId);
		Long stationId = partnerStationRel.getStationId();
		Long instanceId = partnerStationRel.getId();

		OperatorDto operatorDto = OperatorDto.defaultOperator();
		String operator = operatorDto.getOperator();

		if (ProcessApproveResultEnum.APPROVE_PASS.equals(approveResult)) {
			// 合伙人实例已停业, 更新服务结束时间
			PartnerInstanceDto instance = new PartnerInstanceDto();
			instance.setServiceEndTime(new Date());
			instance.setState(PartnerInstanceStateEnum.CLOSED);
			instance.setId(instanceId);
			instance.copyOperatorDto(operatorDto);
			// instance.setVersion(partnerStationRel.getVersion());这里不需要乐观锁
			partnerInstanceBO.updatePartnerStationRel(instance);

			// 村点已停业
			stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.CLOSED, operator);

			// 更新生命周期表
			updatePartnerLifecycle(instanceId, PartnerLifecycleRoleApproveEnum.AUDIT_PASS);

			// 同步station_apply状态和服务结束时间
			stationApplySyncBO.updateStationApply(instanceId, SyncStationApplyEnum.UPDATE_BASE);
//			EventDispatcher.getInstance().dispatch(EventConstant.CUNTAO_STATION_APPLY_SYNC_EVENT,
//					new StationApplySyncEvent(SyncStationApplyEnum.UPDATE_BASE, instanceId));

			// 记录村点状态变化
			// 去标，通过事件实现
			// 短信推送
			// 通知admin，合伙人退出。让他们监听村点状态变更事件
			dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.CLOSED, operatorDto);
		} else {
			// 合伙人实例已停业
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSING, PartnerInstanceStateEnum.SERVICING, operator);

			// 村点已停业
			stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.SERVICING, operator);

			// 删除停业申请表
			closeStationApplyBO.deleteCloseStationApply(instanceId, operator);

			// 更新生命周期表
			updatePartnerLifecycle(instanceId, PartnerLifecycleRoleApproveEnum.AUDIT_NOPASS);

			// 同步station_apply，只更新状态
			stationApplySyncBO.updateStationApply(instanceId, SyncStationApplyEnum.UPDATE_STATE);
//			EventDispatcher.getInstance().dispatch(EventConstant.CUNTAO_STATION_APPLY_SYNC_EVENT,
//					new StationApplySyncEvent(SyncStationApplyEnum.UPDATE_STATE, instanceId));

			// 记录村点状态变化
			dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.CLOSING_REFUSED, operatorDto);
		}
	}

	/**
	 * 更新生命周期表，流程审批结果
	 * 
	 * @param instanceId
	 * @param operator
	 * @param approveResult
	 */
	private void updatePartnerLifecycle(Long instanceId, PartnerLifecycleRoleApproveEnum approveResult) {
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.CLOSING,
				PartnerLifecycleCurrentStepEnum.PROCESSING);

		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();

		partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
		partnerLifecycleDto.setRoleApprove(approveResult);
		partnerLifecycleDto.setPartnerInstanceId(instanceId);
		partnerLifecycleDto.copyOperatorDto(OperatorDto.defaultOperator());
		partnerLifecycleDto.setLifecycleId(items.getId());

		partnerLifecycleBO.updateLifecycle(partnerLifecycleDto);
	}

	/**
	 * 处理退出审批结果
	 * 
	 * @param stationApplyId
	 * @param approveResult
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void monitorQuitApprove(Long stationApplyId, ProcessApproveResultEnum approveResult) throws Exception {
		OperatorDto operatorDto = OperatorDto.defaultOperator();
		String operator = operatorDto.getOperator();

		PartnerStationRel instance = partnerInstanceBO.getPartnerStationRelByStationApplyId(stationApplyId);
		Long instanceId = instance.getId();
		Long stationId = instance.getStationId();

		// 校验退出申请单是否存在
		QuitStationApply quitApply = quitStationApplyBO.findQuitStationApply(instanceId);
		if (quitApply == null) {
			logger.error("QuitStationApply is null param:" + instanceId);
			return;
		}

		// 审批通过
		if (ProcessApproveResultEnum.APPROVE_PASS.equals(approveResult)) {
			// 村点已撤点
			if (quitApply.getIsQuitStation() == null || "y".equals(quitApply.getIsQuitStation())) {
				stationBO.changeState(stationId, StationStatusEnum.QUITING, StationStatusEnum.QUIT, operator);
			}

			// 处理合伙人、淘帮手、村拍档不一样的业务
			partnerInstanceHandler.handleDifferQuitAuditPass(instanceId, PartnerInstanceTypeEnum.valueof(instance.getType()));
			
			// 提交去支付宝标任务
			Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(instance.getTaobaoUserId());
			String accountNo = partner.getAlipayAccount();
			generalTaskSubmitService.submitQuitApprovedTask(instanceId, instance.getTaobaoUserId(), accountNo, operator);
		} else {
			// 合伙人实例已停业
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.QUITING, PartnerInstanceStateEnum.CLOSED, operator);
			// 村点已停业
			if (quitApply.getIsQuitStation() == null || "y".equals(quitApply.getIsQuitStation())) {
				stationBO.changeState(stationId, StationStatusEnum.QUITING, StationStatusEnum.CLOSED, operator);
			}

			// 删除退出申请单
			quitStationApplyBO.deleteQuitStationApply(instanceId, operator);
			
			//更新什么周期表
			PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId,
					PartnerLifecycleBusinessTypeEnum.QUITING, PartnerLifecycleCurrentStepEnum.PROCESSING);

			PartnerLifecycleDto param = new PartnerLifecycleDto();
			param.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_NOPASS);
			param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
			param.setLifecycleId(items.getId());
			partnerLifecycleBO.updateLifecycle(param);
			
			// 同步station_apply
			stationApplySyncBO.updateStationApply(instanceId, SyncStationApplyEnum.UPDATE_STATE);
//			EventDispatcher.getInstance().dispatch(EventConstant.CUNTAO_STATION_APPLY_SYNC_EVENT,
//					new StationApplySyncEvent(SyncStationApplyEnum.UPDATE_STATE, instanceId));

			// 发送合伙人实例状态变化事件
			dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.QUITTING_REFUSED, operatorDto);
		}
	}

	/**
	 * 监听任务已启动,修改生命周期表，流程中心任务已启动
	 * 
	 * @param stationApplyId
	 * @param businessType
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void monitorTaskStarted(Long stationApplyId, PartnerLifecycleBusinessTypeEnum businessType) {
		Long instanceId = partnerInstanceBO.getInstanceIdByStationApplyId(stationApplyId);

		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, businessType,
				PartnerLifecycleCurrentStepEnum.PROCESSING);

		if (PartnerLifecycleRoleApproveEnum.TO_AUDIT.getCode().equals(items.getRoleApprove())) {
			return;
		}

		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();

		partnerLifecycleDto.setRoleApprove(PartnerLifecycleRoleApproveEnum.TO_AUDIT);
		partnerLifecycleDto.setLifecycleId(items.getId());
		partnerLifecycleDto.copyOperatorDto(OperatorDto.defaultOperator());

		partnerLifecycleBO.updateLifecycle(partnerLifecycleDto);
	}

	private void dispatchInstStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChange, OperatorDto operator) {
		PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
		PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(stateChange, partnerInstanceDto,
				operator);
		EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
	}
}
