package com.taobao.cun.auge.station.service.impl;

import java.util.List;
import java.util.Map;

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

import com.taobao.cun.appResource.dto.AppResourceDto;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.attachment.enums.AttachmentBizTypeEnum;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.DecorationInfoDecision;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StationDecorate;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.DecorationInfoDecisionBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationDecorateBO;
import com.taobao.cun.auge.station.bo.StationDecorateOrderBO;
import com.taobao.cun.auge.station.convert.StationConverter;
import com.taobao.cun.auge.station.convert.StationDecorateConverter;
import com.taobao.cun.auge.station.dto.DecorationInfoDecisionDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.StartProcessDto;
import com.taobao.cun.auge.station.dto.StationDecorateAuditDto;
import com.taobao.cun.auge.station.dto.StationDecorateDto;
import com.taobao.cun.auge.station.dto.StationDecorateOrderDto;
import com.taobao.cun.auge.station.dto.StationDecorateReflectDto;
import com.taobao.cun.auge.station.enums.DecorationInfoDecisionStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleDecorateStatusEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.StationDecoratePaymentTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecorateStatusEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.ProcessService;
import com.taobao.cun.auge.station.service.StationDecorateService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("stationDecorateService")
@HSFProvider(serviceInterface = StationDecorateService.class, clientTimeout = 8000)
public class StationDecorateServiceImpl implements StationDecorateService {
	
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
	
	@Override
	public void audit(StationDecorateAuditDto stationDecorateAuditDto){
		// 参数校验
		BeanValidator.validateWithThrowable(stationDecorateAuditDto);
//		if (!stationDecorateAuditDto.getIsAgree()) {
//			String error = getErrorMessage("audit",JSONObject.toJSONString(stationDecorateAuditDto), "is agree is false");
//			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,error);
//		}
			Long sdId = stationDecorateAuditDto.getId();
			StationDecorate sd = stationDecorateBO.getStationDecorateById(sdId);
			if (sd == null) {
				String error = getErrorMessage("audit",JSONObject.toJSONString(stationDecorateAuditDto), "StationDecorate is null");
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
			}
			//审批服务站装修记录
			auditStationDecorate(stationDecorateAuditDto);
			//更新生命周期表为已完成
			setLifecycleDecorate(stationDecorateAuditDto, sd);
	}

	private void setLifecycleDecorate(
			StationDecorateAuditDto stationDecorateAuditDto, StationDecorate sd) {
		PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(sd.getPartnerUserId());
		if (rel == null) {
			String error = getErrorMessage("audit",JSONObject.toJSONString(stationDecorateAuditDto), "PartnerStationRel is null");
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
		}
		
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(rel.getId(),
				PartnerLifecycleBusinessTypeEnum.DECORATING, PartnerLifecycleCurrentStepEnum.PROCESSING);
		//固点3.0 特殊流程，允许先开业，再装修反馈
		if(items==null){
			 items = partnerLifecycleBO.getLifecycleItems(rel.getId(),
					PartnerLifecycleBusinessTypeEnum.DECORATING, PartnerLifecycleCurrentStepEnum.END);
		}
		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
		partnerLifecycleDto.setLifecycleId(items.getId());
		if(stationDecorateAuditDto.getIsAgree()){
		    partnerLifecycleDto.setDecorateStatus(PartnerLifecycleDecorateStatusEnum.Y);
		    partnerLifecycleDto.copyOperatorDto(stationDecorateAuditDto);
		    partnerLifecycleBO.updateLifecycle(partnerLifecycleDto);
        }
	}

	private void auditStationDecorate(
			StationDecorateAuditDto stationDecorateAuditDto) {
		StationDecorateDto sdDto =new StationDecorateDto();
		sdDto.setId(stationDecorateAuditDto.getId());
		if(stationDecorateAuditDto.getIsAgree()){
		    sdDto.setStatus(StationDecorateStatusEnum.DONE);
		}else{
		    sdDto.setStatus(StationDecorateStatusEnum.AUDIT_NOT_PASS);
		}
		sdDto.copyOperatorDto(stationDecorateAuditDto);
		stationDecorateBO.updateStationDecorate(sdDto);
	}
	
