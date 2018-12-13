package com.taobao.cun.auge.station.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.taobao.cun.auge.client.result.ResultModel;
import com.taobao.cun.auge.station.dto.*;
import com.taobao.cun.crius.train.dto.FileUploadDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.ecs.xhtml.param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.china.member.service.MemberReadService;
import com.alibaba.china.member.service.models.MemberModel;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.organization.api.orgstruct.enums.OrgStructStatus;
import com.alibaba.organization.api.orgstruct.model.OrgStructModel;
import com.alibaba.organization.api.orgstruct.param.OrgStructBaseParam;
import com.alibaba.organization.api.orgstruct.param.OrgStructPostParam;
import com.alibaba.organization.api.orgstruct.param.QueryOrgStructParam;
import com.alibaba.organization.api.orgstruct.service.OrgStructReadService;
import com.alibaba.organization.api.orgstruct.service.OrgStructWriteService;
import com.google.common.collect.Lists;
import com.taobao.cun.appResource.dto.AppResourceDto;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.attachment.dto.AttachmentDto;
import com.taobao.cun.attachment.enums.AttachmentBizTypeEnum;
import com.taobao.cun.attachment.enums.AttachmentTypeIdEnum;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.DecorationInfoDecision;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.PartnerStationRelExample;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StationDecorate;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.DecorationInfoDecisionBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationDecorateBO;
import com.taobao.cun.auge.station.bo.StationDecorateOrderBO;
import com.taobao.cun.auge.station.convert.StationConverter;
import com.taobao.cun.auge.station.convert.StationDecorateConverter;
import com.taobao.cun.auge.station.enums.DecorationInfoDecisionStatusEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleDecorateStatusEnum;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.StationDecoratePaymentTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecorateStatusEnum;
import com.taobao.cun.auge.station.enums.StationDecorateTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.ProcessService;
import com.taobao.cun.auge.station.service.StationDecorateService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("stationDecorateService")
@HSFProvider(serviceInterface = StationDecorateService.class, clientTimeout = 8000)
public class StationDecorateServiceImpl implements StationDecorateService {

    private static final Logger logger = LoggerFactory.getLogger(StationDecorateServiceImpl.class);

    
	@Autowired
	StationDecorateBO stationDecorateBO;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	PartnerLifecycleBO partnerLifecycleBO;

	@Autowired
	StationDecorateOrderBO stationDecorateOrderBO;

	@Autowired
	AppResourceService appResourceService;

	@Autowired
	StationBO stationBO;

	@Autowired
	AttachmentService criusAttachmentService;

	@Autowired
	DecorationInfoDecisionBO decorationInfoDecisionBO;

	/**
	 * 淘宝商品图片
	 */
	@Value("${taobao.image.url}")
	private String taobaoImageUrl;

	/**
	 * 淘宝订单详情
	 */
	@Value("${taobao.orderDetail.url}")
	private String taobaoOrderDetailUrl;

	/**
	 * 淘宝商品
	 */
	@Value("${taobao.item.url}")
	private String taobaoItemUrl;

	/**
	 * 装修反馈url
	 */
	@Value("${station.decorate.reflect.url}")
	private String stationDecorateReflectUrl;

	@Autowired
	OrgStructWriteService orgStructWriteService;
	@Autowired
	MemberReadService memberReadService;
	@Autowired
	ProcessService processService;

	@Value("${cbu.market.parent_code}")
	private Long parentId;
	@Autowired
	OrgStructReadService orgStructReadService;
	
	@Autowired
	private PartnerStationRelMapper partnerStationRelMapper;

