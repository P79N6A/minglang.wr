package com.taobao.cun.auge.station.notify.listener;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.common.category.util.StringUtil;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.CuntaoFlowRecord;
import com.taobao.cun.auge.dal.domain.PartnerInstanceLevel;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.platform.enums.ProcessBusinessCodeEnum;
import com.taobao.cun.auge.platform.service.BusiWorkBaseInfoService;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.bo.CloseStationApplyBO;
import com.taobao.cun.auge.station.bo.CuntaoFlowRecordBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceLevelBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.CuntaoFlowRecordTargetTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessMsgTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.auge.station.service.StationService;
import com.taobao.cun.auge.station.sync.StationApplySyncBO;
import com.taobao.notify.message.StringMessage;

@Component("processProcessor")
public class ProcessProcessor {

	private static final Logger logger = LoggerFactory.getLogger(ProcessProcessor.class);
	private static final String ERROR_MSG = "ProcessProcessor-ERROR ";

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

	@Autowired
	StationApplySyncBO stationApplySyncBO;

	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;
	@Autowired
	BusiWorkBaseInfoService busiWorkBaseInfoService;

	@Autowired
	StationService stationService;
	
	@Autowired
	AppResourceBO appResourceBO;

	@Autowired
	CuntaoFlowRecordBO cuntaoFlowRecordBO;
	
	@Autowired
	PartnerInstanceService partnerInstanceService;
	@Autowired
	PartnerInstanceLevelBO partnerInstanceLevelBO;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void handleProcessMsg(StringMessage strMessage, JSONObject ob) throws Exception {
		String msgType = strMessage.getMessageType();
		String businessCode = ob.getString("businessCode");
		String objectId = ob.getString("objectId");
		String partnerInstanceId = ob.getString("partnerInstanceId");
		Long businessId = Long.valueOf(objectId);
		// 监听流程实例结束
		if (ProcessMsgTypeEnum.PROC_INST_FINISH.getCode().equals(msgType)) {
			JSONObject instanceStatus = ob.getJSONObject("instanceStatus");
			String resultCode = instanceStatus.getString("code");

			// 村点强制停业
			if (ProcessBusinessEnum.stationForcedClosure.getCode().equals(businessCode)||ProcessBusinessEnum.TPV_CLOSE.getCode().equals(businessCode)) {
				monitorCloseApprove(businessId, ProcessApproveResultEnum.valueof(resultCode));
				// 合伙人退出
			} else if (ProcessBusinessEnum.stationQuitRecord.getCode().equals(businessCode)||ProcessBusinessEnum.TPV_QUIT.getCode().equals(businessCode)) {
				if(StringUtil.isNotBlank(partnerInstanceId)){
					quitApprove(Long.valueOf(partnerInstanceId), ProcessApproveResultEnum.valueof(resultCode));
				}else{
					monitorQuitApprove(businessId, ProcessApproveResultEnum.valueof(resultCode));
				}
			} else if (ProcessBusinessCodeEnum.noticeHomePage.name().equals(businessCode)
					|| ProcessBusinessCodeEnum.activityHomePage.name().equals(businessCode)
					|| ProcessBusinessCodeEnum.projectHomePage.name().equals(businessCode)
					|| ProcessBusinessCodeEnum.trainingHomePage.name().equals(businessCode)) {
				monitorHomepageShowApprove(objectId, businessCode, ProcessApproveResultEnum.valueof(resultCode));
			//村点撤点
			}else if (ProcessBusinessEnum.SHUT_DOWN_STATION.getCode().equals(businessCode)) {
				stationService.auditQuitStation(businessId, ProcessApproveResultEnum.valueof(resultCode));
			}else if (ProcessBusinessEnum.partnerInstanceLevelAudit.getCode().equals(businessCode)) {
				logger.info("monitorLevelApprove, JSONObject :" + ob.toJSONString());
				monitorLevelApprove(ob, ProcessApproveResultEnum.valueof(resultCode));
			}
			// 节点被激活
		} else if (ProcessMsgTypeEnum.ACT_INST_START.getCode().equals(msgType)) {
			// 任务被激活
		} else if (ProcessMsgTypeEnum.TASK_ACTIVATED.getCode().equals(msgType)) {
			// 村点强制停业
			if (ProcessBusinessEnum.stationForcedClosure.getCode().equals(businessCode)||ProcessBusinessEnum.TPV_CLOSE.getCode().equals(businessCode)) {
				monitorTaskStarted(businessId, PartnerLifecycleBusinessTypeEnum.CLOSING);
				// 村点退出
			} else if (ProcessBusinessEnum.stationQuitRecord.getCode().equals(businessCode)||ProcessBusinessEnum.TPV_QUIT.getCode().equals(businessCode)) {
				monitorTaskStarted(businessId, PartnerLifecycleBusinessTypeEnum.QUITING);
			}
			//任务完成
		}else if(ProcessMsgTypeEnum.TASK_COMPLETED.getCode().equals(msgType)){
			recordCloseQuitApprove(ob, businessCode, businessId);
			//流程启动
		}else if(ProcessMsgTypeEnum.PROC_INST_START.getCode().equals(msgType)){
			
		}
	}

