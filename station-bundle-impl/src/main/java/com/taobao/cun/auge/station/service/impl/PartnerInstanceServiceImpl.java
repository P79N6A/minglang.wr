package com.taobao.cun.auge.station.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.StationApplySyncEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.station.adapter.CaiNiaoAdapter;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.adapter.PaymentAccountQueryAdapter;
import com.taobao.cun.auge.station.adapter.TradeAdapter;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.bo.AttachementBO;
import com.taobao.cun.auge.station.bo.CloseStationApplyBO;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.bo.ProtocolBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.convert.QuitStationApplyConverter;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.dto.ConfirmCloseDto;
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
import com.taobao.cun.auge.station.dto.SyncModifyCainiaoStationDto;
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
import com.taobao.cun.auge.station.enums.PartnerLifecycleQuitProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSettledProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.enums.StationAreaTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.enums.StationlLogisticsStateEnum;
import com.taobao.cun.auge.station.enums.TaskBusinessTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.PartnerInstanceExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.cun.chronus.dto.GeneralTaskDto;
import com.taobao.cun.chronus.service.TaskExecuteService;
import com.taobao.cun.crius.event.client.EventDispatcher;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * 
 * 合伙人实例服务接口
 * 
 * @author quanzhu.wangqz
 *
 */

@HSFProvider(serviceInterface = PartnerInstanceService.class,serviceVersion="1.0.0.daily.fhh")
public class PartnerInstanceServiceImpl implements PartnerInstanceService {

