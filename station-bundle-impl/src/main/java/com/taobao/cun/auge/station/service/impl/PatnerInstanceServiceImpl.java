package com.taobao.cun.auge.station.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.conversion.QuitStationApplyConverter;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.dal.domain.StationApply;
import com.taobao.cun.auge.station.bo.Emp360BO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.ProtocolBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.condition.ForcedCloseCondition;
import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.condition.PartnerLifecycleCondition;
import com.taobao.cun.auge.station.condition.QuitStationApplyCondition;
import com.taobao.cun.auge.station.convert.CuntaoFlowRecordEventConverter;
import com.taobao.cun.auge.station.convert.StationStatusChangedEventConverter;
import com.taobao.cun.auge.station.dto.ProcessApproveResultDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleConfirmEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleQuitProtocolEnum;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.enums.ProtocolTargetBizTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.auge.station.service.PatnerInstanceService;
import com.taobao.cun.common.exception.ServiceException;
import com.taobao.cun.common.resultmodel.ResultModel;
import com.taobao.cun.crius.event.client.EventDispatcher;
import com.taobao.cun.dto.trade.TaobaoNoEndTradeDto;
import com.taobao.cun.service.trade.TaobaoTradeOrderQueryService;
import com.taobao.tc.domain.dataobject.OrderInfoTO;
import com.taobao.tc.refund.domain.RefundDO;

public class PatnerInstanceServiceImpl implements PatnerInstanceService {

	private static final Logger logger = LoggerFactory.getLogger(PatnerInstanceService.class);

	@Autowired
	ProtocolBO protocolBO;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	PartnerInstanceHandler partnerInstanceHandler;
	
	@Autowired
	PartnerLifecycleBO partnerLifecycleBO;
	
	@Autowired
	StationBO stationBO;
	
	@Autowired
	QuitStationApplyBO quitStationApplyBO;
	
	@Autowired
	QuitStationApplyConverter quitStationApplyConverter;
	
	@Autowired
	StationApplyBO stationApplyBO;
	
	@Autowired
	TaobaoTradeOrderQueryService taobaoTradeOrderQueryService;
	
	@Autowired
	Emp360BO emp360BO;

	@Override
	public Long addTemp(PartnerInstanceCondition condition) throws AugeServiceException {
		return null;
	}