	// 停业、退出打印日志
	private void recordCloseQuitApprove(JSONObject ob, String businessCode, Long businessId) {
		if (ProcessBusinessEnum.stationForcedClosure.getCode().equals(businessCode)
				|| ProcessBusinessEnum.TPV_CLOSE.getCode().equals(businessCode)
				|| ProcessBusinessEnum.stationQuitRecord.getCode().equals(businessCode)
				|| ProcessBusinessEnum.TPV_QUIT.getCode().equals(businessCode)) {
			try {
				PartnerStationRel partnerStationRel = partnerInstanceBO
						.getPartnerStationRelByStationApplyId(businessId);

				CuntaoFlowRecord cuntaoFlowRecord = new CuntaoFlowRecord();

				cuntaoFlowRecord.setTargetId(partnerStationRel.getStationId());
				cuntaoFlowRecord.setTargetType(CuntaoFlowRecordTargetTypeEnum.STATION.getCode());
				cuntaoFlowRecord.setNodeTitle("审批");
				cuntaoFlowRecord.setOperatorName(ob.getString("approverName"));
				cuntaoFlowRecord.setOperatorWorkid(ob.getString("approver"));
				cuntaoFlowRecord.setOperateTime(new Date());
				cuntaoFlowRecord.setOperateOpinion(ob.getString("result"));
				cuntaoFlowRecord.setRemarks(ob.getString("taskRemark"));
				cuntaoFlowRecordBO.addRecord(cuntaoFlowRecord);
			} catch (Exception e) {
				logger.error("Failed to log close quit record.JSONObject=" + ob.toJSONString(), e);
			}
		}
	}
	
	private void monitorLevelApprove(JSONObject ob, ProcessApproveResultEnum approveResult) {
		try {
			PartnerInstanceLevelDto partnerInstanceLevelDto = JSON.parseObject(ob.getString("evaluateInfo"), PartnerInstanceLevelDto.class);
			if (ProcessApproveResultEnum.APPROVE_PASS.equals(approveResult)) {
				String adjustLevel = ob.getString("adjustLevel");
				partnerInstanceLevelDto.setPreLevel(partnerInstanceLevelDto.getCurrentLevel());
				if (StringUtils.isNotBlank(adjustLevel)) {
					String remark = "申请层级为: " + partnerInstanceLevelDto.getExpectedLevel().getLevel().toString() + ", 人工调整为 : "
							+ adjustLevel;
					partnerInstanceLevelDto.setCurrentLevel(PartnerInstanceLevelEnum.valueof(adjustLevel));
					partnerInstanceLevelDto.setRemark(remark);
				}else{
					partnerInstanceLevelDto.setCurrentLevel(partnerInstanceLevelDto.getExpectedLevel());
				}
				partnerInstanceLevelDto.setExpectedLevel(null);
				partnerInstanceService.evaluatePartnerInstanceLevel(partnerInstanceLevelDto);
			} else {
				PartnerInstanceLevel level = partnerInstanceLevelBO
						.getPartnerInstanceLevelByPartnerInstanceId(partnerInstanceLevelDto.getPartnerInstanceId());
				level.setExpectedLevel(null);
				String remark = "申请合伙人层级 " + partnerInstanceLevelDto.getCurrentLevel().getLevel().toString() + " 被拒绝";
				level.setRemark(remark);
				partnerInstanceLevelBO.updatePartnerInstanceLevel(level);
			}
		} catch (Exception e) {
			logger.error(ERROR_MSG + "monitorLevelApprove: " + ob.toJSONString(), e);
			throw e;
		}
	}

	private void monitorHomepageShowApprove(String objectId, String businessCode, ProcessApproveResultEnum approveResult) {
		busiWorkBaseInfoService.updateHomepageShowApproveResult(Long.parseLong(objectId), businessCode,
				ProcessApproveResultEnum.APPROVE_PASS.equals(approveResult));
		
	}
	