	private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceService.class);
	public static final String RULE_REGEX = "^[0-9A-Z]+$";
	private static final String TPAMAX_TYPE = "tpl_max";
	private static final String TPAMAX_KEY = "tpl_max_num";
	private static final Long TPAMAX_DEFAULT = 5L;

	@Autowired
	ProtocolBO protocolBO;

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
	StationApplyBO stationApplyBO;

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
	TaskExecuteService taskExecuteService;
	
	@Autowired
	AppResourceBO appResourceBO;
	
	@Autowired
	CaiNiaoAdapter caiNiaoAdapter;
	
	@Autowired
	CuntaoCainiaoStationRelBO cuntaoCainiaoStationRelBO;

	private Long addCommon(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		StationDto stationDto = partnerInstanceDto.getStationDto();
		stationDto.copyOperatorDto(partnerInstanceDto);
		// 判断服务站编号是否使用中
		checkStationNumDuplicate(null, stationDto.getStationNum());
		
		Long stationId = stationBO.addStation(stationDto);
		attachementBO.addAttachementBatch(stationDto.getAttachements(), stationId, AttachementBizTypeEnum.CRIUS_STATION,
				partnerInstanceDto.getOperator());
		// 更新固点协议
		saveStationFixProtocol(stationDto, stationId);

		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
		if (StringUtils.isNotEmpty(partnerDto.getTaobaoNick())) {
			Long taobaoUserId = uicReadAdapter.getTaobaoUserIdByTaobaoNick(partnerDto.getTaobaoNick());
			if (taobaoUserId == null) {
				throw new AugeServiceException(CommonExceptionEnum.TAOBAONICK_ERROR);
			}
			partnerDto.setTaobaoUserId(taobaoUserId);
		}
		partnerDto.copyOperatorDto(partnerInstanceDto);
		Long partnerId = partnerBO.addPartner(partnerDto);
		attachementBO.addAttachementBatch(partnerDto.getAttachements(), partnerId, AttachementBizTypeEnum.PARTNER,
				partnerInstanceDto.getOperator());

		partnerInstanceDto.setStationId(stationId);
		partnerInstanceDto.setPartnerId(partnerId);
		partnerInstanceDto.setIsCurrent(PartnerInstanceIsCurrentEnum.Y);
		return partnerInstanceBO.addPartnerStationRel(partnerInstanceDto);
	}
	

	private void updateCommon(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(partnerInstanceDto.getId());
		Long stationId = rel.getStationId();
		Long partnerId = rel.getPartnerId();

		StationDto stationDto = partnerInstanceDto.getStationDto();
		// 判断服务站编号是否使用中
		checkStationNumDuplicate(stationId, stationDto.getStationNum());

		stationDto.copyOperatorDto(partnerInstanceDto);
		stationDto.setId(stationId);
		stationBO.updateStation(stationDto);
		// 更新固点协议
		saveStationFixProtocol(stationDto, stationId);
		attachementBO.modifyAttachementBatch(partnerInstanceDto.getStationDto().getAttachements(), stationId,
				AttachementBizTypeEnum.CRIUS_STATION, partnerInstanceDto.getOperator());

		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
		if (StringUtils.isNotEmpty(partnerDto.getTaobaoNick())) {
			Long taobaoUserId = uicReadAdapter.getTaobaoUserIdByTaobaoNick(partnerDto.getTaobaoNick());
			if (taobaoUserId == null) {
				throw new AugeServiceException(CommonExceptionEnum.TAOBAONICK_ERROR);
			}
			partnerDto.setTaobaoUserId(taobaoUserId);
		}
		
		partnerDto.copyOperatorDto(partnerInstanceDto);
		partnerDto.setId(partnerId);
		partnerBO.updatePartner(partnerInstanceDto.getPartnerDto());
		attachementBO.modifyAttachementBatch(partnerInstanceDto.getStationDto().getAttachements(), partnerId,
				AttachementBizTypeEnum.PARTNER, partnerInstanceDto.getOperator());

		partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);
	}

	@Override
	public Long saveTemp(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		ValidateUtils.validateParam(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getStationDto());
		ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
		try {
			Long instanceId = partnerInstanceDto.getId();
			if (instanceId == null) {// 新增
				StationDto stationDto = partnerInstanceDto.getStationDto();
				stationDto.setState(StationStateEnum.INVALID);
				stationDto.setStatus(StationStatusEnum.TEMP);

				PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
				partnerDto.setState(PartnerStateEnum.TEMP);

				partnerInstanceDto.setState(PartnerInstanceStateEnum.TEMP);

				instanceId = addCommon(partnerInstanceDto);

				// 同步station_apply
				syncStationApply(SyncStationApplyEnum.ADD, instanceId);
			} else {// 修改
				updateCommon(partnerInstanceDto);

				// 同步station_apply
				syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instanceId);
			}
			return instanceId;
		} catch (AugeServiceException augeException) {
			String error = getErrorMessage("saveTemp", JSONObject.toJSONString(partnerInstanceDto), augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("saveTemp", JSONObject.toJSONString(partnerInstanceDto), e.getMessage());
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
				fixPro.copyOperatorDto(stationDto);
				partnerProtocolRelBO.addPartnerProtocolRel(fixPro);
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
			Long taobaoUserId = validateSettlable(partnerInstanceDto);
			StationDto stationDto = partnerInstanceDto.getStationDto();
			stationDto.setState(StationStateEnum.INVALID);
			stationDto.setStatus(StationStatusEnum.NEW);

			PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
			partnerDto.setState(PartnerStateEnum.TEMP);
			partnerDto.setTaobaoUserId(taobaoUserId);

			partnerInstanceDto.setState(PartnerInstanceStateEnum.SETTLING);
			Long instanceId = addCommon(partnerInstanceDto);

			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.ADD, instanceId);

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

	public static boolean isSpecialStr(String str) {
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
			Long taobaoUserId = validateSettlable(partnerInstanceDto);

			StationDto stationDto = partnerInstanceDto.getStationDto();
			stationDto.setStatus(StationStatusEnum.NEW);

			PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
			partnerDto.setTaobaoUserId(taobaoUserId);

			partnerInstanceDto.setState(PartnerInstanceStateEnum.SETTLING);

			updateCommon(partnerInstanceDto);
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
						partnerInstanceUpdateServicingDto.getOperator());
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

				// 更新固点协议
				saveStationFixProtocol(stationDto, stationId);
				attachementBO.modifyAttachementBatch(sDto.getAttachements(), stationId, AttachementBizTypeEnum.CRIUS_STATION,
						partnerInstanceUpdateServicingDto.getOperator());
			}
			syncUpdateCainiaoStation(partnerInstanceId, partnerInstanceUpdateServicingDto.getOperator());
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

	private void syncUpdateCainiaoStation(Long instanceId, String operatorId) {
		try {
			GeneralTaskDto cainiaoTaskVo = new GeneralTaskDto();
			cainiaoTaskVo.setBusinessNo(String.valueOf(instanceId));
			cainiaoTaskVo.setBeanName("caiNiaoService");
			cainiaoTaskVo.setMethodName("updateCainiaoStation");
			cainiaoTaskVo.setBusinessStepNo(1l);
			cainiaoTaskVo.setBusinessType(TaskBusinessTypeEnum.STATION_QUITE_CONFIRM.getCode());
			cainiaoTaskVo.setBusinessStepDesc("修改物流站点");
			cainiaoTaskVo.setOperator(operatorId);

			SyncModifyCainiaoStationDto syncModifyCainiaoStationDto = new SyncModifyCainiaoStationDto();
			syncModifyCainiaoStationDto.setPartnerInstanceId(Long.valueOf(instanceId));

			cainiaoTaskVo.setParameter(syncModifyCainiaoStationDto);

			// 提交任务
			taskExecuteService.submitTask(cainiaoTaskVo);
		} catch (Exception e) {
			logger.error("Failed to submit syncUpdateCainiaoStation task. instanceId=" + instanceId + " operatorId = " + operatorId, e);
		}
	}

	public void validateStationCanUpdateInfo(StationUpdateServicingDto stationDto) {
		if (stationDto == null) {
			return;
		}
		if (StringUtils.isNotBlank(stationDto.getName())) {
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

	public void validateParnterCanUpdateInfo(PartnerUpdateServicingDto partnerDto) {
		if (partnerDto == null) {
			return;
		}
		if (partnerDto.getMobile() != null) {
			if (!isMobileNO(partnerDto.getMobile())) {
				throw new AugeServiceException(PartnerExceptionEnum.PARTNER_MOBILE_CHECK_FAIL);
			}
		}
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((1))\\d{10}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

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
	}

	@Override
	public void signSettledProtocol(Long taobaoUserId, Double waitFrozenMoney, Long version) throws AugeServiceException {
		ValidateUtils.notNull(taobaoUserId);
		ValidateUtils.notNull(waitFrozenMoney);
		try {
			Long instanceId = partnerInstanceBO.getInstanceIdByTaobaoUserId(taobaoUserId, PartnerInstanceStateEnum.SETTLING);
			partnerProtocolRelBO.signProtocol(taobaoUserId, ProtocolTypeEnum.SETTLE_PRO, instanceId,
					PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);

			addWaitFrozenMoney(instanceId, taobaoUserId, waitFrozenMoney);

			PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.SETTLING,
					PartnerLifecycleCurrentStepEnum.SETTLED_PROTOCOL);
			if (items != null) {
				PartnerLifecycleDto param = new PartnerLifecycleDto();
				param.setSettledProtocol(PartnerLifecycleSettledProtocolEnum.SIGNED);
				param.setBond(PartnerLifecycleBondEnum.WAIT_FROZEN);
				param.setCurrentStep(PartnerLifecycleCurrentStepEnum.BOND);
				param.setLifecycleId(items.getId());
				partnerLifecycleBO.updateLifecycle(param);
			}

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
			throw new AugeServiceException(StationExceptionEnum.SYSTEM_ERROR);
		}
	}

	private void addWaitFrozenMoney(Long instanceId, Long taobaoUserId, Double waitFrozenMoney) {
		AccountMoneyDto accountMoneyDto = new AccountMoneyDto();
		accountMoneyDto.setMoney(BigDecimal.valueOf(waitFrozenMoney));
		accountMoneyDto.setOperator(String.valueOf(taobaoUserId));
		accountMoneyDto.setObjectId(instanceId);
		accountMoneyDto.setState(AccountMoneyStateEnum.WAIT_FROZEN);
		accountMoneyDto.setTaobaoUserId(String.valueOf(taobaoUserId));
		accountMoneyDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
		accountMoneyDto.setType(AccountMoneyTypeEnum.PARTNER_BOND);
		accountMoneyBO.addAccountMoney(accountMoneyDto);
	}

	@Override
	public void signManageProtocol(Long taobaoUserId) throws AugeServiceException {
		ValidateUtils.notNull(taobaoUserId);
		try {
			PartnerStationRel partnerStationRel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);

			if (partnerStationRel == null) {
				throw new AugeServiceException(StationExceptionEnum.SIGN_MANAGE_PROTOCOL_FAIL);
			}
			partnerProtocolRelBO.signProtocol(taobaoUserId, ProtocolTypeEnum.MANAGE_PRO, partnerStationRel.getId(),
					PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);

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

	@Override
	public boolean freezeBond(Long taobaoUserId, Double frozenMoney) throws AugeServiceException {
		PartnerStationRel instance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		PartnerLifecycleItems settleItems = partnerLifecycleBO.getLifecycleItems(instance.getId(),
				PartnerLifecycleBusinessTypeEnum.SETTLING, PartnerLifecycleCurrentStepEnum.BOND);
		AccountMoneyDto bondMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
				AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instance.getId());
		if (!PartnerInstanceStateEnum.SETTLING.getCode().equals(instance.getState()) || null == settleItems || null == bondMoney
				|| !AccountMoneyStateEnum.WAIT_FROZEN.getCode().equals(bondMoney.getState())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_STATE_NOT_APPLICABLE);
		}

		// 修改什么周期表
		PartnerLifecycleDto lifecycleUpdateDto = new PartnerLifecycleDto();
		lifecycleUpdateDto.setLifecycleId(settleItems.getId());
		lifecycleUpdateDto.setBond(PartnerLifecycleBondEnum.HAS_FROZEN);
		lifecycleUpdateDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
		partnerLifecycleBO.updateLifecycle(lifecycleUpdateDto);

		// 修改保证金冻结状态
		AccountMoneyDto accountMoneyUpdateDto = new AccountMoneyDto();
		accountMoneyUpdateDto.setObjectId(bondMoney.getObjectId());
		accountMoneyUpdateDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
		accountMoneyUpdateDto.setFrozenTime(new Date());
		accountMoneyUpdateDto.setState(AccountMoneyStateEnum.HAS_FROZEN);
		accountMoneyBO.updateAccountMoneyByObjectId(accountMoneyUpdateDto);

		Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
		generalTaskSubmitService.submitFreezeBondTasks(PartnerInstanceConverter.convert(instance, null, partner));

		// 流转日志, 合伙人入驻
		// bulidRecordEventForPartnerEnter(stationApplyDetailDto);

		// 同步station_apply
		syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instance.getId());
		return true;
	}

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
			// 记录村点状态变化
			EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
					PartnerInstanceEventConverter.convert(PartnerInstanceStateChangeEnum.START_SERVICING,
							partnerInstanceBO.getPartnerInstanceById(openStationDto.getPartnerInstanceId()), openStationDto));
		} else {// 定时开业
			partnerInstanceBO.updateOpenDate(openStationDto.getPartnerInstanceId(), openStationDto.getOpenDate(),
					openStationDto.getOperator());
		}
		// 同步station_apply
		syncStationApply(SyncStationApplyEnum.UPDATE_BASE, openStationDto.getPartnerInstanceId());

		return false;
	}

	@Override
	public boolean checkKyPackage() {
		// TODO:检查开业包
		return true;
	}

	@Override
	public boolean applyCloseByPartner(Long taobaoUserId) throws AugeServiceException {
		ValidateUtils.notNull(taobaoUserId);
		PartnerStationRel partnerInstance = partnerInstanceBO.getPartnerInstanceByTaobaoUserId(taobaoUserId,
				PartnerInstanceStateEnum.SERVICING);
		if (partnerInstance == null) {
			throw new AugeServiceException(PartnerExceptionEnum.NO_RECORD);
		}
		OperatorDto operatorDto = new OperatorDto();
		operatorDto.setOperator(String.valueOf(taobaoUserId));
		operatorDto.setOperatorType(OperatorTypeEnum.HAVANA);

		// FIXME FHH 合伙人主动申请退出时，为什么不校验是否存在淘帮手，非要到审批时再校验

		// 更新合伙人实例状态为停业中
		PartnerInstanceDto partnerInstanceDto = new PartnerInstanceDto();
		partnerInstanceDto.setId(partnerInstance.getId());
		partnerInstanceDto.setState(PartnerInstanceStateEnum.CLOSING);
		partnerInstanceDto.setCloseType(PartnerInstanceCloseTypeEnum.PARTNER_QUIT);
		partnerInstanceDto.copyOperatorDto(operatorDto);
		partnerInstanceDto.setVersion(partnerInstance.getVersion());
		partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);

		// 更新村点状态为停业中
		stationBO.changeState(partnerInstance.getStationId(), StationStatusEnum.SERVICING, StationStatusEnum.CLOSING,
				String.valueOf(taobaoUserId));

		// 插入生命周期扩展表
		PartnerLifecycleDto partnerLifecycle = new PartnerLifecycleDto();
		partnerLifecycle.setPartnerType(PartnerInstanceTypeEnum.valueof(partnerInstance.getType()));
		partnerLifecycle.setBusinessType(PartnerLifecycleBusinessTypeEnum.CLOSING);
		partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.WAIT_CONFIRM);
		partnerLifecycle.setQuitProtocol(PartnerLifecycleQuitProtocolEnum.SIGNED);
		partnerLifecycle.setCurrentStep(PartnerLifecycleCurrentStepEnum.CONFIRM);
		partnerLifecycle.setPartnerInstanceId(partnerInstance.getId());
		partnerLifecycle.copyOperatorDto(operatorDto);
		partnerLifecycleBO.addLifecycle(partnerLifecycle);

		// TDODO:村拍档时候要插入停业协议
		PartnerProtocolRelDto proRelDto = new PartnerProtocolRelDto();
		Date quitProDate = new Date();
		proRelDto.setConfirmTime(quitProDate);
		proRelDto.setObjectId(partnerInstance.getId());
		proRelDto.setProtocolTypeEnum(ProtocolTypeEnum.PARTNER_QUIT_PRO);
		proRelDto.setStartTime(quitProDate);
		proRelDto.setTaobaoUserId(taobaoUserId);
		proRelDto.setTargetType(PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
		proRelDto.copyOperatorDto(operatorDto);
		partnerProtocolRelBO.addPartnerProtocolRel(proRelDto);

		// 新增停业申请
		CloseStationApplyDto closeStationApplyDto = new CloseStationApplyDto();
		closeStationApplyDto.setPartnerInstanceId(partnerInstance.getId());
		closeStationApplyDto.setType(PartnerInstanceCloseTypeEnum.PARTNER_QUIT);
		closeStationApplyDto.setOperator(operatorDto.getOperator());
		closeStationApplyDto.setOperatorOrgId(operatorDto.getOperatorOrgId());
		closeStationApplyDto.setOperatorType(operatorDto.getOperatorType());
		closeStationApplyBO.addCloseStationApply(closeStationApplyDto);

		// 发送状态变化事件
		PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convert(
				PartnerInstanceStateChangeEnum.START_CLOSING,
				partnerInstanceBO.getPartnerInstanceById(partnerInstance.getId()), partnerLifecycle);
		EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);

		// 同步station_apply
		syncStationApply(SyncStationApplyEnum.UPDATE_ALL, partnerInstance.getId());

		return true;
	}

	@Override
	public boolean confirmClose(ConfirmCloseDto confirmCloseDto) throws AugeServiceException {
		// 参数校验
		BeanValidator.validateWithThrowable(confirmCloseDto);
		Long partnerInstanceId = confirmCloseDto.getPartnerInstanceId();

		String employeeId = confirmCloseDto.getOperator();
		Boolean isAgree = confirmCloseDto.isAgree();
		try {
			PartnerStationRel partnerInstance = partnerInstanceBO.findPartnerInstanceById(confirmCloseDto.getPartnerInstanceId());
			if (partnerInstance == null || !PartnerInstanceStateEnum.CLOSING.getCode().equals(partnerInstance.getState())) {
				throw new AugeServiceException(PartnerExceptionEnum.NO_RECORD);
			}
			// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
			partnerInstanceHandler.validateExistValidChildren(PartnerInstanceTypeEnum.valueof(partnerInstance.getType()),
					partnerInstanceId);

			Long lifecycleId = partnerLifecycleBO.getLifecycleItemsId(partnerInstance.getId(), PartnerLifecycleBusinessTypeEnum.CLOSING,
					PartnerLifecycleCurrentStepEnum.CONFIRM);
			PartnerLifecycleDto partnerLifecycle = new PartnerLifecycleDto();
			partnerLifecycle.setLifecycleId(lifecycleId);
			partnerLifecycle.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
			partnerLifecycle.copyOperatorDto(confirmCloseDto);

			if (isAgree) {
				PartnerInstanceDto partnerInstanceDto = new PartnerInstanceDto();
				partnerInstanceDto.setId(partnerInstanceId);
				partnerInstanceDto.setState(PartnerInstanceStateEnum.CLOSED);
				partnerInstanceDto.setServiceEndTime(new Date());
				partnerInstanceDto.copyOperatorDto(confirmCloseDto);
				partnerInstanceDto.setVersion(partnerInstance.getVersion());
				partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);
				

				stationBO.changeState(partnerInstance.getStationId(), StationStatusEnum.CLOSING, StationStatusEnum.CLOSED, employeeId);

				partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.CONFIRM);
				partnerLifecycleBO.updateLifecycle(partnerLifecycle);

				PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convert(PartnerInstanceStateChangeEnum.CLOSED,
						partnerInstanceBO.getPartnerInstanceById(partnerInstanceId), confirmCloseDto);
				EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
			} else {
				partnerInstanceBO.changeState(partnerInstanceId, PartnerInstanceStateEnum.CLOSING, PartnerInstanceStateEnum.SERVICING,
						employeeId);
				stationBO.changeState(partnerInstance.getStationId(), StationStatusEnum.CLOSING, StationStatusEnum.SERVICING, employeeId);

				partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.CANCEL);
				partnerLifecycleBO.updateLifecycle(partnerLifecycle);
				
				partnerProtocolRelBO.cancelProtocol(partnerInstance.getTaobaoUserId(), ProtocolTypeEnum.PARTNER_QUIT_PRO, partnerInstanceId, PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE, employeeId);
				
				//删除停业申请表
				closeStationApplyBO.deleteCloseStationApply(partnerInstanceId,employeeId);
				
				PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convert(
						PartnerInstanceStateChangeEnum.CLOSING_REFUSED, partnerInstanceBO.getPartnerInstanceById(partnerInstanceId),
						confirmCloseDto);
				EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
			}

			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_BASE, partnerInstance.getId());
			return true;
		} catch (AugeServiceException augeException) {
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("confirmClose", JSONObject.toJSONString(confirmCloseDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	@Override
	public void applyCloseByManager(ForcedCloseDto forcedCloseDto) throws AugeServiceException {
		// 参数校验
		BeanValidator.validateWithThrowable(forcedCloseDto);

		Long instanceId = forcedCloseDto.getInstanceId();
		PartnerStationRel partnerStationRel = partnerInstanceBO.findPartnerInstanceById(instanceId);
		Long stationId = partnerStationRel.getStationId();

		// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
		partnerInstanceHandler.validateExistValidChildren(PartnerInstanceTypeEnum.valueof(partnerStationRel.getType()), instanceId);

		// 合伙人实例停业中,退出类型为强制清退
		PartnerInstanceDto partnerInstanceDto = new PartnerInstanceDto();

		partnerInstanceDto.setId(instanceId);
		partnerInstanceDto.setState(PartnerInstanceStateEnum.CLOSING);
		partnerInstanceDto.setCloseType(PartnerInstanceCloseTypeEnum.WORKER_QUIT);
		partnerInstanceDto.copyOperatorDto(forcedCloseDto);
		partnerInstanceDto.setVersion(partnerStationRel.getVersion());
		partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);

		// 村点停业中
		stationBO.changeState(stationId, StationStatusEnum.SERVICING, StationStatusEnum.CLOSING, forcedCloseDto.getOperator());

		// 添加停业生命周期记录
		addManagerClosingLifecycle(forcedCloseDto, instanceId, partnerStationRel);

		// 新增停业申请
		CloseStationApplyDto closeStationApplyDto = new CloseStationApplyDto();
		closeStationApplyDto.setCloseReason(forcedCloseDto.getReason());
		closeStationApplyDto.setOtherReason(forcedCloseDto.getRemarks());
		closeStationApplyDto.setPartnerInstanceId(instanceId);
		closeStationApplyDto.setType(PartnerInstanceCloseTypeEnum.WORKER_QUIT);
		closeStationApplyDto.setOperator(forcedCloseDto.getOperator());
		closeStationApplyDto.setOperatorOrgId(forcedCloseDto.getOperatorOrgId());
		closeStationApplyDto.setOperatorType(forcedCloseDto.getOperatorType());
		closeStationApplyBO.addCloseStationApply(closeStationApplyDto);

		// 通过事件，定时钟，启动停业流程
		PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convert(PartnerInstanceStateChangeEnum.START_CLOSING,
				partnerInstanceBO.getPartnerInstanceById(instanceId), forcedCloseDto);

		event.setRemark(CloseStationApplyCloseReasonEnum.OTHER.equals(forcedCloseDto.getReason()) ? forcedCloseDto.getRemarks()
				: forcedCloseDto.getReason().getDesc());

		EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);

		// 同步station_apply
		syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);

		// tair
	}

	private void addManagerClosingLifecycle(ForcedCloseDto forcedCloseDto, Long instanceId, PartnerStationRel partnerStationRel) {
		PartnerLifecycleDto itemsDO = new PartnerLifecycleDto();
		itemsDO.setPartnerInstanceId(instanceId);
		itemsDO.setPartnerType(PartnerInstanceTypeEnum.valueof(partnerStationRel.getType()));
		itemsDO.setBusinessType(PartnerLifecycleBusinessTypeEnum.CLOSING);
		itemsDO.setRoleApprove(PartnerLifecycleRoleApproveEnum.TO_AUDIT);
		itemsDO.setCurrentStep(PartnerLifecycleCurrentStepEnum.ROLE_APPROVE);
		itemsDO.copyOperatorDto(forcedCloseDto);
		partnerLifecycleBO.addLifecycle(itemsDO);
	}

	@Override
	public void applyQuitByManager(QuitStationApplyDto quitDto) throws AugeServiceException {
		// 参数校验
		BeanValidator.validateWithThrowable(quitDto);

		Long instanceId = quitDto.getInstanceId();
		String operator = quitDto.getOperator();

		PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);
		Partner partner = partnerBO.getPartnerById(instance.getPartnerId());

		// 校验申请退出的条件
		validateQuitPreCondition(instance, partner);

		// 保存退出申请单
		QuitStationApply quitStationApply = QuitStationApplyConverter.convert(quitDto, instance, buildOperatorName(quitDto));
		quitStationApplyBO.saveQuitStationApply(quitStationApply, operator);

		// 合伙人实例退出中
		partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSED, PartnerInstanceStateEnum.QUITING, operator);

		// 村点退出中
		if (quitDto.getIsQuitStation()) {
			stationBO.changeState(instance.getStationId(), StationStatusEnum.CLOSED, StationStatusEnum.QUITING, operator);
		}

		// 添加退出生命周期
		partnerInstanceHandler.handleApplyQuit(quitDto, PartnerInstanceTypeEnum.valueof(instance.getType()));

		// 退出审批流程，由事件监听完成 记录村点状态变化
		PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convert(PartnerInstanceStateChangeEnum.START_QUITTING,
				partnerInstanceBO.getPartnerInstanceById(instanceId), quitDto);
		EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);

		// 失效tair
		// tairCache.invalid(TairCache.STATION_APPLY_ID_KEY_DETAIL_VALUE_PRE
		// + quitStationApplyDto.getStationApplyId());

		// 同步station_apply
		syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instanceId);
	}

	private void validateQuitPreCondition(PartnerStationRel instance, Partner partner) throws AugeServiceException {
		Long instanceId = instance.getId();
		// 校验是否已经存在退出申请单
		QuitStationApply quitStationApply = quitStationApplyBO.findQuitStationApply(instanceId);
		if (quitStationApply != null) {
			throw new AugeServiceException(StationExceptionEnum.QUIT_STATION_APPLY_EXIST);
		}

		// 校验是否存在未结束的订单
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

	@Override
	public Long applySettle(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		ValidateUtils.notNull(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getType());
		Long instanceId = partnerInstanceDto.getId();
		if (instanceId == null) {
			// 新增入驻
			instanceId = addSubmit(partnerInstanceDto);
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.ADD, instanceId);
		} else {
			// 暂存后，修改入驻
			updateSubmit(partnerInstanceDto);
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instanceId);
		}
		// 不同类型合伙人，执行不同的生命周期
		partnerInstanceHandler.handleApplySettle(partnerInstanceDto, partnerInstanceDto.getType());

		return instanceId;

	}

	private void syncStationApply(SyncStationApplyEnum type, Long instanceId) {
		EventDispatcher.getInstance().dispatch(EventConstant.CUNTAO_STATION_APPLY_SYNC_EVENT, new StationApplySyncEvent(type, instanceId));
	}

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

	@Override
	public void degradePartnerInstance(PartnerInstanceDegradeDto degradeDto)
			throws AugeServiceException {
		ValidateUtils.validateParam(degradeDto);
		Long instanceId = degradeDto.getInstanceId();
		Long parentTaobaoUserId = degradeDto.getParentTaobaoUserId();
		ValidateUtils.notNull(instanceId);
		ValidateUtils.notNull(parentTaobaoUserId);
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
		if (rel == null) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}
		if (!StringUtils.equals(PartnerInstanceTypeEnum.TP.getCode(),rel.getType())) {
			throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_PARTNER_TYPE_FAIL);
		}
		
		if (!PartnerInstanceStateEnum.canDegradeStateCodeList().contains(rel.getState())) {
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
		if (!StringUtils.equals(PartnerInstanceStateEnum.SERVICING.getCode(),parentRel.getState())) {
			throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_TARGET_PARTNER_NOT_SERVICING);
		}
		
		if (!StringUtils.equals(PartnerInstanceTypeEnum.TP.getCode(),parentRel.getType())) {
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
		
		
		PartnerInstanceDto param= new PartnerInstanceDto();
		param.setId(instanceId);
		param.setBit(-1);
		param.setParentStationId(parentStation.getId());
		param.setType(PartnerInstanceTypeEnum.TPA);
		param.copyOperatorDto(degradeDto);
		partnerInstanceBO.updatePartnerStationRel(param);
		
		addCNStationFeature(rel.getStationId(),rel.getTaobaoUserId(),parentRel.getStationId(),parentTaobaoUserId);
		
		//TODO:添加服务记录及关系布点
        //addDegradePartnerRelation(partnerDto.getTaobaoUserId(), stationApplyDetailDto.getTaobaoUserId());
		
	}
	
    private void addCNStationFeature(Long stationId,Long taobaoUserId,Long parentStationId,Long  parentTaobaoUserId){
    	if(parentStationId == null || stationId == null || parentTaobaoUserId == null){
    		logger.error("addCNStationFeature exception parameters is null!stationId:["+stationId+"],partnerStationId["+parentStationId+"],partnerTaobaoUserId["+parentTaobaoUserId+"]");
    		return;
    	}
    	Long cnStationId = cuntaoCainiaoStationRelBO.getCainiaoStationId(stationId);
    	if(cnStationId != null){
			//调用新菜鸟接口
			LinkedHashMap<String, String> featureMap = new LinkedHashMap<String, String>();
			featureMap.put(CaiNiaoAdapter.CTP_TB_UID, parentTaobaoUserId.toString());
			featureMap.put(CaiNiaoAdapter.CTP_ORG_STA_ID, parentStationId.toString());
			featureMap.put(CaiNiaoAdapter.CTP_TYPE, "CtPA1_0");
			caiNiaoAdapter.updateStationFeatures(cnStationId, featureMap);
			
			LinkedHashMap<String, String> featureMap1 = new LinkedHashMap<String, String>();
			featureMap1.put(CaiNiaoAdapter.PARTNER_ID, parentTaobaoUserId.toString());
			caiNiaoAdapter.updateStationUserRelFeature(taobaoUserId, featureMap1);
    	}
    }
  

	private Long getTpaMax() {
		AppResource resource = appResourceBO.queryAppResource(TPAMAX_TYPE, TPAMAX_KEY);
		if (resource != null && !StringUtils.isEmpty(resource.getValue())) {
			return Long.parseLong(resource.getValue());
		}
		return TPAMAX_DEFAULT;
	}

	@Override
	public void applySettleSuccess(
			PartnerInstanceSettleSuccessDto settleSuccessDto)
			throws AugeServiceException {
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

	@Override
	public void updateSettle(PartnerInstanceDto partnerInstanceDto)
			throws AugeServiceException {
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
			boolean canUpdate = partnerInstanceHandler.handleValidateUpdateSettle(instanceId,  PartnerInstanceTypeEnum.valueof(rel.getType()));
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

}
