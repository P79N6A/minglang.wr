package com.taobao.cun.auge.station.tpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.common.lang.StringUtil;
import com.taobao.cun.attachment.dto.AttachmentDto;
import com.taobao.cun.attachment.enums.AttachmentBizTypeEnum;
import com.taobao.cun.attachment.enums.AttachmentTypeIdEnum;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.LatitudeUtil;
import com.taobao.cun.auge.common.utils.PositionUtil;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.payment.account.PaymentAccountQueryService;
import com.taobao.cun.auge.payment.account.dto.AliPaymentAccountDto;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceUpdateServicingDto;
import com.taobao.cun.auge.station.dto.PartnerUpdateServicingDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.dto.StationUpdateServicingDto;
import com.taobao.cun.auge.station.dto.TpaApplyInfoDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.StationAreaTypeEnum;
import com.taobao.cun.auge.station.enums.StationlLogisticsStateEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.request.CheckTpaApplyRequest;
import com.taobao.cun.auge.station.response.CheckTpaApplyResponse;
import com.taobao.cun.auge.station.response.TpaApplyResponse;
import com.taobao.cun.auge.station.service.PartnerInstanceExtService;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.crius.alipay.dto.AlipayRiskScanData;
import com.taobao.cun.crius.alipay.dto.AlipayRiskScanResult;
import com.taobao.cun.crius.alipay.service.AlipayRiskScanService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.recruit.partner.dto.PartnerApplyDto;
import com.taobao.cun.recruit.partner.enums.PartnerApplyStateEnum;
import com.taobao.cun.recruit.partner.service.PartnerApplyService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.namelist.model.NamelistResult;
import com.taobao.namelist.param.NamelistMatchParam;
import com.taobao.namelist.service.NamelistMatchService;
import com.taobao.security.util.SensitiveDataUtil;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;

@Service("tpaApplyService")
@HSFProvider(serviceInterface = TpaApplyService.class, clientTimeout = 7000)
public class TpaApplyServiceImpl implements TpaApplyService {
	@Autowired
	private PartnerInstanceService partnerInstanceService;

	@Autowired
	private PartnerInstanceExtService partnerInstanceExtService;

	@Autowired
	private PartnerInstanceQueryService partnerInstanceQueryService;

	@Autowired
	private UicReadServiceClient uicReadServiceClient;

	@Autowired
	private PaymentAccountQueryService paymentAccountQueryService;

	@Autowired
	private PartnerApplyService partnerApplyService;

	@Autowired
	private NamelistMatchService namelistMatchService;

	@Autowired
	private AlipayRiskScanService alipayRiskScanService;

	@Autowired
	AttachmentService criusAttachmentService;
	
	@Autowired
	private StationBO stationBO;
	private static final Logger logger = LoggerFactory.getLogger(TpaApplyServiceImpl.class);

