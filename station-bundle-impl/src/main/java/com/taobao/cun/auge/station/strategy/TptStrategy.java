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
import com.taobao.cun.appResource.dto.AppResourceDto;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.attachment.dto.AttachmentDto;
import com.taobao.cun.attachment.enums.AttachmentBizTypeEnum;
import com.taobao.cun.attachment.enums.AttachmentTypeIdEnum;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecord;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.domain.PartnerStationStateChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.partner.service.PartnerAssetService;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.PartnerApplyBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationDecorateBO;
import com.taobao.cun.auge.station.convert.OperatorConverter;
import com.taobao.cun.auge.station.convert.PartnerConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.PartnerApplyDto;
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
import com.taobao.cun.auge.station.enums.PartnerApplyStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceIsCurrentEnum;
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
import com.taobao.cun.auge.station.enums.PartnerPeixunCourseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.enums.StationDecoratePaymentTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecorateTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerInstanceExtService;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.station.service.StationDecorateService;
import com.taobao.cun.auge.station.sync.StationApplySyncBO;

@Component("tptStrategy")
public class TptStrategy extends CommonStrategy implements PartnerInstanceStrategy {

	private static final Logger logger = LoggerFactory.getLogger(TptStrategy.class);

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
    AttachmentService criusAttachmentService;

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
	AppResourceService appResourceService;
	
	@Autowired
	PartnerAssetService partnerAssetService;
	
	@Autowired
    PartnerInstanceQueryService partnerInstanceQueryService;
	
	@Autowired
	StationDecorateService stationDecorateService;
	
	@Autowired
	PartnerInstanceExtService partnerInstanceExtService;

	@Autowired
	PartnerApplyBO partnerApplyBO;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void applySettle(PartnerInstanceDto partnerInstanceDto){
		validateDecorateAndPaymentType(partnerInstanceDto);

		// 构建入驻生命周期
		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
		partnerLifecycleDto.setPartnerType(PartnerInstanceTypeEnum.TPT);
		partnerLifecycleDto.copyOperatorDto(partnerInstanceDto);
		partnerLifecycleDto.setBusinessType(PartnerLifecycleBusinessTypeEnum.SETTLING);
		partnerLifecycleDto.setSettledProtocol(PartnerLifecycleSettledProtocolEnum.SIGNING);
		partnerLifecycleDto.setBond(PartnerLifecycleBondEnum.WAIT_FROZEN);
		partnerLifecycleDto.setSystem(PartnerLifecycleSystemEnum.WAIT_PROCESS);
		partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);
		partnerLifecycleDto.setPartnerInstanceId(partnerInstanceDto.getId());
		partnerLifecycleBO.addLifecycle(partnerLifecycleDto);

