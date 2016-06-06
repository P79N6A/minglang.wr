package com.taobao.cun.auge.station.notify.listener;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.event.StationApplySyncEvent;
import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.station.bo.CloseStationApplyBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.crius.event.client.EventDispatcher;

@Component("processApproveResultProcessor")
public class ProcessApproveResultProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(ProcessApproveResultProcessor.class);
	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	StationBO stationBO;

	@Autowired
	QuitStationApplyBO quitStationApplyBO;
	
	@Autowired
	CloseStationApplyBO closeStationApplyBO;
	
	@Autowired
	PartnerInstanceHandler partnerInstanceHandler;
	
	@Autowired
	PartnerLifecycleBO partnerLifecycleBO;

	/**
	 * 处理停业审批结果
	 * 
	 * @param stationApplyId
	 * @param approveResult
	 * @throws Exception
	 */
	public void monitorCloseApprove(Long stationApplyId, ProcessApproveResultEnum approveResult) throws Exception {
		Long instanceId = partnerInstanceBO.getInstanceIdByStationApplyId(stationApplyId);
		Long stationId = partnerInstanceBO.findStationIdByInstanceId(instanceId);

		OperatorDto operator = new OperatorDto();
		String operatorId = "sys";
		operator.setOperator(operatorId);
		operator.setOperatorType(OperatorTypeEnum.SYSTEM);

		if (ProcessApproveResultEnum.APPROVE_PASS.equals(approveResult)) {
			// 合伙人实例已停业
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSING, PartnerInstanceStateEnum.CLOSED,
					operatorId);
			// 更新服务结束时间
			PartnerInstanceDto instance = new PartnerInstanceDto();
			instance.setServiceEndTime(new Date());
			instance.setId(instanceId);
			instance.setOperator(operatorId);
			partnerInstanceBO.updatePartnerStationRel(instance);

			// 村点已停业
			stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.CLOSED, operatorId);
			
			//更新生命周期表
			updatePartnerLifecycle(instanceId, operator,PartnerLifecycleRoleApproveEnum.AUDIT_PASS);
			
			// 同步station_apply
			EventDispatcher.getInstance().dispatch(EventConstant.CUNTAO_STATION_APPLY_SYNC_EVENT, new StationApplySyncEvent(SyncStationApplyEnum.UPDATE_STATE, instanceId));

			// 记录村点状态变化
			// 去标，通过事件实现
			// 短信推送
			// 通知admin，合伙人退出。让他们监听村点状态变更事件
			EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
					PartnerInstanceEventConverter.convert(PartnerInstanceStateChangeEnum.CLOSED,
							partnerInstanceBO.getPartnerInstanceById(instanceId), operator));
		} else {
			// 合伙人实例已停业
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSING,
					PartnerInstanceStateEnum.SERVICING, operatorId);

			// 村点已停业
			stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.SERVICING, operatorId);
			
			//删除停业申请表
			closeStationApplyBO.deleteCloseStationApply(instanceId,operatorId);
			
			//更新生命周期表
			updatePartnerLifecycle(instanceId, operator,PartnerLifecycleRoleApproveEnum.AUDIT_NOPASS);
			
			// 同步station_apply
			EventDispatcher.getInstance().dispatch(EventConstant.CUNTAO_STATION_APPLY_SYNC_EVENT, new StationApplySyncEvent(SyncStationApplyEnum.UPDATE_STATE, instanceId));

			// 记录村点状态变化
			EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
					PartnerInstanceEventConverter.convert(PartnerInstanceStateChangeEnum.CLOSING_REFUSED,
							partnerInstanceBO.getPartnerInstanceById(instanceId), operator));
		}
	}

	private void updatePartnerLifecycle(Long instanceId, OperatorDto operator,PartnerLifecycleRoleApproveEnum approveResult) {
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.CLOSING, PartnerLifecycleCurrentStepEnum.ROLE_APPROVE);
		
		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
		
		partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
		partnerLifecycleDto.setRoleApprove(approveResult);
		partnerLifecycleDto.setPartnerInstanceId(instanceId);
		partnerLifecycleDto.setOperator(operator.getOperator());
		partnerLifecycleDto.setOperatorType(operator.getOperatorType());
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
	public void monitorQuitApprove(Long stationApplyId, ProcessApproveResultEnum approveResult) throws Exception {
		OperatorDto operator = new OperatorDto();
		String operatorId = "sys";
		operator.setOperator(operatorId);
		operator.setOperatorType(OperatorTypeEnum.SYSTEM);

		PartnerStationRel  instance = partnerInstanceBO.getPartnerStationRelByStationApplyId(stationApplyId);
		if (instance == null) {
			logger.error("monitorQuitApprove.getPartnerStationRelByStationApplyId is null param:"+stationApplyId);
			return;
		}
		Boolean isAgree = false;
		if (ProcessApproveResultEnum.APPROVE_PASS.equals(approveResult)) {
			isAgree = true;
		}
		partnerInstanceHandler.handleAuditQuit(isAgree, instance.getId(), PartnerInstanceTypeEnum.valueof(instance.getType()));
		// tair清空缓存
	}
}