	private String getErrorMessage(String methodName, String param, String error) {
		StringBuilder sb = new StringBuilder();
		sb.append("StationDecorateService-Error|").append(methodName).append("(.param=").append(param).append(").").append("errorMessage:")
				.append(error);
		return sb.toString();
	}

	@Override
	public StationDecorateDto getInfoByTaobaoUserId(Long taobaoUserId){
		ValidateUtils.notNull(taobaoUserId);
			PartnerStationRel  rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
			if (rel == null) {
				String error = getErrorMessage("getInfoByTaobaoUserId",String.valueOf(taobaoUserId), "rel is null");
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
			}
			return getInfoByStationId(rel.getStationId());
	}
	
	/**
	 * 设置 店铺url
	 * @param sdDto
	 */
	private void setShopItemInfo(StationDecorateDto sdDto) {
			if (sdDto != null && StringUtils.isNotEmpty(sdDto.getSellerTaobaoUserId())) {
				AppResourceDto resource = appResourceService.queryAppResource("shop_Item_info_v4", sdDto.getSellerTaobaoUserId());
				if (resource != null && !StringUtils.isEmpty(resource.getValue())) {
					sdDto.setTaobaoItemUrl(taobaoItemUrl+resource.getValue());
				}
			}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void reflectStationDecorate(StationDecorateReflectDto stationDecorateReflectDto){
		// 参数校验
		BeanValidator.validateWithThrowable(stationDecorateReflectDto);
			StationDecorate sd = stationDecorateBO.getStationDecorateById(stationDecorateReflectDto.getId());
			if (sd == null) {
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"查询不到当前装修记录");
			}
			if (StationDecorateStatusEnum.UNDECORATE.getCode().equals(sd.getStatus())||
					StationDecorateStatusEnum.DONE.getCode().equals(sd.getStatus())) {
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"当前状态不能提交反馈");
			}
			//判断村点是否装修中状态，非装修中状态 不允许反馈
			
//			if(!StationStatusEnum.DECORATING.getCode().equals(station.getStatus())){
//				throw new AugeServiceException("当前村点非装修状态");
//			}
			StationDecorateDto sdDto = buildStationDecorateDtoForReflect(stationDecorateReflectDto);
			stationDecorateBO.updateStationDecorate(sdDto);
			
