package com.taobao.cun.auge.station.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.appResource.dto.AppResourceDto;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.StationDecorate;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationDecorateBO;
import com.taobao.cun.auge.station.bo.StationDecorateOrderBO;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.StationDecorateAuditDto;
import com.taobao.cun.auge.station.dto.StationDecorateDto;
import com.taobao.cun.auge.station.dto.StationDecorateOrderDto;
import com.taobao.cun.auge.station.dto.StationDecorateReflectDto;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleDecorateStatusEnum;
import com.taobao.cun.auge.station.enums.StationDecoratePaymentTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecorateStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.service.StationDecorateService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("stationDecorateService")
@HSFProvider(serviceInterface = StationDecorateService.class)
public class StationDecorateServiceImpl implements StationDecorateService {
	
	private static final Logger logger = LoggerFactory.getLogger(StationDecorateService.class);
	
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
	
	@Override
	public void audit(StationDecorateAuditDto stationDecorateAuditDto) throws AugeServiceException {
		// 参数校验
		BeanValidator.validateWithThrowable(stationDecorateAuditDto);
		if (!stationDecorateAuditDto.getIsAgree()) {
			String error = getErrorMessage("audit",JSONObject.toJSONString(stationDecorateAuditDto), "is agree is false");
			logger.error(error);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
		try {
			Long sdId = stationDecorateAuditDto.getId();
			StationDecorate sd = stationDecorateBO.getStationDecorateById(sdId);
			if (sd == null) {
				String error = getErrorMessage("audit",JSONObject.toJSONString(stationDecorateAuditDto), "StationDecorate is null");
				logger.error(error);
				throw new AugeServiceException(CommonExceptionEnum.DATA_UNNORMAL);
			}
			//审批服务站装修记录
			auditStationDecorate(stationDecorateAuditDto);
			//更新生命周期表为已完成
			setLifecycleDecorate(stationDecorateAuditDto, sd);
		} catch (AugeServiceException augeException) {
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("audit", JSONObject.toJSONString(stationDecorateAuditDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	private void setLifecycleDecorate(
			StationDecorateAuditDto stationDecorateAuditDto, StationDecorate sd) {
		PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(sd.getPartnerUserId());
		if (rel == null) {
			String error = getErrorMessage("audit",JSONObject.toJSONString(stationDecorateAuditDto), "PartnerStationRel is null");
			logger.error(error);
			throw new AugeServiceException(CommonExceptionEnum.DATA_UNNORMAL);
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
		partnerLifecycleDto.setDecorateStatus(PartnerLifecycleDecorateStatusEnum.Y);
		partnerLifecycleDto.copyOperatorDto(stationDecorateAuditDto);
		partnerLifecycleBO.updateLifecycle(partnerLifecycleDto);
	}

	private void auditStationDecorate(
			StationDecorateAuditDto stationDecorateAuditDto) {
		StationDecorateDto sdDto =new StationDecorateDto();
		sdDto.setId(stationDecorateAuditDto.getId());
		sdDto.setAuditOpinion(stationDecorateAuditDto.getAuditOpinion() == null ? "" : stationDecorateAuditDto.getAuditOpinion());
		sdDto.setStatus(StationDecorateStatusEnum.DONE);
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
	public StationDecorateDto getInfoByTaobaoUserId(Long taobaoUserId) throws AugeServiceException {
		ValidateUtils.notNull(taobaoUserId);
		try {
			PartnerStationRel  rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
			if (rel == null) {
				String error = getErrorMessage("getInfoByTaobaoUserId",String.valueOf(taobaoUserId), "rel is null");
				logger.error(error);
				throw new AugeServiceException(CommonExceptionEnum.DATA_UNNORMAL);
			}
			return getInfoByStationId(rel.getStationId());
		} catch (AugeServiceException augeException) {
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("getInfoByTaobaoUserId", JSONObject.toJSONString(taobaoUserId), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 设置 店铺url
	 * @param sdDto
	 */
	private void setShopItemInfo(StationDecorateDto sdDto) {
		try {
			if (sdDto != null && StringUtils.isNotEmpty(sdDto.getSellerTaobaoUserId())) {
				AppResourceDto resource = appResourceService.queryAppResource("shop_Item_info", sdDto.getSellerTaobaoUserId());
				if (resource != null && !StringUtils.isEmpty(resource.getValue())) {
					sdDto.setTaobaoItemUrl(taobaoItemUrl+resource.getValue());
				}
			}
		} catch (Exception e) {
			logger.error("setShopInfo error: key"+sdDto.getSellerTaobaoUserId());
		}
	}
	@Override
	public void reflectStationDecorate(StationDecorateReflectDto stationDecorateReflectDto) throws AugeServiceException {
		// 参数校验
		BeanValidator.validateWithThrowable(stationDecorateReflectDto);
		try {
			StationDecorate sd = stationDecorateBO.getStationDecorateById(stationDecorateReflectDto.getId());
			if (sd == null) {
				throw new AugeServiceException("查询不到当前装修记录");
			}
			if (StationDecorateStatusEnum.UNDECORATE.getCode().equals(sd.getStatus())||
					StationDecorateStatusEnum.DONE.getCode().equals(sd.getStatus())) {
				throw new AugeServiceException("当前状态不能提交反馈");
			}
			//判断村点是否装修中状态，非装修中状态 不允许反馈
//			Station station=stationBO.getStationById(sd.getStationId());
//			if(!StationStatusEnum.DECORATING.getCode().equals(station.getStatus())){
//				throw new AugeServiceException("当前村点非装修状态");
//			}
			StationDecorateDto sdDto = buildStationDecorateDtoForReflect(stationDecorateReflectDto);
			stationDecorateBO.updateStationDecorate(sdDto);
		} catch (AugeServiceException augeException) {
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("reflectStationDecorate", JSONObject.toJSONString(stationDecorateReflectDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
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
		sdDto.setAttachements(stationDecorateReflectDto.getAttachements());
		sdDto.copyOperatorDto(stationDecorateReflectDto);
		return sdDto;
		
	}
	
	public List<StationDecorateDto> getStationDecorateListForSchedule(int pageNum,int pageSize){
		return stationDecorateBO.getStationDecorateListForSchedule(pageNum, pageSize);
	}

	
	public int getStationDecorateListCountForSchedule(){
		return stationDecorateBO.getStationDecorateListCountForSchedule();
	}

	@Override
	public void updateStationDecorate(StationDecorateDto stationDecorateDto) {
		 stationDecorateBO.updateStationDecorate(stationDecorateDto);
	}

	@Override
	public void syncStationDecorateFromTaobao(
			StationDecorateDto stationDecorateDto) throws AugeServiceException {
		stationDecorateBO.syncStationDecorateFromTaobao(stationDecorateDto);
		
	}

	@Override
	public StationDecorateDto getInfoByStationId(Long stationId)
			throws AugeServiceException {
		ValidateUtils.notNull(stationId);
		try {
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
		} catch (AugeServiceException augeException) {
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("getInfoByStationId", String.valueOf(stationId), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}

	@Override
	public Map<Long, StationDecorateStatusEnum> getStatusByStationId(
			List<Long> stationIds) throws AugeServiceException {
		return stationDecorateBO.getStatusByStationId(stationIds);
	}

	@Override
	public String getReflectUrl(Long taobaoUserId) throws AugeServiceException {
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
			throw new RuntimeException("村点装修状态不允许退出，请先审核装修反馈记录");
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
}
