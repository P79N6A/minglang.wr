package com.taobao.cun.auge.station.bo.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StationDecorate;
import com.taobao.cun.auge.dal.domain.StationDecorateExample;
import com.taobao.cun.auge.dal.domain.StationDecorateExample.Criteria;
import com.taobao.cun.auge.dal.mapper.StationDecorateMapper;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgService;
import com.taobao.cun.auge.org.service.OrgRangeType;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.bo.AttachementBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationDecorateBO;
import com.taobao.cun.auge.station.bo.StationDecorateOrderBO;
import com.taobao.cun.auge.station.convert.StationConverter;
import com.taobao.cun.auge.station.convert.StationDecorateConverter;
import com.taobao.cun.auge.station.dto.StationDecorateDto;
import com.taobao.cun.auge.station.dto.StationDecorateOrderDto;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecorateIsValidEnum;
import com.taobao.cun.auge.station.enums.StationDecorateStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;


@Component("stationDecorateBO")
public class StationDecorateBOImpl implements StationDecorateBO {
	
	private static final Logger logger = LoggerFactory.getLogger(StationDecorateBO.class);

	@Autowired
	StationDecorateMapper stationDecorateMapper;
	@Autowired
	AppResourceBO appResourceBO;
	@Autowired
	CuntaoOrgService cuntaoOrgService;
	@Autowired
	StationBO stationBO;
	@Autowired
	AttachementBO attachementBO;
	@Autowired
	StationDecorateOrderBO stationDecorateOrderBO;
	
	@Override
	public StationDecorate addStationDecorate(StationDecorateDto stationDecorateDto)
			throws AugeServiceException {
		ValidateUtils.notNull(stationDecorateDto);
		Long stationId = stationDecorateDto.getStationId();
		ValidateUtils.notNull(stationId);
		StationDecorate record;
		try {
			StationDecorate sd = this.getStationDecorateByStationId(stationId);
			if (sd != null) {
				return sd;
			}
			
			record = StationDecorateConverter.toStationDecorate(stationDecorateDto);
			//添加店铺id
			if (record.getSellerTaobaoUserId() ==null) {
				record.setSellerTaobaoUserId(getSeller(stationId));
			}
			record.setStatus(StationDecorateStatusEnum.UNDECORATE.getCode());
			record.setIsValid(StationDecorateIsValidEnum.Y.getCode());
			DomainUtils.beforeInsert(record, stationDecorateDto.getOperator());
			stationDecorateMapper.insert(record);
			return record;
		} catch (Exception e) {
			logger.error("StationDecorateBO.addStationDecorate.error. param:"+JSONObject.toJSONString(stationDecorateDto),e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
		
	}
	
	private String getSeller(Long stationId) {
		Station station = stationBO.getStationById(stationId);
		if (station == null) {
			logger.error("stationBO.getStationById is null"+stationId);
			throw new AugeServiceException(CommonExceptionEnum.DATA_UNNORMAL);
		}
		
		Long largeAreaOrgId = getLargeAreaOrgId(station);
		return getSellerTaobaoUserId(String.valueOf(largeAreaOrgId));
	}

	private Long getLargeAreaOrgId(Station station) {
		
		CuntaoOrgDto coDto = cuntaoOrgService.getCuntaoOrg(station.getApplyOrg());
		Long largeAreaOrgId = null;
		CuntaoOrgDto cunOrg = coDto;
		
		while(true){ 
			if(OrgRangeType.LARGE_AREA.type.equals(cunOrg.getOrgRangeType())){ 
				largeAreaOrgId = coDto.getId();
				break;
			}
			cunOrg = cunOrg.getParent();
			if (cunOrg == null) {
				break;
			}		
		}
		if (largeAreaOrgId == null) {
			logger.error("getCuntaoOrg error: stationId"+station.getId());
			throw new RuntimeException("getCuntaoOrg error: stationId"+station.getId());
		}
		return largeAreaOrgId;
	}
	
	private String getSellerTaobaoUserId(String key) {
		AppResource resource = appResourceBO.queryAppResource("decorate_Selller", key);
		if (resource != null && !StringUtils.isEmpty(resource.getValue())) {
			return resource.getValue();
		}
		logger.error("getShop error: key"+key);
		throw new RuntimeException("getShop error: key"+key);
	}

	@Override
	public List<StationDecorateDto> getStationDecorateListForSchedule(int pageNum,int pageSize)
			throws AugeServiceException {
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
		return example;
	}
	
	@Override
	public int getStationDecorateListCountForSchedule()
			throws AugeServiceException {
		StationDecorateExample example = buildExampleForSchedule();
		return stationDecorateMapper.countByExample(example);
	}

	@Override
	public void updateStationDecorate(StationDecorateDto stationDecorateDto)
			throws AugeServiceException {
		ValidateUtils.validateParam(stationDecorateDto);
		ValidateUtils.notNull(stationDecorateDto.getId());
		StationDecorate record = StationDecorateConverter.toStationDecorate(stationDecorateDto);
		DomainUtils.beforeUpdate(record, stationDecorateDto.getOperator());
		
		//更新附件
		if (stationDecorateDto.getAttachements() != null) {
			attachementBO.modifyAttachementBatch(stationDecorateDto.getAttachements(), stationDecorateDto.getId(), AttachementBizTypeEnum.STATION_DECORATE, stationDecorateDto);
			
		}
		stationDecorateMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public StationDecorateDto getStationDecorateDtoByStationId(Long stationId)
			throws AugeServiceException {
		ValidateUtils.notNull(stationId);
		StationDecorate sd = getStationDecorateByStationId(stationId);
		if (sd == null) {
			return null;
		}
		StationDecorateDto sdDto = StationDecorateConverter.toStationDecorateDto(sd);
		//添加附件
		sdDto.setAttachements(attachementBO.getAttachementList(sd.getId(), AttachementBizTypeEnum.STATION_DECORATE));
		
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
			throws AugeServiceException {
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
			StationDecorateDto stationDecorateDto) throws AugeServiceException {
		StationDecorateDto updateDto = new StationDecorateDto();
		updateDto.copyOperatorDto(OperatorDto.defaultOperator());
		updateDto.setId(stationDecorateDto.getId());
		if(StationDecorateStatusEnum.UNDECORATE.getCode().equals(stationDecorateDto.getStatus().getCode())){
			StationDecorateOrderDto decorateOrder =	stationDecorateOrderBO.getDecorateOrder(Long.parseLong(stationDecorateDto.getSellerTaobaoUserId()), stationDecorateDto.getPartnerUserId()).orElse(null);
			if(decorateOrder != null){
				if(decorateOrder.isPaid()){
					updateDto.setStatus(StationDecorateStatusEnum.DECORATING);
					updateDto.setTaobaoOrderNum(decorateOrder.getBizOrderId()+"");
					updateStationDecorate(updateDto);
				}else{
					if(!decorateOrder.isRefund()){
						updateDto.setTaobaoOrderNum(decorateOrder.getBizOrderId()+"");
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
			throws AugeServiceException {
		ValidateUtils.notNull(id);
		return stationDecorateMapper.selectByPrimaryKey(id);
	}

	
}
