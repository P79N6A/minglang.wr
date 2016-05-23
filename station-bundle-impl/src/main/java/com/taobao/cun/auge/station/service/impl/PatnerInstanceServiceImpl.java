package com.taobao.cun.auge.station.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.event.domain.StationStatusChangedEvent;
import com.taobao.cun.auge.station.bo.Emp360BO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.ProtocolBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.TradeBO;
import com.taobao.cun.auge.station.condition.PartnerLifecycleCondition;
import com.taobao.cun.auge.station.convert.CuntaoFlowRecordEventConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.convert.StationStatusChangedEventConverter;
import com.taobao.cun.auge.station.dto.ApplySettleDto;
import com.taobao.cun.auge.station.dto.ConfirmCloseDto;
import com.taobao.cun.auge.station.dto.ForcedCloseDto;
import com.taobao.cun.auge.station.dto.OpenStationDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.ProcessApproveResultDto;
import com.taobao.cun.auge.station.dto.QuitDto;
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
import com.taobao.cun.crius.event.client.EventDispatcher;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
/**
 * 
 * @author quanzhu.wangqz
 *
 */
@HSFProvider(serviceInterface= PatnerInstanceService.class)
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
	StationApplyBO stationApplyBO;
	
	@Autowired
	Emp360BO emp360BO;
	
	@Autowired
	PartnerBO partnerBO;
	
	@Autowired
	TradeBO tradeBO;

	@Override
	public Long addTemp(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		return null;
	}

	@Override
	public Long updateTemp(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long addSubmit(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long updateSubmit(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
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
	public boolean openStation(OpenStationDto openStationDto) throws AugeServiceException {
		if (openStationDto.isImme()) {//立即开业
			//TODO:检查开业包
			if (!checkKyPackage()) {
				throw new AugeServiceException(StationExceptionEnum.KAYE_PACKAGE_NOT_EXIST);
			}
			partnerInstanceBO.changeState(openStationDto.getPartnerInstanceId(), PartnerInstanceStateEnum.DECORATING, PartnerInstanceStateEnum.SERVICING, openStationDto.getOperator());
			partnerInstanceBO.updateOpenDate(openStationDto.getPartnerInstanceId(), openStationDto.getOpenDate(), openStationDto.getOperator());
			// 记录村点状态变化
			EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, PartnerInstanceEventConverter
					.convert(PartnerInstanceStateEnum.DECORATING, PartnerInstanceStateEnum.SERVICING,partnerInstanceBO.getPartnerInstanceById(openStationDto.getPartnerInstanceId())));
		}else{//定时开业
			partnerInstanceBO.updateOpenDate(openStationDto.getPartnerInstanceId(), openStationDto.getOpenDate(), openStationDto.getOperator());
		}
		
		return false;
	}
	
	private boolean checkKyPackage(){
		//TODO:检查开业包
		return true;
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
			EventDispatcher.getInstance().dispatch("xxxxx", partnerLifecycle);
			//TODO:发送状态换砖 事件，接受事件里 1记录OPLOG日志  2短信推送 3 状态转换日志
			return true;
		} catch (Exception e) {
			logger.error("applyCloseByPartner.error.param:"+taobaoUserId,e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}
	

	@Override
	public boolean confirmClose(ConfirmCloseDto confirmCloseDto) throws AugeServiceException {
		Long partnerInstanceId = confirmCloseDto.getPartnerInstanceId();
		String employeeId= confirmCloseDto.getOperator();
		boolean isAgree = confirmCloseDto.isAgree();
		try {
		
			PartnerStationRel partnerInstance =  partnerInstanceBO.findPartnerInstanceById(confirmCloseDto.getPartnerInstanceId());
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
	public void applyCloseByManager(ForcedCloseDto forcedCloseDto) throws AugeServiceException {
		Long instanceId = forcedCloseDto.getInstanceId();
		PartnerStationRel partnerStationRel = partnerInstanceBO.findPartnerInstanceById(instanceId);
		Long stationId = partnerStationRel.getStationId();

		// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
		partnerInstanceHandler.validateExistValidChildren(PartnerInstanceTypeEnum.valueof(partnerStationRel.getType()),
				instanceId);

		// 合伙人实例停业中
		partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.SERVICING, PartnerInstanceStateEnum.CLOSING,
				forcedCloseDto.getOperator());

		// 村点停业中
		stationBO.changeState(stationId, StationStatusEnum.SERVICING, StationStatusEnum.CLOSING,
				forcedCloseDto.getOperator());

		// 通过事件，定时钟，启动停业流程
		StationStatusChangedEvent event = StationStatusChangedEventConverter.convert(StationStatusEnum.SERVICING,
				StationStatusEnum.CLOSING, partnerInstanceBO.getPartnerInstanceById(instanceId),
				forcedCloseDto.getOperator());
		event.setRemark(null != forcedCloseDto.getReason() ? forcedCloseDto.getReason().getDesc() : "");
		event.setOperatorOrgId(forcedCloseDto.getOperatorOrgId());
		EventDispatcher.getInstance().dispatch(EventConstant.CUNTAO_STATION_STATUS_CHANGED_EVENT, event);
	}

	@Override
	public void applyQuitByManager(QuitDto quitDto)	throws AugeServiceException {
		try {
			Long instanceId = quitDto.getInstanceId();
			String operator = quitDto.getOperator();
			
			PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);
			Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
			
//			quitDto.

			//校验申请退出的条件
			validateQuitPreCondition(instance, partner);

			//保存退出申请单
			QuitStationApply quitStationApply = convert(quitDto, instance);
			quitStationApplyBO.saveQuitStationApply(quitStationApply, operator);

			// 合伙人实例退出中
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSED, PartnerInstanceStateEnum.QUITING,
					operator);

			// 村点退出中
			stationBO.changeState(instance.getStationId(), StationStatusEnum.CLOSED, StationStatusEnum.QUITING, operator);
			
			//退出审批流程，由事件监听完成
			// 记录村点状态变化
			EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, PartnerInstanceEventConverter
					.convert(PartnerInstanceStateEnum.CLOSED, PartnerInstanceStateEnum.QUITING,partnerInstanceBO.getPartnerInstanceById(instanceId)));

			EventDispatcher.getInstance().dispatch(EventConstant.CUNTAO_STATION_STATUS_CHANGED_EVENT, StationStatusChangedEventConverter.convert(StationStatusEnum.CLOSED,
					StationStatusEnum.QUITING, partnerInstanceBO.getPartnerInstanceById(instanceId),operator));
			
			// 失效tair
			// tairCache.invalid(TairCache.STATION_APPLY_ID_KEY_DETAIL_VALUE_PRE
			// + quitStationApplyDto.getStationApplyId());
		} catch (Exception e) {
			// FIXME FHH
			logger.error(StationExceptionEnum.SIGN_SETTLE_PROTOCOL_FAIL.getDesc(), e);
			throw new AugeServiceException(StationExceptionEnum.SIGN_SETTLE_PROTOCOL_FAIL);
		}
	}

	private void validateQuitPreCondition(PartnerStationRel instance, Partner partner)
			throws AugeServiceException {
		Long instanceId = instance.getId();
		// 校验是否已经存在退出申请单
		QuitStationApply quitStationApply = quitStationApplyBO.findQuitStationApply(instanceId);
		if (quitStationApply != null) {
			throw new AugeServiceException(StationExceptionEnum.QUIT_STATION_APPLY_EXIST);
		}

		// 校验是否存在未结束的订单
		tradeBO.validateNoEndTradeOrders(partner.getTaobaoUserId(),instance.getServiceEndTime());

		// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
		partnerInstanceHandler.validateExistValidChildren(PartnerInstanceTypeEnum.valueof(instance.getType()),
				instanceId);
	}

	private QuitStationApply convert(QuitDto quitStationApplyDto, PartnerStationRel instance) throws AugeServiceException {
		QuitStationApply quitStationApply;
		quitStationApply = new QuitStationApply();
		quitStationApply.setPartnerInstanceId(instance.getId());
		quitStationApply.setStationApplyId(instance.getStationApplyId());
		quitStationApply.setRevocationAppFormFileName(quitStationApplyDto.getRevocationAppFormFileName());
		quitStationApply.setOtherDescription(quitStationApplyDto.getOtherDescription());
		quitStationApply.setAssetType(quitStationApplyDto.getAssertUseState().getCode());
		quitStationApply.setLoanHasClose(quitStationApplyDto.getLoanHasClose());
		// FIXME FHH 枚举
		quitStationApply.setState("FINISHED");
		quitStationApply.setSubmittedPeopleName(emp360BO.getName(quitStationApplyDto.getOperator()));
		return quitStationApply;
	}
	
	@Override
	public Long applySettle(ApplySettleDto applySettleDto) throws AugeServiceException {
		return partnerInstanceHandler.handleApplySettle(applySettleDto, applySettleDto.getPartnerInstanceTypeEnum());
	}

}
