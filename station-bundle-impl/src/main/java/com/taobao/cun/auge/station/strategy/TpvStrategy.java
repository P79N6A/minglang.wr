package com.taobao.cun.auge.station.strategy;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.AttachementBO;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.bo.LogisticsStationBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerConverter;
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
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleLogisticsApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;

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
	
	@Autowired
	AttachementBO attachementBO;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
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
			return ProcessBusinessEnum.stationForcedClosure;
		}else if(ProcessTypeEnum.QUITING_PRO.equals(processType)){
			return ProcessBusinessEnum.stationQuitRecord;
		}
		return null;
	}
	
	/**
	 * 新逻辑 占时不用
	 */
/*	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
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
	}*/
	
	/**
	 * 过渡使用 支持村拍档  老逻辑删除功能
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto,
			PartnerStationRel rel) throws AugeServiceException {
		
		if (!StringUtils.equals("AUDIT_FAIL", rel.getState())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_DELETE_FAIL);
		}
		if (partnerInstanceDeleteDto.getIsDeleteStation()) {
			Long stationId =  rel.getStationId();
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
			partnerBO.deletePartner(partnerId, partnerInstanceDeleteDto.getOperator());
		}
		
		partnerInstanceBO.deletePartnerStationRel(rel.getId(), partnerInstanceDeleteDto.getOperator());
		partnerLifecycleBO.deleteLifecycleItems(rel.getId(), partnerInstanceDeleteDto.getOperator());
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
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
			param.copyOperatorDto(partnerInstanceQuitDto);
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
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void applyQuit(QuitStationApplyDto quitDto, PartnerInstanceTypeEnum typeEnum) throws AugeServiceException {
		PartnerLifecycleDto itemsDO = new PartnerLifecycleDto();
		itemsDO.setPartnerInstanceId(quitDto.getInstanceId());
		itemsDO.setPartnerType(typeEnum);
		itemsDO.setBusinessType(PartnerLifecycleBusinessTypeEnum.QUITING);
		itemsDO.setRoleApprove(PartnerLifecycleRoleApproveEnum.TO_START);
		itemsDO.setCurrentStep(PartnerLifecycleCurrentStepEnum.ROLE_APPROVE);
		itemsDO.copyOperatorDto(quitDto);
		partnerLifecycleBO.addLifecycle(itemsDO);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void auditQuit(ProcessApproveResultEnum approveResult, Long partnerInstanceId) throws AugeServiceException {
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(partnerInstanceId,
				PartnerLifecycleBusinessTypeEnum.QUITING, PartnerLifecycleCurrentStepEnum.ROLE_APPROVE);

		if (ProcessApproveResultEnum.APPROVE_PASS.equals(approveResult) && items != null) {
			PartnerLifecycleDto param = new PartnerLifecycleDto();
			param.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_PASS);
			param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
			param.setLifecycleId(items.getId());
			partnerLifecycleBO.updateLifecycle(param);
		} else {
			PartnerLifecycleDto param = new PartnerLifecycleDto();
			param.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_NOPASS);
			param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
			param.setLifecycleId(items.getId());
			partnerLifecycleBO.updateLifecycle(param);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void settleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto,	PartnerStationRel rel) throws AugeServiceException {
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
			List<AttachementDto>  attDtoList = attachementBO.getAttachementList(partnerId, AttachementBizTypeEnum.PARTNER,AttachementTypeIdEnum.IDCARD_IMG);
			if (CollectionUtils.isNotEmpty(attDtoList)) {
				attachementBO.modifyAttachementBatch(attDtoList, oldPartnerId,
						AttachementBizTypeEnum.PARTNER,AttachementTypeIdEnum.IDCARD_IMG, settleSuccessDto);
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
			piDto.setVersion(rel.getVersion());
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
			piDto.setVersion(rel.getVersion());
			piDto.copyOperatorDto(settleSuccessDto);
			partnerInstanceBO.updatePartnerStationRel(piDto);
		}
		
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId,
				PartnerLifecycleBusinessTypeEnum.SETTLING, PartnerLifecycleCurrentStepEnum.LOGISTICS_APPROVE);
		if (items != null) {
			PartnerLifecycleDto param = new PartnerLifecycleDto();
			param.setLogisticsApprove(PartnerLifecycleLogisticsApproveEnum.AUDIT_PASS);
			param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
			param.setLifecycleId(items.getId());
			param.copyOperatorDto(settleSuccessDto);
			partnerLifecycleBO.updateLifecycle(param);
		}
		
	}

	@Override
	public Boolean validateUpdateSettle(Long instanceId)
			throws AugeServiceException {
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId,
				PartnerLifecycleBusinessTypeEnum.SETTLING, PartnerLifecycleCurrentStepEnum.ROLE_APPROVE);
		if (items != null) {
			return true;
		}
		return false;
	}

	@Override
	public void submitRemoveAlipayTagTask(Long taobaoUserId) {
		// TODO Auto-generated method stub
		
	}
}
