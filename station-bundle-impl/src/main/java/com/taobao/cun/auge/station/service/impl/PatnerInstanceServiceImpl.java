package com.taobao.cun.auge.station.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.conversion.QuitStationApplyConverter;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.StationApply;
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
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleConfirmEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleQuitProtocolEnum;
import com.taobao.cun.auge.station.enums.ProtocolTargetBizTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.auge.station.service.PatnerInstanceService;

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

	@Override
	public Long addTemp(PartnerInstanceCondition condition) throws AugeServiceException {
		// TODO Auto-generated method stub
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
				partnerInstanceBO.changeState(partnerInstance.getId(), PartnerInstanceStateEnum.CLOSING, PartnerInstanceStateEnum.CLOSED,employeeId);
				stationBO.changeState(partnerInstance.getId(), StationStatusEnum.CLOSING, StationStatusEnum.CLOSED,employeeId);
				partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.CONFIRM);
			}else {
				partnerInstanceBO.changeState(partnerInstance.getId(), PartnerInstanceStateEnum.CLOSING, PartnerInstanceStateEnum.SERVICING, employeeId);
				stationBO.changeState(partnerInstance.getId(), StationStatusEnum.CLOSING, StationStatusEnum.SERVICING, employeeId);
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
	public void auditClose(Long stationApplyId, String approver, boolean isAgree) throws Exception {
		// 记录日志事件
		// sendForcedClosureEvent(stationQuitFlowDto, context, applyId,
		// PermissionNameEnum.BOPS_FORCE_QUIT_PROVINCE_AUDIT.getCode());

		Long instanceId = partnerInstanceBO.findPartnerInstanceId(stationApplyId);

		if (isAgree) {
			// 合伙人实例已停业
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSING, PartnerInstanceStateEnum.CLOSED,
					approver);
			// 更新服务结束时间
			PartnerStationRel instance = new PartnerStationRel();
			instance.setServiceEndTime(new Date());
			partnerInstanceBO.updatePartnerInstance(instanceId, instance, approver);

			// 村点已停业
			stationBO.changeState(instanceId, StationStatusEnum.CLOSING, StationStatusEnum.CLOSED, approver);
			// 去标事件
			// sendForcedQuitAuditEvent(applyId, context);
		} else {
			// 合伙人实例已停业
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSING,
					PartnerInstanceStateEnum.SERVICING, approver);

			// 村点已停业
			stationBO.changeState(instanceId, StationStatusEnum.CLOSING, StationStatusEnum.SERVICING, approver);
		}
	}

	@Override
	public void applyQuitByEmployee(QuitStationApplyCondition quitApplyCondition,String employeeId) throws AugeServiceException {
		try {
			Long stationApplyId = quitApplyCondition.getStationApplyId();
			//查询申请单，不存在会抛异常
			StationApply stationApply = stationApplyBO.findStationApplyById(stationApplyId);
			
			Long instanceId = partnerInstanceBO.findPartnerInstanceId(stationApplyId);
			
//			
//			QuitStationApply quitStationApply = quitStationApplyBO.findQuitStationApply(instanceId);
//	        if (quitStationApply != null) {
//	            throw new AugeServiceException(StationExceptionEnum.QUIT_STATION_APPLY_EXIST);
//	        }
//
//
//	        ResultModel<TaobaoNoEndTradeDto> taobaoNoEndTradeDtoResultModel = taobaoTradeOrderQueryService.findNoEndTradeOrders(stationApplyDetailDto.getTaobaoUserId(),stationApplyDetailDto.getServiceEndDate());
//	        if (!taobaoNoEndTradeDtoResultModel.isSuccess()) {
//	            throw new ServiceException(taobaoNoEndTradeDtoResultModel.getException());
//	        }
//
//	        TaobaoNoEndTradeDto taobaoNoEndTradeDto = taobaoNoEndTradeDtoResultModel.getResult();
//	        if (taobaoNoEndTradeDto.isExistsNoEndOrder()) {
//				StringBuilder build = new StringBuilder();
//	        	for(OrderInfoTO info : taobaoNoEndTradeDto.getBatchQueryOrderInfoResultDO().getOrderList()){
//	        		build.append(info.getBizOrderDO().getBizOrderId());
//	        		build.append(info.getBizOrderDO().getAuctionTitle());
//	        		build.append("\n");
//	        	}
//	        	for(RefundDO refund : taobaoNoEndTradeDto.getBatchRefundResultDO().getRefundList()){
//	        		build.append("退款中:\n");
//	        		build.append(refund.getBizOrderId());
//	        		build.append(refund.getAuctionTitle());
//	        		build.append("\n");
//	        	}
//	            throw new ServiceException("村掌柜仍有未完成的代购单（交易订单确认收货）、待退款（退款完结），请联系掌柜核实" + build.toString());
//	        }
//
//	        Long cnt = tpaStationBO.getTpaCount(stationApplyDetailDto.getStationId());
//	        if(cnt > 0){
//	        	throw new ServiceException("该合伙人下面挂靠有淘帮手，请清退淘帮手后才能撤点");
//	        }
//
//	        quitStationApplyDO = new QuitStationApplyDO();
//	        quitStationApplyDO.setStationApplyId(stationApplyDetailDto.getStationApplyId());
//	        quitStationApplyDO.setRevocationAppFormFileName(quitStationApplyDto.getRevocationAppFormFileName());
//	        quitStationApplyDO.setOtherDescription(quitStationApplyDto.getOtherDescription());
//	        quitStationApplyDO.setAssetType(quitStationApplyDto.getQuitStationApplyAssertTypeEnum().getCode());
//	        quitStationApplyDO.setLoanHasClose(quitStationApplyDto.getLoanHasClose());
//	        quitStationApplyDO.setState(QuitStationApplyDO.STATE_FINASH);
//	        quitStationApplyDO.setCreator(context.getWorkNo());
//	        quitStationApplyDO.setModifier(context.getWorkNo());
//	        quitStationApplyDO.setIsDeleted("n");
//	        List<String> workNoList = new ArrayList<String>();
//	        workNoList.add(quitStationApplyDO.getCreator());
//	        Map<String, EmpInfoListVo> empInfoListVoMap =  emp360BO.getEmpInfoByWorkNos(workNoList);
//	        String name = "";
//	        if (empInfoListVoMap != null && empInfoListVoMap.size() > 0) {
//	            EmpInfoListVo empInfoListVo = empInfoListVoMap.get(context.getWorkNo());
//	            if (empInfoListVo != null) {
//	                name = empInfoListVo.getName();
//	                quitStationApplyDO.setSubmittedPeopleName(empInfoListVo.getName());
//	            }
//	        }
//
//	        //重构，增量数据同步，退出申请数据
//	        //吃掉异常，目的是不影响以前的业务
//	        try {
//	            Long instanceId = syncPartnerInstanceBO.findPartnerInstanceId(stationApplyDetailDto.getStationApplyId());
//	            quitStationApplyDO.setPartnerInstanceId(instanceId);
//	        } catch (Exception e) {
//	            logger.error("退出申请时，查询合伙人实例id失败。stationApplyId = " + stationApplyDetailDto.getStationApplyId());
//	        }
//
//	        Long quitStationApplyId = (Long)quitStationApplyDao.insertQuitStationApply(quitStationApplyDO);
//
//	        if (quitStationApplyId == null || quitStationApplyId <= 0) {
//	            throw new ServiceException("quitStationApplyDao.insertQuitStationApply error !");
//	        }
//
//	        Boolean flag = stationApplyBO.quitAuditStationApply(quitStationApplyDto.getStationApplyId(),context);
//
//	        if (!flag) {
//	            throw new ServiceException("stationApplyBO.closeStationApply error !");
//	        }
//
//
//
//	        // 创建退出村点任务流程
//	        StationQuitFlowDto stationQuitFlowDto = new StationQuitFlowDto();
//	        stationQuitFlowDto.setTargetId(quitStationApplyDto.getStationApplyId());
//	        stationQuitFlowDto.setOperatorWorkid(context.getWorkNo());
//	        stationQuitFlowDto.setType(CuntaoFlowTargetTypeEnum.STATION_QUIT.getCode());
//	        stationQuitFlowDto.setOrgId(String.valueOf(stationApplyDetailDto.getCuntaoOrg().getParentId()));
//	        
//	        //退出流程启动
//			TaskVo task = new TaskVo();
//			task.setBusinessNo(quitStationApplyDto.getStationApplyId().toString());
//			task.setBeanName("stationQuitFlowBo");
//			task.setMethodName("startQuitStationTask");
//			task.setBusinessStepNo(1l);
//			task.setBusinessType(BusinessTypeEnum.STATION_QUIT_FLOW_TASK);
//			task.setBusinessStepDesc(BusinessTypeEnum.STATION_QUIT_FLOW_TASK.getValue());
//			task.setOperator(context.getWorkNo());
//	        task.setParameter(stationQuitFlowDto);
//			taskExecuteService.submitTask(task, false);
//
//	        ResultModel<Long> resultModel = new ResultModel<Long>();
//	        resultModel.setSuccess(true);
//	        resultModel.setResult(quitStationApplyId);
//
//	        return resultModel;
//			
//			
//			Long quitStationApplyId = quitStationApplyBO
//					.saveQuitStationApply(quitStationApplyConverter.toQuitStationApply(quitApplyCondition));
//			return quitStationApplyId;
			// 失效tair
			// tairCache.invalid(TairCache.STATION_APPLY_ID_KEY_DETAIL_VALUE_PRE
			// + quitStationApplyDto.getStationApplyId());
		} catch (Exception e) {
			// FIXME FHH
			logger.error(StationExceptionEnum.SIGN_SETTLE_PROTOCOL_FAIL.getDesc(), e);
			throw new AugeServiceException(StationExceptionEnum.SIGN_SETTLE_PROTOCOL_FAIL);
		}
	}

	@Override
	public boolean auditQuit(ForcedCloseCondition forcedCloseCondition, String employeeId) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long applySettle(PartnerInstanceCondition condition, PartnerInstanceTypeEnum partnerInstanceTypeEnum)
			throws AugeServiceException {
		return partnerInstanceHandler.handleApplySettle(condition, partnerInstanceTypeEnum);
	}

}