	/**
	 * 处理停业审批结果
	 * 
	 * @param stationApplyId
	 * @param approveResult
	 * @throws Exception
	 */
	public void monitorCloseApprove(Long stationApplyId, ProcessApproveResultEnum approveResult) throws Exception {
		PartnerStationRel partnerStationRel = partnerInstanceBO.getPartnerStationRelByStationApplyId(stationApplyId);
		closeApprove(partnerStationRel.getId(), approveResult);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void closeApprove(Long instanceId, ProcessApproveResultEnum approveResult) throws Exception {
		try {
			PartnerStationRel partnerStationRel = partnerInstanceBO.findPartnerInstanceById(instanceId);

			Long stationId = partnerStationRel.getStationId();

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

				// 记录村点状态变化
				// 去标，通过事件实现
				// 短信推送
				// 通知admin，合伙人退出。让他们监听村点状态变更事件
				dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.CLOSED, operatorDto);
			} else {
				//获取停业申请单
				CloseStationApplyDto closeStationApplyDto = closeStationApplyBO.getCloseStationApply(instanceId);
				PartnerInstanceStateEnum sourceInstanceState = closeStationApplyDto.getInstanceState();
		
				// 合伙人实例已停业
				partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSING, sourceInstanceState, operator);

				// 村点已停业
				if (PartnerInstanceStateEnum.SERVICING.equals(sourceInstanceState)) {
					stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.SERVICING, operator);
				} else if (PartnerInstanceStateEnum.DECORATING.equals(sourceInstanceState)) {
					stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.DECORATING, operator);
				} else {
					throw new AugeServiceException("partner state is not decorating or servicing.");
				}

				// 删除停业申请表
				closeStationApplyBO.deleteCloseStationApply(instanceId, operator);

				// 更新生命周期表
				updatePartnerLifecycle(instanceId, PartnerLifecycleRoleApproveEnum.AUDIT_NOPASS);

				// 同步station_apply，只更新状态
				stationApplySyncBO.updateStationApply(instanceId, SyncStationApplyEnum.UPDATE_STATE);

				// 记录村点状态变化
				dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.CLOSING_REFUSED, operatorDto);
			}
		} catch (Exception e) {
			logger.error(ERROR_MSG + "monitorCloseApprove", e);
			throw e;
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
	public void monitorQuitApprove(Long stationApplyId, ProcessApproveResultEnum approveResult) throws Exception {
		PartnerStationRel partnerStationRel = partnerInstanceBO.getPartnerStationRelByStationApplyId(stationApplyId);
		quitApprove(partnerStationRel.getId(), approveResult);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void quitApprove(Long instanceId, ProcessApproveResultEnum approveResult) throws Exception {
		try {
			OperatorDto operatorDto = OperatorDto.defaultOperator();
			String operator = operatorDto.getOperator();

			PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);

			Long stationId = instance.getStationId();

			// 校验退出申请单是否存在
			QuitStationApply quitApply = quitStationApplyBO.findQuitStationApply(instanceId);

			// 审批通过
			if (ProcessApproveResultEnum.APPROVE_PASS.equals(approveResult)) {
				// 村点已撤点
				if (quitApply.getIsQuitStation() == null || "y".equals(quitApply.getIsQuitStation())) {
					stationBO.changeState(stationId, StationStatusEnum.QUITING, StationStatusEnum.QUIT, operator);
				}

				// 处理合伙人、淘帮手、村拍档不一样的业务
				partnerInstanceHandler.handleDifferQuitAuditPass(instanceId, PartnerInstanceTypeEnum.valueof(instance.getType()));

				generalTaskSubmitService.submitQuitApprovedTask(instanceId, stationId, instance.getTaobaoUserId(),
						quitApply.getIsQuitStation());
			} else {
				// 合伙人实例已停业
				partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.QUITING, PartnerInstanceStateEnum.CLOSED, operator);
				// 村点已停业
				if (quitApply.getIsQuitStation() == null || "y".equals(quitApply.getIsQuitStation())) {
					stationBO.changeState(stationId, StationStatusEnum.QUITING, StationStatusEnum.CLOSED, operator);
				}

				// 删除退出申请单
				quitStationApplyBO.deleteQuitStationApply(instanceId, operator);

				// 更新什么周期表
				PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.QUITING,
						PartnerLifecycleCurrentStepEnum.PROCESSING);

				PartnerLifecycleDto param = new PartnerLifecycleDto();
				param.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_NOPASS);
				param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
				param.setLifecycleId(items.getId());
				partnerLifecycleBO.updateLifecycle(param);

				// 同步station_apply
				stationApplySyncBO.updateStationApply(instanceId, SyncStationApplyEnum.UPDATE_STATE);

				// 发送合伙人实例状态变化事件
				dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.QUITTING_REFUSED, operatorDto);
			}
		} catch (Exception e) {
			logger.error(ERROR_MSG + "monitorQuitApprove", e);
			throw e;
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

		if (null == items || PartnerLifecycleRoleApproveEnum.TO_AUDIT.getCode().equals(items.getRoleApprove())) {
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
		EventDispatcherUtil.dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
	}
}
