package com.taobao.cun.auge.station.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.appResource.dto.AppResourceDto;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.attachment.enums.AttachmentBizTypeEnum;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StationDecorate;
import com.taobao.cun.auge.dal.domain.StationDecorateExample;
import com.taobao.cun.auge.dal.domain.StationDecorateExample.Criteria;
import com.taobao.cun.auge.dal.mapper.StationDecorateMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.org.service.OrgRangeType;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationDecorateBO;
import com.taobao.cun.auge.station.bo.StationDecorateOrderBO;
import com.taobao.cun.auge.station.convert.OperatorConverter;
import com.taobao.cun.auge.station.convert.StationConverter;
import com.taobao.cun.auge.station.convert.StationDecorateConverter;
import com.taobao.cun.auge.station.dto.StationDecorateDto;
import com.taobao.cun.auge.station.dto.StationDecorateOrderDto;
import com.taobao.cun.auge.station.enums.StationDecorateIsValidEnum;
import com.taobao.cun.auge.station.enums.StationDecoratePaymentTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecorateStatusEnum;
import com.taobao.cun.auge.station.enums.StationDecorateTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;


@Component("stationDecorateBO")
public class StationDecorateBOImpl implements StationDecorateBO {
	
	private static final Logger logger = LoggerFactory.getLogger(StationDecorateBO.class);

	@Autowired
	StationDecorateMapper stationDecorateMapper;
	@Autowired
	AppResourceService appResourceService;
	@Autowired
	CuntaoOrgServiceClient cuntaoOrgServiceClient;
	@Autowired
	StationBO stationBO;
    @Autowired
    AttachmentService criusAttachmentService;
	@Autowired
	StationDecorateOrderBO stationDecorateOrderBO;
	
	@Override
	public StationDecorate addStationDecorate(StationDecorateDto stationDecorateDto)
			 {
		Long stationId = stationDecorateDto.getStationId();
		validateAddDecorate(stationDecorateDto);
		StationDecorate record;
//			StationDecorate sd = this.getStationDecorateByStationId(stationId);
//			if (sd != null) {
//				return sd;
//			}
			//更新历史装修记录的有效性状态
			updateOldDecorateRecordInvalid(stationId);
			record = StationDecorateConverter.toStationDecorate(stationDecorateDto);
			//添加店铺id,仅自费装修需要添加
			if (record.getSellerTaobaoUserId() == null
					&& StationDecoratePaymentTypeEnum.SELF.getCode().equals(
							record.getPaymentType())) {
				record.setSellerTaobaoUserId(getSeller(stationId));
			}
			record.setStatus(StationDecorateStatusEnum.UNDECORATE.getCode());
			record.setIsValid(StationDecorateIsValidEnum.Y.getCode());
			DomainUtils.beforeInsert(record, stationDecorateDto.getOperator());
			stationDecorateMapper.insert(record);
			return record;
	}
	
	private void validateAddDecorate(StationDecorateDto stationDecorateDto){
		ValidateUtils.notNull(stationDecorateDto);
		Long stationId = stationDecorateDto.getStationId();
		ValidateUtils.notNull(stationId);
		ValidateUtils.notNull(stationDecorateDto.getPaymentType());
		ValidateUtils.notNull(stationDecorateDto.getDecorateType());
	}
	
	private void updateOldDecorateRecordInvalid(Long stationId){
		Map<String,String> param=new HashMap<String,String>();
		param.put("valid", StationDecorateIsValidEnum.N.getCode());
		param.put("stationId", String.valueOf(stationId));
		stationDecorateMapper.invalidOldDecorateRecord(param);
	}
	