	@Override
	public CheckTpaApplyResponse checkTpaApply(CheckTpaApplyRequest request) {
		CheckTpaApplyResponse response = new CheckTpaApplyResponse();
		if (StringUtils.isEmpty(request.getTaobaoNick())) {
			response.setSuccess(false);
			response.setErrorMessage("淘宝账号为空");
			return response;
		}
		if (request.getPartnerStationId() == null) {
			response.setSuccess(false);
			response.setErrorMessage("合伙人站点ID为空");
			return response;
		}

		boolean validateChildNum = partnerInstanceExtService.validateChildNum(request.getPartnerStationId());
		Integer tpaMax = partnerInstanceExtService.findPartnerMaxChildNum(request.getPartnerStationId());
		if (validateChildNum) {
			response.setSuccess(false);
			response.setErrorMessage("淘帮手上限超过" + tpaMax + "个");
			return response;
		}

		ResultDO<BaseUserDO> baseUserDO = uicReadServiceClient.getBaseUserByNick(request.getTaobaoNick());
		if (baseUserDO == null || baseUserDO.getModule() == null) {
			response.setSuccess(false);
			response.setErrorMessage("淘宝账号不存在");
			return response;
		}

		// StationApplyDO tpaStationApplyDO =
		// stationApplyDAO.getUserProcessingStationApplyForTpa(baseUserDO.getModule().getUserId());
		PartnerInstanceDto piDto = partnerInstanceQueryService
				.getActivePartnerInstance(baseUserDO.getModule().getUserId());
		if (piDto != null) {
			response.setSuccess(false);
			response.setErrorMessage("淘帮手站点已存在");
			return response;
		}

		PartnerInstanceDto partnerInstance = partnerInstanceQueryService
				.getCurrentPartnerInstanceByStationId(request.getPartnerStationId());
		if (partnerInstance == null || partnerInstance.getStationDto() == null) {
			response.setSuccess(false);
			response.setErrorMessage("合伙人站点不存在");
			return response;
		}
		try {
			paymentAccountQueryService.queryStationMemberPaymentAccountByNick(request.getTaobaoNick());
		} catch (AugeBusinessException e) {
			response.setSuccess(false);
			response.setErrorMessage(e.getMessage());
			return response;
		}

		if (partnerInstance.getPartnerDto().getTaobaoUserId() == baseUserDO.getModule().getUserId()) {
			response.setSuccess(false);
			response.setErrorMessage("淘帮手账号和合伙人账号相同");
			return response;
		}

		PartnerApplyDto partnerApplyDto = partnerApplyService
				.getPartnerApplyByTaobaoUserId(baseUserDO.getModule().getUserId());

		if (partnerApplyDto == null) {
			response.setSuccess(false);
			response.setErrorMessage("该淘宝账号尚未在线报名，在线报名考试后才能新增");
			return response;
		}
		if (partnerApplyDto.getPartnerApplyStateEnum() == null || PartnerApplyStateEnum.STATE_APPLY_WAIT.getCode()
				.equals(partnerApplyDto.getPartnerApplyStateEnum().getCode())) {
			response.setSuccess(false);
			response.setErrorMessage("该淘宝账号尚未通过初审，请耐心等待初审结果（3-4小时内）");
			return response;
		}
		if (PartnerApplyStateEnum.STATE_APPLY_REFUSE.getCode()
				.equals(partnerApplyDto.getPartnerApplyStateEnum().getCode())) {
			response.setSuccess(false);
			response.setErrorMessage("该淘宝账号报名状态为审核拒绝，无法成为合伙人或淘帮手");
			return response;
		}
		if (!partnerInstance.getStationDto().getApplyOrg().equals(Long.parseLong(partnerApplyDto.getOwnOrgId()))) {
			response.setSuccess(false);
			response.setErrorMessage("该淘宝账号的报名信息非本地区内，请核实报名人信息");
			return response;
		}

		if (isTaobaoBuyerOrSellerBlack(partnerApplyDto.getTaobaoUserId())) {
			response.setSuccess(false);
			response.setErrorMessage("该淘宝账号属于淘宝黑名单");
			return response;
		}

		if (isAlipayRiskUser(partnerApplyDto.getTaobaoUserId())) {
			response.setSuccess(false);
			response.setErrorMessage("该淘宝用户支付宝账号属于支付宝风险账号");
			return response;
		}

		if (!"pass".equals(partnerApplyDto.getExamState())) {
			response.setSuccess(false);
			response.setErrorMessage("淘帮手未通过考试");
			return response;
		}
		response.setSuccess(true);
		return response;

	}

	@Override
	public TpaApplyResponse applyTpa(TpaApplyInfoDto tpaApplyInfo) {
		TpaApplyResponse response = new TpaApplyResponse();
		try {
			PartnerDto partnerDto = buildPartnerDto(tpaApplyInfo);
			StationDto stationDto = buildStationDto(tpaApplyInfo);
			PartnerInstanceDto partnerInstanceDto = buildPartnerInstanceDto(stationDto, partnerDto, tpaApplyInfo);
			partnerInstanceService.applySettle(partnerInstanceDto);
			response.setSuccess(true);
		} catch (Exception e) {
			response.setSuccess(false);
			response.setErrorMessage("申请淘帮手失败");
			logger.error("applyTpa error!", e);
		}
		return response;
	}