			StartProcessDto startProcessDto =new StartProcessDto();
            startProcessDto.setBusiness(ProcessBusinessEnum.decorationFeedback);
            startProcessDto.setBusinessId(sd.getId());
            Station station=stationBO.getStationById(sd.getStationId());
            startProcessDto.setBusinessName(station.getName()+station.getStationNum());
            startProcessDto.setBusinessOrgId(station.getApplyOrg());
            startProcessDto.copyOperatorDto(stationDecorateReflectDto);
            processService.startApproveProcess(startProcessDto);
	}

	private StationDecorateDto buildStationDecorateDtoForReflect(
			StationDecorateReflectDto stationDecorateReflectDto) {
		StationDecorateDto sdDto =new StationDecorateDto();
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
	public List<StationDecorateDto> getStationDecorateListForSchedule(int pageNum, int pageSize){
		return stationDecorateBO.getStationDecorateListForSchedule(pageNum, pageSize);
	}

	
	@Override
	public int getStationDecorateListCountForSchedule(){
		return stationDecorateBO.getStationDecorateListCountForSchedule();
	}

	@Override
	public void updateStationDecorate(StationDecorateDto stationDecorateDto) {
		 stationDecorateBO.updateStationDecorate(stationDecorateDto);
	}

	@Override
	public void syncStationDecorateFromTaobao(
			StationDecorateDto stationDecorateDto){
		stationDecorateBO.syncStationDecorateFromTaobao(stationDecorateDto);
		
	}

	@Override
	public StationDecorateDto getInfoByStationId(Long stationId)
			{
		ValidateUtils.notNull(stationId);
			StationDecorateDto sdDto =  null;
			sdDto = stationDecorateBO.getStationDecorateDtoByStationId(stationId);
			if (sdDto == null) {
				return null;
			}
			if(!StationDecoratePaymentTypeEnum.SELF.getCode().equals(
					sdDto.getPaymentType().getCode())){
				return sdDto;
			}
			//容错，因为定时钟更新装修记录有时间差，防止数据不准确，调淘宝接口，更新数据并返回
			if ((StationDecorateStatusEnum.UNDECORATE.equals(sdDto.getStatus()) || StationDecorateStatusEnum.DECORATING
					.equals(sdDto.getStatus()))) {
				stationDecorateBO.syncStationDecorateFromTaobao(sdDto);
				sdDto = stationDecorateBO.getStationDecorateDtoByStationId(stationId);
			}
			
			if (StringUtils.isNotEmpty(sdDto.getTaobaoOrderNum())) {
				StationDecorateOrderDto sdod = stationDecorateOrderBO.getDecorateOrderById(Long.parseLong(sdDto.getTaobaoOrderNum())).orElse(null);
				if (sdod == null) {
					sdDto.setStatus(StationDecorateStatusEnum.NO_ORDER);
				}else {
					if(!sdod.isPaid()) {
						sdDto.setStatus(StationDecorateStatusEnum.WAIT_PAY);
					}
					if (StringUtils.isNotEmpty(sdod.getAuctionPicUrl())) {
						sdod.setAuctionPicUrl(taobaoImageUrl+sdod.getAuctionPicUrl());
					}
					sdDto.setStationDecorateOrderDto(sdod);
					//订单详情
					sdDto.setTaobaoOrderDetailUrl(taobaoOrderDetailUrl+sdDto.getTaobaoOrderNum());
				}
			}else {
				sdDto.setStatus(StationDecorateStatusEnum.NO_ORDER);
			}
			setShopItemInfo(sdDto);
			return sdDto;
	}

	@Override
	public Map<Long, StationDecorateStatusEnum> getStatusByStationId(
			List<Long> stationIds){
		return stationDecorateBO.getStatusByStationId(stationIds);
	}

	@Override
	public String getReflectUrl(Long taobaoUserId){
		StationDecorateDto dto = this.getInfoByTaobaoUserId(taobaoUserId);
		if (dto != null) {
			if (StationDecorateStatusEnum.DECORATING.equals(dto.getStatus())
					||StationDecorateStatusEnum.WAIT_AUDIT.equals(dto.getStatus())) {
				return stationDecorateReflectUrl+dto.getStationId();
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
		if(sdDto==null){
			return;
		}
		if (StationDecorateStatusEnum.WAIT_AUDIT.getCode().equals(
				sdDto.getStatus().getCode())) {
			// 装修反馈待审核，需要小二审核完毕才能退出
			throw new AugeBusinessException(AugeErrorCodes.DECORATE_BUSINESS_CHECK_ERROR_CODE,"村点装修状态不允许退出，请先审核装修反馈记录");
		} 
		//其他状态暂时不做判断，走线下流程
//		if (StationDecorateStatusEnum.DONE.getCode().equals(
//				sdDto.getStatus().getCode())) {
//			// 装修完成，允许退出
//			return;
//		} else if (StationDecorateStatusEnum.WAIT_AUDIT.getCode().equals(
//				sdDto.getStatus().getCode())) {
//			// 装修反馈待审核，需要小二审核完毕才能退出
//			throw new RuntimeException("村点装修状态不允许退出，请先审核装修反馈记录");
//		} 
//		else if (StationDecoratePaymentTypeEnum.SELF.getCode().equals(
//				sdDto.getPaymentType().getCode())) {
//			// 自费装修需要判断装修订单，政府出资的不做判断
//			if (StationDecorateStatusEnum.UNDECORATE.getCode().equals(
//					sdDto.getStatus().getCode())) {
//				// 未下单，允许退出
//				return;
//			} else if(StationDecorateStatusEnum.WAIT_PAY.getCode().equals(
//					sdDto.getStatus().getCode())){
//				throw new RuntimeException("存在未付款装修订单，请先关闭订单");
//			}else{
//				// 判断淘宝装修订单状态，非交易关闭或完结状态，不允许退出
//				stationDecorateOrderBO.judgeTcOrderStatusForQuit(
//						new Long(sdDto.getSellerTaobaoUserId()),
//						sdDto.getPartnerUserId());
//			}
//		}
	}

	@Override
	public void openAccessCbuMarket(Long taobaoUserId) {
		Assert.notNull(taobaoUserId);
		PartnerStationRel rel=partnerInstanceBO.getCurrentPartnerInstanceByTaobaoUserId(taobaoUserId);
		Assert.notNull(rel);
		PartnerInstanceDto partnerInstanceDto=partnerInstanceBO.getPartnerInstanceById(rel.getId());
		//Long taobaoUserId = partnerInstanceDto.getPartnerDto().getTaobaoUserId();
		MemberModel memberModel = memberReadService.findMemberByUserId(taobaoUserId);
		if(memberModel==null||StringUtils.isEmpty(memberModel.getMemberId())){
			throw new AugeBusinessException(AugeErrorCodes.MEMBER_ID_GET_ERROR,
					"memberid获取失败"+partnerInstanceDto.getPartnerDto().getTaobaoNick());
		}
		try{
			String memberId = memberModel.getMemberId();
			QueryOrgStructParam queryparam = new QueryOrgStructParam();
			queryparam.setMemberId(memberModel.getMemberId());
			queryparam.setStatuses(OrgStructStatus.getEffectiveStatus());
			List<OrgStructModel> modelList = orgStructReadService
					.queryOrgStructs(queryparam);
			if (modelList != null && modelList.size() > 0) {
				return;
			}
			OrgStructPostParam param = new OrgStructPostParam();
			param.setCreatorMemberId(memberId);
			param.setCreatorUserId(partnerInstanceDto.getTaobaoUserId());
			param.setMemberId(memberId);
			param.setParentId(parentId);
			Long structId=orgStructWriteService.postOrgStruct(param);
			OrgStructBaseParam pa = new OrgStructBaseParam();
			pa.setOrgStructId(structId);
			pa.setNewStatus(OrgStructStatus.success.getValue());
			orgStructWriteService.modifyBaseInfo(pa);
		}catch(Exception e){
			throw new AugeBusinessException(AugeErrorCodes.CBU_MARKET_ACCESS_ERROR,
					"1688商城授权失败"+partnerInstanceDto.getPartnerDto().getTaobaoNick());
		}
	}

    public StationDecorateDto getInfoById(Long Id) {
        ValidateUtils.notNull(Id);
        StationDecorate sd = stationDecorateBO.getStationDecorateById(Id);
        StationDecorateDto sdDto = StationDecorateConverter.toStationDecorateDto(sd);
      //添加附件
        sdDto.setAttachments(criusAttachmentService.getAttachmentList(sd.getId(), AttachmentBizTypeEnum.STATION_DECORATE));
        if (sdDto.getStationId() != null) {
            Station s = stationBO.getStationById(sd.getStationId());
            if (s != null) {
                sdDto.setStationDto(StationConverter.toStationDto(s));
            }
        }
        return sdDto;
    }

    public DecorationInfoDecisionDto getDecorationDecisionById(Long id) {
        ValidateUtils.notNull(id);
        DecorationInfoDecision info = decorationInfoDecisionBO.queryDecorationInfoById(id);
        DecorationInfoDecisionDto dto = StationDecorateConverter.toDecorationInfoDecisionDto(info);
        //toDOxxxxxxxxxx 待新的文件类型添加后 设置附件
        //dto.setAttachments(criusAttachmentService.getAttachmentList(info.getId(), AttachmentBizTypeEnum.STATION_DECORATE));
        if (info.getStationId() != null) {
            Station s = stationBO.getStationById(info.getStationId());
            if (s != null) {
                dto.setStationDto(StationConverter.toStationDto(s));
            }
        }
        return dto;
    }

    public void auditDecorationDecision(DecorationInfoDecisionDto decorationInfoDecisionDto) {
        // 参数校验
        BeanValidator.validateWithThrowable(decorationInfoDecisionDto);
        Long id = decorationInfoDecisionDto.getId();
        DecorationInfoDecision info = decorationInfoDecisionBO.queryDecorationInfoById(id);
        if (info == null) {
            String error = getErrorMessage("audit",JSONObject.toJSONString(decorationInfoDecisionDto), "decorationInfoDecision is null");
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
        }
        //审批装修图纸信息
        DecorationInfoDecisionDto dto =new DecorationInfoDecisionDto();
        dto.setId(decorationInfoDecisionDto.getId());
        if(decorationInfoDecisionDto.getIsAgree()){
            dto.setStatus(DecorationInfoDecisionStatusEnum.AUDIT_PASS);
        }else{
            dto.setStatus(DecorationInfoDecisionStatusEnum.AUDIT_NOT_PASS);
        }
        dto.copyOperatorDto(decorationInfoDecisionDto);
        decorationInfoDecisionBO.updateDecorationInfo(dto);
    }

    public void updateDecorationDecision(DecorationInfoDecisionDto decorationInfoDecisionDto) {
        decorationInfoDecisionBO.updateDecorationInfo(decorationInfoDecisionDto);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void submitDecorationDecision(DecorationInfoDecisionDto decorationInfoDecisionDto) {
        ValidateUtils.notNull(decorationInfoDecisionDto.getStationId());
        decorationInfoDecisionDto.setStatus(DecorationInfoDecisionStatusEnum.WAIT_AUDIT);
        DecorationInfoDecision rm = decorationInfoDecisionBO.queryDecorationInfoByStationId(decorationInfoDecisionDto.getStationId());
        Long id = null;
        if(rm == null){
            id =  decorationInfoDecisionBO.addDecorationInfoDecision(decorationInfoDecisionDto);
        }else if(rm.getStatus().equals(DecorationInfoDecisionStatusEnum.WAIT_AUDIT.getCode())){
            //同一个村点待审核的装修图纸记录只能有一条
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"装修图纸待审核状态，请勿重复提交");
        }else if(rm.getStatus().equals(DecorationInfoDecisionStatusEnum.AUDIT_NOT_PASS.getCode())){
            decorationInfoDecisionBO.updateDecorationInfo(decorationInfoDecisionDto);
        }
       
       StartProcessDto startProcessDto =new StartProcessDto();
       startProcessDto.setBusiness(ProcessBusinessEnum.decorationInfoDecision);
       startProcessDto.setBusinessId(id);
       Station station=stationBO.getStationById(decorationInfoDecisionDto.getStationId());
       startProcessDto.setBusinessName(station.getName()+station.getStationNum());
       startProcessDto.setBusinessOrgId(station.getApplyOrg());
       startProcessDto.copyOperatorDto(decorationInfoDecisionDto);
       processService.startApproveProcess(startProcessDto);
    }

    public DecorationInfoDecisionDto getDecorationDecisionByStationId(Long stationId) {
        ValidateUtils.notNull(stationId);
        DecorationInfoDecision info = decorationInfoDecisionBO.queryDecorationInfoByStationId(stationId);
        DecorationInfoDecisionDto dto = StationDecorateConverter.toDecorationInfoDecisionDto(info);
        //toDOxxxxxxxxxx 待新的文件类型添加后 设置附件
        //dto.setAttachments(criusAttachmentService.getAttachmentList(info.getId(), AttachmentBizTypeEnum.STATION_DECORATE));
        if (info.getStationId() != null) {
            Station s = stationBO.getStationById(info.getStationId());
            if (s != null) {
                dto.setStationDto(StationConverter.toStationDto(s));
            }
        }
        return dto;
    }
}
