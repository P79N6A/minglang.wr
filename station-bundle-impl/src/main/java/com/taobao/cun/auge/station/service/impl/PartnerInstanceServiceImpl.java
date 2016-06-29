package com.taobao.cun.auge.station.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.EventConstant;
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
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.convert.QuitStationApplyConverter;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.AuditSettleDto;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.dto.ConfirmCloseDto;
import com.taobao.cun.auge.station.dto.DegradePartnerInstanceSuccessDto;
import com.taobao.cun.auge.station.dto.ForcedCloseDto;
import com.taobao.cun.auge.station.dto.OpenStationDto;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDegradeDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
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
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.enums.CloseStationApplyCloseReasonEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceCloseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceIsCurrentEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleConfirmEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
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
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.auge.station.sync.StationApplySyncBO;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.cun.crius.event.client.EventDispatcher;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * 
 * 合伙人实例服务接口
 * 
 * @author quanzhu.wangqz
 *
 */
@Service("partnerInstanceService")
@HSFProvider(serviceInterface = PartnerInstanceService.class)
public class PartnerInstanceServiceImpl implements PartnerInstanceService {

	private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceService.class);
	public static final String RULE_REGEX = "^[0-9A-Z]+$";
	private static final String TPAMAX_TYPE = "tpl_max";
	private static final String TPAMAX_KEY = "tpl_max_num";
	private static final Long TPAMAX_DEFAULT = 5L;
	
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

	private Long addCommon(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		StationDto stationDto = partnerInstanceDto.getStationDto();
		stationDto.copyOperatorDto(partnerInstanceDto);
		// 判断服务站编号是否使用中
		checkStationNumDuplicate(null, stationDto.getStationNum());

		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
		if (StringUtils.isNotEmpty(partnerDto.getTaobaoNick())) {
			Long taobaoUserId = uicReadAdapter.getTaobaoUserIdByTaobaoNick(partnerDto.getTaobaoNick());
			if (taobaoUserId == null) {
				throw new AugeServiceException(CommonExceptionEnum.TAOBAONICK_ERROR);
			}
			partnerDto.setTaobaoUserId(taobaoUserId);
			partnerInstanceDto.setTaobaoUserId(taobaoUserId);
			// station表历史字段 同步保存
			stationDto.setTaobaoNick(partnerDto.getTaobaoNick());
			stationDto.setTaobaoUserId(taobaoUserId);
		}
		stationDto.setAlipayAccount(partnerDto.getAlipayAccount());

		Long stationId = stationBO.addStation(stationDto);
		attachementBO.addAttachementBatch(stationDto.getAttachements(), stationId, AttachementBizTypeEnum.CRIUS_STATION,
				partnerInstanceDto);
		// 更新固点协议
		saveStationFixProtocol(stationDto, stationId);

		partnerDto.copyOperatorDto(partnerInstanceDto);
		Long partnerId = partnerBO.addPartner(partnerDto);
		attachementBO.addAttachementBatch(partnerDto.getAttachements(), partnerId, AttachementBizTypeEnum.PARTNER, partnerInstanceDto);

		partnerInstanceDto.setStationId(stationId);
		partnerInstanceDto.setPartnerId(partnerId);
		partnerInstanceDto.setIsCurrent(PartnerInstanceIsCurrentEnum.Y);
		partnerInstanceDto.setVersion(0L);
		return partnerInstanceBO.addPartnerStationRel(partnerInstanceDto);
	}

	private void updateCommon(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(partnerInstanceDto.getId());
		Long stationId = rel.getStationId();
		Long partnerId = rel.getPartnerId();

		StationDto stationDto = partnerInstanceDto.getStationDto();
		// 判断服务站编号是否使用中
		checkStationNumDuplicate(stationId, stationDto.getStationNum());

		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();

		if (StringUtils.isNotEmpty(partnerDto.getTaobaoNick())) {
			Long taobaoUserId = uicReadAdapter.getTaobaoUserIdByTaobaoNick(partnerDto.getTaobaoNick());
			if (taobaoUserId == null) {
				throw new AugeServiceException(CommonExceptionEnum.TAOBAONICK_ERROR);
			}
			partnerDto.setTaobaoUserId(taobaoUserId);
			partnerInstanceDto.setTaobaoUserId(taobaoUserId);
			// station表历史字段 同步保存
			stationDto.setTaobaoNick(partnerDto.getTaobaoNick());
			stationDto.setTaobaoUserId(taobaoUserId);
		}
		stationDto.setAlipayAccount(partnerDto.getAlipayAccount());

		stationDto.copyOperatorDto(partnerInstanceDto);
		stationDto.setId(stationId);
		stationBO.updateStation(stationDto);
		// 更新固点协议
		saveStationFixProtocol(stationDto, stationId);
		attachementBO.modifyAttachementBatch(partnerInstanceDto.getStationDto().getAttachements(), stationId,
				AttachementBizTypeEnum.CRIUS_STATION, partnerInstanceDto);

		partnerDto.copyOperatorDto(partnerInstanceDto);
		partnerDto.setId(partnerId);
		partnerBO.updatePartner(partnerInstanceDto.getPartnerDto());
		attachementBO.modifyAttachementBatch(partnerInstanceDto.getPartnerDto().getAttachements(), partnerId,
				AttachementBizTypeEnum.PARTNER, partnerInstanceDto);

		partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Long addTemp(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		ValidateUtils.validateParam(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getStationDto());
		ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
		try {
			setTempCommonInfo(partnerInstanceDto);
			Long instanceId = addCommon(partnerInstanceDto);
			// 同步station_apply
			syncStationApplyBO.addStationApply(instanceId);
			return instanceId;
		} catch (AugeServiceException augeException) {
			String error = getErrorMessage("addTemp", JSONObject.toJSONString(partnerInstanceDto), augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("addTemp", JSONObject.toJSONString(partnerInstanceDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	private void setTempCommonInfo(PartnerInstanceDto partnerInstanceDto) {
		StationDto stationDto = partnerInstanceDto.getStationDto();
		stationDto.setState(StationStateEnum.INVALID);
		stationDto.setStatus(StationStatusEnum.TEMP);
		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
		partnerDto.setState(PartnerStateEnum.TEMP);
		partnerInstanceDto.setState(PartnerInstanceStateEnum.TEMP);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Long updateTemp(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
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
			String error = getErrorMessage("updateTemp", JSONObject.toJSONString(partnerInstanceDto), augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("updateTemp", JSONObject.toJSONString(partnerInstanceDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

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

	private Long addSubmit(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		ValidateUtils.validateParam(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getStationDto());
		ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
		try {
			// Long taobaoUserId =
			validateSettlable(partnerInstanceDto);
			setSubmitCommonInfo(partnerInstanceDto);
			Long instanceId = addCommon(partnerInstanceDto);
			return instanceId;
		} catch (AugeServiceException augeException) {
			String error = getErrorMessage("addSubmit", JSONObject.toJSONString(partnerInstanceDto), augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("addSubmit", JSONObject.toJSONString(partnerInstanceDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	private void setSubmitCommonInfo(PartnerInstanceDto partnerInstanceDto) {
		StationDto stationDto = partnerInstanceDto.getStationDto();
		stationDto.setState(StationStateEnum.INVALID);
		stationDto.setStatus(StationStatusEnum.NEW);
		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
		partnerDto.setState(PartnerStateEnum.TEMP);
		partnerInstanceDto.setState(PartnerInstanceStateEnum.SETTLING);
		partnerInstanceDto.setApplyTime(new Date());
		partnerInstanceDto.setApplierId(partnerInstanceDto.getOperator());
		partnerInstanceDto.setApplierType(partnerInstanceDto.getOperatorType().getCode());
	}

	private static boolean isSpecialStr(String str) {
		Pattern pat = Pattern.compile(RULE_REGEX);
		Matcher mat = pat.matcher(str);
		if (mat.find()) {
			return false;
		} else {
			return true;
		}
	}

	private Long validateSettlable(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		ValidateUtils.notNull(partnerInstanceDto);
		StationDto stationDto = partnerInstanceDto.getStationDto();
		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
		ValidateUtils.notNull(stationDto);

		if (StringUtils.isBlank(stationDto.getName())) {
			throw new AugeServiceException(StationExceptionEnum.STATION_NAME_IS_NULL);
		}
		Address address = stationDto.getAddress();
		if (address == null) {
			throw new AugeServiceException(StationExceptionEnum.STATION_ADDRESS_IS_NULL);
		}
		String stationName = "";
		if (StringUtils.isNotBlank(address.getCountyDetail())) {
			stationName += address.getCountyDetail();
		}
		stationName += stationDto.getName();
		try {
			if (stationName.getBytes("UTF-8").length > 64) {
				throw new AugeServiceException(StationExceptionEnum.CAINIAO_STATION_NAME_TOO_LENGTH);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("validate:", e);
		}

		String stationNum = stationDto.getStationNum();
		if (StringUtils.isEmpty(stationNum)) {

			throw new AugeServiceException(StationExceptionEnum.STATION_NUM_IS_NULL);
		}
		stationNum = stationNum.toUpperCase();
		if (stationNum.length() > 16) {
			throw new AugeServiceException(StationExceptionEnum.STATION_NUM_TOO_LENGTH);
		}

		if (isSpecialStr(stationNum)) {
			throw new AugeServiceException(StationExceptionEnum.STATION_NUM_ILLEGAL);
		}

		if (StringUtils.isBlank(partnerDto.getTaobaoNick())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_TAOBAONICK_IS_NULL);
		}
		if (StringUtils.isBlank(partnerDto.getAlipayAccount())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_ALIPAYACCOUNT_IS_NULL);
		}
		if (StringUtils.isBlank(partnerDto.getIdenNum())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_IDENNUM_IS_NULL);
		}
		if (StringUtils.isBlank(partnerDto.getName())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_NAME_IS_NULL);
		}

		if (partnerDto.getMobile() == null) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_MOBILE_IS_NULL);
		}
		if (!isMobileNO(partnerDto.getMobile())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_MOBILE_CHECK_FAIL);
		}

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
		return paDto.getTaobaoUserId();
	}

	private void checkStationNumDuplicate(Long stationId, String newStationNum) {
		// 判断服务站编号是否使用中
		String oldStationNum = null;
		if (stationId != null) {
			Station oldStation = stationBO.getStationById(stationId);
			oldStationNum = oldStation.getStationNum();
		}
		int count = stationBO.getStationCountByStationNum(newStationNum);
		if (StringUtils.equals(oldStationNum, newStationNum)) {
			if (count > 1) {
				throw new AugeServiceException(StationExceptionEnum.STATION_NUM_IS_DUPLICATE);
			}
		} else {
			if (count > 0) {
				throw new AugeServiceException(StationExceptionEnum.STATION_NUM_IS_DUPLICATE);
			}
		}
	}

	private Long updateSubmit(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		ValidateUtils.validateParam(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getStationDto());
		ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
		ValidateUtils.notNull(partnerInstanceDto.getId());
		try {
			// Long taobaoUserId =
			validateSettlable(partnerInstanceDto);
			setSubmitCommonInfo(partnerInstanceDto);
			updateCommon(partnerInstanceDto);
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_ALL, partnerInstanceDto.getId());
			return partnerInstanceDto.getId();
		} catch (AugeServiceException augeException) {
			String error = getErrorMessage("updateSubmit", JSONObject.toJSONString(partnerInstanceDto), augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("updateSubmit", JSONObject.toJSONString(partnerInstanceDto), e.getMessage());
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
		validateParnterCanUpdateInfo(partnerInstanceUpdateServicingDto.getPartnerDto());
		validateStationCanUpdateInfo(partnerInstanceUpdateServicingDto.getStationDto());
		try {
			Long partnerInstanceId = partnerInstanceUpdateServicingDto.getId();
			PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(partnerInstanceId);
			Long stationId = rel.getStationId();
			Long partnerId = rel.getPartnerId();

			if (partnerInstanceUpdateServicingDto.getPartnerDto() != null) {
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
			if (partnerInstanceUpdateServicingDto.getStationDto() != null) {
				StationUpdateServicingDto sDto = partnerInstanceUpdateServicingDto.getStationDto();

				// 判断服务站编号是否使用中
				checkStationNumDuplicate(stationId, sDto.getStationNum());

				StationDto stationDto = new StationDto();
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
				stationDto.setStationNum(sDto.getStationNum());
				stationDto.copyOperatorDto(partnerInstanceUpdateServicingDto);
				stationBO.updateStation(stationDto);

				// 更新固点协议
				saveStationFixProtocol(stationDto, stationId);
				attachementBO.modifyAttachementBatch(sDto.getAttachements(), stationId, AttachementBizTypeEnum.CRIUS_STATION,
						partnerInstanceUpdateServicingDto);
			}

			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_ALL, partnerInstanceId);

			generalTaskSubmitService.submitUpdateCainiaoStation(partnerInstanceId, partnerInstanceUpdateServicingDto.getOperator());
		} catch (AugeServiceException augeException) {
			String error = getErrorMessage("update", JSONObject.toJSONString(partnerInstanceUpdateServicingDto), augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("update", JSONObject.toJSONString(partnerInstanceUpdateServicingDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	private void validateStationCanUpdateInfo(StationUpdateServicingDto stationDto) {
		if (stationDto == null) {
			return;
		}
		if (StringUtils.isEmpty(stationDto.getName())) {
			throw new AugeServiceException(StationExceptionEnum.STATION_NAME_IS_NULL);
		}
		Address address = stationDto.getAddress();
		if (address == null) {
			throw new AugeServiceException(StationExceptionEnum.STATION_ADDRESS_IS_NULL);
		}
		String stationName = "";
		if (StringUtils.isNotBlank(address.getCountyDetail())) {
			stationName += address.getCountyDetail();
		}
		stationName += stationDto.getName();
		try {
			if (stationName.getBytes("UTF-8").length > 64) {
				throw new AugeServiceException(StationExceptionEnum.CAINIAO_STATION_NAME_TOO_LENGTH);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("validate:", e);
		}

		String stationNum = stationDto.getStationNum();
		if (StringUtils.isEmpty(stationNum)) {

			throw new AugeServiceException(StationExceptionEnum.STATION_NUM_IS_NULL);
		}
		stationNum = stationNum.toUpperCase();
		if (stationNum.length() > 16) {
			throw new AugeServiceException(StationExceptionEnum.STATION_NUM_TOO_LENGTH);
		}

		if (isSpecialStr(stationNum)) {
			throw new AugeServiceException(StationExceptionEnum.STATION_NUM_ILLEGAL);
		}
	}

	private void validateParnterCanUpdateInfo(PartnerUpdateServicingDto partnerDto) {
		if (partnerDto == null) {
			return;
		}
		if (partnerDto.getMobile() != null) {
			if (!isMobileNO(partnerDto.getMobile())) {
				throw new AugeServiceException(PartnerExceptionEnum.PARTNER_MOBILE_CHECK_FAIL);
			}
		}
	}

	private static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((1))\\d{10}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto) throws AugeServiceException {
		ValidateUtils.validateParam(partnerInstanceDeleteDto);
		Long instanceId = partnerInstanceDeleteDto.getInstanceId();
		ValidateUtils.notNull(instanceId);

		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
		if (rel == null || StringUtils.isEmpty(rel.getType())) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}
		partnerInstanceHandler.handleDelete(partnerInstanceDeleteDto, rel);
		//同步删除
		syncStationApplyBO.deleteStationApply(rel.getStationApplyId());
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void signSettledProtocol(Long taobaoUserId, Double waitFrozenMoney, Long version) throws AugeServiceException {
		ValidateUtils.notNull(taobaoUserId);
		ValidateUtils.notNull(waitFrozenMoney);
		try {
			PartnerStationRel psRel = partnerInstanceBO.getPartnerInstanceByTaobaoUserId(taobaoUserId, PartnerInstanceStateEnum.SETTLING);
			ValidateUtils.notNull(psRel);
			Long instanceId = psRel.getId();
			PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.SETTLING,
					PartnerLifecycleCurrentStepEnum.PROCESSING);
			if (items == null) {
				throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
			}
			PartnerLifecycleItemCheckResultEnum checkSettled = PartnerLifecycleRuleParser.parseExecutable(PartnerInstanceTypeEnum.valueof(psRel.getType()), PartnerLifecycleItemCheckEnum.settledProtocol, items);
			if (PartnerLifecycleItemCheckResultEnum.EXECUTED == checkSettled) {
				return;
			}else if (PartnerLifecycleItemCheckResultEnum.NONEXCUTABLE == checkSettled) {
				throw new AugeServiceException(CommonExceptionEnum.RECORD_CAN_NOT_UPDATE);
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
			String error = getErrorMessage("signSettledProtocol", String.valueOf(taobaoUserId), augeException.toString());
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

			if (partnerStationRel == null) {
				throw new AugeServiceException(StationExceptionEnum.SIGN_MANAGE_PROTOCOL_FAIL);
			}
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
			String error = getErrorMessage("signManageProtocol", String.valueOf(taobaoUserId), augeException.toString());
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
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public boolean openStation(OpenStationDto openStationDto) throws AugeServiceException {
		// 参数校验
		BeanValidator.validateWithThrowable(openStationDto);
		if (openStationDto.isImme()) {// 立即开业
			// TODO:检查开业包
			if (!checkKyPackage()) {
				throw new AugeServiceException(StationExceptionEnum.KAYE_PACKAGE_NOT_EXIST);
			}
			partnerInstanceBO.changeState(openStationDto.getPartnerInstanceId(), PartnerInstanceStateEnum.DECORATING,
					PartnerInstanceStateEnum.SERVICING, openStationDto.getOperator());
			partnerInstanceBO.updateOpenDate(openStationDto.getPartnerInstanceId(), openStationDto.getOpenDate(),
					openStationDto.getOperator());

			PartnerInstanceDto piDto = partnerInstanceBO.getPartnerInstanceById(openStationDto.getPartnerInstanceId());
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_BASE, openStationDto.getPartnerInstanceId());

			// 记录村点状态变化
			EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, PartnerInstanceEventConverter
					.convertStateChangeEvent(PartnerInstanceStateChangeEnum.START_SERVICING, piDto, openStationDto));
			// 开业包项目事件
			dispachToServiceEvent(openStationDto, piDto);

		} else {// 定时开业
			partnerInstanceBO.updateOpenDate(openStationDto.getPartnerInstanceId(), openStationDto.getOpenDate(),
					openStationDto.getOperator());
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_BASE, openStationDto.getPartnerInstanceId());
		}

		return true;
	}

	private void dispachToServiceEvent(OpenStationDto openStationDto, PartnerInstanceDto piDto) {
		try {
			PartnerInstanceStateChangeEvent partnerInstanceEvent = new PartnerInstanceStateChangeEvent();
			partnerInstanceEvent.setExecDate(com.taobao.cun.auge.common.utils.DateUtil.format(openStationDto.getOpenDate()));
			partnerInstanceEvent.setOwnOrgId(piDto.getStationDto().getApplyOrg());
			partnerInstanceEvent.setTaobaoUserId(piDto.getTaobaoUserId());
			partnerInstanceEvent.setTaobaoNick(piDto.getPartnerDto().getTaobaoNick());
			partnerInstanceEvent.setStationId(piDto.getStationId());
			partnerInstanceEvent.setStationName(piDto.getStationDto().getStationNum());
			partnerInstanceEvent.setOperator(openStationDto.getOperator());
			String msg = EventDispatcher.getInstance().dispatch("STATION_TO_SERVICE_EVENT", partnerInstanceEvent);
			logger.info("dispachToServiceEvent success partnerInstanceId:" + openStationDto.getPartnerInstanceId() + "msgId:" + msg);
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
			ValidateUtils.notNull(taobaoUserId);

			// 合伙人实例状态校验
			PartnerStationRel partnerInstance = partnerInstanceBO.getPartnerInstanceByTaobaoUserId(taobaoUserId,
					PartnerInstanceStateEnum.SERVICING);
			if (partnerInstance == null) {
				throw new AugeServiceException(PartnerExceptionEnum.NO_RECORD);
			}

			Long instanceId = partnerInstance.getId();
			Long stationId = partnerInstance.getStationId();
			
			String taobaoUserIdStr = String.valueOf(taobaoUserId);
			OperatorDto operatorDto = new OperatorDto();
			operatorDto.setOperator(taobaoUserIdStr);
			operatorDto.setOperatorType(OperatorTypeEnum.HAVANA);

			// FIXME FHH 合伙人主动申请退出时，为什么不校验是否存在淘帮手，非要到审批时再校验

			// 更新合伙人实例状态为停业中
			closingPartnerInstance(partnerInstance, PartnerInstanceCloseTypeEnum.PARTNER_QUIT,operatorDto);

			// 更新村点状态为停业中
			stationBO.changeState(stationId, StationStatusEnum.SERVICING, StationStatusEnum.CLOSING, taobaoUserIdStr);

			// 添加停业生命周期记录
			addClosingLifecycle(operatorDto, partnerInstance,PartnerInstanceCloseTypeEnum.PARTNER_QUIT);

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
			EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);

		} catch (AugeServiceException augeException) {
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("applyCloseByPartner", String.valueOf(taobaoUserId), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	/**
	 * 添加停业协议
	 * 
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
	 * 
	 * @param partnerInstance
	 * @param closeType
	 * @param operatorDto
	 */
	private void closingPartnerInstance(PartnerStationRel partnerInstance, PartnerInstanceCloseTypeEnum closeType,OperatorDto operatorDto) {
		PartnerInstanceDto partnerInstanceDto = new PartnerInstanceDto();

		partnerInstanceDto.setId(partnerInstance.getId());
		partnerInstanceDto.setState(PartnerInstanceStateEnum.CLOSING);
		partnerInstanceDto.setCloseType(closeType);
		partnerInstanceDto.copyOperatorDto(operatorDto);
		partnerInstanceDto.setVersion(partnerInstance.getVersion());
		partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);
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
			if (partnerInstance == null
					|| !PartnerInstanceStateEnum.CLOSING.getCode().equals(partnerInstance.getState())) {
				throw new AugeServiceException(PartnerExceptionEnum.NO_RECORD);
			}

			Long stationId = partnerInstance.getStationId();

			// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
			PartnerInstanceTypeEnum partnerType = PartnerInstanceTypeEnum.valueof(partnerInstance.getType());
			partnerInstanceHandler.validateExistValidChildren(partnerType, instanceId);

			Long lifecycleId = partnerLifecycleBO.getLifecycleItemsId(instanceId,
					PartnerLifecycleBusinessTypeEnum.CLOSING, PartnerLifecycleCurrentStepEnum.PROCESSING);
			PartnerLifecycleDto partnerLifecycle = new PartnerLifecycleDto();
			partnerLifecycle.setLifecycleId(lifecycleId);
			partnerLifecycle.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
			partnerLifecycle.copyOperatorDto(confirmCloseDto);

			if (isAgree) {
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
				partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSING,
						PartnerInstanceStateEnum.SERVICING, employeeId);

				// 更新村点状态，服务中
				stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.SERVICING, employeeId);

				// 更新生命周期，拒绝
				partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.CANCEL);
				partnerLifecycleBO.updateLifecycle(partnerLifecycle);

				// 删除停业协议
				partnerProtocolRelBO.cancelProtocol(partnerInstance.getTaobaoUserId(),
						ProtocolTypeEnum.PARTNER_QUIT_PRO, instanceId,
						PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE, employeeId);

				// 删除停业申请单
				closeStationApplyBO.deleteCloseStationApply(instanceId, employeeId);

				// 同步station_apply
				syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);

				// 发出合伙人实例状态变更事件
				dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.CLOSING_REFUSED,
						confirmCloseDto);
			}
		} catch (AugeServiceException augeException) {
			throw augeException;
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
			Long operatorOrgId = forcedCloseDto.getOperatorOrgId();
			if (null == operatorOrgId || 0l == operatorOrgId) {
				logger.error("operatorOrgId not null");
				throw new AugeServiceException("operatorOrgId not null");
			}

			Long instanceId = forcedCloseDto.getInstanceId();
			PartnerStationRel partnerStationRel = partnerInstanceBO.findPartnerInstanceById(instanceId);
			Long stationId = partnerStationRel.getStationId();
			PartnerInstanceTypeEnum partnerType = PartnerInstanceTypeEnum.valueof(partnerStationRel.getType());

			// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
			partnerInstanceHandler.validateExistValidChildren(partnerType, instanceId);

			// 合伙人实例停业中,退出类型为强制清退
			closingPartnerInstance(partnerStationRel, PartnerInstanceCloseTypeEnum.WORKER_QUIT, forcedCloseDto);

			// 村点停业中
			stationBO.changeState(stationId, StationStatusEnum.SERVICING, StationStatusEnum.CLOSING,
					forcedCloseDto.getOperator());

			// 添加停业生命周期记录
			addClosingLifecycle(forcedCloseDto, partnerStationRel, PartnerInstanceCloseTypeEnum.WORKER_QUIT);

			// 新增停业申请单
			CloseStationApplyDto closeStationApplyDto = new CloseStationApplyDto();
			closeStationApplyDto.setCloseReason(forcedCloseDto.getReason());
			closeStationApplyDto.setOtherReason(forcedCloseDto.getRemarks());
			closeStationApplyDto.setPartnerInstanceId(instanceId);
			closeStationApplyDto.setType(PartnerInstanceCloseTypeEnum.WORKER_QUIT);
			closeStationApplyDto.setOperator(forcedCloseDto.getOperator());
			closeStationApplyDto.setOperatorOrgId(forcedCloseDto.getOperatorOrgId());
			closeStationApplyDto.setOperatorType(forcedCloseDto.getOperatorType());
			closeStationApplyBO.addCloseStationApply(closeStationApplyDto);

			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);

			// 通过事件，定时钟，启动停业流程
			PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(
					PartnerInstanceStateChangeEnum.START_CLOSING, partnerInstanceBO.getPartnerInstanceById(instanceId),
					forcedCloseDto);

			event.setRemark(CloseStationApplyCloseReasonEnum.OTHER.equals(forcedCloseDto.getReason())
					? forcedCloseDto.getRemarks() : forcedCloseDto.getReason().getDesc());

			EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
			// 失效tair
		} catch (AugeServiceException augeException) {
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("applyCloseByManager",
					"强制停业失败。ForcedCloseDto =" + JSON.toJSONString(forcedCloseDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	/**
	 * 添加停业生命周期
	 * 
	 * @param operatorDto
	 * @param partnerStationRel
	 * @param closeType
	 */
	private void addClosingLifecycle(OperatorDto operatorDto, PartnerStationRel partnerStationRel,
			PartnerInstanceCloseTypeEnum closeType) {
		PartnerLifecycleDto partnerLifecycle = new PartnerLifecycleDto();
		
		partnerLifecycle.setPartnerInstanceId(partnerStationRel.getId());
		partnerLifecycle.setPartnerType(PartnerInstanceTypeEnum.valueof(partnerStationRel.getType()));
		partnerLifecycle.setBusinessType(PartnerLifecycleBusinessTypeEnum.CLOSING);
		
		if (PartnerInstanceCloseTypeEnum.WORKER_QUIT.equals(closeType)) {
			partnerLifecycle.setRoleApprove(PartnerLifecycleRoleApproveEnum.TO_START);
		} else if (PartnerInstanceCloseTypeEnum.PARTNER_QUIT.equals(closeType)) {
			partnerLifecycle.setQuitProtocol(PartnerLifecycleQuitProtocolEnum.SIGNED);
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
			validateApplyQuitPreCondition(instance);

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
		} catch (AugeServiceException augeException) {
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("applyQuitByManager", "退出失败。QuitStationApplyDto =" + JSON.toJSONString(quitDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	/**
	 * 校验申请退出的前置条件
	 * 
	 * @param instance
	 * @throws AugeServiceException
	 */
	private void validateApplyQuitPreCondition(PartnerStationRel instance) throws AugeServiceException {
		Long instanceId = instance.getId();
		// 校验是否已经存在退出申请单
		QuitStationApply quitStationApply = quitStationApplyBO.findQuitStationApply(instanceId);
		if (quitStationApply != null) {
			logger.error("Quit station apply already exist");
			throw new AugeServiceException(StationExceptionEnum.QUIT_STATION_APPLY_EXIST);
		}
		
		// 校验是否存在未结束的订单
		Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
		tradeAdapter.validateNoEndTradeOrders(partner.getTaobaoUserId(), instance.getServiceEndTime());

		// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
		partnerInstanceHandler.validateExistValidChildren(PartnerInstanceTypeEnum.valueof(instance.getType()), instanceId);
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
		return "";
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Long applySettle(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		ValidateUtils.notNull(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getType());
		Long instanceId = partnerInstanceDto.getId();
		if (instanceId == null) {
			// 新增入驻
			instanceId = addSubmit(partnerInstanceDto);

			// 不同类型合伙人，执行不同的生命周期
			partnerInstanceDto.setId(instanceId);
			partnerInstanceHandler.handleApplySettle(partnerInstanceDto, partnerInstanceDto.getType());

			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.ADD,instanceId);
		} else {
			// 暂存后，修改入驻
			updateSubmit(partnerInstanceDto);
			// 不同类型合伙人，执行不同的生命周期
			partnerInstanceHandler.handleApplySettle(partnerInstanceDto, partnerInstanceDto.getType());
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instanceId);
		}
		return instanceId;

	}

	private void syncStationApply(SyncStationApplyEnum type, Long instanceId) {
		syncStationApplyBO.updateStationApply(instanceId, type);
		// EventDispatcher.getInstance().dispatch(EventConstant.CUNTAO_STATION_APPLY_SYNC_EVENT,
		// new StationApplySyncEvent(type, instanceId));
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void quitPartnerInstance(PartnerInstanceQuitDto partnerInstanceQuitDto) throws AugeServiceException {
		ValidateUtils.notNull(partnerInstanceQuitDto);
		Long instanceId = partnerInstanceQuitDto.getInstanceId();
		ValidateUtils.notNull(instanceId);

		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
		if (rel == null || StringUtils.isEmpty(rel.getType())) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}
		partnerInstanceHandler.handleQuit(partnerInstanceQuitDto, PartnerInstanceTypeEnum.valueof(rel.getType()));
		// 同步station_apply
		syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);
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
		PartnerInstanceDto rel = partnerInstanceBO.getPartnerInstanceById(instanceId);
		if (rel == null) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}
		if (!StringUtils.equals(PartnerInstanceTypeEnum.TP.getCode(), rel.getType().getCode())) {
			throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_PARTNER_TYPE_FAIL);
		}

		if (!PartnerInstanceStateEnum.canDegradeStateCodeList().contains(rel.getState().getCode())) {
			throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_PARTNER_STATE_FAIL);
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
		if (parentTpaCount >= getTpaMax()) {
			throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_TARGET_PARTNER_HAS_TPA_MAX);
		}

		// 判断是不是归属同一个县
		Station parentStation = stationBO.getStationById(parentRel.getStationId());
		Station station = stationBO.getStationById(rel.getStationId());
		if (parentStation.getApplyOrg().longValue() != station.getApplyOrg().longValue()) {
			throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_PARTNER_ORG_NOT_SAME);
		}
		generalTaskSubmitService.submitDegradePartner(rel, PartnerInstanceConverter.convert(parentRel), degradeDto.getOperator());
	}

	private Long getTpaMax() {
		AppResource resource = appResourceBO.queryAppResource(TPAMAX_TYPE, TPAMAX_KEY);
		if (resource != null && !StringUtils.isEmpty(resource.getValue())) {
			return Long.parseLong(resource.getValue());
		}
		return TPAMAX_DEFAULT;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void applySettleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto) throws AugeServiceException {
		ValidateUtils.validateParam(settleSuccessDto);
		Long instanceId = settleSuccessDto.getInstanceId();
		ValidateUtils.notNull(instanceId);

		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
		if (rel == null || StringUtils.isEmpty(rel.getType())) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}
		partnerInstanceHandler.handleSettleSuccess(settleSuccessDto, rel);

		// 同步station_apply
		syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void updateSettle(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		ValidateUtils.validateParam(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getStationDto());
		ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
		Long instanceId = partnerInstanceDto.getId();
		ValidateUtils.notNull(instanceId);
		try {
			PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
			if (rel == null || StringUtils.isEmpty(rel.getType())) {
				throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
			}
			boolean canUpdate = partnerInstanceHandler.handleValidateUpdateSettle(instanceId,
					PartnerInstanceTypeEnum.valueof(rel.getType()));
			if (!canUpdate) {
				throw new AugeServiceException(CommonExceptionEnum.RECORD_CAN_NOT_UPDATE);
			}
			updateCommon(partnerInstanceDto);
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instanceId);
		} catch (AugeServiceException augeException) {
			String error = getErrorMessage("updateSettle", JSONObject.toJSONString(partnerInstanceDto), augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("updateSettle", JSONObject.toJSONString(partnerInstanceDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}

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
			if (items == null) {
				String error = getErrorMessage("auditSettleByManager", JSONObject.toJSONString(auditSettleDto), "SETTLING items is null");
				logger.error(error);
				throw new AugeServiceException(CommonExceptionEnum.DATA_UNNORMAL);
			}

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
			}
		} catch (AugeServiceException augeException) {
			String error = getErrorMessage("auditSettleByManager", JSONObject.toJSONString(auditSettleDto), augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("auditSettleByManager", JSONObject.toJSONString(auditSettleDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	@Override
	public void degradePartnerInstanceSuccess(DegradePartnerInstanceSuccessDto degradePartnerInstanceSuccessDto)
			throws AugeServiceException {

		ValidateUtils.validateParam(degradePartnerInstanceSuccessDto);

		Long instanceId = degradePartnerInstanceSuccessDto.getInstanceId();
		ValidateUtils.notNull(instanceId);
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
		Long parentInstanceId = degradePartnerInstanceSuccessDto.getParentInstanceId();
		ValidateUtils.notNull(parentInstanceId);
		PartnerStationRel parentRel = partnerInstanceBO.findPartnerInstanceById(parentInstanceId);
		if (null == rel || StringUtils.isEmpty(rel.getType()) || parentRel == null || StringUtils.isEmpty(parentRel.getType())) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}

		PartnerInstanceDto param = new PartnerInstanceDto();
		param.setId(instanceId);
		param.setBit(-1);
		param.setParentStationId(parentRel.getStationId());
		param.setType(PartnerInstanceTypeEnum.TPA);
		param.setState(PartnerInstanceStateEnum.SERVICING);
		param.copyOperatorDto(degradePartnerInstanceSuccessDto);
		partnerInstanceBO.updatePartnerStationRel(param);

		// 发送降级成功事件
		PartnerInstanceTypeChangeEvent event = new PartnerInstanceTypeChangeEvent();
		event.setPartnerInstanceId(instanceId);
		event.setTypeChangeEnum(PartnerInstanceTypeChangeEnum.TP_DEGREE_2_TPA);
		event.setTaobaoUserId(rel.getTaobaoUserId());
		event.setStationId(rel.getStationId());
		event.setParentTaobaoUserId(parentRel.getTaobaoUserId());
		event.setOperator(degradePartnerInstanceSuccessDto.getOperator());
		event.setOperatorType(degradePartnerInstanceSuccessDto.getOperatorType());
		EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_TYPE_CHANGE_EVENT, event);

	}

	private void dispatchInstStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChange, OperatorDto operator) {
		PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
		PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(stateChange, partnerInstanceDto,
				operator);
		EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
	}
}