	private PartnerDto buildPartnerDto(TpaApplyInfoDto request) {
		PartnerDto pDto = new PartnerDto();
		setAlipayInfo(request, pDto);
		pDto.setEmail(request.getEmail());
		pDto.setMobile(request.getMobile());

		List<AttachmentDto> attachmentsList = new ArrayList<AttachmentDto>();
		
		PartnerApplyDto partnerApplyDto = partnerApplyService.getPartnerApplyByTaobaoUserId(request.getTaobaoUserId());
		AttachmentDto result = criusAttachmentService.getAttachment(partnerApplyDto.getId(),
				AttachmentBizTypeEnum.PARTNER_IDCARD_JOINTIMG);
		if (null != result) {
			result.setAttachmentTypeId(AttachmentTypeIdEnum.IDCARD_IMG);
			result.setBizType(AttachmentBizTypeEnum.PARTNER);
		}
		attachmentsList.add(result);
		pDto.setAttachments(attachmentsList);
		pDto.setBusinessType(PartnerBusinessTypeEnum.PARTTIME);
		return pDto;
	}

	private void initStationAttachments(List<AttachmentDto> attachements) {
		if (attachements != null && !attachements.isEmpty()) {
			for (AttachmentDto attachment : attachements) {
				attachment.setAttachmentTypeId(AttachmentTypeIdEnum.CURRENT_STATUS_IMG);
				attachment.setBizType(AttachmentBizTypeEnum.CRIUS_STATION);
			}
		}
	}

