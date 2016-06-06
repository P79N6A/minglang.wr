package com.taobao.cun.auge.station.strategy;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.station.bo.AttachementBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.AttachementDto;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.enums.AttachementTypeIdEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSettledProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.crius.event.client.EventDispatcher;
import com.taobao.pandora.util.StringUtils;

@Component("tpaStrategy")
public class TpaStrategy implements PartnerInstanceStrategy {
	
	private static final Logger logger = LoggerFactory.getLogger(TpaStrategy.class);
	
	@Autowired
	PartnerLifecycleBO partnerLifecycleBO;
	
	@Autowired
	StationBO stationBO;
	
	@Autowired
	PartnerBO partnerBO;
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	QuitStationApplyBO quitStationApplyBO;
	
	@Autowired
	AttachementBO attachementBO;
	
	
	@Override
	public void applySettle(PartnerInstanceDto partnerInstanceDto)
			throws AugeServiceException {
		//构建入驻生命周期
		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
		partnerLifecycleDto.setPartnerType(PartnerInstanceTypeEnum.TPA);
		partnerLifecycleDto.copyOperatorDto(partnerInstanceDto);
		partnerLifecycleDto.setBusinessType(PartnerLifecycleBusinessTypeEnum.SETTLING);
		partnerLifecycleDto.setPartnerInstanceId(partnerInstanceDto.getId());
		
		if (StringUtils.equals(OperatorTypeEnum.BUC.getCode(), partnerInstanceDto.getOperatorType().getCode())) {//小二提交 走到确认协议
			partnerLifecycleDto.setSettledProtocol(PartnerLifecycleSettledProtocolEnum.SIGNING);
			partnerLifecycleDto.setBond(PartnerLifecycleBondEnum.WAIT_FROZEN);
			partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.SETTLED_PROTOCOL);
			
			
		}else if(StringUtils.equals(OperatorTypeEnum.HAVANA.getCode(), partnerInstanceDto.getOperatorType().getCode())) {//合伙人提交 走到 小二审批
			partnerLifecycleDto.setSettledProtocol(PartnerLifecycleSettledProtocolEnum.SIGNING);
			partnerLifecycleDto.setBond(PartnerLifecycleBondEnum.WAIT_FROZEN);
			partnerLifecycleDto.setRoleApprove(PartnerLifecycleRoleApproveEnum.TO_AUDIT);
			partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.ROLE_APPROVE);
		} 
		partnerLifecycleBO.addLifecycle(partnerLifecycleDto);
	}

	@Override
	public void validateExistValidChildren(Long instanceId) throws AugeServiceException{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public ProcessBusinessEnum findProcessBusiness(ProcessTypeEnum processType) throws AugeServiceException{
		if(ProcessTypeEnum.CLOSING_PRO.equals(processType)){
			return ProcessBusinessEnum.stationForcedClosure;
		}else if(ProcessTypeEnum.QUITING_PRO.equals(processType)){
			return ProcessBusinessEnum.stationQuitRecord;
		}
		return null;
	}

	@Override
	public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto,
			PartnerStationRel rel) throws AugeServiceException {
		if (!StringUtils.equals(PartnerInstanceStateEnum.TEMP.getCode(), rel.getState()) 
				&& !StringUtils.equals(PartnerInstanceStateEnum.SETTLE_FAIL.getCode(), rel.getState())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_DELETE_FAIL);
		}
		if (partnerInstanceDeleteDto.getIsDeleteStation()) {
			Long stationId =  rel.getStationId();
			Station station = stationBO.getStationById(stationId);
			if (!StringUtils.equals(StationStatusEnum.TEMP.getCode(), station.getStatus())
					 && !StringUtils.equals(StationStatusEnum.INVALID.getCode(), rel.getState())) {
				throw new AugeServiceException(StationExceptionEnum.STATION_DELETE_FAIL);
			}
			stationBO.deleteStation(stationId, partnerInstanceDeleteDto.getOperator());
		}
		
		if (partnerInstanceDeleteDto.getIsDeletePartner()) {
			Long partnerId =  rel.getPartnerId();
			Partner partner = partnerBO.getPartnerById(partnerId);
			if (!StringUtils.equals(PartnerStateEnum.TEMP.getCode(), partner.getState())) {
				throw new AugeServiceException(PartnerExceptionEnum.PARTNER_DELETE_FAIL);
			}
			partnerBO.deletePartner(partnerId, partnerInstanceDeleteDto.getOperator());
		}
		
		partnerInstanceBO.deletePartnerStationRel(rel.getId(), partnerInstanceDeleteDto.getOperator());
		partnerLifecycleBO.deleteLifecycleItems(rel.getId(), partnerInstanceDeleteDto.getOperator());
	}

	@Override
	public void quit(PartnerInstanceQuitDto partnerInstanceQuitDto)
			throws AugeServiceException {
		ValidateUtils.validateParam(partnerInstanceQuitDto);
		ValidateUtils.notNull(partnerInstanceQuitDto.getInstanceId());
	    Long instanceId = partnerInstanceQuitDto.getInstanceId();
		partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.QUITING, 
				PartnerInstanceStateEnum.QUIT, partnerInstanceQuitDto.getOperator());
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId,
				PartnerLifecycleBusinessTypeEnum.QUITING, PartnerLifecycleCurrentStepEnum.BOND);
		if (items != null) {
			PartnerLifecycleDto param = new PartnerLifecycleDto();
			param.setBond(PartnerLifecycleBondEnum.HAS_THAW);
			param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
			param.setLifecycleId(items.getId());
			partnerLifecycleBO.updateLifecycle(param);
		}
		/*if(partnerInstanceQuitDto.getIsQuitStation()) {
			Long stationId = partnerInstanceBO.findStationIdByInstanceId(instanceId);
			Station station = stationBO.getStationById(stationId);
			if (station != null) {
				if (StringUtils.equals(StationStatusEnum.QUITING.getCode(), station.getStatus())) {
					stationBO.changeState(stationId, StationStatusEnum.QUITING, StationStatusEnum.QUIT, partnerInstanceQuitDto.getOperator());
				}
			}
		}*/
		
	}

	@Override
	public void applySettleNewly(PartnerInstanceDto partnerInstanceDto)
			throws AugeServiceException {
		// TODO Auto-generated method stub
	}

	@Override
	public void applyQuit(QuitStationApplyDto quitDto, PartnerInstanceTypeEnum typeEnum) throws AugeServiceException {
		PartnerLifecycleDto itemsDO = new PartnerLifecycleDto();
		itemsDO.setPartnerInstanceId(quitDto.getInstanceId());
		itemsDO.setPartnerType(typeEnum);
		itemsDO.setBusinessType(PartnerLifecycleBusinessTypeEnum.QUITING);
		itemsDO.setRoleApprove(PartnerLifecycleRoleApproveEnum.TO_AUDIT);
		itemsDO.setBond(PartnerLifecycleBondEnum.WAIT_THAW);
		itemsDO.setCurrentStep(PartnerLifecycleCurrentStepEnum.ROLE_APPROVE);
		itemsDO.copyOperatorDto(quitDto);
		partnerLifecycleBO.addLifecycle(itemsDO);
	}

	@Override
	public void auditQuit(Boolean isAgree, Long partnerInstanceId)
			throws AugeServiceException {
		QuitStationApply quitApply = quitStationApplyBO.findQuitStationApply(partnerInstanceId);
		
		if (quitApply == null) {
			logger.error("QuitStationApply is null param:"+ partnerInstanceId);
			return;
		}
		OperatorDto operator = new OperatorDto();
		operator.setOperator(DomainUtils.DEFAULT_OPERATOR);
		operator.setOperatorType(OperatorTypeEnum.SYSTEM);
		
		if (isAgree) {
			if (quitApply.getIsQuitStation() == null || "y".equals(quitApply.getIsQuitStation())) {
				Long stationId = partnerInstanceBO.findStationIdByInstanceId(partnerInstanceId);
				// 村点已撤点
				stationBO.changeState(stationId, StationStatusEnum.QUITING, StationStatusEnum.QUIT, DomainUtils.DEFAULT_OPERATOR);
			}
			
			PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(partnerInstanceId,
					PartnerLifecycleBusinessTypeEnum.QUITING, PartnerLifecycleCurrentStepEnum.ROLE_APPROVE);
			if (items != null) {
				PartnerLifecycleDto param = new PartnerLifecycleDto();
				param.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_PASS);
				param.setCurrentStep(PartnerLifecycleCurrentStepEnum.BOND);
				param.setLifecycleId(items.getId());
				partnerLifecycleBO.updateLifecycle(param);
			}
			

			// 取消物流站点，取消支付宝标示，
			EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
					PartnerInstanceEventConverter.convert(PartnerInstanceStateChangeEnum.QUIT,
							partnerInstanceBO.getPartnerInstanceById(partnerInstanceId), operator));
		}else {
			// 合伙人实例已停业
			partnerInstanceBO.changeState(partnerInstanceId, PartnerInstanceStateEnum.QUITING, PartnerInstanceStateEnum.CLOSED,
					DomainUtils.DEFAULT_OPERATOR);
			if ("y".equals(quitApply.getIsQuitStation())) {
				// 村点已停业
				Long stationId = partnerInstanceBO.findStationIdByInstanceId(partnerInstanceId);
				stationBO.changeState(stationId, StationStatusEnum.QUITING, StationStatusEnum.CLOSED, DomainUtils.DEFAULT_OPERATOR);
			}
			
			PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(partnerInstanceId,
					PartnerLifecycleBusinessTypeEnum.QUITING, PartnerLifecycleCurrentStepEnum.ROLE_APPROVE);
			if (items != null) {
				PartnerLifecycleDto param = new PartnerLifecycleDto();
				param.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_NOPASS);
				param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
				param.setLifecycleId(items.getId());
				partnerLifecycleBO.updateLifecycle(param);
			}
		
			// 删除退出申请单
			quitStationApplyBO.deleteQuitStationApply(partnerInstanceId, DomainUtils.DEFAULT_OPERATOR);
			// 记录村点状态变化
			EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
					PartnerInstanceEventConverter.convert(PartnerInstanceStateChangeEnum.QUITTING_REFUSED,
							partnerInstanceBO.getPartnerInstanceById(partnerInstanceId), operator));
		}
	}

	@Override
	public void settleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto,PartnerStationRel rel)
			throws AugeServiceException {
		Long instanceId = settleSuccessDto.getInstanceId();
		
		Calendar now = Calendar.getInstance();// 得到一个Calendar的实例
		Date serviceBeginTime = now.getTime();
		now.add(Calendar.YEAR, 1);
		Date serviceEndTime =  now.getTime();
		
		Long partnerId = rel.getPartnerId();
		Long stationId = rel.getStationId();
		Long taobaoUserId = rel.getTaobaoUserId();
		
		StationDto stationDto = new StationDto();
		stationDto.setState(StationStateEnum.NORMAL);
		stationDto.setStatus(StationStatusEnum.SERVICING);
		stationDto.setId(stationId);
		stationDto.copyOperatorDto(settleSuccessDto);
		stationBO.updateStation(stationDto);
		
		//保证partner表有效记录唯一性
		Long oldPartnerId = partnerBO.getNormalPartnerIdByTaobaoUserId(taobaoUserId);
		if (oldPartnerId != null) {
			//更新身份证
			List<AttachementDto>  attDtoList = attachementBO.selectAttachementList(partnerId, AttachementBizTypeEnum.PARTNER,AttachementTypeIdEnum.IDCARD_IMG);
			if (CollectionUtils.isNotEmpty(attDtoList)) {
				attachementBO.modifyAttachementBatch(attDtoList, oldPartnerId,
						AttachementBizTypeEnum.PARTNER,AttachementTypeIdEnum.IDCARD_IMG, settleSuccessDto.getOperator());
			}
			
			//更新合伙人表信息
			Partner newPartner = partnerBO.getPartnerById(partnerId);
			newPartner.setId(oldPartnerId);
			PartnerDto newPartnerDto = PartnerConverter.toPartnerDto(newPartner);
			newPartnerDto.copyOperatorDto(settleSuccessDto);
			partnerBO.updatePartner(PartnerConverter.toPartnerDto(newPartner));
			
			PartnerInstanceDto piDto = new PartnerInstanceDto();
			piDto.setServiceBeginTime(serviceBeginTime);
			piDto.setServiceEndTime(serviceEndTime);
			piDto.setId(instanceId);
			piDto.setState(PartnerInstanceStateEnum.SERVICING);
			piDto.setPartnerId(oldPartnerId);
			piDto.copyOperatorDto(settleSuccessDto);
			partnerInstanceBO.updatePartnerStationRel(piDto);
			
			partnerBO.deletePartner(partnerId, settleSuccessDto.getOperator());
			
		}else {
			PartnerInstanceDto piDto = new PartnerInstanceDto();
			piDto.setServiceBeginTime(serviceBeginTime);
			piDto.setServiceEndTime(serviceEndTime);
			piDto.setId(instanceId);
			piDto.setState(PartnerInstanceStateEnum.SERVICING);
			piDto.copyOperatorDto(settleSuccessDto);
			partnerInstanceBO.updatePartnerStationRel(piDto);
		}
		
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId,
				PartnerLifecycleBusinessTypeEnum.SETTLING, PartnerLifecycleCurrentStepEnum.BOND);
		if (items != null) {
			PartnerLifecycleDto param = new PartnerLifecycleDto();
			param.setBond(PartnerLifecycleBondEnum.HAS_FROZEN);
			param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
			param.setLifecycleId(items.getId());
			partnerLifecycleBO.updateLifecycle(param);
		}
	}
		
}