	private String getSeller(Long stationId) {
		Station station = stationBO.getStationById(stationId);
		if (station == null) {
			logger.error("stationBO.getStationById is null"+stationId);
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"station is null");
		}
		return getStationDecorateSellerInfo(station);
	}

	/**
	 * 村点装修根据村点所在org获取对应卖家信息,规则如下:
	 * 1.先从省一级卖家信息看,如果没有的话
	 * 2.获取所在大区的卖家信息看
	 * @param station
	 * @return
	 */
	private String getStationDecorateSellerInfo(Station station) {
		if (station == null || station.getApplyOrg() == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"非法的村点对象!");
		}
		CuntaoOrgDto coDto = cuntaoOrgServiceClient.getAncestor(station.getApplyOrg(), OrgRangeType.PROVINCE);
		AppResourceDto resource = appResourceService.queryAppResource("decorate_Selller", String.valueOf(coDto.getId()));
		if (resource == null) {
			coDto = cuntaoOrgServiceClient.getAncestor(station.getApplyOrg(), OrgRangeType.LARGE_AREA);
			resource = appResourceService.queryAppResource("decorate_Selller", String.valueOf(coDto.getId()));
		}
		if (resource == null || StringUtils.isEmpty(resource.getValue())) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"找不到装修卖家信息,station org:" + station.getApplyOrg());
		}
		return resource.getValue();
	}


	@Override
	public List<StationDecorateDto> getStationDecorateListForSchedule(int pageNum,int pageSize)
			 {
		if (pageNum < 0) {
			pageNum = 1;
		}
		if (pageSize < 0) {
			pageSize = 300;
		}
		
		StationDecorateExample example = buildExampleForSchedule();
		PageHelper.startPage(pageNum, pageSize);
		List<StationDecorate> sdList = stationDecorateMapper.selectByExample(example);
		return StationDecorateConverter.toStationDecorateDtos(sdList);
	}

	private StationDecorateExample buildExampleForSchedule() {
		StationDecorateExample example = new StationDecorateExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		List<String> statusList = new ArrayList<String>();
		statusList.add(StationDecorateStatusEnum.UNDECORATE.getCode());
		statusList.add(StationDecorateStatusEnum.DECORATING.getCode());
		criteria.andStatusIn(statusList);
		List<String> paymentTypeList=new ArrayList<String>();
		paymentTypeList.add(StationDecoratePaymentTypeEnum.SELF.getCode());
		criteria.andPaymentTypeIn(paymentTypeList);
		return example;
	}
	
	@Override
	public int getStationDecorateListCountForSchedule()
			 {
		StationDecorateExample example = buildExampleForSchedule();
		return stationDecorateMapper.countByExample(example);
	}

	@Override
	public void updateStationDecorate(StationDecorateDto stationDecorateDto)
			 {
		ValidateUtils.validateParam(stationDecorateDto);
		ValidateUtils.notNull(stationDecorateDto.getId());
		StationDecorate record = StationDecorateConverter.toStationDecorate(stationDecorateDto);
		DomainUtils.beforeUpdate(record, stationDecorateDto.getOperator());
		
		if(stationDecorateDto.getAttachments() != null){
			criusAttachmentService.modifyAttachementBatch(stationDecorateDto.getAttachments(), stationDecorateDto.getId(), AttachmentBizTypeEnum.STATION_DECORATE, OperatorConverter.convert(stationDecorateDto));
		}
		stationDecorateMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public StationDecorateDto getStationDecorateDtoByStationId(Long stationId)
			 {
		ValidateUtils.notNull(stationId);
		StationDecorate sd = getStationDecorateByStationId(stationId);
		if (sd == null) {
			return null;
		}
		StationDecorateDto sdDto = StationDecorateConverter.toStationDecorateDto(sd);
		//添加附件
		sdDto.setAttachments(criusAttachmentService.getAttachmentList(sd.getId(), AttachmentBizTypeEnum.STATION_DECORATE));
		if (sdDto.getStationId() != null) {
			Station s = stationBO.getStationById(stationId);
			if (s != null) {
				sdDto.setStationDto(StationConverter.toStationDto(s));
			}
		}
		return sdDto; 
	}

	@Override
	public StationDecorate getStationDecorateByStationId(Long stationId)
			 {
		ValidateUtils.notNull(stationId);
		StationDecorateExample example = new StationDecorateExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andStationIdEqualTo(stationId);
		criteria.andIsValidEqualTo(StationDecorateIsValidEnum.Y.getCode());
		List<StationDecorate> resList = stationDecorateMapper.selectByExample(example);
		return ResultUtils.selectOne(resList);
	}

	@Override
	public void syncStationDecorateFromTaobao(
			StationDecorateDto stationDecorateDto)  {
		if(!StationDecoratePaymentTypeEnum.SELF.getCode().equals(
				stationDecorateDto.getPaymentType().getCode())){
			return;
		}
		StationDecorateDto updateDto = new StationDecorateDto();
		updateDto.copyOperatorDto(OperatorDto.defaultOperator());
		updateDto.setId(stationDecorateDto.getId());
		if(StationDecorateStatusEnum.UNDECORATE.getCode().equals(stationDecorateDto.getStatus().getCode())){
			StationDecorateOrderDto decorateOrder = null;
//			if(StringUtils.isNotEmpty(stationDecorateDto.getTaobaoOrderNum())){
//				decorateOrder =	stationDecorateOrderBO.getDecorateOrderById(Long.parseLong(stationDecorateDto.getTaobaoOrderNum())).orElse(null);
//			}else{
				decorateOrder =	stationDecorateOrderBO.getDecorateOrder(Long.parseLong(stationDecorateDto.getSellerTaobaoUserId()), stationDecorateDto.getPartnerUserId()).orElse(null);
//			}
			 if(decorateOrder != null){
				if(decorateOrder.isPaid()){
					updateDto.setStatus(StationDecorateStatusEnum.DECORATING);
					updateDto.setTaobaoOrderNum(decorateOrder.getBizOrderId()+"");
					updateStationDecorate(updateDto);
				}else{
					if(!decorateOrder.isRefund()){
						updateDto.setTaobaoOrderNum(decorateOrder.getBizOrderId()+"");
						updateStationDecorate(updateDto);
					}else{
						updateDto.setStatus(StationDecorateStatusEnum.UNDECORATE);
						updateDto.setTaobaoOrderNum("");
						updateStationDecorate(updateDto);
					}
				}
			}	
		}if(StationDecorateStatusEnum.DECORATING.getCode().equals(stationDecorateDto.getStatus().getCode())){
			StationDecorateOrderDto decorateOrder =	stationDecorateOrderBO.getDecorateOrderById(Long.parseLong(stationDecorateDto.getTaobaoOrderNum())).orElse(null);
			if(decorateOrder != null)  {
				if(decorateOrder.isRefund()){
					updateDto.setStatus(StationDecorateStatusEnum.UNDECORATE);
					updateDto.setTaobaoOrderNum("");
					updateStationDecorate(updateDto);
				}
			}
		}
	}

	@Override
	public StationDecorate getStationDecorateById(Long id)
			 {
		ValidateUtils.notNull(id);
		return stationDecorateMapper.selectByPrimaryKey(id);
	}

	@Override
	public Map<Long, StationDecorateStatusEnum> getStatusByStationId(
			List<Long> stationIds)  {
		ValidateUtils.notEmpty(stationIds);
		StationDecorateExample example = new StationDecorateExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andStationIdIn(stationIds);
		criteria.andIsValidEqualTo(StationDecorateIsValidEnum.Y.getCode());
		List<StationDecorate> resList = stationDecorateMapper.selectByExample(example);
		if (!CollectionUtils.isEmpty(resList)) {
			Map<Long, StationDecorateStatusEnum> res = new HashMap<Long, StationDecorateStatusEnum>();
			for (StationDecorate sd : resList) {
				res.put(sd.getStationId(), StationDecorateStatusEnum.valueof(sd.getStatus()));
			}
			return res;
		}
		return null;
	}

	@Override
	public boolean handleAcessDecorating(Long stationId) {
		ValidateUtils.notNull(stationId);
		StationDecorateExample example = new StationDecorateExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n").andIsValidEqualTo(StationDecorateIsValidEnum.Y.getCode()).andStationIdEqualTo(stationId);
		List<StationDecorate> resList = stationDecorateMapper.selectByExample(example);
		if(resList.size()>0){
			StationDecorate sd=resList.get(0);
			if(StationDecorateTypeEnum.ORIGIN.getCode().equals(sd.getDecorateType())){
				//免装修，直接更新为装修完成
				sd.setGmtModified(new Date());
				sd.setModifier("SYSTEM");
				sd.setStatus(StationDecorateStatusEnum.DONE.getCode());
				stationDecorateMapper.updateByPrimaryKey(sd);
				return true;
			}
		}
		return false;
	}

	@Override
	public void confirmAcessDecorating(Long id) {
		StationDecorate sd = stationDecorateMapper.selectByPrimaryKey(id);
		if (sd == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"not find record "
					+ String.valueOf(id));
		}
		// 只有政府出资的装修 合伙人才能确认
		if (StationDecoratePaymentTypeEnum.GOV_ALL.getCode().equals(
				sd.getPaymentType())
				|| StationDecoratePaymentTypeEnum.GOV_PART.getCode().equals(
						sd.getPaymentType())) {
			sd.setGmtModified(new Date());
			sd.setStatus(StationDecorateStatusEnum.DECORATING.getCode());
			stationDecorateMapper.updateByPrimaryKey(sd);
		} else {
			throw new AugeBusinessException(AugeErrorCodes.DECORATE_PAYMENT_TYPE_ERROR_CODE,"非政府出资装修，无法确认。 "
					+ sd.getPaymentType());
		}

	}

	@Override
	public void invalidStationDecorate(Long stationId) {
		ValidateUtils.notNull(stationId);
		StationDecorateExample example = new StationDecorateExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n")
				.andIsValidEqualTo(StationDecorateIsValidEnum.Y.getCode())
				.andStationIdEqualTo(stationId);
		List<StationDecorate> resList = stationDecorateMapper
				.selectByExample(example);
		if (resList.size() > 0) {
			StationDecorate sd = resList.get(0);
			sd.setGmtModified(new Date());
			sd.setModifier("SYSTEM");
			sd.setStatus(StationDecorateStatusEnum.INVALID.getCode());
			stationDecorateMapper.updateByPrimaryKey(sd);
		}
	}

	
}
