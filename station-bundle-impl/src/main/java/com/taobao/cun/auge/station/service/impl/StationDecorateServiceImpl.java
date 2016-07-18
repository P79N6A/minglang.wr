package com.taobao.cun.auge.station.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.StationDecorate;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
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
	AppResourceBO appResourceBO;
	
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
			StationDecorateDto sdDto =  null;
			sdDto = stationDecorateBO.getStationDecorateDtoByStationId(rel.getStationId());
			if (sdDto == null) {
				return null;
			}
			//容错，因为定时钟更新装修记录有时间差，防止数据不准确，调淘宝接口，更新数据并返回
			if (StationDecorateStatusEnum.UNDECORATE.equals(sdDto.getStatus()) ||
					StationDecorateStatusEnum.DECORATING.equals(sdDto.getStatus())) {
				stationDecorateBO.syncStationDecorateFromTaobao(sdDto);
				sdDto = stationDecorateBO.getStationDecorateDtoByStationId(rel.getStationId());
			}
			
			if (StringUtils.isNotEmpty(sdDto.getTaobaoOrderNum())) {
				StationDecorateOrderDto sdod = stationDecorateOrderBO.getDecorateOrderById(Long.parseLong(sdDto.getTaobaoOrderNum())).orElse(null);
				if (sdod == null) {
					sdDto.setStatus(StationDecorateStatusEnum.NO_ORDER);
				}else {
					if(!sdod.isPaid()) {
						sdDto.setStatus(StationDecorateStatusEnum.WAIT_PAY);
					}
					sdDto.setStationDecorateOrderDto(sdod);
				}
			}
			setShopInfo(sdDto);
			return sdDto;
		} catch (AugeServiceException augeException) {
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("getInfoByTaobaoUserId", JSONObject.toJSONString(taobaoUserId), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 设置 店铺url 和付款url
	 * @param sdDto
	 */
	private void setShopInfo(StationDecorateDto sdDto) {
		if (sdDto != null && StringUtils.isNotEmpty(sdDto.getSellerTaobaoUserId())) {
			AppResource resource = appResourceBO.queryAppResource("shop_info", sdDto.getSellerTaobaoUserId());
			if (resource != null && !StringUtils.isEmpty(resource.getValue())) {
				String str = resource.getValue();
				String[] shopInfo = str.split("##");
				sdDto.setSellerShopUrl(shopInfo[0]);
				sdDto.setSellerPayUrl(shopInfo[1]);
			}
			logger.error("getShop error: key"+sdDto.getSellerTaobaoUserId());
			throw new RuntimeException("getShop error: key"+sdDto.getSellerTaobaoUserId());
		}
	}
	@Override
	public void reflectStationDecorate(StationDecorateReflectDto stationDecorateReflectDto) throws AugeServiceException {
		// 参数校验
		BeanValidator.validateWithThrowable(stationDecorateReflectDto);
		try {
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
}