		Long taobaoUserId = partnerInstanceDto.getTaobaoUserId();
		String taobaoNick = partnerInstanceDto.getPartnerDto().getTaobaoNick();
		// 生成启航班培训记录和成长营培训记录
		partnerPeixunBO.initPeixunRecord(taobaoUserId,
				PartnerPeixunCourseTypeEnum.APPLY_IN, appResourceService
						.queryAppResourceValue("PARTNER_PEIXUN_CODE",
								"APPLY_IN"));
		partnerPeixunBO.initPeixunRecord(taobaoUserId,
				PartnerPeixunCourseTypeEnum.UPGRADE, appResourceService
						.queryAppResourceValue("PARTNER_PEIXUN_CODE",
								"UPGRADE"));
		// 分发启航班试卷
		partnerPeixunBO.dispatchApplyInExamPaper(taobaoUserId, taobaoNick,
				appResourceService.queryAppResourceValue(
						"PARTNER_PEIXUN_ONLINE", "EXAM_ID"));
		// 生成装修记录
		StationDecorateDto stationDecorateDto = new StationDecorateDto();
		stationDecorateDto.copyOperatorDto(OperatorDto.defaultOperator());
		stationDecorateDto.setStationId(partnerInstanceDto.getStationId());
		stationDecorateDto.setPartnerUserId(taobaoUserId);
		stationDecorateDto.setDecorateType(partnerInstanceDto.getStationDecorateTypeEnum());
		stationDecorateDto.setPaymentType(partnerInstanceDto.getStationDecoratePaymentTypeEnum());
		stationDecorateBO.addStationDecorate(stationDecorateDto);
	}

	private void validateDecorateAndPaymentType(PartnerInstanceDto partnerInstanceDto) {
		ValidateUtils.notNull(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getId());
		ValidateUtils.notNull(partnerInstanceDto.getTaobaoUserId());
		ValidateUtils.notNull(partnerInstanceDto.getStationId());

		StationDecorateTypeEnum decorate = partnerInstanceDto.getStationDecorateTypeEnum();
		StationDecoratePaymentTypeEnum pay = partnerInstanceDto.getStationDecoratePaymentTypeEnum();
		ValidateUtils.notNull(decorate);
		ValidateUtils.notNull(pay);

		if (decoratePaymentTypeEquals(decorate, StationDecorateTypeEnum.ORIGIN, pay, StationDecoratePaymentTypeEnum.SELF)
				|| decoratePaymentTypeEquals(decorate, StationDecorateTypeEnum.ORIGIN, pay, StationDecoratePaymentTypeEnum.GOV_PART)
				|| decoratePaymentTypeEquals(decorate, StationDecorateTypeEnum.ORIGIN, pay, StationDecoratePaymentTypeEnum.GOV_ALL)
				|| decoratePaymentTypeEquals(decorate, StationDecorateTypeEnum.NEW, pay, StationDecoratePaymentTypeEnum.NONE)
				|| decoratePaymentTypeEquals(decorate, StationDecorateTypeEnum.ORIGIN_UPGRADE, pay, StationDecoratePaymentTypeEnum.NONE)) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"illegal decorate_type & payment_type combination");
		}
	}

	private boolean decoratePaymentTypeEquals(StationDecorateTypeEnum decorate, StationDecorateTypeEnum decorateExpect,
	                                          StationDecoratePaymentTypeEnum pay, StationDecoratePaymentTypeEnum payExpect) {
		return decorateExpect.getCode().equals(decorate.getCode()) && payExpect.getCode().equals(pay.getCode());
	}

	@Override
	public void validateExistChildrenForQuit(PartnerStationRel instance) {

	}
	
	@Override
	public void validateClosePreCondition(PartnerStationRel partnerStationRel) {

	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto, PartnerStationRel rel){
		String operator = partnerInstanceDeleteDto.getOperator();
		if (PartnerInstanceIsCurrentEnum.N.getCode().equals(rel.getIsCurrent())) {//历史数据不能删除
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"历史数据不能删除");
		}
		if (!StringUtils.equals(PartnerInstanceStateEnum.TEMP.getCode(), rel.getState())
				&& !StringUtils.equals(PartnerInstanceStateEnum.SETTLE_FAIL.getCode(), rel.getState())
				&& !StringUtils.equals(PartnerInstanceStateEnum.SETTLING.getCode(), rel.getState())) {
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE, "当前状态合伙人信息不能删除");
		}
		// 保证金已经结不能删除
		if (isBondHasFrozen(rel.getId())) {
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"保证金已经结不能删除");
		}
		//该村点只有当前合伙人，直接删除,如果有其他的合伙人（不管是什么状态，都置为已停业）
		Long stationId = rel.getStationId();
		Station station = stationBO.getStationById(stationId);
		if (!StringUtils.equals(StationStatusEnum.TEMP.getCode(), station.getStatus())
				&& !StringUtils.equals(StationStatusEnum.INVALID.getCode(), station.getStatus())
				&& !StringUtils.equals(StationStatusEnum.NEW.getCode(), station.getStatus())) {
			throw new AugeBusinessException(AugeErrorCodes.STATION_BUSINESS_CHECK_ERROR_CODE,"当前状态的服务站信息不能删除");
		}
		
		List<PartnerStationRel> sList = partnerInstanceBO.findPartnerInstances(stationId);
		if (sList != null && sList.size()>1) {
			stationBO.changeState(stationId, StationStatusEnum.valueof(station.getStatus()), StationStatusEnum.CLOSED, operator);
			
			updateIsCurrentForPreInstance(rel, sList);
			
		}else {
			stationBO.deleteStation(stationId, partnerInstanceDeleteDto.getOperator());
		}
		//删除装修记录
		stationDecorateBO.invalidStationDecorate(stationId);
		//删除启航班培训记录和成长营培训记录
		partnerPeixunBO.invalidPeixunRecord(rel.getTaobaoUserId(),
				PartnerPeixunCourseTypeEnum.APPLY_IN, appResourceService
						.queryAppResourceValue("PARTNER_PEIXUN_CODE",
								"APPLY_IN"));
		partnerPeixunBO.invalidPeixunRecord(rel.getTaobaoUserId(),
				PartnerPeixunCourseTypeEnum.UPGRADE, appResourceService
						.queryAppResourceValue("PARTNER_PEIXUN_CODE",
								"UPGRADE"));
		Long partnerId = rel.getPartnerId();

		partnerBO.deletePartner(partnerId, partnerInstanceDeleteDto.getOperator());
	
		partnerInstanceBO.deletePartnerStationRel(rel.getId(), operator);
		partnerLifecycleBO.deleteLifecycleItems(rel.getId(), operator);
	}
	
	/**
	 * 固点的村点，如果当前人删除，把上一个合伙人设置为当前人（上一个合伙人入驻其他村点，不设置）
	 * @param rel
	 * @param sList
	 */
	private void updateIsCurrentForPreInstance(PartnerStationRel rel,
			List<PartnerStationRel> sList) {
		Long preTaobaoUserId = null;
		Long preInstanceId = null;
		for (PartnerStationRel r : sList) {
			if (r.getId().longValue() != rel.getId().longValue()) {
				preTaobaoUserId = r.getTaobaoUserId();
				preInstanceId = r.getId();
				break;
			}
		}
		
		PartnerStationRel preCurRel = partnerInstanceBO.getCurrentPartnerInstanceByTaobaoUserId(preTaobaoUserId);
		if (preCurRel == null) {//没有入驻其他服务站
			partnerInstanceBO.updateIsCurrentByInstanceId(preInstanceId,PartnerInstanceIsCurrentEnum.Y);
		}
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
	public void quit(PartnerInstanceQuitDto partnerInstanceQuitDto){
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
	public void applySettleNewly(PartnerInstanceDto partnerInstanceDto){
		// TODO Auto-generated method stub
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void applyQuit(QuitStationApplyDto quitDto, PartnerInstanceTypeEnum typeEnum){
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
	public void handleDifferQuitAuditPass(Long partnerInstanceId){
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(partnerInstanceId, PartnerLifecycleBusinessTypeEnum.QUITING,
				PartnerLifecycleCurrentStepEnum.PROCESSING);

		PartnerLifecycleDto param = new PartnerLifecycleDto();
		param.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_PASS);
		param.setLifecycleId(items.getId());
		partnerLifecycleBO.updateLifecycle(param);

		// 同步station_apply
		stationApplySyncBO.updateStationApply(partnerInstanceId, SyncStationApplyEnum.UPDATE_STATE);

		PartnerApplyDto partnerApplyDto = new PartnerApplyDto();
			PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(partnerInstanceId);
			partnerApplyDto.setTaobaoUserId(instance.getTaobaoUserId());
			partnerApplyDto.setState(PartnerApplyStateEnum.STATE_APPLY_SUCC);
			partnerApplyDto.setOperator("system");
			partnerApplyBO.restartPartnerApplyByUserId(partnerApplyDto);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void settleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto, PartnerStationRel rel){
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
	private Boolean containCountyOrgId(Long countyOrgId) {

		if (countyOrgId != null) {
			AppResourceDto resource = appResourceService.queryAppResource("gudian_county", "countyid");
			if (resource != null && !StringUtils.isEmpty(resource.getValue())) {
				List<Long> countyIdList = JSON.parseArray(resource.getValue(), Long.class);
				return countyIdList.contains(countyOrgId);

			} else {

				return true;
			}
		}
		return true;
	}

	/**
	 * 构建装修中生命周期
	 *
	 * @param rel
	 */
	private void initPartnerLifeCycleForDecorating(PartnerStationRel rel) {
		
		Station s = stationBO.getStationById(rel.getStationId());
		if(containCountyOrgId(s.getApplyOrg())) {
			return;
		}
		
		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
		partnerLifecycleDto.setPartnerType(PartnerInstanceTypeEnum.TPT);
		partnerLifecycleDto.copyOperatorDto(OperatorDto.defaultOperator());
		partnerLifecycleDto.setBusinessType(PartnerLifecycleBusinessTypeEnum.DECORATING);
		partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);
		partnerLifecycleDto.setPartnerInstanceId(rel.getId());
		
		//培训
		PartnerCourseRecord record = partnerPeixunBO.queryOfflinePeixunRecord(
				rel.getTaobaoUserId(), PartnerPeixunCourseTypeEnum.APPLY_IN,
				appResourceService.queryAppResourceValue("PARTNER_PEIXUN_CODE",
						"APPLY_IN"));
		if(record!=null&&PartnerPeixunStatusEnum.DONE.getCode().equals(record.getStatus())){
			partnerLifecycleDto.setCourseStatus(PartnerLifecycleCourseStatusEnum.Y);
		}else{
			partnerLifecycleDto.setCourseStatus(PartnerLifecycleCourseStatusEnum.N);
		}
		
		//装修
		boolean hasDecorateDone = stationDecorateBO.handleAcessDecorating(rel.getStationId());
		if (hasDecorateDone) {
			partnerLifecycleDto.setDecorateStatus(PartnerLifecycleDecorateStatusEnum.Y);
		}else {
			partnerLifecycleDto.setDecorateStatus(PartnerLifecycleDecorateStatusEnum.N);
		}
		partnerLifecycleBO.addLifecycle(partnerLifecycleDto);
	}

	private void sendPartnerInstanceStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChangeEnum,
			OperatorDto operator) {
		PartnerInstanceDto piDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
		EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
				PartnerInstanceEventConverter.convertStateChangeEvent(stateChangeEnum, piDto, operator));
	}

	private void syncNewPartnerInfoToOldPartnerId(Long newPartnerId, Long oldPartnerId, OperatorDto operatorDto) {
		// 更新身份证
		List<AttachmentDto> attDtoList = criusAttachmentService.getAttachmentList(newPartnerId, AttachmentBizTypeEnum.PARTNER,
				AttachmentTypeIdEnum.IDCARD_IMG);
		if (CollectionUtils.isNotEmpty(attDtoList)) {
			criusAttachmentService.modifyAttachmentBatch(attDtoList, oldPartnerId, AttachmentBizTypeEnum.PARTNER, AttachmentTypeIdEnum.IDCARD_IMG,
					OperatorConverter.convert(operatorDto));
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
				EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_STATION_STATE_CHANGE_EVENT, pisc);
			}
		} catch (Exception e) {
			logger.error("dispatchEvent error param: instanceId" + rel.getId(), e);
		}
	}

	@Override
	public Boolean validateUpdateSettle(Long instanceId){
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.SETTLING,
				PartnerLifecycleCurrentStepEnum.PROCESSING);
		if (items != null) {
			return true;
		}
		return false;
	}
	
	@Override
	public void startClosing(Long instanceId, String stationName, OperatorDto operatorDto){
	}

	@Override
	public void startQuiting(Long instanceId, String stationName, OperatorDto operatorDto){
	}
	
	@Override
	public void validateAssetBack(Long instanceId){
		boolean isBackAsset = partnerAssetService.isBackAsset(instanceId);
		if(!isBackAsset){
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"3件资产尚未回收，请用小二APP回收资产。");
		}
	}

	@Override
	public void validateOtherPartnerQuit(Long instanceId) {
		boolean isOtherPartnerQuit = partnerInstanceQueryService.isOtherPartnerQuit(instanceId);
		if(!isOtherPartnerQuit){
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"村点上存在未退出的合伙人，不能撤点。");
		}
	}
	
	@Override
	public void startService(Long instanceId, Long taobaoUserId, OperatorDto operatorDto) {

	}
}
