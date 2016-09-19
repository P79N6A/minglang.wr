package com.taobao.cun.auge.station.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.ChangeTPEvent;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.PartnerInstanceLevelChangeEvent;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.PartnerInstanceTypeChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.adapter.PaymentAccountQueryAdapter;
import com.taobao.cun.auge.station.adapter.TradeAdapter;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.bo.AttachementBO;
import com.taobao.cun.auge.station.bo.CloseStationApplyBO;
import com.taobao.cun.auge.station.bo.CuntaoFlowRecordBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceExtBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceLevelBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.CloseStationApplyConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceLevelEventConverter;
import com.taobao.cun.auge.station.convert.QuitStationApplyConverter;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.AuditSettleDto;
import com.taobao.cun.auge.station.dto.ChangeTPDto;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.dto.ConfirmCloseDto;
import com.taobao.cun.auge.station.dto.DegradePartnerInstanceSuccessDto;
import com.taobao.cun.auge.station.dto.ForcedCloseDto;
import com.taobao.cun.auge.station.dto.OpenStationDto;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDegradeDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceExtDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceUpdateServicingDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.dto.PartnerUpdateServicingDto;
import com.taobao.cun.auge.station.dto.PaymentAccountDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.dto.StationUpdateServicingDto;
import com.taobao.cun.auge.station.dto.SyncModifyBelongTPForTpaDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceCloseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceIsCurrentEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEvaluateTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleConfirmEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCourseStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleDecorateStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleItemCheckEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleItemCheckResultEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleQuitProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSettledProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.enums.StationAreaTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.PartnerInstanceExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.auge.station.rule.PartnerLifecycleRuleParser;
import com.taobao.cun.auge.station.service.CaiNiaoService;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerInstanceExtService;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.auge.station.sync.StationApplySyncBO;
import com.taobao.cun.auge.station.validate.PartnerInstanceValidator;
import com.taobao.cun.auge.station.validate.PartnerValidator;
import com.taobao.cun.auge.station.validate.StationValidator;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * 合伙人实例服务接口
 *
 * @author quanzhu.wangqz
 */
@Service("partnerInstanceService")
@HSFProvider(serviceInterface = PartnerInstanceService.class)
public class PartnerInstanceServiceImpl implements PartnerInstanceService {

