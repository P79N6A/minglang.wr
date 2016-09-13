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

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecord;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StationDecorate;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.domain.PartnerStationStateChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.bo.AttachementBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationDecorateBO;
import com.taobao.cun.auge.station.convert.PartnerConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.AttachementDto;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.dto.StationDecorateDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.enums.AttachementTypeIdEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCourseStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleDecorateStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSettledProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSystemEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.StationDecorateStatusEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.sync.StationApplySyncBO;

@Component("tpStrategy")
public class TpStrategy implements PartnerInstanceStrategy {

	private static final Logger logger = LoggerFactory.getLogger(TpStrategy.class);

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	PartnerLifecycleBO partnerLifecycleBO;

	@Autowired
	StationBO stationBO;

	@Autowired
	PartnerBO partnerBO;

	@Autowired
	QuitStationApplyBO quitStationApplyBO;

	@Autowired
	AttachementBO attachementBO;

	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;

	@Autowired
	StationApplySyncBO stationApplySyncBO;

	@Autowired
	AccountMoneyBO accountMoneyBO;
	
	@Autowired
	PartnerPeixunBO partnerPeixunBO;
	
	@Autowired
	StationDecorateBO stationDecorateBO;
	
	@Autowired
	AppResourceBO appResourceBO;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void applySettle(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {

		// 构建入驻生命周期
		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
		partnerLifecycleDto.setPartnerType(PartnerInstanceTypeEnum.TP);
		partnerLifecycleDto.copyOperatorDto(partnerInstanceDto);
		partnerLifecycleDto.setBusinessType(PartnerLifecycleBusinessTypeEnum.SETTLING);
		partnerLifecycleDto.setSettledProtocol(PartnerLifecycleSettledProtocolEnum.SIGNING);
		partnerLifecycleDto.setBond(PartnerLifecycleBondEnum.WAIT_FROZEN);
		partnerLifecycleDto.setSystem(PartnerLifecycleSystemEnum.WAIT_PROCESS);
		partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);
		partnerLifecycleDto.setPartnerInstanceId(partnerInstanceDto.getId());
		partnerLifecycleBO.addLifecycle(partnerLifecycleDto);
	}

	@Override
	public void validateExistChildrenForQuit(Long instanceId) throws AugeServiceException {
		List<PartnerInstanceStateEnum> states = PartnerInstanceStateEnum.getPartnerStatusForValidateQuit();
		List<PartnerStationRel> children = partnerInstanceBO.findChildPartners(instanceId,states);

		if (CollectionUtils.isEmpty(children)) {
			return;
		}
		for (PartnerStationRel rel : children) {
			if (!StringUtils.equals(PartnerInstanceStateEnum.QUITING.getCode(), rel.getState())) {
				logger.warn("合伙人存在淘帮手");
				throw new AugeServiceException(StationExceptionEnum.HAS_CHILDREN_TPA);
			} else {
				PartnerLifecycleItems item = partnerLifecycleBO.getLifecycleItems(rel.getId(),PartnerLifecycleBusinessTypeEnum.QUITING);
				if (null != item && StringUtils.equals(PartnerLifecycleCurrentStepEnum.PROCESSING.getCode(),
						item.getCurrentStep())) {
					if (PartnerLifecycleBondEnum.WAIT_THAW.getCode().equals(item.getBond()) && PartnerLifecycleRoleApproveEnum.AUDIT_PASS.getCode().equals(item.getRoleApprove())) {
						continue;
					}
					logger.warn("合伙人存在淘帮手");
					throw new AugeServiceException(StationExceptionEnum.HAS_CHILDREN_TPA);
				}
			}
		}
	}
	
	@Override
	public void validateExistChildrenForClose(Long instanceId) throws AugeServiceException {
		List<PartnerInstanceStateEnum> states = PartnerInstanceStateEnum.getPartnerStatusForValidateClose();
		List<PartnerStationRel> children = partnerInstanceBO.findChildPartners(instanceId, states);

		if (CollectionUtils.isNotEmpty(children)) {
			logger.warn("合伙人存在淘帮手");
			throw new AugeServiceException(StationExceptionEnum.HAS_CHILDREN_TPA);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto, PartnerStationRel rel) throws AugeServiceException {

		if (!StringUtils.equals(PartnerInstanceStateEnum.TEMP.getCode(), rel.getState())
				&& !StringUtils.equals(PartnerInstanceStateEnum.SETTLE_FAIL.getCode(), rel.getState())
				&& !StringUtils.equals(PartnerInstanceStateEnum.SETTLING.getCode(), rel.getState())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_DELETE_FAIL);
		}

		// 保证金已经结不能删除
		if (isBondHasFrozen(rel.getId())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_DELETE_FAIL);
		}