	@Override
	public void audit(StationDecorateAuditDto stationDecorateAuditDto) {
		// 参数校验
		BeanValidator.validateWithThrowable(stationDecorateAuditDto);
		// if (!stationDecorateAuditDto.getIsAgree()) {
		// String error =
		// getErrorMessage("audit",JSONObject.toJSONString(stationDecorateAuditDto),
		// "is agree is false");
		// throw new
		// AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,error);
		// }
		Long sdId = stationDecorateAuditDto.getId();
		StationDecorate sd = stationDecorateBO.getStationDecorateById(sdId);
		if (sd == null) {
			String error = getErrorMessage("audit", JSONObject.toJSONString(stationDecorateAuditDto),
					"StationDecorate is null");
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, error);
		}
		// 审批服务站装修记录
		auditStationDecorate(stationDecorateAuditDto);
		// 更新生命周期表为已完成
		setLifecycleDecorate(stationDecorateAuditDto, sd);
	}

	private void setLifecycleDecorate(StationDecorateAuditDto stationDecorateAuditDto, StationDecorate sd) {
		PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(sd.getPartnerUserId());
		if (rel == null) {
			String error = getErrorMessage("audit", JSONObject.toJSONString(stationDecorateAuditDto),
					"PartnerStationRel is null");
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, error);
		}

		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(rel.getId(),
				PartnerLifecycleBusinessTypeEnum.DECORATING, PartnerLifecycleCurrentStepEnum.PROCESSING);
		// 固点3.0 特殊流程，允许先开业，再装修反馈
		if (items == null) {
			items = partnerLifecycleBO.getLifecycleItems(rel.getId(), PartnerLifecycleBusinessTypeEnum.DECORATING,
					PartnerLifecycleCurrentStepEnum.END);
		}
		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
		partnerLifecycleDto.setLifecycleId(items.getId());
		if (stationDecorateAuditDto.getIsAgree()) {
			partnerLifecycleDto.setDecorateStatus(PartnerLifecycleDecorateStatusEnum.Y);
			partnerLifecycleDto.copyOperatorDto(stationDecorateAuditDto);
			partnerLifecycleBO.updateLifecycle(partnerLifecycleDto);
		}
	}

	private void auditStationDecorate(StationDecorateAuditDto stationDecorateAuditDto) {
		StationDecorateDto sdDto = new StationDecorateDto();
		sdDto.setId(stationDecorateAuditDto.getId());
		if (stationDecorateAuditDto.getIsAgree()) {
			sdDto.setStatus(StationDecorateStatusEnum.DONE);
		} else {
			sdDto.setStatus(StationDecorateStatusEnum.AUDIT_NOT_PASS);
		}
		sdDto.copyOperatorDto(stationDecorateAuditDto);
		stationDecorateBO.updateStationDecorate(sdDto);
	}

	private String getErrorMessage(String methodName, String param, String error) {
		StringBuilder sb = new StringBuilder();
		sb.append("StationDecorateService-Error|").append(methodName).append("(.param=").append(param).append(").")
				.append("errorMessage:").append(error);
		return sb.toString();
	}

	@Override
	public StationDecorateDto getInfoByTaobaoUserId(Long taobaoUserId) {
		ValidateUtils.notNull(taobaoUserId);
		PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		if (rel == null) {
			String error = getErrorMessage("getInfoByTaobaoUserId", String.valueOf(taobaoUserId), "rel is null");
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, error);
		}
		return getInfoByStationId(rel.getStationId());
	}

	/**
	 * 设置 店铺url
	 * 
	 * @param sdDto
	 */
	private void setShopItemInfo(StationDecorateDto sdDto) {
		if (sdDto != null && StringUtils.isNotEmpty(sdDto.getSellerTaobaoUserId())) {
			AppResourceDto resource = appResourceService.queryAppResource("shop_Item_info_v4",
					sdDto.getSellerTaobaoUserId());
			if (resource != null && !StringUtils.isEmpty(resource.getValue())) {
				sdDto.setTaobaoItemUrl(taobaoItemUrl + resource.getValue());
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void reflectStationDecorate(StationDecorateReflectDto stationDecorateReflectDto) {
		// 参数校验
		BeanValidator.validateWithThrowable(stationDecorateReflectDto);
		StationDecorate sd = stationDecorateBO.getStationDecorateById(stationDecorateReflectDto.getId());
		if (sd == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "查询不到当前装修记录");
		}
		if (StationDecorateStatusEnum.UNDECORATE.getCode().equals(sd.getStatus())
				|| StationDecorateStatusEnum.DONE.getCode().equals(sd.getStatus())) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "当前状态不能提交反馈");
		}
		// 判断村点是否装修中状态，非装修中状态 不允许反馈

		// if(!StationStatusEnum.DECORATING.getCode().equals(station.getStatus())){
		// throw new AugeServiceException("当前村点非装修状态");
		// }
		StationDecorateDto sdDto = buildStationDecorateDtoForReflect(stationDecorateReflectDto);
		stationDecorateBO.updateStationDecorate(sdDto);

		StartProcessDto startProcessDto = new StartProcessDto();
		startProcessDto.setBusiness(ProcessBusinessEnum.decorationFeedback);
		startProcessDto.setBusinessId(sd.getId());
		Station station = stationBO.getStationById(sd.getStationId());
		startProcessDto.setBusinessName(station.getName() + station.getStationNum());
		startProcessDto.setBusinessOrgId(station.getApplyOrg());
		startProcessDto.copyOperatorDto(stationDecorateReflectDto);

		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceByStationId(sd.getStationId());
		String stationType = "stationType:" + rel.getType();
		startProcessDto.setJsonParams(stationType);
		processService.startApproveProcess(startProcessDto);
	}

	private StationDecorateDto buildStationDecorateDtoForReflect(StationDecorateReflectDto stationDecorateReflectDto) {
		StationDecorateDto sdDto = new StationDecorateDto();
		sdDto.setId(stationDecorateReflectDto.getId());
		sdDto.setStatus(StationDecorateStatusEnum.WAIT_AUDIT);
		sdDto.setDoorArea(stationDecorateReflectDto.getDoorArea());
		sdDto.setGlassArea(stationDecorateReflectDto.getGlassArea());
		sdDto.setInsideArea(stationDecorateReflectDto.getInsideArea());
		sdDto.setReflectNick(stationDecorateReflectDto.getReflectNick());
		sdDto.setReflectOrderNum(stationDecorateReflectDto.getReflectOrderNum());
		sdDto.setReflectUserId(stationDecorateReflectDto.getReflectUserId());
		sdDto.setWallArea(stationDecorateReflectDto.getWallArea());
		sdDto.setCarpetArea(stationDecorateReflectDto.getCarpetArea());
		sdDto.setReflectSatisfySolid(stationDecorateReflectDto.getReflectSatisfySolid());
		sdDto.setAttachments(stationDecorateReflectDto.getAttachments());
		sdDto.copyOperatorDto(stationDecorateReflectDto);
		return sdDto;

	}

	@Override
	public List<StationDecorateDto> getStationDecorateListForSchedule(int pageNum, int pageSize) {
		return stationDecorateBO.getStationDecorateListForSchedule(pageNum, pageSize);
	}

	@Override
	public int getStationDecorateListCountForSchedule() {
		return stationDecorateBO.getStationDecorateListCountForSchedule();
	}

	@Override
	public void updateStationDecorate(StationDecorateDto stationDecorateDto) {
		stationDecorateBO.updateStationDecorate(stationDecorateDto);
	}

	@Override
	public void syncStationDecorateFromTaobao(StationDecorateDto stationDecorateDto) {
		stationDecorateBO.syncStationDecorateFromTaobao(stationDecorateDto);

	}

	@Override
	public StationDecorateDto getInfoByStationId(Long stationId) {
		ValidateUtils.notNull(stationId);
		StationDecorateDto sdDto = null;
		sdDto = stationDecorateBO.getStationDecorateDtoByStationId(stationId);
		if (sdDto == null) {
			return null;
		}
		//兼容老数据
		if(!StationDecorateTypeEnum.NEW_SELF.getCode().equals(sdDto.getDecorateType().getCode())){
	        	if(StationDecorateStatusEnum.DECORATING.getCode().equals(sdDto.getStatus().getCode())){
	        		sdDto.setStatus(StationDecorateStatusEnum.WAIT_CHECK_UPLOAD);
	        	}
	    }

		// 容错，因为定时钟更新装修记录有时间差，防止数据不准确，调淘宝接口，更新数据并返回
		  /**if (StationDecorateStatusEnum.UNDECORATE.equals(sdDto.getStatus())
				|| StationDecorateStatusEnum.DECORATING.equals(sdDto.getStatus())
				|| StationDecorateStatusEnum.AUDIT_NOT_PASS.equals(sdDto.getStatus())
				|| StationDecorateStatusEnum.WAIT_AUDIT.equals(sdDto.getStatus())) {
			stationDecorateBO.syncStationDecorateFromTaobao(sdDto);
			sdDto = stationDecorateBO.getStationDecorateDtoByStationId(stationId);
		}

		if (StringUtils.isNotEmpty(sdDto.getTaobaoOrderNum())) {
			StationDecorateOrderDto sdod = stationDecorateOrderBO
					.getDecorateOrderById(Long.parseLong(sdDto.getTaobaoOrderNum())).orElse(null);
			if (sdod == null) {
				sdDto.setStatus(StationDecorateStatusEnum.NO_ORDER);
			} else {
				if (!sdod.isPaid()) {
					sdDto.setStatus(StationDecorateStatusEnum.WAIT_PAY);
				}
				if (StringUtils.isNotEmpty(sdod.getAuctionPicUrl())) {
					sdod.setAuctionPicUrl(taobaoImageUrl + sdod.getAuctionPicUrl());
				}
				sdDto.setStationDecorateOrderDto(sdod);
				// 订单详情
				sdDto.setTaobaoOrderDetailUrl(taobaoOrderDetailUrl + sdDto.getTaobaoOrderNum());
			}
		} else {
			sdDto.setStatus(StationDecorateStatusEnum.NO_ORDER);
		}
		setShopItemInfo(sdDto);**/
		return sdDto;
	}

	@Override
	public Map<Long, StationDecorateStatusEnum> getStatusByStationId(List<Long> stationIds) {
		return stationDecorateBO.getStatusByStationId(stationIds);
	}

	@Override
	public String getReflectUrl(Long taobaoUserId) {
		StationDecorateDto dto = this.getInfoByTaobaoUserId(taobaoUserId);
		if (dto != null) {
			if (StationDecorateStatusEnum.DECORATING.equals(dto.getStatus())
					|| StationDecorateStatusEnum.WAIT_AUDIT.equals(dto.getStatus())) {
				return stationDecorateReflectUrl + dto.getStationId();
			}
		}
		return null;
	}

	@Override
	public void confirmAcessDecorating(Long id) {
		ValidateUtils.notNull(id);
		stationDecorateBO.confirmAcessDecorating(id);
	}

	@Override
	public void judgeDecorateQuit(Long stationId) {
		StationDecorateDto sdDto = getInfoByStationId(stationId);
		if (sdDto == null) {
			return;
		}
		if (StationDecorateStatusEnum.WAIT_AUDIT.getCode().equals(sdDto.getStatus().getCode())) {
			// 装修反馈待审核，需要小二审核完毕才能退出
			throw new AugeBusinessException(AugeErrorCodes.DECORATE_BUSINESS_CHECK_ERROR_CODE,
					"村点装修状态不允许退出，请先审核装修反馈记录");
		}
		// 其他状态暂时不做判断，走线下流程
		// if (StationDecorateStatusEnum.DONE.getCode().equals(
		// sdDto.getStatus().getCode())) {
		// // 装修完成，允许退出
		// return;
		// } else if (StationDecorateStatusEnum.WAIT_AUDIT.getCode().equals(
		// sdDto.getStatus().getCode())) {
		// // 装修反馈待审核，需要小二审核完毕才能退出
		// throw new RuntimeException("村点装修状态不允许退出，请先审核装修反馈记录");
		// }
		// else if (StationDecoratePaymentTypeEnum.SELF.getCode().equals(
		// sdDto.getPaymentType().getCode())) {
		// // 自费装修需要判断装修订单，政府出资的不做判断
		// if (StationDecorateStatusEnum.UNDECORATE.getCode().equals(
		// sdDto.getStatus().getCode())) {
		// // 未下单，允许退出
		// return;
		// } else if(StationDecorateStatusEnum.WAIT_PAY.getCode().equals(
		// sdDto.getStatus().getCode())){
		// throw new RuntimeException("存在未付款装修订单，请先关闭订单");
		// }else{
		// // 判断淘宝装修订单状态，非交易关闭或完结状态，不允许退出
		// stationDecorateOrderBO.judgeTcOrderStatusForQuit(
		// new Long(sdDto.getSellerTaobaoUserId()),
		// sdDto.getPartnerUserId());
		// }
		// }
	}

	@Override
	public void openAccessCbuMarket(Long taobaoUserId) {
		Assert.notNull(taobaoUserId);
		PartnerStationRel rel = partnerInstanceBO.getCurrentPartnerInstanceByTaobaoUserId(taobaoUserId);
		Assert.notNull(rel);
		PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(rel.getId());
		// Long taobaoUserId =
		// partnerInstanceDto.getPartnerDto().getTaobaoUserId();
		MemberModel memberModel = memberReadService.findMemberByUserId(taobaoUserId);
		if (memberModel == null || StringUtils.isEmpty(memberModel.getMemberId())) {
			throw new AugeBusinessException(AugeErrorCodes.MEMBER_ID_GET_ERROR,
					"memberid获取失败" + partnerInstanceDto.getPartnerDto().getTaobaoNick());
		}
		try {
			String memberId = memberModel.getMemberId();
			QueryOrgStructParam queryparam = new QueryOrgStructParam();
			queryparam.setMemberId(memberModel.getMemberId());
			queryparam.setStatuses(OrgStructStatus.getEffectiveStatus());
			List<OrgStructModel> modelList = orgStructReadService.queryOrgStructs(queryparam);
			if (modelList != null && modelList.size() > 0) {
				return;
			}
			OrgStructPostParam param = new OrgStructPostParam();
			param.setCreatorMemberId(memberId);
			param.setCreatorUserId(partnerInstanceDto.getTaobaoUserId());
			param.setMemberId(memberId);
			param.setParentId(parentId);
			Long structId = orgStructWriteService.postOrgStruct(param);
			OrgStructBaseParam pa = new OrgStructBaseParam();
			pa.setOrgStructId(structId);
			pa.setNewStatus(OrgStructStatus.success.getValue());
			orgStructWriteService.modifyBaseInfo(pa);
		} catch (Exception e) {
			throw new AugeBusinessException(AugeErrorCodes.CBU_MARKET_ACCESS_ERROR,
					"1688商城授权失败" + partnerInstanceDto.getPartnerDto().getTaobaoNick());
		}
	}

	@Override
	public StationDecorateDto getInfoById(Long Id) {
		ValidateUtils.notNull(Id);
		StationDecorate sd = stationDecorateBO.getStationDecorateById(Id);
		StationDecorateDto sdDto = StationDecorateConverter.toStationDecorateDto(sd);
		// 添加附件
		sdDto.setAttachments(
				criusAttachmentService.getAttachmentList(sd.getId(), AttachmentBizTypeEnum.STATION_DECORATE));
		if (sdDto.getStationId() != null) {
			Station s = stationBO.getStationById(sd.getStationId());
			if (s != null) {
				sdDto.setStationDto(StationConverter.toStationDto(s));
			}
		}
		List<AttachmentDto> designAttachments = criusAttachmentService.getAttachmentList(sdDto.getId(),
				AttachmentBizTypeEnum.STATION_DECORATION_DESIGN);
		sdDto.setDoorAttachments(getAttachmentsByType(designAttachments, AttachmentTypeIdEnum.DESIGN_DECORATION_DOOR));
		sdDto.setWallAttachments(getAttachmentsByType(designAttachments, AttachmentTypeIdEnum.DESIGN_DECORATION_WALL));
		sdDto.setInsideAttachments(
				getAttachmentsByType(designAttachments, AttachmentTypeIdEnum.DESIGN_DECORATION_INSIDE));

		List<AttachmentDto> checkAttachments = criusAttachmentService.getAttachmentList(sdDto.getId(),
				AttachmentBizTypeEnum.STATION_DECORATION_CHECK);
		sdDto.setCheckWallDeskAttachments(
				getAttachmentsByType(checkAttachments, AttachmentTypeIdEnum.CHECK_DECORATION_WALL_DESK));
		sdDto.setCheckDoorAttachments(
				getAttachmentsByType(checkAttachments, AttachmentTypeIdEnum.CHECK_DECORATION_DOOR));
		sdDto.setCheckInsideAttachments(
				getAttachmentsByType(checkAttachments, AttachmentTypeIdEnum.CHECK_DECORATION_INSIDE));
		sdDto.setCheckMaterielAttachments(
				getAttachmentsByType(checkAttachments, AttachmentTypeIdEnum.CHECK_DECORATION_MATERIEL));
		sdDto.setCheckOutsideAttachments(
				getAttachmentsByType(checkAttachments, AttachmentTypeIdEnum.CHECK_DECORATION_OUTSIDE));
		sdDto.setCheckInsideVideoAttachments(
				getAttachmentsByType(checkAttachments, AttachmentTypeIdEnum.CHECK_DECORATION_INSIDE_VIDEO));
		sdDto.setCheckOutsideVideoAttachments(
				getAttachmentsByType(checkAttachments, AttachmentTypeIdEnum.CHECK_DECORATION_OUTSIDE_VIDEO));
		return sdDto;
	}

	private List<AttachmentDto> getAttachmentsByType(List<AttachmentDto> attachments, AttachmentTypeIdEnum type) {
		if (attachments == null) {
			return null;
		}
		return attachments.stream().filter(att -> type.getCode().equals(att.getAttachmentTypeId().getCode()))
				.collect(Collectors.toList());
	}

	@Override
	public DecorationInfoDecisionDto getDecorationDecisionById(Long id) {
		ValidateUtils.notNull(id);
		DecorationInfoDecision info = decorationInfoDecisionBO.queryDecorationInfoById(id);
		DecorationInfoDecisionDto dto = StationDecorateConverter.toDecorationInfoDecisionDto(info);
		dto.setAttachments(
				criusAttachmentService.getAttachmentList(info.getId(), AttachmentBizTypeEnum.DECORATION_INFO_DECISION));
		if (info.getStationId() != null) {
			Station s = stationBO.getStationById(info.getStationId());
			if (s != null) {
				dto.setStationDto(StationConverter.toStationDto(s));
			}
		}
		return dto;
	}

	@Override
	public void auditDecorationDecision(DecorationInfoDecisionDto decorationInfoDecisionDto) {
		// 参数校验
		BeanValidator.validateWithThrowable(decorationInfoDecisionDto);
		Long id = decorationInfoDecisionDto.getId();
		DecorationInfoDecision info = decorationInfoDecisionBO.queryDecorationInfoById(id);
		if (info == null) {
			String error = getErrorMessage("audit", JSONObject.toJSONString(decorationInfoDecisionDto),
					"decorationInfoDecision is null");
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, error);
		}
		// 审批装修图纸信息
		DecorationInfoDecisionDto dto = new DecorationInfoDecisionDto();
		dto.setId(decorationInfoDecisionDto.getId());
		if (decorationInfoDecisionDto.getIsAgree()) {
			dto.setStatus(DecorationInfoDecisionStatusEnum.AUDIT_PASS);
		} else {
			dto.setStatus(DecorationInfoDecisionStatusEnum.AUDIT_NOT_PASS);
		}
		dto.copyOperatorDto(decorationInfoDecisionDto);
		decorationInfoDecisionBO.updateDecorationInfo(dto);
	}

	@Override
	public void updateDecorationDecision(DecorationInfoDecisionDto decorationInfoDecisionDto) {
		decorationInfoDecisionBO.updateDecorationInfo(decorationInfoDecisionDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void submitDecorationDecision(DecorationInfoDecisionDto decorationInfoDecisionDto) {
		ValidateUtils.notNull(decorationInfoDecisionDto.getStationId());
		decorationInfoDecisionDto.setStatus(DecorationInfoDecisionStatusEnum.WAIT_AUDIT);
		DecorationInfoDecision rm = decorationInfoDecisionBO
				.queryDecorationInfoByStationId(decorationInfoDecisionDto.getStationId());
		Long id = null;
		if (rm == null) {
			id = decorationInfoDecisionBO.addDecorationInfoDecision(decorationInfoDecisionDto);
		} else if (rm.getStatus().equals(DecorationInfoDecisionStatusEnum.WAIT_AUDIT.getCode())) {
			// 同一个村点待审核的装修图纸记录只能有一条
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "装修图纸待审核状态，请勿重复提交");
		} else if (rm.getStatus().equals(DecorationInfoDecisionStatusEnum.AUDIT_NOT_PASS.getCode())) {
			decorationInfoDecisionBO.updateDecorationInfo(decorationInfoDecisionDto);
		}

		StartProcessDto startProcessDto = new StartProcessDto();
		startProcessDto.setBusiness(ProcessBusinessEnum.decorationInfoDecision);
		startProcessDto.setBusinessId(id);
		Station station = stationBO.getStationById(decorationInfoDecisionDto.getStationId());
		startProcessDto.setBusinessName(station.getName() + station.getStationNum());
		startProcessDto.setBusinessOrgId(station.getApplyOrg());
		startProcessDto.copyOperatorDto(decorationInfoDecisionDto);
		processService.startApproveProcess(startProcessDto);
	}

	@Override
	public DecorationInfoDecisionDto getDecorationDecisionByStationId(Long stationId) {
		ValidateUtils.notNull(stationId);
		DecorationInfoDecision info = decorationInfoDecisionBO.queryDecorationInfoByStationId(stationId);
		DecorationInfoDecisionDto dto = StationDecorateConverter.toDecorationInfoDecisionDto(info);
		dto.setAttachments(
				criusAttachmentService.getAttachmentList(info.getId(), AttachmentBizTypeEnum.DECORATION_INFO_DECISION));
		if (info.getStationId() != null) {
			Station s = stationBO.getStationById(info.getStationId());
			if (s != null) {
				dto.setStationDto(StationConverter.toStationDto(s));
			}
		}
		return dto;
	}

	@Override
	public void fixStationDecorate(List<Long> stationIds) {
		for (Long stationId : stationIds) {
			Station station = stationBO.getStationById(stationId);
			StationDecorateDto stationDecorateDto = new StationDecorateDto();
			stationDecorateDto.copyOperatorDto(OperatorDto.defaultOperator());
			stationDecorateDto.setStationId(stationId);
			stationDecorateDto.setPartnerUserId(station.getTaobaoUserId());
			stationDecorateDto.setDecorateType(StationDecorateTypeEnum.NEW_SELF);
			stationDecorateDto.setPaymentType(StationDecoratePaymentTypeEnum.SELF);
			stationDecorateBO.addStationDecorate(stationDecorateDto);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Long uploadStationDecorateDesign(StationDecorateDesignDto stationDecorateDesignDto) {
		Long result = stationDecorateBO.uploadStationDecorateDesign(stationDecorateDesignDto);
		if (result != null) {
			StartProcessDto startProcessDto = new StartProcessDto();
			startProcessDto.setBusiness(ProcessBusinessEnum.decorationDesignAudit);
			startProcessDto.setBusinessId(result);
			Station station = stationBO.getStationById(stationDecorateDesignDto.getStationId());
			startProcessDto.setBusinessName(station.getName() + station.getStationNum());
			startProcessDto.setBusinessOrgId(station.getApplyOrg());
			OperatorDto operator = new OperatorDto();
			operator.setOperator(stationDecorateDesignDto.getOperator());
			operator.setOperatorType(OperatorTypeEnum.HAVANA);
			startProcessDto.copyOperatorDto(operator);
			processService.startApproveProcess(startProcessDto);
		}
		return result;
	}



	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void auditStationDecorateDesign(Long stationId, ProcessApproveResultEnum approveResultEnum,
			String auditOpinion) {
		stationDecorateBO.auditStationDecorateDesign(stationId, approveResultEnum, auditOpinion);
	}


	@Override
	public void auditStationDecorateDesignByCounty(Long stationId, ProcessApproveResultEnum approveResultEnum,
										   String auditOpinion) {
		stationDecorateBO.auditStationDecorateDesignByCounty(stationId, approveResultEnum, auditOpinion);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Long uploadStationDecorateCheck(StationDecorateCheckDto stationDecorateCheckDto) {
		Long result = stationDecorateBO.uploadStationDecorateCheck(stationDecorateCheckDto);
		if (result != null) {
			StartProcessDto startProcessDto = new StartProcessDto();
			startProcessDto.setBusiness(ProcessBusinessEnum.decorationCheckAudit);
			startProcessDto.setBusinessId(result);
			Station station = stationBO.getStationById(stationDecorateCheckDto.getStationId());
			startProcessDto.setBusinessName(station.getName() + station.getStationNum());
			startProcessDto.setBusinessOrgId(station.getApplyOrg());
			OperatorDto operator = new OperatorDto();
			operator.setOperator(stationDecorateCheckDto.getOperator());
			operator.setOperatorType(OperatorTypeEnum.HAVANA);
			startProcessDto.copyOperatorDto(operator);
			processService.startApproveProcess(startProcessDto);
		}
		return result;
	}

	@Override
	public ResultModel<Boolean> uploadStationDecorateCheckForMobile(StationDecorateFeedBackDto stationDecorateFeedBackDto) {
		ResultModel<Boolean> resultModel = new ResultModel<>();
		try {
			//参数校验
			validateDecorationCheck(stationDecorateFeedBackDto);
			//保存url至attachement表
            Long result = stationDecorateBO.uploadStationDecorateFeedback(stationDecorateFeedBackDto);
            if(result != null){
				resultModel.setSuccess(true);
				resultModel.setResult(true);
				return resultModel;
			}
		}catch (IllegalArgumentException e){
			resultModel.setSuccess(true);
			resultModel.setResult(false);
			resultModel.setErrorMessage(e.getMessage());
		}catch (Exception ex){
			resultModel.setSuccess(false);
			resultModel.setResult(false);
			resultModel.setErrorMessage("系统异常");
			logger.error("更新装修反馈信息失败,{stationDecorateFeedBackDto}",stationDecorateFeedBackDto,ex);
		}
		return resultModel;
	}

	@Override
	public ResultModel<StationDecorateFeedBackDto> getStationDecorateFeedBackDtoByUserId(Long taobaoUserId) {

		return stationDecorateBO.queryStationDecorateFeedBackDtoByUserId(taobaoUserId);

	}

	private void validateDecorationCheck(StationDecorateFeedBackDto param) {
		Assert.notNull(param, "参数不可空");
		Assert.notNull(param.getStationId(), "村点ID不能为空");
		Assert.notEmpty(param.getFeedbackDoorPhoto(),"门头完工图不能为空");
		Assert.notEmpty(param.getFeedbackOutsidePhoto(),"室外全景图不能为空");
		Assert.notEmpty(param.getFeedbackOutsideVideo(),"室外视频不能为空");
		Assert.notEmpty(param.getFeedbackInsideVideo(),"室内视频不能为空");
		if(param.getFeedbackInsidePhoto()== null || param.getFeedbackInsidePhoto().size() != 2){
			throw new IllegalArgumentException("室内全景照片要求上传2张");
		}
		if(param.getFeedbackWallDeskPhoto()== null || param.getFeedbackWallDeskPhoto().size() != 2){
			throw new IllegalArgumentException("背景墙和前台桌面要求上传2张");
		}
		if(param.getFeedbackMaterielPhoto()== null || param.getFeedbackMaterielPhoto().size() >5){
			throw new IllegalArgumentException("其他LOGO物料图不超过5张");
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void auditStationDecorateCheck(Long stationId, ProcessApproveResultEnum approveResultEnum,
			String auditOpinion) {
		stationDecorateBO.auditStationDecorateCheck(stationId, approveResultEnum, auditOpinion);
		StationDecorate sd = stationDecorateBO.getStationDecorateByStationId(stationId);
		StationDecorateAuditDto stationDecorateAuditDto = new StationDecorateAuditDto();
		stationDecorateAuditDto.setAuditOpinion(auditOpinion);
		stationDecorateAuditDto.setId(sd.getId());
		stationDecorateAuditDto.setOperator("system");
		stationDecorateAuditDto.setOperatorType(OperatorTypeEnum.SYSTEM);
		stationDecorateAuditDto.setIsAgree(
				ProcessApproveResultEnum.APPROVE_PASS.getCode().equals(approveResultEnum.getCode()) ? true : false);
		setLifecycleDecorate(stationDecorateAuditDto, sd);
	}

	@Override
	public void auditStationDecorateCheckByCountyLeader(Long stationId, ProcessApproveResultEnum approveResultEnum,
			String auditOpinion) {
		stationDecorateBO.auditStationDecorateCheckByCountyLeader(stationId, approveResultEnum, auditOpinion);
	}

	@Override
	public void batchOpenAccessCbuMarket(List<Long> taobaoUserIds) {
//		PartnerStationRelExample example = new PartnerStationRelExample();
//		example.createCriteria().andIsDeletedEqualTo("n").andIsCurrentEqualTo("y").andTypeEqualTo("TP").andStateIn(Lists.newArrayList("SERVICING","DECORATING"));
//		 List<PartnerStationRel> rels = partnerStationRelMapper.selectByExample(example);
//		for(PartnerStationRel rel :rels){
//			try {
//				this.openAccessCbuMarket(rel.getTaobaoUserId());
//				logger.info("OpenAccessCbuMarket success:"+rel.getTaobaoUserId());
//			} catch (Exception e) {
//				logger.info("OpenAccessCbuMarket error:["+e.getMessage()+"]"+rel.getTaobaoUserId());
//			}
//		}
		
		for(Long taobaoUserId :taobaoUserIds){
			try {
				this.openAccessCbuMarket(taobaoUserId);
				logger.info("OpenAccessCbuMarket success:"+taobaoUserId);
			} catch (Exception e) {
				logger.info("OpenAccessCbuMarket error:["+e.getMessage()+"]"+taobaoUserId);
			}
		}
	}
}