	private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceService.class);

	private static final int DEFAULT_EVALUATE_INTERVAL = 6;

	@Autowired
	PartnerProtocolRelBO partnerProtocolRelBO;
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
	Emp360Adapter emp360Adapter;
	@Autowired
	UicReadAdapter uicReadAdapter;
	@Autowired
	PartnerBO partnerBO;
	@Autowired
	TradeAdapter tradeAdapter;
	@Autowired
	AttachementBO attachementBO;
	@Autowired
	PaymentAccountQueryAdapter paymentAccountQueryAdapter;
	@Autowired
	AccountMoneyBO accountMoneyBO;
	@Autowired
	CloseStationApplyBO closeStationApplyBO;
	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;
	@Autowired
	AppResourceBO appResourceBO;
	@Autowired
	StationApplySyncBO syncStationApplyBO;

	@Autowired
	PartnerInstanceExtService partnerInstanceExtService;

	@Autowired
	PartnerInstanceExtBO partnerInstanceExtBO;
	@Autowired
	PartnerInstanceLevelBO partnerInstanceLevelBO;

	@Autowired
	CuntaoFlowRecordBO cuntaoFlowRecordBO;

	@Autowired
	CaiNiaoService caiNiaoService;

	@Autowired
	PartnerInstanceValidator partnerInstanceValidator;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Long addTemp(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		throw new AugeServiceException("deprecated method, call applySettle() instead");
		/*
		ValidateUtils.validateParam(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getStationDto());
		ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
		try {
			setTempCommonInfo(partnerInstanceDto);
			Long instanceId = addCommon(partnerInstanceDto);
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.ADD, instanceId);
			return instanceId;
		} catch (AugeServiceException augeException) {
			String error = getAugeExceptionErrorMessage("addTemp", JSONObject.toJSONString(partnerInstanceDto), augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("addTemp", JSONObject.toJSONString(partnerInstanceDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
		*/
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Long updateTemp(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		throw new AugeServiceException("deprecated method, call updateSettle() instead");
		/*
		ValidateUtils.validateParam(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getStationDto());
		ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
		ValidateUtils.notNull(partnerInstanceDto.getId());
		try {
			setTempCommonInfo(partnerInstanceDto);
			Long instanceId = partnerInstanceDto.getId();
			updateCommon(partnerInstanceDto);
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instanceId);
			return instanceId;
		} catch (AugeServiceException augeException) {
			String error = getAugeExceptionErrorMessage("updateTemp", JSONObject.toJSONString(partnerInstanceDto),
					augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("updateTemp", JSONObject.toJSONString(partnerInstanceDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
		*/
	}

	/**
	 * 更新固点协议
	 *
	 * @param stationDto
	 * @param stationId
	 */
	private void saveStationFixProtocol(StationDto stationDto, Long stationId) {
		if (stationDto.getAreaType() != null) {
			if (StringUtils.equals(StationAreaTypeEnum.FIX_NEW.getCode(), stationDto.getAreaType().getCode())) {
				PartnerProtocolRelDeleteDto deleteDto = new PartnerProtocolRelDeleteDto();
				deleteDto.setObjectId(stationId);
				deleteDto.setTargetType(PartnerProtocolRelTargetTypeEnum.CRIUS_STATION);
				List<ProtocolTypeEnum> fixProList = new ArrayList<ProtocolTypeEnum>();
				fixProList.add(ProtocolTypeEnum.GOV_FIXED);
				fixProList.add(ProtocolTypeEnum.TRIPARTITE_FIXED);
				deleteDto.copyOperatorDto(stationDto);
				deleteDto.setProtocolTypeList(fixProList);

				partnerProtocolRelBO.deletePartnerProtocolRel(deleteDto);
				PartnerProtocolRelDto fixPro = stationDto.getFixedProtocols();

				if (fixPro != null) {
					fixPro.copyOperatorDto(stationDto);
					fixPro.setObjectId(stationId);
					partnerProtocolRelBO.addPartnerProtocolRel(fixPro);
				}
			}
		}
	}

	private String getErrorMessage(String methodName, String param, String error) {
		StringBuilder sb = new StringBuilder();
		sb.append("PartnerInstanceService-Error|").append(methodName).append("(.param=").append(param).append(").").append("errorMessage:")
				.append(error);
		return sb.toString();
	}

	private String getAugeExceptionErrorMessage(String methodName, String param, String error) {
		StringBuilder sb = new StringBuilder();
		sb.append("PartnerInstanceService|").append(methodName).append("(.param=").append(param).append(").").append("errorMessage:")
				.append(error);
		return sb.toString();
	}


	private void validateSettlable(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		ValidateUtils.notNull(partnerInstanceDto);
		StationDto stationDto = partnerInstanceDto.getStationDto();
		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
		ValidateUtils.notNull(stationDto);
		StationValidator.validateStationInfo(stationDto);
		PartnerValidator.validatePartnerInfo(partnerDto);

		OperatorDto operator = new OperatorDto();
		operator.copyOperatorDto(partnerInstanceDto);
		PaymentAccountDto paDto = paymentAccountQueryAdapter.queryPaymentAccountByNick(partnerDto.getTaobaoNick(), operator);
		if (!partnerDto.getAlipayAccount().equals(paDto.getAlipayId())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_ALIPAYACCOUNT_NOTEQUAL);
		}
		if (!partnerDto.getName().equals(paDto.getFullName()) || !partnerDto.getIdenNum().equals(paDto.getIdCardNumber())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_PERSION_INFO_NOTEQUAL);
		}

		// 判断淘宝账号是否使用中
		PartnerStationRel existPartnerInstance = partnerInstanceBO.getActivePartnerInstance(paDto.getTaobaoUserId());
		if (null != existPartnerInstance) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_TAOBAOUSERID_HAS_USED);
		}

		// 入驻老村点，村点状态为已停业
		Long stationId = stationDto.getId();
		if (stationId != null) {
			PartnerStationRel currentRel = partnerInstanceBO.findPartnerInstanceByStationId(stationId);
			if (currentRel != null && PartnerInstanceStateEnum.CLOSED.getCode().equals(currentRel.getState())) {
				throw new AugeServiceException(PartnerInstanceExceptionEnum.PARTNER_INSTANCE_MUST_BE_CLOSED);
			}
		}
	}

	private void checkStationNumDuplicate(Long stationId, String newStationNum) {
		// 判断服务站编号是否使用中
		String oldStationNum = null;
		if (stationId != null) {
			Station oldStation = stationBO.getStationById(stationId);
			oldStationNum = oldStation.getStationNum();
		}
		if (!StringUtils.equals(oldStationNum, newStationNum)) {
			int count = stationBO.getStationCountByStationNum(newStationNum);
			if (count > 0) {
				throw new AugeServiceException(StationExceptionEnum.STATION_NUM_IS_DUPLICATE);
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void updateByPartner(PartnerInstanceUpdateServicingDto partnerInstanceUpdateServicingDto) throws AugeServiceException {
		ValidateUtils.validateParam(partnerInstanceUpdateServicingDto);
		PartnerValidator.validateParnterUpdateInfoByPartner(partnerInstanceUpdateServicingDto.getPartnerDto());
		StationValidator.validateStationUpdateInfoByPartner(partnerInstanceUpdateServicingDto.getStationDto());
		Long stationId = partnerInstanceUpdateServicingDto.getStationDto().getStationId();
		ValidateUtils.notNull(stationId);

		try {
			Long instanceId = partnerInstanceBO.findPartnerInstanceIdByStationId(stationId);
			partnerInstanceUpdateServicingDto.setId(instanceId);
			updateInternal(partnerInstanceUpdateServicingDto);
		} catch (AugeServiceException augeException) {
			String error = getAugeExceptionErrorMessage("update", JSONObject.toJSONString(partnerInstanceUpdateServicingDto),
					augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("update", JSONObject.toJSONString(partnerInstanceUpdateServicingDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void update(PartnerInstanceUpdateServicingDto partnerInstanceUpdateServicingDto) throws AugeServiceException {
		ValidateUtils.validateParam(partnerInstanceUpdateServicingDto);
		ValidateUtils.notNull(partnerInstanceUpdateServicingDto.getId());
		ValidateUtils.notNull(partnerInstanceUpdateServicingDto.getVersion());
		PartnerValidator.validateParnterCanUpdateInfo(partnerInstanceUpdateServicingDto.getPartnerDto());
		StationValidator.validateStationCanUpdateInfo(partnerInstanceUpdateServicingDto.getStationDto());
		try {
			updateInternal(partnerInstanceUpdateServicingDto);

			// 修改子成员配额
			Integer childNum = partnerInstanceUpdateServicingDto.getChildNum();
			if (null != childNum) {
				PartnerInstanceExtDto instanceExtDto = new PartnerInstanceExtDto();

				instanceExtDto.setInstanceId(partnerInstanceUpdateServicingDto.getId());
				instanceExtDto.setMaxChildNum(childNum);
				instanceExtDto.copyOperatorDto(partnerInstanceUpdateServicingDto);

				partnerInstanceExtService.savePartnerExtInfo(instanceExtDto);
			}
		} catch (AugeServiceException augeException) {
			String error = getAugeExceptionErrorMessage("update", JSONObject.toJSONString(partnerInstanceUpdateServicingDto),
					augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("update", JSONObject.toJSONString(partnerInstanceUpdateServicingDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	private void updateInternal(PartnerInstanceUpdateServicingDto partnerInstanceUpdateServicingDto) {
		Long partnerInstanceId = partnerInstanceUpdateServicingDto.getId();
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(partnerInstanceId);
		Long stationId = rel.getStationId();
		Long partnerId = rel.getPartnerId();

		if (partnerInstanceUpdateServicingDto.getPartnerDto() != null) {
			updatePartnerForServicing(partnerInstanceUpdateServicingDto, partnerId);
		}
		if (partnerInstanceUpdateServicingDto.getStationDto() != null) {
			updateStationForServicing(partnerInstanceUpdateServicingDto, stationId);
		}
		// 同步station_apply
		syncStationApply(SyncStationApplyEnum.UPDATE_ALL, partnerInstanceId);

		if (isNeedToUpdateCainiaoStation(rel.getState())) {
			generalTaskSubmitService.submitUpdateCainiaoStation(partnerInstanceId, partnerInstanceUpdateServicingDto.getOperator());
		}
	}

	private boolean isNeedToUpdateCainiaoStation(String state) {
		return PartnerInstanceStateEnum.DECORATING.getCode().equals(state) || PartnerInstanceStateEnum.SERVICING.getCode().equals(state)
				|| PartnerInstanceStateEnum.CLOSING.getCode().equals(state);
	}

	private void updateStationForServicing(PartnerInstanceUpdateServicingDto partnerInstanceUpdateServicingDto, Long stationId) {
		StationUpdateServicingDto sDto = partnerInstanceUpdateServicingDto.getStationDto();

		StationDto stationDto = new StationDto();
		// 判断服务站编号是否使用中
		if (StringUtil.isNotBlank(sDto.getStationNum())) {
			checkStationNumDuplicate(stationId, sDto.getStationNum());
			stationDto.setStationNum(sDto.getStationNum());
		}

		stationDto.setAddress(sDto.getAddress());
		stationDto.setAreaType(sDto.getAreaType());
		stationDto.setCovered(sDto.getCovered());
		stationDto.setDescription(sDto.getDescription());
		stationDto.setFeature(sDto.getFeature());
		stationDto.setFixedProtocols(sDto.getFixedProtocols());
		stationDto.setFixedType(sDto.getFixedType());
		stationDto.setFormat(sDto.getFormat());
		stationDto.setId(stationId);
		stationDto.setLogisticsState(sDto.getLogisticsState());
		stationDto.setManagerId(sDto.getManagerId());
		stationDto.setName(sDto.getName());
		stationDto.setProducts(sDto.getProducts());

		stationDto.copyOperatorDto(partnerInstanceUpdateServicingDto);
		stationBO.updateStation(stationDto);

		// 更新固点协议
		saveStationFixProtocol(stationDto, stationId);
		attachementBO.modifyAttachementBatch(sDto.getAttachements(), stationId, AttachementBizTypeEnum.CRIUS_STATION,
				partnerInstanceUpdateServicingDto);
	}

	private void updatePartnerForServicing(PartnerInstanceUpdateServicingDto partnerInstanceUpdateServicingDto, Long partnerId) {
		PartnerUpdateServicingDto pDto = partnerInstanceUpdateServicingDto.getPartnerDto();
		PartnerDto partnerDto = new PartnerDto();
		partnerDto.copyOperatorDto(partnerInstanceUpdateServicingDto);
		partnerDto.setId(partnerId);
		partnerDto.setMobile(pDto.getMobile());
		partnerDto.setEmail(pDto.getEmail());
		partnerDto.setBusinessType(pDto.getBusinessType());
		partnerBO.updatePartner(partnerDto);
		attachementBO.modifyAttachementBatch(pDto.getAttachements(), partnerId, AttachementBizTypeEnum.PARTNER,
				partnerInstanceUpdateServicingDto);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto) throws AugeServiceException {
		ValidateUtils.validateParam(partnerInstanceDeleteDto);
		Long instanceId = partnerInstanceDeleteDto.getInstanceId();
		ValidateUtils.notNull(instanceId);
		try {
			PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
			if (rel == null || StringUtils.isEmpty(rel.getType())) {
				throw new NullPointerException("partner instance not exists");
			}
			partnerInstanceHandler.handleDelete(partnerInstanceDeleteDto, rel);
			// 同步删除
			syncStationApplyBO.deleteStationApply(rel.getStationApplyId());
		} catch (AugeServiceException augeException) {
			String error = getAugeExceptionErrorMessage("delete", JSONObject.toJSONString(partnerInstanceDeleteDto),
					augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("delete", JSONObject.toJSONString(partnerInstanceDeleteDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void signSettledProtocol(Long taobaoUserId, Double waitFrozenMoney, Long version) throws AugeServiceException {
		ValidateUtils.notNull(taobaoUserId);
		ValidateUtils.notNull(waitFrozenMoney);
		try {
			PartnerStationRel psRel = partnerInstanceBO.getPartnerInstanceByTaobaoUserId(taobaoUserId, PartnerInstanceStateEnum.SETTLING);
			Assert.notNull(psRel, "partner instance not exists");
			Long instanceId = psRel.getId();
			PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.SETTLING);
			Assert.notNull(items, "PartnerLifecycleItems not exists");

			PartnerLifecycleItemCheckResultEnum checkSettled = PartnerLifecycleRuleParser.parseExecutable(
					PartnerInstanceTypeEnum.valueof(psRel.getType()), PartnerLifecycleItemCheckEnum.settledProtocol, items);
			if (PartnerLifecycleItemCheckResultEnum.EXECUTED == checkSettled) {
				return;
			} else if (PartnerLifecycleItemCheckResultEnum.NONEXCUTABLE == checkSettled) {
				throw new AugeServiceException(PartnerInstanceExceptionEnum.PARTNER_INSTANCE_ITEM_UNEXECUTABLE);
			}

			partnerProtocolRelBO.signProtocol(taobaoUserId, ProtocolTypeEnum.SETTLE_PRO, instanceId,
					PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);

			addWaitFrozenMoney(instanceId, taobaoUserId, waitFrozenMoney);

			PartnerLifecycleDto param = new PartnerLifecycleDto();
			param.setSettledProtocol(PartnerLifecycleSettledProtocolEnum.SIGNED);
			param.setLifecycleId(items.getId());
			partnerLifecycleBO.updateLifecycle(param);

			// 乐观锁
			PartnerInstanceDto instance = new PartnerInstanceDto();
			instance.setId(instanceId);
			instance.setVersion(version);
			instance.setOperator(String.valueOf(taobaoUserId));
			instance.setOperatorType(OperatorTypeEnum.HAVANA);
			partnerInstanceBO.updatePartnerStationRel(instance);

			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instanceId);
		} catch (AugeServiceException augeException) {
			String error = getAugeExceptionErrorMessage("signSettledProtocol", String.valueOf(taobaoUserId), augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("signSettledProtocol", String.valueOf(taobaoUserId), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	private void addWaitFrozenMoney(Long instanceId, Long taobaoUserId, Double waitFrozenMoney) {
		ValidateUtils.notNull(instanceId);
		AccountMoneyDto accountMoneyDto = new AccountMoneyDto();
		accountMoneyDto.setMoney(BigDecimal.valueOf(waitFrozenMoney));
		accountMoneyDto.setOperator(String.valueOf(taobaoUserId));
		accountMoneyDto.setOperatorType(OperatorTypeEnum.HAVANA);
		accountMoneyDto.setObjectId(instanceId);
		accountMoneyDto.setState(AccountMoneyStateEnum.WAIT_FROZEN);
		accountMoneyDto.setTaobaoUserId(String.valueOf(taobaoUserId));
		accountMoneyDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
		accountMoneyDto.setType(AccountMoneyTypeEnum.PARTNER_BOND);

		AccountMoneyDto dupRecord = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
				AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instanceId);
		if (dupRecord != null) {
			accountMoneyBO.updateAccountMoneyByObjectId(accountMoneyDto);
		} else {
			accountMoneyBO.addAccountMoney(accountMoneyDto);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void signManageProtocol(Long taobaoUserId, Long version) throws AugeServiceException {
		ValidateUtils.notNull(taobaoUserId);
		try {
			PartnerStationRel partnerStationRel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
			Assert.notNull(partnerStationRel, "partner instance not exists");

			partnerProtocolRelBO.signProtocol(taobaoUserId, ProtocolTypeEnum.MANAGE_PRO, partnerStationRel.getId(),
					PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);

			// 乐观锁
			PartnerInstanceDto instance = new PartnerInstanceDto();
			instance.setId(partnerStationRel.getId());
			instance.setVersion(version);
			instance.setOperator(String.valueOf(taobaoUserId));
			instance.setOperatorType(OperatorTypeEnum.HAVANA);
			partnerInstanceBO.updatePartnerStationRel(instance);

			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_ALL, partnerStationRel.getId());
		} catch (AugeServiceException augeException) {
			String error = getAugeExceptionErrorMessage("signManageProtocol", String.valueOf(taobaoUserId), augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("signManageProtocol", String.valueOf(taobaoUserId), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public boolean freezeBond(Long taobaoUserId, Double frozenMoney) throws AugeServiceException {
		try {
			PartnerStationRel instance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
			PartnerLifecycleItems settleItems = partnerLifecycleBO.getLifecycleItems(instance.getId(),
					PartnerLifecycleBusinessTypeEnum.SETTLING, PartnerLifecycleCurrentStepEnum.PROCESSING);
			AccountMoneyDto bondMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
					AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instance.getId());
			if (!PartnerInstanceStateEnum.SETTLING.getCode().equals(instance.getState()) || null == settleItems || null == bondMoney
					|| !AccountMoneyStateEnum.WAIT_FROZEN.equals(bondMoney.getState())) {
				throw new AugeServiceException(PartnerExceptionEnum.PARTNER_STATE_NOT_APPLICABLE);
			}
			String operator = String.valueOf(taobaoUserId);

			// 修改生命周期表
			PartnerLifecycleDto lifecycleUpdateDto = new PartnerLifecycleDto();
			lifecycleUpdateDto.setLifecycleId(settleItems.getId());
			lifecycleUpdateDto.setBond(PartnerLifecycleBondEnum.HAS_FROZEN);
			lifecycleUpdateDto.setOperator(operator);
			lifecycleUpdateDto.setOperatorType(OperatorTypeEnum.HAVANA);
			partnerLifecycleBO.updateLifecycle(lifecycleUpdateDto);

			// 修改保证金冻结状态
			AccountMoneyDto accountMoneyUpdateDto = new AccountMoneyDto();
			accountMoneyUpdateDto.setObjectId(bondMoney.getObjectId());
			accountMoneyUpdateDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
			accountMoneyUpdateDto.setType(AccountMoneyTypeEnum.PARTNER_BOND);
			accountMoneyUpdateDto.setFrozenTime(new Date());
			accountMoneyUpdateDto.setState(AccountMoneyStateEnum.HAS_FROZEN);
			accountMoneyUpdateDto.setOperator(operator);
			accountMoneyUpdateDto.setOperatorType(OperatorTypeEnum.HAVANA);
			accountMoneyBO.updateAccountMoneyByObjectId(accountMoneyUpdateDto);

			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instance.getId());

			Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
			generalTaskSubmitService.submitSettlingSysProcessTasks(PartnerInstanceConverter.convert(instance, null, partner), operator);

			// 流转日志, 合伙人入驻
			// bulidRecordEventForPartnerEnter(stationApplyDetailDto);

			return true;
		} catch (Exception e) {
			String error = getErrorMessage("freezeBond", String.valueOf(taobaoUserId), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public boolean openStation(OpenStationDto openStationDto) throws AugeServiceException {
		// 参数校验
		BeanValidator.validateWithThrowable(openStationDto);
		Long instanceId = openStationDto.getPartnerInstanceId();
		String operator = openStationDto.getOperator();
		try {
			// 检查装修中的生命周期（装修，培训）完成后，才能开业
			checkPartnerLifecycleForOpenStation(instanceId);

			if (openStationDto.isImme()) {// 立即开业
				PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
				if (rel == null || !PartnerInstanceStateEnum.DECORATING.getCode().equals(rel.getState())) {
					throw new AugeServiceException(PartnerExceptionEnum.NO_RECORD);
				}
				// 更新合伙人实例状态为服务中
				partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.DECORATING, PartnerInstanceStateEnum.SERVICING,
						operator);
				// 更新村点状态为服务中
				stationBO.changeState(rel.getStationId(), StationStatusEnum.DECORATING, StationStatusEnum.SERVICING, operator);
				// 更新开业时间
				partnerInstanceBO.updateOpenDate(openStationDto.getPartnerInstanceId(), openStationDto.getOpenDate(),
						openStationDto.getOperator());
				// 同步station_apply
				syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);

				// 初始化合伙人层级
				initPartnerInstanceLevel(rel);

				// 记录村点状态变化
				sendPartnerInstanceStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.START_SERVICING, openStationDto);
				// 开业包项目事件
				dispachToServiceEvent(openStationDto, instanceId);
			} else {// 定时开业
				partnerInstanceBO.updateOpenDate(openStationDto.getPartnerInstanceId(), openStationDto.getOpenDate(),
						openStationDto.getOperator());
				// 同步station_apply
				syncStationApply(SyncStationApplyEnum.UPDATE_BASE, openStationDto.getPartnerInstanceId());
			}
		} catch (AugeServiceException augeException) {
			String error = getErrorMessage("openStation", JSONObject.toJSONString(openStationDto), augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("openStation", JSONObject.toJSONString(openStationDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
		return true;
	}

	private void initPartnerInstanceLevel(PartnerStationRel instance) {
		PartnerInstanceLevelDto dto = new PartnerInstanceLevelDto();
		dto.setPartnerInstanceId(instance.getId());
		dto.setTaobaoUserId(instance.getTaobaoUserId());
		dto.setStationId(instance.getStationId());
		dto.setCurrentLevel(PartnerInstanceLevelEnum.S_4);
		dto.setEvaluateBy(OperatorDto.DEFAULT_OPERATOR);
		Calendar c = Calendar.getInstance();
		Date today = c.getTime();
		dto.setEvaluateDate(today);

		c.add(Calendar.MONTH, DEFAULT_EVALUATE_INTERVAL);
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (day > 1) {
			c.add(Calendar.MONTH, 1);
		}
		c.set(Calendar.DAY_OF_MONTH, 1);
		dto.setNextEvaluateDate(c.getTime());
		dto.copyOperatorDto(OperatorDto.defaultOperator());
		Station station = stationBO.getStationById(instance.getStationId());
		dto.setCountyOrgId(station.getApplyOrg());
		partnerInstanceLevelBO.addPartnerInstanceLevel(dto);
	}

	/**
	 * 检查装修中的生命周期（装修，培训）完成后，才能开业
	 * @param instanceId
	 */
	private void checkPartnerLifecycleForOpenStation(Long instanceId) throws AugeServiceException {
		ValidateUtils.notNull(instanceId);
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.DECORATING,
				PartnerLifecycleCurrentStepEnum.PROCESSING);
		if (items == null) {
			// 没有数据 认为是标准化项目之前的数据，直接可以开业
			return;
		}
		if (!PartnerLifecycleCourseStatusEnum.Y.getCode().equals(items.getCourseStatus())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_NOT_FINISH_COURSE);
		}

		if (!PartnerLifecycleDecorateStatusEnum.Y.getCode().equals(items.getDecorateStatus())) {
			throw new AugeServiceException(StationExceptionEnum.STATION_NOT_FINISH_DECORATE);
		}

		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
		partnerLifecycleDto.setLifecycleId(items.getId());
		partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
		partnerLifecycleDto.copyOperatorDto(OperatorDto.defaultOperator());
		partnerLifecycleBO.updateLifecycle(partnerLifecycleDto);
	}

	private void sendPartnerInstanceStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChangeEnum,
	                                                 OperatorDto operator) {
		PartnerInstanceDto piDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
		EventDispatcherUtil.dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
				PartnerInstanceEventConverter.convertStateChangeEvent(stateChangeEnum, piDto, operator));
	}

	private void dispachToServiceEvent(OpenStationDto openStationDto, Long instanceId) {
		try {
			PartnerInstanceDto piDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
			PartnerInstanceStateChangeEvent partnerInstanceEvent = new PartnerInstanceStateChangeEvent();
			partnerInstanceEvent.setExecDate(com.taobao.cun.auge.common.utils.DateUtil.format(openStationDto.getOpenDate()));
			partnerInstanceEvent.setOwnOrgId(piDto.getStationDto().getApplyOrg());
			partnerInstanceEvent.setTaobaoUserId(piDto.getTaobaoUserId());
			partnerInstanceEvent.setTaobaoNick(piDto.getPartnerDto().getTaobaoNick());
			partnerInstanceEvent.setStationId(piDto.getStationId());
			partnerInstanceEvent.setStationName(piDto.getStationDto().getStationNum());
			partnerInstanceEvent.setOperator(openStationDto.getOperator());
			EventDispatcherUtil.dispatch("STATION_TO_SERVICE_EVENT", partnerInstanceEvent);
			logger.info("dispachToServiceEvent success partnerInstanceId:" + openStationDto.getPartnerInstanceId());
		} catch (Exception e) {
			String error = getErrorMessage("dispachToServiceEvent", JSONObject.toJSONString(openStationDto), e.getMessage());
			logger.error(error, e);
		}
	}

	@Override
	public boolean checkKyPackage() {
		// TODO:检查开业包
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void applyCloseByPartner(Long taobaoUserId) throws AugeServiceException {
		try {
			// 参数校验
			Assert.notNull(taobaoUserId, "taobaoUserId is null");

			// 合伙人实例状态校验
			PartnerStationRel partnerInstance = partnerInstanceBO.getPartnerInstanceByTaobaoUserId(taobaoUserId,
					PartnerInstanceStateEnum.SERVICING);
			Assert.notNull(partnerInstance, "没有服务中的合伙人。taobaoUserId = " + taobaoUserId);

			Long instanceId = partnerInstance.getId();
			Long stationId = partnerInstance.getStationId();

			String taobaoUserIdStr = String.valueOf(taobaoUserId);
			OperatorDto operatorDto = new OperatorDto();
			operatorDto.setOperator(taobaoUserIdStr);
			operatorDto.setOperatorType(OperatorTypeEnum.HAVANA);

			// FIXME FHH 合伙人主动申请退出时，为什么不校验是否存在淘帮手，非要到审批时再校验

			// 更新合伙人实例状态为停业中
			closingPartnerInstance(partnerInstance, PartnerInstanceCloseTypeEnum.PARTNER_QUIT, operatorDto);

			// 更新村点状态为停业中
			stationBO.changeState(stationId, StationStatusEnum.SERVICING, StationStatusEnum.CLOSING, taobaoUserIdStr);

			// 添加停业生命周期记录
			addClosingLifecycle(operatorDto, partnerInstance, PartnerInstanceCloseTypeEnum.PARTNER_QUIT);

			// 插入停业协议
			addCloseProtocol(taobaoUserId, instanceId, operatorDto);

			// 新增停业申请
			CloseStationApplyDto closeStationApplyDto = new CloseStationApplyDto();
			closeStationApplyDto.setPartnerInstanceId(instanceId);
			closeStationApplyDto.setType(PartnerInstanceCloseTypeEnum.PARTNER_QUIT);
			closeStationApplyDto.copyOperatorDto(operatorDto);
			closeStationApplyBO.addCloseStationApply(closeStationApplyDto);

			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instanceId);

			// 发送状态变化事件
			PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(
					PartnerInstanceStateChangeEnum.START_CLOSING, partnerInstanceBO.getPartnerInstanceById(instanceId), operatorDto);
			EventDispatcherUtil.dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);

		} catch (AugeServiceException e) {
			String error = getAugeExceptionErrorMessage("applyCloseByPartner", String.valueOf(taobaoUserId), e.toString());
			logger.error(error, e);
			throw e;
		} catch (Exception e) {
			String error = getErrorMessage("applyCloseByPartner", String.valueOf(taobaoUserId), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	/**
	 * 添加停业协议

	 * @param taobaoUserId
	 * @param instanceId
	 * @param operatorDto
	 */
	private void addCloseProtocol(Long taobaoUserId, Long instanceId, OperatorDto operatorDto) {
		PartnerProtocolRelDto proRelDto = new PartnerProtocolRelDto();
		Date quitProDate = new Date();
		proRelDto.setObjectId(instanceId);
		proRelDto.setProtocolTypeEnum(ProtocolTypeEnum.PARTNER_QUIT_PRO);
		proRelDto.setConfirmTime(quitProDate);
		proRelDto.setStartTime(quitProDate);
		proRelDto.setTaobaoUserId(taobaoUserId);
		proRelDto.setTargetType(PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
		proRelDto.copyOperatorDto(operatorDto);
		partnerProtocolRelBO.addPartnerProtocolRel(proRelDto);
	}

	/**
	 * 合伙人实例 停业中
	 * @param partnerInstance
	 * @param closeType
	 * @param operatorDto
	 */
	private void closingPartnerInstance(PartnerStationRel partnerInstance, PartnerInstanceCloseTypeEnum closeType,
	                                    OperatorDto operatorDto) {
		PartnerInstanceDto partnerInstanceDto = new PartnerInstanceDto();

		partnerInstanceDto.setId(partnerInstance.getId());
		partnerInstanceDto.setState(PartnerInstanceStateEnum.CLOSING);
		partnerInstanceDto.setCloseType(closeType);
		partnerInstanceDto.copyOperatorDto(operatorDto);
		partnerInstanceDto.setVersion(partnerInstance.getVersion());
		partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);
	}

	/**
	 * 村点 停业中
	 *
	 * @param operatorDto
	 * @param stationId
	 * @param instanceStateChange
	 */
	private void closingStation(Long stationId, PartnerInstanceStateChangeEnum instanceStateChange, OperatorDto operatorDto) {
		if (PartnerInstanceStateChangeEnum.DECORATING_CLOSING.equals(instanceStateChange)) {
			stationBO.changeState(stationId, StationStatusEnum.DECORATING, StationStatusEnum.CLOSING, operatorDto.getOperator());
		} else if (PartnerInstanceStateChangeEnum.START_CLOSING.equals(instanceStateChange)) {
			stationBO.changeState(stationId, StationStatusEnum.SERVICING, StationStatusEnum.CLOSING, operatorDto.getOperator());
		} else {
			throw new AugeServiceException("partner state is not decorating or servicing.");
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void confirmClose(ConfirmCloseDto confirmCloseDto) throws AugeServiceException {
		try {
			// 参数校验
			BeanValidator.validateWithThrowable(confirmCloseDto);

			Long instanceId = confirmCloseDto.getPartnerInstanceId();
			String employeeId = confirmCloseDto.getOperator();
			Boolean isAgree = confirmCloseDto.isAgree();
			PartnerStationRel partnerInstance = partnerInstanceBO.findPartnerInstanceById(instanceId);
			PartnerLifecycleItems partnerLifecycleItem = partnerLifecycleBO.getLifecycleItems(instanceId,
					PartnerLifecycleBusinessTypeEnum.CLOSING);

			if (!PartnerInstanceStateEnum.CLOSING.getCode().equals(partnerInstance.getState()) || null == partnerLifecycleItem) {
				throw new RuntimeException("没有停业申请中的合伙人。ConfirmCloseDto = " + JSON.toJSONString(confirmCloseDto));
			}

			Long stationId = partnerInstance.getStationId();

			PartnerLifecycleItemCheckResultEnum confirmExecutable = PartnerLifecycleRuleParser.parseExecutable(
					PartnerInstanceTypeEnum.valueof(partnerInstance.getType()), PartnerLifecycleItemCheckEnum.confirm,
					partnerLifecycleItem);
			if (!PartnerLifecycleItemCheckResultEnum.EXECUTABLE.equals(confirmExecutable)) {
				throw new RuntimeException(PartnerInstanceExceptionEnum.PARTNER_INSTANCE_ITEM_UNEXECUTABLE.getDesc());
			}
			PartnerLifecycleDto partnerLifecycle = new PartnerLifecycleDto();
			partnerLifecycle.setLifecycleId(partnerLifecycleItem.getId());
			partnerLifecycle.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
			partnerLifecycle.copyOperatorDto(confirmCloseDto);

			if (isAgree) {
				// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
				PartnerInstanceTypeEnum partnerType = PartnerInstanceTypeEnum.valueof(partnerInstance.getType());
				partnerInstanceHandler.validateClosePreCondition(partnerType, partnerInstance);

				// 更新合伙人实例，已停业
				PartnerInstanceDto partnerInstanceDto = new PartnerInstanceDto();
				partnerInstanceDto.setId(instanceId);
				partnerInstanceDto.setState(PartnerInstanceStateEnum.CLOSED);
				partnerInstanceDto.setServiceEndTime(new Date());
				partnerInstanceDto.copyOperatorDto(confirmCloseDto);
				partnerInstanceDto.setVersion(partnerInstance.getVersion());
				partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);

				// 更新村点状态，已停业
				stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.CLOSED, employeeId);

				// 更新生命周期，已确认
				partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.CONFIRM);
				partnerLifecycleBO.updateLifecycle(partnerLifecycle);

				// 同步station_apply
				syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);

				// 发出合伙人实例状态变更事件
				dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.CLOSED, confirmCloseDto);
			} else {
				// 更新合伙人实例，服务中
				partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSING, PartnerInstanceStateEnum.SERVICING, employeeId);

				// 更新村点状态，服务中
				stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.SERVICING, employeeId);

				// 更新生命周期，拒绝
				partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.CANCEL);
				partnerLifecycleBO.updateLifecycle(partnerLifecycle);

				// 删除停业协议
				partnerProtocolRelBO.cancelProtocol(partnerInstance.getTaobaoUserId(), ProtocolTypeEnum.PARTNER_QUIT_PRO, instanceId,
						PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE, employeeId);

				// 删除停业申请单
				closeStationApplyBO.deleteCloseStationApply(instanceId, employeeId);

				// 同步station_apply
				syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);

				// 发出合伙人实例状态变更事件
				dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.CLOSING_REFUSED, confirmCloseDto);
			}
		} catch (AugeServiceException e) {
			String error = getAugeExceptionErrorMessage("confirmClose", JSONObject.toJSONString(confirmCloseDto), e.toString());
			logger.error(error, e);
			throw e;
		} catch (Exception e) {
			String error = getErrorMessage("confirmClose", JSONObject.toJSONString(confirmCloseDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void applyCloseByManager(ForcedCloseDto forcedCloseDto) throws AugeServiceException {
		try {
			// 参数校验
			BeanValidator.validateWithThrowable(forcedCloseDto);
			//校验操作人的组织id
			forcedCloseDto.validateOperatorOrgId();

			Long instanceId = forcedCloseDto.getInstanceId();
			PartnerStationRel partnerStationRel = partnerInstanceBO.findPartnerInstanceById(instanceId);

			//生成状态变化枚举
			PartnerInstanceStateChangeEnum instanceStateChange = buildInstanceClosingStateChange(partnerStationRel);

			// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
			PartnerInstanceTypeEnum partnerType = PartnerInstanceTypeEnum.valueof(partnerStationRel.getType());

			partnerInstanceHandler.validateClosePreCondition(partnerType, partnerStationRel);

			// 合伙人实例停业中,退出类型为强制清退
			closingPartnerInstance(partnerStationRel, PartnerInstanceCloseTypeEnum.WORKER_QUIT, forcedCloseDto);

			// 村点停业中
			closingStation(partnerStationRel.getStationId(), instanceStateChange, forcedCloseDto);

			// 添加停业生命周期记录
			addClosingLifecycle(forcedCloseDto, partnerStationRel, PartnerInstanceCloseTypeEnum.WORKER_QUIT);

			// 新增停业申请单
			CloseStationApplyDto closeStationApplyDto = CloseStationApplyConverter.toWorkCloseStationApplyDto(forcedCloseDto, instanceStateChange);
			closeStationApplyBO.addCloseStationApply(closeStationApplyDto);

			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);

			// 通过事件，定时钟，启动停业流程
			dispatchInstStateChangeEvent(instanceId, instanceStateChange, forcedCloseDto);
			// 失效tair
		} catch (AugeServiceException e) {
			String error = getAugeExceptionErrorMessage("applyCloseByManager", "ForcedCloseDto =" + JSON.toJSONString(forcedCloseDto),
					e.toString());
			logger.error(error, e);
			throw e;
		} catch (Exception e) {
			String error = getErrorMessage("applyCloseByManager", "ForcedCloseDto =" + JSON.toJSONString(forcedCloseDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	private PartnerInstanceStateChangeEnum buildInstanceClosingStateChange(PartnerStationRel partnerStationRel) {
		if (PartnerInstanceStateEnum.DECORATING.getCode().equals(partnerStationRel.getState())) {
			return PartnerInstanceStateChangeEnum.DECORATING_CLOSING;
		} else if (PartnerInstanceStateEnum.SERVICING.getCode().equals(partnerStationRel.getState())) {
			return PartnerInstanceStateChangeEnum.START_CLOSING;
		} else {
			//状态校验,只有装修中，或者服务中可以停业
			throw new AugeServiceException("partner state is not decorating or servicing.");
		}
	}

	/**
	 * 添加停业生命周期
	 *
	 * @param operatorDto
	 * @param partnerStationRel
	 * @param closeType
	 */
	private void addClosingLifecycle(OperatorDto operatorDto, PartnerStationRel partnerStationRel, PartnerInstanceCloseTypeEnum closeType) {
		PartnerLifecycleDto partnerLifecycle = new PartnerLifecycleDto();

		partnerLifecycle.setPartnerInstanceId(partnerStationRel.getId());
		partnerLifecycle.setPartnerType(PartnerInstanceTypeEnum.valueof(partnerStationRel.getType()));
		partnerLifecycle.setBusinessType(PartnerLifecycleBusinessTypeEnum.CLOSING);

		if (PartnerInstanceCloseTypeEnum.WORKER_QUIT.equals(closeType)) {
			partnerLifecycle.setRoleApprove(PartnerLifecycleRoleApproveEnum.TO_START);
		} else if (PartnerInstanceCloseTypeEnum.PARTNER_QUIT.equals(closeType)) {
			partnerLifecycle.setQuitProtocol(PartnerLifecycleQuitProtocolEnum.SIGNED);
			partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.WAIT_CONFIRM);
		}
		partnerLifecycle.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);
		partnerLifecycle.copyOperatorDto(operatorDto);

		partnerLifecycleBO.addLifecycle(partnerLifecycle);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void applyQuitByManager(QuitStationApplyDto quitDto) throws AugeServiceException {
		try {
			// 参数校验
			BeanValidator.validateWithThrowable(quitDto);

			Long instanceId = quitDto.getInstanceId();
			String operator = quitDto.getOperator();

			PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);

			// 校验申请退出的前置条件：是否存在下级合伙人，是否存在未结束订单，是否已经提交过退出
			partnerInstanceValidator.validateApplyQuitPreCondition(instance, quitDto);

			// 保存退出申请单
			QuitStationApply quitStationApply = QuitStationApplyConverter.convert(quitDto, instance, buildOperatorName(quitDto));
			quitStationApplyBO.saveQuitStationApply(quitStationApply, operator);

			// 合伙人实例状态变更为退出中
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSED, PartnerInstanceStateEnum.QUITING, operator);

			// 村点状态变更为退出中
			if (quitDto.getIsQuitStation()) {
				stationBO.changeState(instance.getStationId(), StationStatusEnum.CLOSED, StationStatusEnum.QUITING, operator);
			}

			// 不同合伙人不同退出生命周期
			partnerInstanceHandler.handleDifferQuiting(quitDto, PartnerInstanceTypeEnum.valueof(instance.getType()));

			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instanceId);

			// 发送合伙人实例状态变化事件
			dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.START_QUITTING, quitDto);

			// 失效tair
		} catch (AugeServiceException e) {
			String error = getAugeExceptionErrorMessage("applyQuitByManager", "QuitStationApplyDto =" + JSON.toJSONString(quitDto),
					e.toString());
			logger.error(error, e);
			throw e;
		} catch (Exception e) {
			String error = getErrorMessage("applyQuitByManager", "QuitStationApplyDto =" + JSON.toJSONString(quitDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	private String buildOperatorName(OperatorDto operatorDto) {
		String operator = operatorDto.getOperator();
		OperatorTypeEnum type = operatorDto.getOperatorType();

		// 小二工号
		if (OperatorTypeEnum.BUC.equals(type)) {
			return emp360Adapter.getName(operator);
		} else if (OperatorTypeEnum.HAVANA.equals(type)) {
			return uicReadAdapter.getFullName(Long.parseLong(operator));
		}
		logger.warn("查询操作人姓名失败，不支持的操作人类型。OperatorTypeEnum=" + (null != type ? type.getDesc() : ""));
		return "";
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Long applySettle(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		ValidateUtils.validateParam(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getStationDto());
		ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
		ValidateUtils.notNull(partnerInstanceDto.getType());

		try {
			validateSettlable(partnerInstanceDto);

			Long stationId = partnerInstanceDto.getStationId();
			Long instanceId;

			if (stationId == null) {
				stationId = addStation(partnerInstanceDto);
			} else {
				updateStation(stationId, partnerInstanceDto);
			}

			Long partnerId = addPartner(partnerInstanceDto);
			instanceId = addPartnerInstanceRel(partnerInstanceDto, stationId, partnerId);

			// 不同类型合伙人，执行不同的生命周期
			partnerInstanceDto.setId(instanceId);
			partnerInstanceHandler.handleApplySettle(partnerInstanceDto, partnerInstanceDto.getType());

			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.ADD, instanceId);

			// 记录村点状态变化
			sendPartnerInstanceStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.START_SETTLING, partnerInstanceDto);
			return instanceId;
		} catch (Exception e) {
			String error = getErrorMessage("applySettle", JSON.toJSONString(partnerInstanceDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	private Long addPartnerInstanceRel(PartnerInstanceDto partnerInstanceDto, Long stationId, Long partnerId) {
		partnerInstanceDto.setState(PartnerInstanceStateEnum.SETTLING);
		partnerInstanceDto.setApplyTime(new Date());
		partnerInstanceDto.setApplierId(partnerInstanceDto.getOperator());
		partnerInstanceDto.setApplierType(partnerInstanceDto.getOperatorType().getCode());
		partnerInstanceDto.setStationId(stationId);
		partnerInstanceDto.setPartnerId(partnerId);
		partnerInstanceDto.setIsCurrent(PartnerInstanceIsCurrentEnum.Y);
		partnerInstanceDto.setVersion(0L);
		// 当前partner_station_rel.isCurrent = n, 并添加新的当前partner_station_rel
		return partnerInstanceBO.addPartnerStationRel(partnerInstanceDto);
	}

	private Long addPartner(PartnerInstanceDto partnerInstanceDto) {
		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
		partnerDto.copyOperatorDto(partnerInstanceDto);
		partnerDto.setState(PartnerStateEnum.TEMP);
		Long partnerId = partnerBO.addPartner(partnerDto);
		attachementBO.addAttachementBatch(partnerDto.getAttachements(), partnerId, AttachementBizTypeEnum.PARTNER, partnerInstanceDto);
		return partnerId;
	}

	private Long addStation(PartnerInstanceDto partnerInstanceDto) {
		StationDto stationDto = partnerInstanceDto.getStationDto();
		stationDto.setState(StationStateEnum.INVALID);
		stationDto.setStatus(StationStatusEnum.NEW);
		stationDto.copyOperatorDto(partnerInstanceDto);

		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
		if (partnerDto != null) {
			stationDto.setTaobaoNick(partnerDto.getTaobaoNick());
			stationDto.setAlipayAccount(partnerDto.getAlipayAccount());
			stationDto.setTaobaoUserId(partnerDto.getTaobaoUserId());
		}

		// 判断服务站编号是否使用中
		Long stationId = partnerInstanceDto.getStationId();
		checkStationNumDuplicate(stationId, stationDto.getStationNum());
		stationId = stationBO.addStation(stationDto);
		if (partnerInstanceDto.getParentStationId() == null) {
			// TP新建站点: parentStationId = stationId
			partnerInstanceDto.setPartnerId(stationId);
		}
		attachementBO.addAttachementBatch(stationDto.getAttachements(), stationId, AttachementBizTypeEnum.CRIUS_STATION, partnerInstanceDto);
		saveStationFixProtocol(stationDto, stationId);
		return stationId;
	}

	private void syncStationApply(SyncStationApplyEnum type, Long instanceId) {
		switch (type) {
			case ADD:
				syncStationApplyBO.addStationApply(instanceId);
				break;
			case DELETE:
				throw new RuntimeException("delete id not support");
			default:
				syncStationApplyBO.updateStationApply(instanceId, type);
				break;
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void quitPartnerInstance(PartnerInstanceQuitDto partnerInstanceQuitDto) throws AugeServiceException {
		ValidateUtils.validateParam(partnerInstanceQuitDto);
		Long instanceId = partnerInstanceQuitDto.getInstanceId();
		ValidateUtils.notNull(instanceId);
		try {
			PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
			if (rel == null || StringUtils.isEmpty(rel.getType())) {
				if (PartnerInstanceStateEnum.QUIT.getCode().equals(rel.getState())) {
					return;
				} else if (!PartnerInstanceStateEnum.QUITING.getCode().equals(rel.getState())) {
					throw new RuntimeException(CommonExceptionEnum.DATA_UNNORMAL.getDesc());
				}
			}
			partnerInstanceHandler.handleQuit(partnerInstanceQuitDto, PartnerInstanceTypeEnum.valueof(rel.getType()));
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);
		} catch (AugeServiceException augeException) {
			String error = getAugeExceptionErrorMessage("quitPartnerInstance", JSON.toJSONString(partnerInstanceQuitDto),
					augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("quitPartnerInstance", JSON.toJSONString(partnerInstanceQuitDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	@Override
	public Long applyResettle(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void degradePartnerInstance(PartnerInstanceDegradeDto degradeDto) throws AugeServiceException {
		ValidateUtils.validateParam(degradeDto);
		Long instanceId = degradeDto.getInstanceId();
		Long parentTaobaoUserId = degradeDto.getParentTaobaoUserId();
		ValidateUtils.notNull(instanceId);
		ValidateUtils.notNull(parentTaobaoUserId);
		try {
			PartnerInstanceDto rel = partnerInstanceBO.getPartnerInstanceById(instanceId);
			Assert.notNull(rel, "partner instance is null");
			if (!StringUtils.equals(PartnerInstanceTypeEnum.TP.getCode(), rel.getType().getCode())) {
				throw new RuntimeException(PartnerInstanceExceptionEnum.DEGRADE_PARTNER_TYPE_FAIL.getDesc());
			}

			if (!PartnerInstanceStateEnum.canDegradeStateCodeList().contains(rel.getState().getCode())) {
				throw new RuntimeException(PartnerInstanceExceptionEnum.DEGRADE_PARTNER_STATE_FAIL.getDesc());
			}
			int tpaCount = partnerInstanceBO.getActiveTpaByParentStationId(rel.getParentStationId());

			if (tpaCount > 0) {
				throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_PARTNER_HAS_TPA);
			}

			PartnerStationRel parentRel = partnerInstanceBO.getActivePartnerInstance(parentTaobaoUserId);
			if (parentRel == null) {
				throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_PARTNER_TAOBAOUSERID_ERROR);
			}
			// 归属合伙人是否属于服务中状态
			if (!StringUtils.equals(PartnerInstanceStateEnum.SERVICING.getCode(), parentRel.getState())) {
				throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_TARGET_PARTNER_NOT_SERVICING);
			}

			if (!StringUtils.equals(PartnerInstanceTypeEnum.TP.getCode(), parentRel.getType())) {
				throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_TARGET_PARTNER_TYPE_NOT_TP);
			}

			if (parentTaobaoUserId.longValue() == rel.getTaobaoUserId().longValue()) {
				throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_PARTNER_TAOBAOUSERID_SAME);
			}

			// 所归属的合伙人的淘帮手不能大于等于5个
			int parentTpaCount = partnerInstanceBO.getActiveTpaByParentStationId(parentRel.getParentStationId());
			Integer maxChildNum = partnerInstanceExtBO.findPartnerMaxChildNum(parentRel.getId());
			if (parentTpaCount >= maxChildNum) {
				throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_TARGET_PARTNER_HAS_TPA_MAX);
			}

			// 判断是不是归属同一个县
			Station parentStation = stationBO.getStationById(parentRel.getStationId());
			Station station = stationBO.getStationById(rel.getStationId());
			if (parentStation.getApplyOrg().longValue() != station.getApplyOrg().longValue()) {
				throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_PARTNER_ORG_NOT_SAME);
			}
			generalTaskSubmitService.submitDegradePartner(rel, PartnerInstanceConverter.convert(parentRel), degradeDto);
		} catch (AugeServiceException augeException) {
			String error = getAugeExceptionErrorMessage("degradePartnerInstance", JSON.toJSONString(degradeDto), augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("degradePartnerInstance", JSON.toJSONString(degradeDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	// private Long getTpaMax() {
	// AppResource resource = appResourceBO.queryAppResource(TPAMAX_TYPE,
	// TPAMAX_KEY);
	// if (resource != null && !StringUtils.isEmpty(resource.getValue())) {
	// return Long.parseLong(resource.getValue());
	// }
	// return TPAMAX_DEFAULT;
	// }

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void applySettleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto) throws AugeServiceException {
		ValidateUtils.validateParam(settleSuccessDto);
		Long instanceId = settleSuccessDto.getInstanceId();
		ValidateUtils.notNull(instanceId);
		try {
			PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
			Assert.notNull(rel, "partner instance not exists");
			partnerInstanceHandler.handleSettleSuccess(settleSuccessDto, rel);

			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);
		} catch (Exception e) {
			String error = getErrorMessage("applySettleSuccess", JSON.toJSONString(settleSuccessDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void updateSettle(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		ValidateUtils.validateParam(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getStationDto());
		ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
		ValidateUtils.notNull(partnerInstanceDto.getStationId());
		try {
			PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceByStationId(partnerInstanceDto.getStationId());
			Assert.notNull(rel, "partner instance not exists");
			Assert.notNull(rel.getType(), "partner instance type is null");

			boolean canUpdate = partnerInstanceHandler.handleValidateUpdateSettle(rel.getId(),

					PartnerInstanceTypeEnum.valueof(rel.getType()));
			if (!canUpdate) {
				throw new AugeServiceException(CommonExceptionEnum.RECORD_CAN_NOT_UPDATE);
			}
			updateStation(rel.getStationId(), partnerInstanceDto);
			updatePartner(rel.getPartnerId(), partnerInstanceDto);
			partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);

			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_ALL, rel.getId());
		} catch (Exception e) {
			String error = getErrorMessage("updateSettle", JSONObject.toJSONString(partnerInstanceDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}

	}

	private void updatePartner(Long partnerId, PartnerInstanceDto partnerInstanceDto) {
		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
		partnerDto.copyOperatorDto(partnerInstanceDto);
		partnerDto.setId(partnerId);
		partnerBO.updatePartner(partnerDto);
		attachementBO.modifyAttachementBatch(partnerDto.getAttachements(), partnerId, AttachementBizTypeEnum.PARTNER, partnerInstanceDto);
	}

	private void updateStation(Long stationId, PartnerInstanceDto partnerInstanceDto) {
		StationDto stationDto = partnerInstanceDto.getStationDto();
		stationDto.copyOperatorDto(partnerInstanceDto);
		stationDto.setId(stationId);
		// 判断服务站编号是否使用中
		checkStationNumDuplicate(stationId, stationDto.getStationNum());
		stationBO.updateStation(stationDto);
		saveStationFixProtocol(stationDto, stationId);
		attachementBO.modifyAttachementBatch(stationDto.getAttachements(), stationId, AttachementBizTypeEnum.CRIUS_STATION, partnerInstanceDto);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void auditSettleByManager(AuditSettleDto auditSettleDto) throws AugeServiceException {
		ValidateUtils.validateParam(auditSettleDto);
		Long partnerInstanceId = auditSettleDto.getPartnerInstanceId();
		Boolean isAgree = auditSettleDto.getIsAgree();
		ValidateUtils.notNull(partnerInstanceId);
		ValidateUtils.notNull(isAgree);
		try {
			PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(partnerInstanceId, PartnerLifecycleBusinessTypeEnum.SETTLING,
					PartnerLifecycleCurrentStepEnum.PROCESSING);
			Assert.notNull(items, "PartnerLifecycleItems not exists");

			if (isAgree) {
				PartnerLifecycleDto param = new PartnerLifecycleDto();
				param.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_PASS);
				param.setLifecycleId(items.getId());
				param.copyOperatorDto(auditSettleDto);
				partnerLifecycleBO.updateLifecycle(param);
			} else {
				PartnerLifecycleDto param = new PartnerLifecycleDto();
				param.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_NOPASS);
				param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
				param.setLifecycleId(items.getId());
				param.copyOperatorDto(auditSettleDto);
				partnerLifecycleBO.updateLifecycle(param);
				// 合伙人实例入驻失败
				partnerInstanceBO.changeState(partnerInstanceId, PartnerInstanceStateEnum.SETTLING, PartnerInstanceStateEnum.SETTLE_FAIL,
						auditSettleDto.getOperator());
				PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(partnerInstanceId);
				stationBO.changeState(rel.getStationId(), StationStatusEnum.NEW, StationStatusEnum.INVALID, auditSettleDto.getOperator());
			}
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_BASE, partnerInstanceId);
		} catch (AugeServiceException augeException) {
			String error = getAugeExceptionErrorMessage("auditSettleByManager", JSONObject.toJSONString(auditSettleDto),
					augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("auditSettleByManager", JSONObject.toJSONString(auditSettleDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void degradePartnerInstanceSuccess(DegradePartnerInstanceSuccessDto degradePartnerInstanceSuccessDto)
			throws AugeServiceException {

		ValidateUtils.validateParam(degradePartnerInstanceSuccessDto);

		Long instanceId = degradePartnerInstanceSuccessDto.getInstanceId();
		ValidateUtils.notNull(instanceId);
		try {
			PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
			Long parentInstanceId = degradePartnerInstanceSuccessDto.getParentInstanceId();
			ValidateUtils.notNull(parentInstanceId);
			PartnerStationRel parentRel = partnerInstanceBO.findPartnerInstanceById(parentInstanceId);
			Assert.notNull(rel, "partner instance not exists");
			Assert.notNull(rel.getType(), "partner instance type is null");
			Assert.notNull(parentRel, "parent partner instance not exists");
			Assert.notNull(parentRel.getType(), "parent partner instance type is null");

			if (PartnerInstanceStateEnum.CLOSING.getCode().equals(rel.getState())
					|| PartnerInstanceStateEnum.CLOSED.getCode().equals(rel.getState())) {
				// 删除停业申请单
				closeStationApplyBO.deleteCloseStationApply(instanceId, degradePartnerInstanceSuccessDto.getOperator());
			}

			if (PartnerInstanceStateEnum.CLOSING.getCode().equals(rel.getState())) {
				// 删除生命周期表
				PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.CLOSING,
						PartnerLifecycleCurrentStepEnum.PROCESSING);
				PartnerLifecycleDto lifeDto = new PartnerLifecycleDto();
				lifeDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
				lifeDto.setLifecycleId(items.getId());
				lifeDto.copyOperatorDto(degradePartnerInstanceSuccessDto);
				partnerLifecycleBO.updateLifecycle(lifeDto);
			}

			PartnerInstanceDto param = new PartnerInstanceDto();
			param.setId(instanceId);
			param.setBit(-1);
			param.setParentStationId(parentRel.getStationId());
			param.setType(PartnerInstanceTypeEnum.TPA);
			param.setState(PartnerInstanceStateEnum.SERVICING);
			param.copyOperatorDto(degradePartnerInstanceSuccessDto);
			partnerInstanceBO.updatePartnerStationRel(param);

			// 更新station为服务中
			StationDto stationDto = new StationDto();
			stationDto.setStatus(StationStatusEnum.SERVICING);
			stationDto.setId(rel.getStationId());
			stationDto.copyOperatorDto(degradePartnerInstanceSuccessDto);
			stationBO.updateStation(stationDto);

			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);

			// 发送降级成功事件
			PartnerInstanceTypeChangeEvent event = new PartnerInstanceTypeChangeEvent();
			event.setPartnerInstanceId(instanceId);
			event.setTypeChangeEnum(PartnerInstanceTypeChangeEnum.TP_DEGREE_2_TPA);
			event.setTaobaoUserId(rel.getTaobaoUserId());
			event.setStationId(rel.getStationId());
			event.setParentTaobaoUserId(parentRel.getTaobaoUserId());
			event.setOperator(degradePartnerInstanceSuccessDto.getOperator());
			event.setOperatorType(degradePartnerInstanceSuccessDto.getOperatorType());
			EventDispatcherUtil.dispatch(EventConstant.PARTNER_INSTANCE_TYPE_CHANGE_EVENT, event);
		} catch (Exception e) {
			String error = getErrorMessage("degradePartnerInstanceSuccess", JSON.toJSONString(degradePartnerInstanceSuccessDto),
					e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}

	}

	private void dispatchInstStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChange, OperatorDto operator) {
		PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
		PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(stateChange, partnerInstanceDto,
				operator);
		EventDispatcherUtil.dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void evaluatePartnerInstanceLevel(PartnerInstanceLevelDto partnerInstanceLevelDto) throws AugeServiceException {
		//根据taobao_user_id和station_id失效以前的评级is_valid＝'n'
		partnerInstanceLevelBO.invalidatePartnerInstanceLevelBefore(partnerInstanceLevelDto);
		//保存数据库
		partnerInstanceLevelBO.addPartnerInstanceLevel(partnerInstanceLevelDto);
		//发送评级变化事件: 类型为系统评定
		PartnerInstanceLevelChangeEvent event = PartnerInstanceLevelEventConverter.convertLevelChangeEvent(
				PartnerInstanceLevelEvaluateTypeEnum.SYSTEM, partnerInstanceLevelDto);
		EventDispatcherUtil.dispatch(EventConstant.PARTNER_INSTANCE_LEVEL_CHANGE_EVENT, event);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void changeTP(ChangeTPDto changeTPDto) throws AugeServiceException {
		ValidateUtils.notNull(changeTPDto);
		try {
			Long partnerInstanceId = changeTPDto.getPartnerInstanceId();
			Long newParentStationId = changeTPDto.getNewParentStationId();

			PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(changeTPDto.getPartnerInstanceId());
			if (!PartnerInstanceTypeEnum.TPA.equals(partnerInstanceDto.getType())) {
				throw new AugeServiceException("type is not tpa");
			}
			partnerInstanceDto.setParentStationId(newParentStationId);
			partnerInstanceDto.setOperator(changeTPDto.getOperator());
			partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);
			//菜鸟修改淘帮手归属合伙人关系
			Long parentParnterInstanceId = partnerInstanceBO.findPartnerInstanceIdByStationId(newParentStationId);
			SyncModifyBelongTPForTpaDto belongTp = new SyncModifyBelongTPForTpaDto();
			belongTp.setParentPartnerInstanceId(parentParnterInstanceId);
			belongTp.setPartnerInstanceId(partnerInstanceId);
			belongTp.copyOperatorDto(changeTPDto);
			caiNiaoService.updateBelongTPForTpa(belongTp);
			EventDispatcherUtil.dispatch(EventConstant.CHANGE_TP_EVENT, buildChangeTPEvent(changeTPDto));
		} catch (AugeServiceException augeException) {
			String error = getAugeExceptionErrorMessage("changeTP", JSONObject.toJSONString(changeTPDto),
					augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("changeTP", JSONObject.toJSONString(changeTPDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}

	}

	private ChangeTPEvent buildChangeTPEvent(ChangeTPDto changeTPDto) {
		ChangeTPEvent event = new ChangeTPEvent();
		event.setStationId(changeTPDto.getNewParentStationId());
		event.setOperator(changeTPDto.getOperator());
		event.setOperatorOrgId(changeTPDto.getOperatorOrgId());
		event.setOperatorType(changeTPDto.getOperatorType());
		return event;
	}
}