		if (partnerInstanceDeleteDto.getIsDeleteStation()) {
			Long stationId = rel.getStationId();
			Station station = stationBO.getStationById(stationId);
			if (!StringUtils.equals(StationStatusEnum.TEMP.getCode(), station.getStatus())
					&& !StringUtils.equals(StationStatusEnum.INVALID.getCode(), station.getStatus())
					&& !StringUtils.equals(StationStatusEnum.NEW.getCode(), station.getStatus())) {
				throw new AugeServiceException(StationExceptionEnum.STATION_DELETE_FAIL);
			}
			stationBO.deleteStation(stationId, partnerInstanceDeleteDto.getOperator());
		}

		if (partnerInstanceDeleteDto.getIsDeletePartner()) {
			Long partnerId = rel.getPartnerId();
			Partner partner = partnerBO.getPartnerById(partnerId);
			if (!StringUtils.equals(PartnerStateEnum.TEMP.getCode(), partner.getState())) {
				throw new AugeServiceException(PartnerExceptionEnum.PARTNER_DELETE_FAIL);
			}
			partnerBO.deletePartner(partnerId, partnerInstanceDeleteDto.getOperator());
		}

		partnerInstanceBO.deletePartnerStationRel(rel.getId(), partnerInstanceDeleteDto.getOperator());
		partnerLifecycleBO.deleteLifecycleItems(rel.getId(), partnerInstanceDeleteDto.getOperator());
	}

	private boolean isBondHasFrozen(Long id) {
		AccountMoneyDto accountMoneyDto = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
				AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, id);
		if (null == accountMoneyDto || !AccountMoneyStateEnum.HAS_FROZEN.equals(accountMoneyDto.getState())) {
			return false;
		}
		return true;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void quit(PartnerInstanceQuitDto partnerInstanceQuitDto) throws AugeServiceException {
		ValidateUtils.validateParam(partnerInstanceQuitDto);
		ValidateUtils.notNull(partnerInstanceQuitDto.getInstanceId());
		Long instanceId = partnerInstanceQuitDto.getInstanceId();
		partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.QUITING, PartnerInstanceStateEnum.QUIT,
				partnerInstanceQuitDto.getOperator());
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.QUITING,
				PartnerLifecycleCurrentStepEnum.PROCESSING);
		if (items != null) {
			PartnerLifecycleDto param = new PartnerLifecycleDto();
			param.setBond(PartnerLifecycleBondEnum.HAS_THAW);
			param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
			param.setLifecycleId(items.getId());
			param.copyOperatorDto(partnerInstanceQuitDto);
			partnerLifecycleBO.updateLifecycle(param);
		}
		//解冻保证金
		AccountMoneyDto accountMoneyUpdateDto = new AccountMoneyDto();
		accountMoneyUpdateDto.setObjectId(instanceId);
		accountMoneyUpdateDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
		accountMoneyUpdateDto.setType(AccountMoneyTypeEnum.PARTNER_BOND);
		accountMoneyUpdateDto.setThawTime(new Date());
		accountMoneyUpdateDto.setState(AccountMoneyStateEnum.HAS_THAW);
		accountMoneyUpdateDto.copyOperatorDto(partnerInstanceQuitDto);
		accountMoneyBO.updateAccountMoneyByObjectId(accountMoneyUpdateDto);
	}

	@Override
	public void applySettleNewly(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
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
		itemsDO.setBond(PartnerLifecycleBondEnum.WAIT_THAW);
		itemsDO.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);
		itemsDO.copyOperatorDto(quitDto);
		partnerLifecycleBO.addLifecycle(itemsDO);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void handleDifferQuitAuditPass(Long partnerInstanceId) throws AugeServiceException {
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(partnerInstanceId, PartnerLifecycleBusinessTypeEnum.QUITING,
				PartnerLifecycleCurrentStepEnum.PROCESSING);

		PartnerLifecycleDto param = new PartnerLifecycleDto();
		param.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_PASS);
		param.setLifecycleId(items.getId());
		partnerLifecycleBO.updateLifecycle(param);

		// 同步station_apply
		stationApplySyncBO.updateStationApply(partnerInstanceId, SyncStationApplyEnum.UPDATE_STATE);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void settleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto, PartnerStationRel rel) throws AugeServiceException {
		Long instanceId = settleSuccessDto.getInstanceId();

		Long partnerId = rel.getPartnerId();
		Long stationId = rel.getStationId();
		Long taobaoUserId = rel.getTaobaoUserId();

		StationDto stationDto = new StationDto();
		stationDto.setState(StationStateEnum.NORMAL);
		stationDto.setStatus(StationStatusEnum.DECORATING);
		stationDto.setId(stationId);
		stationDto.copyOperatorDto(settleSuccessDto);
		stationBO.updateStation(stationDto);

		// 保证partner表有效记录唯一性
		Long oldPartnerId = partnerBO.getNormalPartnerIdByTaobaoUserId(taobaoUserId);

		if (oldPartnerId != null && (!oldPartnerId.equals(partnerId))) {
			syncNewPartnerInfoToOldPartnerId(partnerId, oldPartnerId, settleSuccessDto);
			setPartnerInstanceToDecorating(rel, settleSuccessDto, oldPartnerId);
		} else {
			setPartnerInstanceToDecorating(rel, settleSuccessDto, null);

			// 更新合伙人表为normal
			Partner newPartner = partnerBO.getPartnerById(partnerId);
			PartnerDto newPartnerDto = PartnerConverter.toPartnerDto(newPartner);
			newPartnerDto.setState(PartnerStateEnum.NORMAL);
			newPartnerDto.copyOperatorDto(settleSuccessDto);
			partnerBO.updatePartner(newPartnerDto);
		}

		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.SETTLING,
				PartnerLifecycleCurrentStepEnum.PROCESSING);
		if (items != null) {
			PartnerLifecycleDto param = new PartnerLifecycleDto();
			param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
			param.setSystem(PartnerLifecycleSystemEnum.HAS_PROCESS);
			param.setLifecycleId(items.getId());
			param.copyOperatorDto(settleSuccessDto);
			partnerLifecycleBO.updateLifecycle(param);
		}
		
		//初始化装修中生命周期
		initPartnerLifeCycleForDecorating(rel);

		// 发送装修中事件
		sendPartnerInstanceStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.START_DECORATING, settleSuccessDto);
		// 发送装修中事件，手机端使用
		dispacthEvent(rel, PartnerInstanceStateEnum.DECORATING.getCode());
	}
	
	private  Boolean containCountyOrgId(Long countyOrgId) {
		if (countyOrgId != null) {
			AppResource resource = appResourceBO.queryAppResource("gudian_county", "countyid");
			if (resource != null && !StringUtils.isEmpty(resource.getValue())) {
				List<Long> countyIdList = JSON.parseArray(resource.getValue(), Long.class);
				return countyIdList.contains(countyOrgId);
			}else {
				return true;
			}
		}
		return true;
	}
	
	/**
	 * 构建装修中生命周期
	 * @param rel
	 */
	private void initPartnerLifeCycleForDecorating(PartnerStationRel rel) {
		
		Station s = stationBO.getStationById(rel.getStationId());
		if(containCountyOrgId(s.getApplyOrg())) {
			return;
		}
		
		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
		partnerLifecycleDto.setPartnerType(PartnerInstanceTypeEnum.TP);
		partnerLifecycleDto.copyOperatorDto(OperatorDto.defaultOperator());
		partnerLifecycleDto.setBusinessType(PartnerLifecycleBusinessTypeEnum.DECORATING);
		partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);
		partnerLifecycleDto.setPartnerInstanceId(rel.getId());
		
		//培训
		PartnerCourseRecord pcr = partnerPeixunBO.initPartnerApplyInRecord(rel.getTaobaoUserId());
		if (PartnerPeixunStatusEnum.DONE.getCode().equals(pcr.getStatus())) {
			partnerLifecycleDto.setCourseStatus(PartnerLifecycleCourseStatusEnum.Y);
		}else {
			partnerLifecycleDto.setCourseStatus(PartnerLifecycleCourseStatusEnum.N);
		}
		
		//装修
		StationDecorateDto stationDecorateDto = new StationDecorateDto();
		stationDecorateDto.copyOperatorDto(OperatorDto.defaultOperator());
		stationDecorateDto.setStationId(rel.getStationId());
		stationDecorateDto.setPartnerUserId(rel.getTaobaoUserId());
		
		StationDecorate sd = stationDecorateBO.addStationDecorate(stationDecorateDto);
		if (StationDecorateStatusEnum.DONE.getCode().equals(sd.getStatus())) {
			partnerLifecycleDto.setDecorateStatus(PartnerLifecycleDecorateStatusEnum.Y);
		}else {
			partnerLifecycleDto.setDecorateStatus(PartnerLifecycleDecorateStatusEnum.N);
		}
		partnerLifecycleBO.addLifecycle(partnerLifecycleDto);
	}

	private void sendPartnerInstanceStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChangeEnum,
			OperatorDto operator) {
		PartnerInstanceDto piDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
		EventDispatcherUtil.dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
				PartnerInstanceEventConverter.convertStateChangeEvent(stateChangeEnum, piDto, operator));
	}

	private void syncNewPartnerInfoToOldPartnerId(Long newPartnerId, Long oldPartnerId, OperatorDto operatorDto) {
		// 更新身份证
		List<AttachementDto> attDtoList = attachementBO.getAttachementList(newPartnerId, AttachementBizTypeEnum.PARTNER,
				AttachementTypeIdEnum.IDCARD_IMG);
		if (CollectionUtils.isNotEmpty(attDtoList)) {
			attachementBO.modifyAttachementBatch(attDtoList, oldPartnerId, AttachementBizTypeEnum.PARTNER, AttachementTypeIdEnum.IDCARD_IMG,
					operatorDto);
		}
		// 更新合伙人表信息
		Partner newPartner = partnerBO.getPartnerById(newPartnerId);
		newPartner.setId(oldPartnerId);
		PartnerDto newPartnerDto = PartnerConverter.toPartnerDto(newPartner);
		newPartnerDto.setState(PartnerStateEnum.NORMAL);
		newPartnerDto.copyOperatorDto(operatorDto);
		partnerBO.updatePartner(newPartnerDto);
		partnerBO.deletePartner(newPartnerId, operatorDto.getOperator());
	}

	/**
	 * 设置关系表为装修中
	 */
	private void setPartnerInstanceToDecorating(PartnerStationRel rel, OperatorDto operatorDto, Long changePartnerId) {

		Calendar now = Calendar.getInstance();// 得到一个Calendar的实例
		Date serviceBeginTime = now.getTime();
		now.add(Calendar.YEAR, 1);
		Date serviceEndTime = now.getTime();

		PartnerInstanceDto piDto = new PartnerInstanceDto();
		piDto.setServiceBeginTime(serviceBeginTime);
		piDto.setServiceEndTime(serviceEndTime);
		piDto.setId(rel.getId());
		piDto.setState(PartnerInstanceStateEnum.DECORATING);
		piDto.setVersion(rel.getVersion());
		piDto.setParentStationId(rel.getStationId());
		piDto.copyOperatorDto(operatorDto);
		if (changePartnerId != null) {
			piDto.setPartnerId(changePartnerId);
		}
		partnerInstanceBO.updatePartnerStationRel(piDto);
	}

	/**
	 * 发送装修中事件 给手机端使用
	 * 
	 * @param PartnerStationRel
	 * @param state
	 */
	private void dispacthEvent(PartnerStationRel rel, String state) {
		try {
			if (rel != null) {
				Station stationDto = stationBO.getStationById(rel.getStationId());
				PartnerStationStateChangeEvent pisc = new PartnerStationStateChangeEvent();
				pisc.setStationId(rel.getStationId());
				pisc.setPartnerInstanceState(state);
				pisc.setStationName(stationDto.getName());
				pisc.setTaobaoUserId(rel.getTaobaoUserId());
				EventDispatcherUtil.dispatch(EventConstant.PARTNER_STATION_STATE_CHANGE_EVENT, pisc);
			}
		} catch (Exception e) {
			logger.error("dispatchEvent error param: instanceId" + rel.getId(), e);
		}
	}

	@Override
	public Boolean validateUpdateSettle(Long instanceId) throws AugeServiceException {
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.SETTLING,
				PartnerLifecycleCurrentStepEnum.PROCESSING);
		if (items != null) {
			return true;
		}
		return false;
	}
	
	@Override
	public void startClosing(Long instanceId, OperatorDto operatorDto, String remark) throws AugeServiceException {
		PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);
		ProcessBusinessEnum business = ProcessBusinessEnum.stationForcedClosure;
		// FIXME FHH 流程暂时为迁移，还是使用stationapplyId关联流程实例
		generalTaskSubmitService.submitApproveProcessTask(business, instance.getStationApplyId(), operatorDto, remark);
	}

	@Override
	public void startQuiting(Long instanceId, OperatorDto operatorDto, String remark) throws AugeServiceException {
		PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);
		ProcessBusinessEnum business = ProcessBusinessEnum.stationQuitRecord;
		// FIXME FHH 流程暂时为迁移，还是使用stationapplyId关联流程实例
		generalTaskSubmitService.submitApproveProcessTask(business, instance.getStationApplyId(), operatorDto, remark);
	}
}