	private void setAlipayInfo(TpaApplyInfoDto request, PartnerDto pDto) {
		pDto.setTaobaoUserId(request.getTaobaoUserId());
		pDto.setTaobaoNick(request.getTaobaoNick());

		AliPaymentAccountDto aliPaymentAccountDto = paymentAccountQueryService
				.queryStationMemberPaymentAccountByNick(request.getTaobaoNick());

		// 判断淘宝黑名单
		if (isTaobaoBuyerOrSellerBlack(request.getTaobaoUserId())) {
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_BUSINESS_CHECK_ERROR_CODE, "该淘宝账号属于淘宝黑名单");
		}
		// 判断支付宝风险用户
		if (isAlipayRiskUser(request.getTaobaoUserId())) {
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_BUSINESS_CHECK_ERROR_CODE, "该用户的支付宝账号属于支付宝风险账号");
		}
		;

		pDto.setAlipayAccount(aliPaymentAccountDto.getAlipayId());
		pDto.setName(aliPaymentAccountDto.getFullName());
		pDto.setIdenNum(aliPaymentAccountDto.getIdCardNumber());
	}

	private boolean isAlipayRiskUser(Long taobaoUserId) {
		ResultModel<AlipayRiskScanResult> riskResult = alipayRiskScanService.checkEntryRisk(taobaoUserId);
		if (riskResult.isSuccess()) {
			AlipayRiskScanResult risk = riskResult.getResult();
			if (risk != null && risk.isSuccess()) {
				AlipayRiskScanData riskData = risk.getData();
				return riskData.isRisk();
			}
		}
		logger.error("alipayRiskScanService error", riskResult.getException());
		// 如果支付宝效验异常暂时放过
		return false;
	}

	private boolean isTaobaoBuyerOrSellerBlack(Long taobaoUserId) {
		if (null == taobaoUserId) {
			return Boolean.FALSE;
		}
		// 黑白名单
		NamelistMatchParam buyer = new NamelistMatchParam();
		buyer.setIdentifier("sm"); // 买家名单表标识
		buyer.setValue(String.valueOf(taobaoUserId));// 名单表里的类型的值
		NamelistMatchParam seller = new NamelistMatchParam();
		seller.setIdentifier("R_172"); // 买家名单表标识
		seller.setValue(String.valueOf(taobaoUserId));// 名单表里的类型的值
		List<NamelistMatchParam> list = new ArrayList<NamelistMatchParam>();
		list.add(buyer);
		list.add(seller);
		Map<NamelistMatchParam, NamelistResult<Boolean>> map = namelistMatchService.batchMatch(list);
		NamelistResult<Boolean> bResult = map.get(buyer);
		// 买家黑名单
		if (null != bResult && bResult.isSuccess() && bResult.getValue()) {
			return Boolean.TRUE;
		}
		NamelistResult<Boolean> sResult = map.get(seller);
		// 卖家黑名单
		if (null != sResult && sResult.isSuccess() && sResult.getValue()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private StationDto buildStationDto(TpaApplyInfoDto request) {
		String logistics = request.getLogisticsState();
		PartnerApplyDto partnerApplyDto = partnerApplyService.getPartnerApplyByTaobaoUserId(request.getTaobaoUserId());
		StationDto sDto = new StationDto();
		sDto.setApplyOrg(Long.parseLong(partnerApplyDto.getOwnOrgId()));
		sDto.setCovered(request.getCovered());
		sDto.setDescription(request.getDesc());
		if (StringUtil.isNotEmpty(logistics)) {
			sDto.setLogisticsState(StationlLogisticsStateEnum.valueof(logistics));
		}
		sDto.setAreaType(StationAreaTypeEnum.FREE);
		sDto.setName(request.getName());
		sDto.setProducts(request.getProducts());
		sDto.setStationNum(request.getStationNum());
		buildPOI(request.getAddress());
		sDto.setAddress(request.getAddress());
		Map<String, String> featureMap = new HashMap<String, String>();
		featureMap.put("villageFlag", request.getVillageFlag());
		featureMap.put("placeFlag", request.getPlaceFlag());
		featureMap.put("category", StationCategoryEnum.valueofDesc(request.getCategory()) != null
				? StationCategoryEnum.valueofDesc(request.getCategory()).getCode() : "");
		featureMap.put("managementType", request.getManagementType());
		sDto.setFeature(featureMap);
		initStationAttachments(request.getAttachements());
		sDto.setAttachments(request.getAttachements());
		return sDto;
	}

	private static void buildPOI(Address address) {
		String villageDetail = address.getVillageDetail();
		String lastDivisionId = getLastDivisionId(address);
		String addressDetail = StringUtils.isNotEmpty(villageDetail) ? villageDetail : address.getAddressDetail();
		Map<String, String> map = LatitudeUtil.findLatitude(lastDivisionId, addressDetail);
		address.setLat(PositionUtil.converUp(map.get("lat")));
		address.setLng(PositionUtil.converUp(map.get("lng")));
	}

	private static String getLastDivisionId(Address address) {
		String lastDivisionId = null;
		lastDivisionId = Optional.ofNullable(StringUtils.trimToNull(address.getTown()))
				.orElse(StringUtils.trimToNull(address.getCounty()));
		lastDivisionId = Optional.ofNullable(StringUtils.trimToNull(lastDivisionId))
				.orElse(StringUtils.trimToNull(address.getCity()));
		lastDivisionId = Optional.ofNullable(StringUtils.trimToNull(lastDivisionId))
				.orElse(StringUtils.trimToNull(address.getProvince()));
		lastDivisionId = Optional.ofNullable(StringUtils.trimToNull(lastDivisionId)).orElse("");
		return lastDivisionId;
	}

	private PartnerInstanceDto buildPartnerInstanceDto(StationDto stationDto, PartnerDto partnerDto,
			TpaApplyInfoDto request) {
		PartnerInstanceDto piDto = new PartnerInstanceDto();
		piDto.setOperator(request.getTaobaoUserId() + "");
		piDto.setOperatorOrgId(stationDto.getApplyOrg());
		piDto.setOperatorType(OperatorTypeEnum.HAVANA);
		piDto.setType(PartnerInstanceTypeEnum.TPA);

		piDto.setTaobaoUserId(partnerDto.getTaobaoUserId());
		piDto.setPartnerId(partnerDto.getId());
		piDto.setStationId(stationDto.getId());
		piDto.setStationDto(stationDto);
		piDto.setPartnerDto(partnerDto);
		piDto.setParentStationId(request.getPartnerStationId());
		return piDto;
	}

	@Override
	public TpaApplyResponse updateTpa(TpaApplyInfoDto request) {
		TpaApplyResponse response = new TpaApplyResponse();
		try {
			PartnerInstanceUpdateServicingDto instanceDto = new PartnerInstanceUpdateServicingDto();
			instanceDto.setStationDto(convertToStationDto(request));
			instanceDto.setPartnerDto(convertToPartnerDto(request));
			instanceDto.setOperator(request.getTaobaoNick());
			instanceDto.setOperatorType(OperatorTypeEnum.HAVANA);
			partnerInstanceService.updateByPartner(instanceDto);
			response.setSuccess(true);
		} catch (Exception e) {
			response.setSuccess(false);
			response.setErrorMessage("修改淘帮手信息失败");
			logger.error("updateTpa error!", e);
		}
		return response;
	}

	public StationUpdateServicingDto convertToStationDto(TpaApplyInfoDto request) {
		StationUpdateServicingDto stationDto = new StationUpdateServicingDto();
		stationDto.setStationId(request.getStationId());
		stationDto.setName(request.getName());
		stationDto.setStationNum(request.getStationNum());
		stationDto.setDescription(request.getDesc());
		stationDto.setCovered(request.getCovered());

		stationDto.setProducts(request.getProducts());
		stationDto.setLogisticsState(StationlLogisticsStateEnum.valueof(request.getLogisticsState()));
		stationDto.setAreaType(StationAreaTypeEnum.FREE);

		initStationAttachments(request.getAttachements());
		stationDto.setAttachments(request.getAttachements());

		Map<String, String> featureMap = new HashMap<String, String>();
		featureMap.put("villageFlag", request.getVillageFlag());
		featureMap.put("placeFlag", request.getPlaceFlag());
		featureMap.put("category", StationCategoryEnum.valueofDesc(request.getCategory()) != null
				? StationCategoryEnum.valueofDesc(request.getCategory()).getCode() : "");
		featureMap.put("managementType", request.getManagementType());
		stationDto.setFeature(featureMap);
		buildPOI(request.getAddress());
		stationDto.setAddress(request.getAddress());

		return stationDto;
	}

	public static PartnerUpdateServicingDto convertToPartnerDto(TpaApplyInfoDto request) {
		PartnerUpdateServicingDto partnerDto = new PartnerUpdateServicingDto();
		partnerDto.setMobile(request.getMobile());
		partnerDto.setEmail(request.getEmail());
		partnerDto.setBusinessType(PartnerBusinessTypeEnum.PARTTIME);
		return partnerDto;
	}

	@Override
	public TpaApplyInfoDto getTpaInfo(Long stationId) {
		Assert.notNull(stationId);
		OperatorDto operator = new OperatorDto();
		operator.setOperator("system");
		operator.setOperatorType(OperatorTypeEnum.HAVANA);
		PartnerInstanceDto instanceDto = partnerInstanceQueryService.queryInfo(stationId, operator);
		TpaApplyInfoDto tpaApplyInfoDto = new TpaApplyInfoDto();

		tpaApplyInfoDto.setPartnerStationId(instanceDto.getParentStationId());

		// 组装站点信息
		buildStationInfo(tpaApplyInfoDto, instanceDto.getStationDto());

		// 组装人的信息
		buildPartnerInfo(tpaApplyInfoDto, instanceDto.getPartnerDto());

		buildAttachements(tpaApplyInfoDto, instanceDto);

		return tpaApplyInfoDto;
	}

	private static void buildStationInfo(TpaApplyInfoDto tpaApplyInfoDto, StationDto stationDto) {
		if (null == stationDto) {
			return;
		}
		tpaApplyInfoDto.setStationId(stationDto.getId());
		tpaApplyInfoDto.setStationNum(stationDto.getStationNum());
		tpaApplyInfoDto.setName(stationDto.getName());

		if (null != stationDto.getLogisticsState()) {
			tpaApplyInfoDto.setLogisticsState(stationDto.getLogisticsState().getCode());
		}

		tpaApplyInfoDto.setCovered(stationDto.getCovered());
		tpaApplyInfoDto.setProducts(stationDto.getProducts());
		tpaApplyInfoDto.setDesc(stationDto.getDescription());

		Map<String, String> features = stationDto.getFeature();

		tpaApplyInfoDto.setVillageFlag(features.get("villageFlag"));
		tpaApplyInfoDto.setPlaceFlag(features.get("placeFlag"));

		StationCategoryEnum categoryEnum = StationCategoryEnum.valueof(features.get("category"));
		if (null != categoryEnum) {
			tpaApplyInfoDto.setCategory(categoryEnum.getDesc());
		}
		tpaApplyInfoDto.setManagementType(features.get("managementType"));

		Address address = stationDto.getAddress();
		if (null != address) {
			tpaApplyInfoDto.setAddress(address);
		}
	}

	private static void buildPartnerInfo(TpaApplyInfoDto tpaApplyInfoDto, PartnerDto partnerDto) {
		if (null == partnerDto) {
			return;
		}
		tpaApplyInfoDto.setTaobaoUserId(partnerDto.getTaobaoUserId());
		tpaApplyInfoDto.setTaobaoNick(partnerDto.getTaobaoNick());
		tpaApplyInfoDto.setAlipayAccount(partnerDto.getAlipayAccount());
		tpaApplyInfoDto.setEmail(partnerDto.getEmail());
		tpaApplyInfoDto.setMobile(partnerDto.getMobile());
		tpaApplyInfoDto.setApplierName(partnerDto.getName());

	}

	private static void buildAttachements(TpaApplyInfoDto tpaApplyInfoDto, PartnerInstanceDto instanceDto) {
		StationDto stationDto = instanceDto.getStationDto();
		List<AttachmentDto> attachements = new ArrayList<AttachmentDto>();
		if(stationDto.getAttachments() != null){
		    List<AttachmentDto> stationAttachments= stationDto.getAttachments().stream()
			.filter(attachment -> AttachmentTypeIdEnum.CURRENT_STATUS_IMG.getCode().equals(attachment.getAttachmentTypeId().getCode()))
			.collect(Collectors.toList());
		    attachements.addAll(stationAttachments);
		}
		tpaApplyInfoDto.setAttachements(attachements);
	}

	@Override
	public TpaApplyInfoDto getTpaApplyInfo(String tpaTaobaoNick, Long partnerStationId) {
		TpaApplyInfoDto tpaApplyInfoDto = new TpaApplyInfoDto();
		PartnerInstanceDto partnerInstanceDto = this.partnerInstanceQueryService.getCurrentPartnerInstanceByStationId(partnerStationId);
		tpaApplyInfoDto.setAddress(partnerInstanceDto.getStationDto().getAddress());
		tpaApplyInfoDto.setPartnerStationId(partnerStationId);
		String suffix = getDefaultNum(partnerInstanceDto.getStationDto().getStationNum(),0);
		tpaApplyInfoDto.setStationNum(partnerInstanceDto.getStationDto().getStationNum()+suffix);
		tpaApplyInfoDto.setName(partnerInstanceDto.getStationDto().getName()+suffix);
		tpaApplyInfoDto.setPartnerTaobaoUserId(partnerInstanceDto.getPartnerDto().getTaobaoUserId());
		
		ResultDO<BaseUserDO> baseUserDO = uicReadServiceClient.getBaseUserByNick(tpaTaobaoNick);
		tpaApplyInfoDto.setTaobaoNick(baseUserDO.getModule().getNick());
		tpaApplyInfoDto.setTaobaoUserId(baseUserDO.getModule().getUserId());
		
		AliPaymentAccountDto alipaymentAccount = paymentAccountQueryService.queryStationMemberPaymentAccountByNick(tpaTaobaoNick);
		String tpaName = alipaymentAccount.getFullName();
		tpaApplyInfoDto.setApplierName(SensitiveDataUtil.customizeHide(tpaName, 0, tpaName.length() - 1, 1));
		tpaApplyInfoDto.setAlipayAccount(SensitiveDataUtil.alipayLogonIdHide(alipaymentAccount.getAlipayId()));
		
		PartnerApplyDto partnerApplyDto = partnerApplyService.getPartnerApplyByTaobaoUserId(baseUserDO.getModule().getUserId());
		tpaApplyInfoDto.setMobile(partnerApplyDto.getPhone());
		return tpaApplyInfoDto;
	}

	
	private String getDefaultNum(String stationNum,int level) {
		int num = (int)(Math.random()*100);
		String tpaStationNum = stationNum+"T"+num;
		Station station = this.stationBO.getStationByStationNum(tpaStationNum);
		if(station == null){
			return tpaStationNum;
		}else{
			level = level+1;
			if(level<5){
				return getDefaultNum(stationNum,level);
			}
		}
		return "T"+num;
	}
}
