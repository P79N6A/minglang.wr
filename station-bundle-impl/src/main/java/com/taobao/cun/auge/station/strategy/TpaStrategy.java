package com.taobao.cun.auge.station.strategy;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.taobao.cun.auge.station.bo.*;
import com.taobao.cun.auge.station.dto.*;
import com.taobao.cun.auge.station.enums.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.partner.service.PartnerAssetService;
import com.taobao.cun.auge.station.convert.PartnerConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.auge.station.notify.listener.ProcessProcessor;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.sync.StationApplySyncBO;
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

	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;

	@Autowired
	StationApplySyncBO stationApplySyncBO;

	@Autowired
	AccountMoneyBO accountMoneyBO;
	
	@Autowired
	ProcessProcessor processProcessor;
	
	@Autowired
	PartnerAssetService partnerAssetService;

	@Autowired
	PartnerApplyBO partnerApplyBO;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void applySettle(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		// 构建入驻生命周期
		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
		partnerLifecycleDto.setPartnerType(PartnerInstanceTypeEnum.TPA);
		partnerLifecycleDto.copyOperatorDto(partnerInstanceDto);
		partnerLifecycleDto.setBusinessType(PartnerLifecycleBusinessTypeEnum.SETTLING);
		partnerLifecycleDto.setPartnerInstanceId(partnerInstanceDto.getId());

		if (StringUtils.equals(OperatorTypeEnum.BUC.getCode(), partnerInstanceDto.getOperatorType().getCode())) {// 小二提交
																													// 走到确认协议
			partnerLifecycleDto.setSettledProtocol(PartnerLifecycleSettledProtocolEnum.SIGNING);
			partnerLifecycleDto.setBond(PartnerLifecycleBondEnum.WAIT_FROZEN);
			partnerLifecycleDto.setSystem(PartnerLifecycleSystemEnum.WAIT_PROCESS);
			partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);

		} else if (StringUtils.equals(OperatorTypeEnum.HAVANA.getCode(), partnerInstanceDto.getOperatorType().getCode())) {// 合伙人提交
																															// 走到
																															// 小二审批
			partnerLifecycleDto.setSettledProtocol(PartnerLifecycleSettledProtocolEnum.SIGNING);
			partnerLifecycleDto.setBond(PartnerLifecycleBondEnum.WAIT_FROZEN);
			partnerLifecycleDto.setRoleApprove(PartnerLifecycleRoleApproveEnum.TO_AUDIT);
			partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);
		}
		partnerLifecycleBO.addLifecycle(partnerLifecycleDto);
	}

	@Override
	public void validateExistChildrenForQuit(Long instanceId) throws AugeServiceException {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void validateClosePreCondition(PartnerStationRel partnerStationRel) throws AugeServiceException {
		// TODO Auto-generated method stub

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
					&& !StringUtils.equals(StationStatusEnum.INVALID.getCode(), station.getState())
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
		EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
				PartnerInstanceEventConverter.convertStateChangeEvent(PartnerInstanceStateChangeEnum.QUIT,
						partnerInstanceBO.getPartnerInstanceById(instanceId), partnerInstanceQuitDto));
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

		PartnerApplyDto partnerApplyDto = new PartnerApplyDto();
		try{
			PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(partnerInstanceId);
			partnerApplyDto.setTaobaoUserId(instance.getTaobaoUserId());
			partnerApplyDto.setState(PartnerApplyStateEnum.STATE_APPLY_SUCC);
			partnerApplyDto.setOperator("system");
			partnerApplyBO.restartPartnerApplyByUserId(partnerApplyDto);
		} catch(Exception e){
			logger.error("handler quit error " + partnerInstanceId,e);
		}
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
		stationDto.setStatus(StationStatusEnum.SERVICING);
		stationDto.setId(stationId);
		stationDto.copyOperatorDto(settleSuccessDto);
		stationBO.updateStation(stationDto);

		// 保证partner表有效记录唯一性
		Long oldPartnerId = partnerBO.getNormalPartnerIdByTaobaoUserId(taobaoUserId);
		if (oldPartnerId != null && (!oldPartnerId.equals(partnerId))) {
			syncNewPartnerInfoToOldPartnerId(partnerId, oldPartnerId, settleSuccessDto);
			setPartnerInstanceToServicing(rel, settleSuccessDto, oldPartnerId);
		} else {
			setPartnerInstanceToServicing(rel, settleSuccessDto, null);
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

		// 发送服务中事件
		sendPartnerInstanceStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.START_SERVICING, settleSuccessDto);
	}

	private void sendPartnerInstanceStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChangeEnum,
			OperatorDto operator) {
		PartnerInstanceDto piDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
		EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
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
	 * 设置关系表为服务中
	 */
	private void setPartnerInstanceToServicing(PartnerStationRel rel, OperatorDto operatorDto, Long changePartnerId) {

		Calendar now = Calendar.getInstance();// 得到一个Calendar的实例
		Date serviceBeginTime = now.getTime();
		now.add(Calendar.YEAR, 1);
		Date serviceEndTime = now.getTime();

		PartnerInstanceDto piDto = new PartnerInstanceDto();
		piDto.setServiceBeginTime(serviceBeginTime);
		piDto.setServiceEndTime(serviceEndTime);
		piDto.setId(rel.getId());
		piDto.setState(PartnerInstanceStateEnum.SERVICING);
		piDto.setVersion(rel.getVersion());
		piDto.copyOperatorDto(operatorDto);
		if (changePartnerId != null) {
			piDto.setPartnerId(changePartnerId);
		}
		partnerInstanceBO.updatePartnerStationRel(piDto);
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
	public void startClosing(Long instanceId, String stationName, OperatorDto operatorDto) throws AugeServiceException {
		try {
			processProcessor.closeApprove(instanceId, ProcessApproveResultEnum.APPROVE_PASS);
		} catch (Exception e) {
			throw new AugeServiceException(e.getMessage());
		}
	}

	@Override
	public void startQuiting(Long instanceId, String stationName, OperatorDto operatorDto) throws AugeServiceException {
		try {
			processProcessor.quitApprove(instanceId, ProcessApproveResultEnum.APPROVE_PASS);
		} catch (Exception e) {
			throw new AugeServiceException(e.getMessage());
		}
	}
	
	@Override
	public void validateAssetBack(Long instanceId){
		boolean isBackAsset = partnerAssetService.isBackAsset(instanceId);
		if(!isBackAsset){
			throw new AugeServiceException("资产未归还。");
		}
	}

	@Override
	public void validateOtherPartnerQuit(Long instanceId) {
		// TODO Auto-generated method stub
		
	}
}