	@Override
	public Long updateTemp(PartnerInstanceCondition condition) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long addSubmit(PartnerInstanceCondition condition) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long updateSubmit(PartnerInstanceCondition condition) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(PartnerInstanceCondition condition) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Long instanceId) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void signSettledProtocol(Long taobaoUserId) throws AugeServiceException {
		try {
			Long instanceId = partnerInstanceBO.findPartnerInstanceId(taobaoUserId, PartnerInstanceStateEnum.SETTLING);
			protocolBO.signProtocol(taobaoUserId, ProtocolTypeEnum.SETTLE_PRO, instanceId,
					ProtocolTargetBizTypeEnum.PARTNER_INSTANCE);
		} catch (Exception e) {
			logger.error(StationExceptionEnum.SIGN_SETTLE_PROTOCOL_FAIL.getDesc(), e);
			throw new AugeServiceException(StationExceptionEnum.SIGN_SETTLE_PROTOCOL_FAIL);
		}
	}

	@Override
	public void signManageProtocol(Long taobaoUserId) throws AugeServiceException {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean freezeBond(Long taobaoUserId, BigDecimal frozenMoney) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean openStation(Long partnerInstanceId, Date openDate, boolean isImme, String employeeId)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean applyCloseByPartner(Long taobaoUserId) throws AugeServiceException {
		try {
			
			PartnerStationRel partnerInstance = partnerInstanceBO.findPartnerInstance(taobaoUserId,PartnerInstanceStateEnum.SERVICING);
			if(partnerInstance == null) {
				throw new AugeServiceException(PartnerExceptionEnum.NO_RECORD);
			}
			partnerInstanceBO.changeState(partnerInstance.getId(), PartnerInstanceStateEnum.SERVICING, PartnerInstanceStateEnum.CLOSING, String.valueOf(taobaoUserId));
			stationBO.changeState(partnerInstance.getId(), StationStatusEnum.SERVICING, StationStatusEnum.CLOSING, String.valueOf(taobaoUserId));
			//插入生命周期扩展表
			PartnerLifecycleCondition partnerLifecycle = new PartnerLifecycleCondition();
			partnerLifecycle.setPartnerType(PartnerInstanceTypeEnum.valueof(partnerInstance.getType()));
			partnerLifecycle.setBusinessType(PartnerLifecycleBusinessTypeEnum.CLOSING);
			partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.WAIT_CONFIRM);
			partnerLifecycle.setQuitProtocol(PartnerLifecycleQuitProtocolEnum.SIGNED);
			partnerLifecycle.setCurrentStep(PartnerLifecycleCurrentStepEnum.CONFIRM);
			partnerLifecycle.setPartnerInstanceId(partnerInstance.getId());
			partnerLifecycleBO.addLifecycle(partnerLifecycle);
			//TODO:插入停业协议
			
			//TODO:发送状态换砖 事件，接受事件里 1记录OPLOG日志  2短信推送 3 状态转换日志
			return true;
		} catch (Exception e) {
			logger.error("applyCloseByPartner.error.param:"+taobaoUserId,e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}
	

	@Override
	public boolean confirmClose(Long partnerInstanceId, String employeeId, boolean isAgree)
			throws AugeServiceException {
		try {
			PartnerStationRel partnerInstance =  partnerInstanceBO.findPartnerInstanceById(partnerInstanceId);
			if(partnerInstance == null) {
				throw new AugeServiceException(PartnerExceptionEnum.NO_RECORD);
			}
			// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
			partnerInstanceHandler.validateExistValidChildren(
					PartnerInstanceTypeEnum.valueof(partnerInstance.getType()),partnerInstanceId);
            
			Long lifecycleId = partnerLifecycleBO.getLifecycleItemsId(partnerInstance.getId(), PartnerLifecycleBusinessTypeEnum.CLOSING, PartnerLifecycleCurrentStepEnum.CONFIRM);
			PartnerLifecycleCondition partnerLifecycle = new PartnerLifecycleCondition();
			partnerLifecycle.setLifecycleId(lifecycleId);
			partnerLifecycle.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
			
			if (isAgree) {
				partnerInstanceBO.changeState(partnerInstanceId, PartnerInstanceStateEnum.CLOSING, PartnerInstanceStateEnum.CLOSED,employeeId);
				//更新服务结束时间
				PartnerStationRel instance= new PartnerStationRel();
				instance.setServiceEndTime(new Date());
				partnerInstanceBO.updatePartnerInstance(partnerInstanceId,instance,employeeId);
				
				stationBO.changeState(partnerInstance.getId(), StationStatusEnum.CLOSING, StationStatusEnum.CLOSED,employeeId);
				partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.CONFIRM);
			}else {
				partnerInstanceBO.changeState(partnerInstanceId, PartnerInstanceStateEnum.CLOSING, PartnerInstanceStateEnum.SERVICING, employeeId);
				stationBO.changeState(partnerInstanceId, StationStatusEnum.CLOSING, StationStatusEnum.SERVICING, employeeId);
				partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.CANCEL);
			}
			partnerLifecycleBO.updateLifecycle(partnerLifecycle);
			//TODO:发送状态换砖 事件，接受事件里 1记录OPLOG日志  2短信推送 3 状态转换日志 4,去标
			return true;
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("partnerInstanceId:").append(partnerInstanceId).append(" employeeId:").append(employeeId).append(" isAgree:").append(isAgree);
			logger.error("confirmClose.error.param:"+sb.toString(),e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	@Override
	public void applyCloseByEmployee(ForcedCloseCondition forcedCloseCondition, String employeeId)
			throws AugeServiceException {
		try {
			Long instanceId = forcedCloseCondition.getInstanceId();
			PartnerStationRel partnerStationRel = partnerInstanceBO.findPartnerInstanceById(instanceId);

			// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
			partnerInstanceHandler.validateExistValidChildren(
					PartnerInstanceTypeEnum.valueof(partnerStationRel.getType()), instanceId);

			// 合伙人实例停业中
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.SERVICING,
					PartnerInstanceStateEnum.CLOSING, employeeId);

			// 村点停业中
			stationBO.changeState(instanceId, StationStatusEnum.SERVICING, StationStatusEnum.CLOSING, employeeId);

			// 通过事件，定时钟，启动停业流程

		} catch (Exception e) {
			logger.error(StationExceptionEnum.SIGN_SETTLE_PROTOCOL_FAIL.getDesc(), e);
			throw new AugeServiceException(StationExceptionEnum.SIGN_SETTLE_PROTOCOL_FAIL);
		}
	}

	@Override
	public void auditClose(ProcessApproveResultDto approveResultDto) throws Exception {
		// 记录审批日志
		EventDispatcher.getInstance().dispatch("cuntao-flow-record-event",
				CuntaoFlowRecordEventConverter.convert(approveResultDto));

		Long stationApplyId = Long.valueOf(approveResultDto.getObjectId());
		Long instanceId = partnerInstanceBO.findPartnerInstanceId(stationApplyId);
		Long stationId = partnerInstanceBO.findStationIdByInstanceId(instanceId);
		String operator = "sys";
		if (ProcessApproveResultEnum.APPROVE_PASS.equals(approveResultDto.getResult())) {
			// 合伙人实例已停业
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSING, PartnerInstanceStateEnum.CLOSED,
					operator);
			// 更新服务结束时间
			PartnerStationRel instance = new PartnerStationRel();
			instance.setServiceEndTime(new Date());
			partnerInstanceBO.updatePartnerInstance(instanceId, instance, operator);

			// 村点已停业
			stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.CLOSED, operator);
			// 去标事件
			// sendForcedQuitAuditEvent(applyId, context);

			// 记录村点状态变化
			EventDispatcher.getInstance().dispatch("station-state-changed-event", StationStatusChangedEventConverter
					.convert(stationId, StationStatusEnum.CLOSED, operator, operator));
		} else {
			// 合伙人实例已停业
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSING,
					PartnerInstanceStateEnum.SERVICING, operator);

			// 村点已停业
			stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.SERVICING, operator);

			// 记录村点状态变化
			EventDispatcher.getInstance().dispatch("station-state-changed-event", StationStatusChangedEventConverter
					.convert(stationId, StationStatusEnum.SERVICING, operator, operator));
		}
	}

	@Override
	public void applyQuitByEmployee(QuitStationApplyCondition quitApplyCondition, String employeeId)
			throws AugeServiceException {
		try {
			Long instanceId = quitApplyCondition.getInstanceId();
			Long stationApplyId = partnerInstanceBO.findStationApplyId(instanceId);

			PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);

			// 查询申请单，不存在会抛异常
			StationApply stationApply = stationApplyBO.findStationApplyById(stationApplyId);

			// 校验是否已经存在退出申请单
			QuitStationApply quitStationApply = quitStationApplyBO.findQuitStationApply(instanceId);
			if (quitStationApply != null) {
				throw new AugeServiceException(StationExceptionEnum.QUIT_STATION_APPLY_EXIST);
			}

			// 校验是否存在未结束的订单
			validateTrade(stationApply);

			// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
			partnerInstanceHandler.validateExistValidChildren(PartnerInstanceTypeEnum.valueof(instance.getType()),
					instanceId);

			quitStationApply = new QuitStationApply();
			quitStationApply.setPartnerInstanceId(instanceId);
			quitStationApply.setStationApplyId(stationApplyId);
			quitStationApply.setRevocationAppFormFileName(quitApplyCondition.getRevocationAppFormFileName());
			quitStationApply.setOtherDescription(quitApplyCondition.getOtherDescription());
			quitStationApply.setAssetType(quitApplyCondition.getAssertUseState().getCode());
			quitStationApply.setLoanHasClose(quitApplyCondition.getLoanHasClose());
			// FIXME FHH 枚举
			quitStationApply.setState("FINISHED");
			quitStationApply.setSubmittedPeopleName(emp360BO.getName(employeeId));

			quitStationApplyBO.saveQuitStationApply(quitStationApply, employeeId);

			// 合伙人实例退出中
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSED, PartnerInstanceStateEnum.QUITING,
					employeeId);

			// 村点退出中
			stationBO.changeState(instanceId, StationStatusEnum.CLOSED, StationStatusEnum.QUITING, employeeId);

			// 退出任务
			createQuitingTask();
			// 失效tair
			// tairCache.invalid(TairCache.STATION_APPLY_ID_KEY_DETAIL_VALUE_PRE
			// + quitStationApplyDto.getStationApplyId());
		} catch (Exception e) {
			// FIXME FHH
			logger.error(StationExceptionEnum.SIGN_SETTLE_PROTOCOL_FAIL.getDesc(), e);
			throw new AugeServiceException(StationExceptionEnum.SIGN_SETTLE_PROTOCOL_FAIL);
		}
	}
	
	private void createQuitingTask(){
//        // 创建退出村点任务流程
//        StationQuitFlowDto stationQuitFlowDto = new StationQuitFlowDto();
//        stationQuitFlowDto.setTargetId(quitStationApplyDto.getStationApplyId());
//        stationQuitFlowDto.setOperatorWorkid(context.getWorkNo());
//        stationQuitFlowDto.setType(CuntaoFlowTargetTypeEnum.STATION_QUIT.getCode());
//        stationQuitFlowDto.setOrgId(String.valueOf(stationApplyDetailDto.getCuntaoOrg().getParentId()));
//        
//        //退出流程启动
//		TaskVo task = new TaskVo();
//		task.setBusinessNo(quitStationApplyDto.getStationApplyId().toString());
//		task.setBeanName("stationQuitFlowBo");
//		task.setMethodName("startQuitStationTask");
//		task.setBusinessStepNo(1l);
//		task.setBusinessType(BusinessTypeEnum.STATION_QUIT_FLOW_TASK);
//		task.setBusinessStepDesc(BusinessTypeEnum.STATION_QUIT_FLOW_TASK.getValue());
//		task.setOperator(context.getWorkNo());
//        task.setParameter(stationQuitFlowDto);
//		taskExecuteService.submitTask(task, false);
//
//        ResultModel<Long> resultModel = new ResultModel<Long>();
//        resultModel.setSuccess(true);
//        resultModel.setResult(quitStationApplyId);
//
//        return resultModel;
	}

    //FIXME FHH 调用了center的接口，后续需要迁移
	private void validateTrade(StationApply stationApply) {
		ResultModel<TaobaoNoEndTradeDto> taobaoNoEndTradeDtoResultModel = taobaoTradeOrderQueryService.findNoEndTradeOrders(stationApply.getTaobaoUserId(),stationApply.getServiceEndDate());
		if (!taobaoNoEndTradeDtoResultModel.isSuccess()) {
		    throw new ServiceException(taobaoNoEndTradeDtoResultModel.getException());
		}

		TaobaoNoEndTradeDto taobaoNoEndTradeDto = taobaoNoEndTradeDtoResultModel.getResult();
		taobaoNoEndTradeDto.getBatchQueryOrderInfoResultDO();
		if (taobaoNoEndTradeDto.isExistsNoEndOrder()) {
			StringBuilder build = new StringBuilder();
			for(OrderInfoTO info : taobaoNoEndTradeDto.getBatchQueryOrderInfoResultDO().getOrderList()){
				build.append(info.getBizOrderDO().getBizOrderId());
				build.append(info.getBizOrderDO().getAuctionTitle());
				build.append("\n");
			}
			for(RefundDO refund : taobaoNoEndTradeDto.getBatchRefundResultDO().getRefundList()){
				build.append("退款中:\n");
				build.append(refund.getBizOrderId());
				build.append(refund.getAuctionTitle());
				build.append("\n");
			}
		    throw new ServiceException("村掌柜仍有未完成的代购单（交易订单确认收货）、待退款（退款完结），请联系掌柜核实" + build.toString());
		}
	}

	@Override
	public void auditQuit(ProcessApproveResultDto approveResultDto) throws Exception {
		String operator = "sys";
		Long stationApplyId = Long.valueOf(approveResultDto.getObjectId());
		Long instanceId = partnerInstanceBO.findPartnerInstanceId(stationApplyId);
		Long stationId = partnerInstanceBO.findStationIdByInstanceId(instanceId);
		
		// 记录审批日志
		EventDispatcher.getInstance().dispatch("cuntao-flow-record-event",
				CuntaoFlowRecordEventConverter.convert(approveResultDto));

		if (ProcessApproveResultEnum.APPROVE_PASS.equals(approveResultDto.getResult())) {
			// 提出任务
			quitTasks();

			// 记录村点状态变化
			EventDispatcher.getInstance().dispatch("cuntao-flow-record-event", StationStatusChangedEventConverter
					.convert(stationId, StationStatusEnum.QUIT, operator, operator));
		} else {
			// 合伙人实例已停业
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.QUITING, PartnerInstanceStateEnum.CLOSED,
					operator);

			// 村点已停业
			stationBO.changeState(stationId, StationStatusEnum.QUITING, StationStatusEnum.CLOSED, operator);

			// 删除退出申请单
			quitStationApplyBO.deleteQuitStationApply(instanceId, operator);

			// 记录村点状态变化
			EventDispatcher.getInstance().dispatch("cuntao-flow-record-event", StationStatusChangedEventConverter
					.convert(stationId, StationStatusEnum.CLOSED, operator, operator));
		}
		// tair清空缓存
	}
	
	private void quitTasks(){
		
	}

	@Override
	public Long applySettle(PartnerInstanceCondition condition, PartnerInstanceTypeEnum partnerInstanceTypeEnum)
			throws AugeServiceException {
		return partnerInstanceHandler.handleApplySettle(condition, partnerInstanceTypeEnum);
	}

}
