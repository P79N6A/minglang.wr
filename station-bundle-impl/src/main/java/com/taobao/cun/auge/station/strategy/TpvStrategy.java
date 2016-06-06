package com.taobao.cun.auge.station.strategy;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.bo.LogisticsStationBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleLogisticsApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.crius.event.client.EventDispatcher;

@Component("tpvStrategy")
public class TpvStrategy implements PartnerInstanceStrategy {
	
	private static final Logger logger = LoggerFactory.getLogger(TpvStrategy.class);
	
	@Autowired
	PartnerLifecycleBO partnerLifecycleBO;
	
	@Autowired
	StationBO stationBO;
	
	@Autowired
	PartnerBO partnerBO;
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	CuntaoCainiaoStationRelBO cuntaoCainiaoStationRelBO;
	
	@Autowired
	LogisticsStationBO logisticsStationBO;
	
	@Autowired
	QuitStationApplyBO quitStationApplyBO;
	
	@Override
	public void applySettle(PartnerInstanceDto partnerInstanceDto)
			throws AugeServiceException {
		///构建入驻生命周期
		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
		partnerLifecycleDto.setPartnerType(PartnerInstanceTypeEnum.TPV);
		partnerLifecycleDto.copyOperatorDto(partnerInstanceDto);
		partnerLifecycleDto.setBusinessType(PartnerLifecycleBusinessTypeEnum.SETTLING);
		partnerLifecycleDto.setLogisticsApprove(PartnerLifecycleLogisticsApproveEnum.TO_AUDIT);
		partnerLifecycleDto.setRoleApprove(PartnerLifecycleRoleApproveEnum.TO_AUDIT);
		partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.ROLE_APPROVE);
		
		partnerLifecycleDto.setPartnerInstanceId(partnerInstanceDto.getId());
		partnerLifecycleBO.addLifecycle(partnerLifecycleDto);
	}

	@Override
	public void validateExistValidChildren(Long instanceId) {
		
	}
	
	@Override
	public ProcessBusinessEnum findProcessBusiness(ProcessTypeEnum processType){
		if(ProcessTypeEnum.CLOSING_PRO.equals(processType)){
			return ProcessBusinessEnum.TPV_FORCED_CLOSURE;
		}else if(ProcessTypeEnum.QUITING_PRO.equals(processType)){
			return ProcessBusinessEnum.TPV_QUIT;
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
			
			//删除物流表
			CuntaoCainiaoStationRel csRel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId, CuntaoCainiaoStationRelTypeEnum.STATION);
			if (csRel != null) {
				Long logisId = csRel.getLogisticsStationId();
				if (logisId != null) {
					logisticsStationBO.delete(logisId, partnerInstanceDeleteDto.getOperator());
				}
				cuntaoCainiaoStationRelBO.deleteCuntaoCainiaoStationRel(stationId, CuntaoCainiaoStationRelTypeEnum.STATION);
			}
			
			
			
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
		//TODO:沒有保证金 审批通
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId,
				PartnerLifecycleBusinessTypeEnum.QUITING, PartnerLifecycleCurrentStepEnum.ROLE_APPROVE);
		if (items != null) {
			PartnerLifecycleDto param = new PartnerLifecycleDto();
			param.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_PASS);
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
			
			partnerInstanceBO.changeState(partnerInstanceId, PartnerInstanceStateEnum.QUITING, 
					PartnerInstanceStateEnum.QUIT, DomainUtils.DEFAULT_OPERATOR);
			
			PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(partnerInstanceId,
					PartnerLifecycleBusinessTypeEnum.QUITING, PartnerLifecycleCurrentStepEnum.ROLE_APPROVE);
			if (items != null) {
				PartnerLifecycleDto param = new PartnerLifecycleDto();
				param.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_PASS);
				param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
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
}
