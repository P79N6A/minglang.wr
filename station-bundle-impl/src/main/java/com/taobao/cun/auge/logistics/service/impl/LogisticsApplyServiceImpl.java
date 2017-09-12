package com.taobao.cun.auge.logistics.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.cainiao.cuntaonetwork.constants.apply.UserTypeEnum;
import com.alibaba.cainiao.cuntaonetwork.constants.common.ModifierType;
import com.alibaba.cainiao.cuntaonetwork.constants.station.StationConst.StationTypeConstant;
import com.alibaba.cainiao.cuntaonetwork.dto.foundation.FeatureDTO;
import com.alibaba.cainiao.cuntaonetwork.param.Modifier;
import com.alibaba.cainiao.cuntaonetwork.param.station.ApplyStationParam;
import com.alibaba.cainiao.cuntaonetwork.result.Result;
import com.alibaba.cainiao.cuntaonetwork.service.station.StationWriteService;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Longs;
import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.CountyStationExample;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.dal.domain.LogisticsStationApply;
import com.taobao.cun.auge.dal.domain.LogisticsStationApplyExample;
import com.taobao.cun.auge.dal.domain.LogisticsStationApplyExample.Criteria;
import com.taobao.cun.auge.dal.mapper.CountyStationMapper;
import com.taobao.cun.auge.dal.mapper.LogisticsStationApplyMapper;
import com.taobao.cun.auge.logistics.convert.LogisticsStationConverter;
import com.taobao.cun.auge.logistics.dto.LogisticsApplyRequest;
import com.taobao.cun.auge.logistics.dto.LogisticsStationApplyDTO;
import com.taobao.cun.auge.logistics.dto.LogisticsStationDto;
import com.taobao.cun.auge.logistics.dto.LogisticsStationQueryDto;
import com.taobao.cun.auge.logistics.enums.LogisticsStationStateEnum;
import com.taobao.cun.auge.logistics.service.LogisticsApplyService;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.bo.LogisticsStationBO;
import com.taobao.cun.auge.station.dto.CuntaoCainiaoStationRelDto;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;
import com.taobao.cun.auge.station.service.StationQueryService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.cun.common.exceptions.ServiceException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
@Service("logisticsApplyService")
@HSFProvider(serviceInterface= LogisticsApplyService.class)
public class LogisticsApplyServiceImpl implements LogisticsApplyService{

	private static final Logger logger = LoggerFactory.getLogger(LogisticsApplyServiceImpl.class);
	
	@Autowired
	private StationWriteService stationWriteService;
	
	@Autowired
	private StationQueryService stationQueryService;
	
	@Resource
	private CuntaoCainiaoStationRelBO cuntaoCainiaoStationRelBO;
	
	@Resource
	private LogisticsStationBO logisticsStationBO;
	
	@Autowired
	private CountyStationMapper countyStationMapper;
	
	@Resource
	private LogisticsStationApplyMapper logisticsStationApplyMapper;
	
	private Modifier createModifier() {
		Modifier modifier =  new Modifier();
		modifier.setNick(Modifier.SYSTEM_NICK);
		modifier.setType(ModifierType.SYSTEM);
		modifier.setUid(Modifier.SYSTEM_UID);
		return modifier;
	}

	
	private ApplyStationParam createApplyParam(StationDto stationDto,Address address,PartnerDto parnterDto, Long appUserId, UserTypeEnum userTypeEnum,
			StationTypeConstant stationType, Map<String, String> feature, Map<String, String> relFeature){
		ApplyStationParam applyStationParam = new ApplyStationParam();
		
		Assert.notNull(address,"AddressDto is null!");
		
		CountyStation countStationDto = findCountyStationByOrgId(stationDto.getApplyOrg());
		Assert.notNull(countStationDto,"can not find CountyStation by orgId["+stationDto.getApplyOrg()+"]");
		
		CuntaoCainiaoStationRel stationRel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(countStationDto.getId(),CuntaoCainiaoStationRelTypeEnum.COUNTY_STATION);
		Assert.notNull(stationRel,"can not find stationRel by objectId["+countStationDto.getId()+"] and type[COUNTY_STATION]");
		
		applyStationParam.setCountyDomainId(stationRel.getCainiaoStationId());
		applyStationParam.setAuthUserId(parnterDto.getTaobaoUserId());
		applyStationParam.setName(stationDto.getName());
		applyStationParam.setLat(LogisticsStationConverter.converDown(address.getLat()));
		applyStationParam.setLng(LogisticsStationConverter.converDown(address.getLng()));
		applyStationParam.setProvinceId(Longs.tryParse(address.getProvince()));
		applyStationParam.setCityId(Longs.tryParse(Strings.nullToEmpty(address.getCity())));
		applyStationParam.setCountyId(Longs.tryParse(Strings.nullToEmpty(address.getCounty())));
		applyStationParam.setTownId(Longs.tryParse(Strings.nullToEmpty(address.getTown())));
		applyStationParam.setCountryId(Longs.tryParse(Strings.nullToEmpty(address.getVillage())));
		applyStationParam.setAddress(getAddressDetail(address));
		applyStationParam.setCtCode(stationDto.getStationNum());
		applyStationParam.setContact(parnterDto.getName());
		applyStationParam.setMobile(parnterDto.getMobile());
		applyStationParam.setEmail(parnterDto.getEmail());
		applyStationParam.setStationType(stationType);
		applyStationParam.setApplyUserId(appUserId);
		applyStationParam.setApplyUserType(userTypeEnum);
		applyStationParam.setUserId(parnterDto.getTaobaoUserId());
		applyStationParam.setReceiverName(parnterDto.getName());
		applyStationParam.setReceiverMobile(parnterDto.getMobile());
		
		FeatureDTO featureDto =  FeatureDTO.from(feature);
		applyStationParam.setFeature(featureDto);
		
		FeatureDTO relFeatureDto =  FeatureDTO.from(relFeature);
		
		applyStationParam.setRelFeature(relFeatureDto);
		
		return applyStationParam;
	}
	
	private CountyStation findCountyStationByOrgId(Long orgId) {
		if (orgId == null) {
			logger.error("orgId is null ");
			throw new ServiceException("orgId is null");
		}

		CountyStationExample example = new CountyStationExample();
		CountyStationExample.Criteria criteria = example.createCriteria().andIsDeletedEqualTo("n");

		criteria.andOrgIdEqualTo(orgId);

		List<CountyStation> stations = countyStationMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(stations)) {
			logger.error("CountyStation is null.orgId = " + orgId);
			throw new ServiceException("CountyStation is null.orgId = " + orgId);
		}
		return stations.get(0);
	}

	private String getAddressDetail(Address address){
		List<String> details = Lists.newArrayList();
		details.add(StringUtils.defaultIfEmpty(address.getProvinceDetail(), " "));
		details.add(StringUtils.defaultIfEmpty(address.getCityDetail(), " "));
		details.add(StringUtils.defaultIfEmpty(address.getCountyDetail(), " "));
		details.add(StringUtils.defaultIfEmpty(address.getTownDetail(), " "));
		details.add(StringUtils.defaultIfEmpty(address.getAddressDetail(), " "));
		return StringUtils.join(details.toArray(), "^^^");
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	private Long applyCainiaoStation(LogisticsApplyRequest request) {
		BeanValidator.validateWithThrowable(request);
		StationDto stationDto = stationQueryService.getStation(request.getApplierStationId());
		Assert.notNull(stationDto, "can not find Station by  stationId[" + request.getApplierStationId() + "]");
		LogisticsStationQueryDto lsQueryDto = new LogisticsStationQueryDto();
		lsQueryDto.setTaobaoUserId(request.getApplierTaobaoUserId());
		LogisticsStationDto logisticsStation = logisticsStationBO.findLogisticsStation(lsQueryDto);
		Assert.isNull(logisticsStation, "logisticsStation is Exists by applierTaobaoUserId[" + request.getApplierTaobaoUserId() + "]");
		
		CuntaoCainiaoStationRel stationRel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(request.getApplierStationId(),CuntaoCainiaoStationRelTypeEnum.STATION);
		Assert.isNull(stationRel, "CainiaoStationRel is Exists  by  stationId[" + request.getApplierStationId() + "]");
		
		Address address = LogisticsStationConverter.buildAddress(request);
		stationDto.setAddress(address);

		Map<String, String> feature = Maps.newHashMap();
		feature.put("CTCODE", stationDto.getStationNum());
		feature.put("ctpTbUid", request.getTaobaoUserId() + "");
		feature.put("ctpOrgStaId", request.getStationId() + "");
		feature.put("ctpType", request.getCtpType());

		Map<String, String> relFeature = Maps.newHashMap();
		relFeature.put("partnerId", request.getTaobaoUserId() + "");

		PartnerDto parnterDto = new PartnerDto();
		parnterDto.setName(request.getApplierName());
		parnterDto.setMobile(request.getMobile());
		parnterDto.setTaobaoUserId(request.getApplierTaobaoUserId());

		ApplyStationParam applyStationParam = createApplyParam(stationDto, stationDto.getAddress(), parnterDto,
				request.getTaobaoUserId(), UserTypeEnum.FROM_UIC, StationTypeConstant.TAO_HELPER, feature, relFeature);

		Long[] ids = addStationAndRel(request.getApplierStationId(), address, applyStationParam);

		Result<Long> cainiaoStationModel = stationWriteService.applyStation(applyStationParam, createModifier());
		Assert.isTrue(cainiaoStationModel.isSuccess(), "applyStation error! " + cainiaoStationModel.toString());
		Assert.notNull(cainiaoStationModel.getData(), "cainiaoStationModel's data is null");

		LogisticsStationDto updatedLogisticsStationDto = new LogisticsStationDto();
		updatedLogisticsStationDto.setOperator("system");
		updatedLogisticsStationDto.setId(ids[0]);
		updatedLogisticsStationDto.setCainiaoStationId(cainiaoStationModel.getData());
		logisticsStationBO.updateLogisticsStation(updatedLogisticsStationDto);

		CuntaoCainiaoStationRel updateRelDto = new CuntaoCainiaoStationRel();
		updateRelDto.setCainiaoStationId(cainiaoStationModel.getData());
		updateRelDto.setId(ids[1]);
		cuntaoCainiaoStationRelBO.updateCainiaoStationRel(updateRelDto,"system");
		return ids[0];
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Boolean  applyLogisticStation(LogisticsApplyRequest request) {
		try {
			LogisticsStationApplyExample example = new LogisticsStationApplyExample();
			example.createCriteria().andIsDeletedEqualTo("n").andTypeEqualTo("applyLogistics").andApplierStationIdEqualTo(request.getApplierStationId());
			List<LogisticsStationApply>  applies = logisticsStationApplyMapper.selectByExample(example);
			if(!CollectionUtils.isEmpty(applies)) throw new RuntimeException("LogisticStation apply exists!stationId["+request.getApplierStationId()+"]");
			BeanValidator.validateWithThrowable(request);
			StationDto stationDto = stationQueryService.getStation(request.getApplierStationId());
			Assert.notNull(stationDto, "can not find Station by  stationId[" + request.getApplierStationId() + "]");
			LogisticsStationApply apply = new LogisticsStationApply();
			BeanUtils.copyProperties(request, apply);
			apply.setIsDeleted("n");
			apply.setGmtCreate(new Date());
			apply.setGmtModified(new Date());
			apply.setCreator("system");
			apply.setModifer("system");
			apply.setType("applyLogistics");
			apply.setApplyStatus("init");
			apply.setApplierOrgId(stationDto.getApplyOrg());
			int insertValue = logisticsStationApplyMapper.insertSelective(apply);
			return insertValue==1;
		} catch (Exception e) {
			throw new ServiceException("applyLogisticStation error!",e);
		}
	
	}

	private Long[] addStationAndRel(Long stationId, Address address,
			ApplyStationParam applyStationParam) {
		LogisticsStationDto logisticsStationDto = LogisticsStationConverter.convert(applyStationParam, address,LogisticsStationStateEnum.TO_AUDIT);
		Long logisticsStationId = logisticsStationBO.addLogisticsStation(logisticsStationDto);
		
		CuntaoCainiaoStationRelDto relDto = new CuntaoCainiaoStationRelDto();
		relDto.setObjectId(stationId);
		relDto.setCainiaoStationId(logisticsStationId);
		relDto.setType(CuntaoCainiaoStationRelTypeEnum.STATION);
		relDto.setIsOwn("y");
		relDto.setOperator(DomainUtils.DEFAULT_OPERATOR);
		
		
		Long cainiaoStationRelId = cuntaoCainiaoStationRelBO.insertCuntaoCainiaoStationRel(relDto);
		return new Long[]{logisticsStationId,cainiaoStationRelId};
	}

	@Override
	public List<LogisticsStationApplyDTO> getLogisticsStationApply(List<Long> stationIds, Long orgId){
		return getLogisticsStationApply(stationIds, Lists.newArrayList(orgId));
	}

	@Override
	public List<LogisticsStationApplyDTO> getLogisticsStationApply(List<Long> stationIds, List<Long> orgIds) {
		stationIds =  ImmutableSet.copyOf(Iterables.filter(stationIds, Predicates.not(Predicates.isNull()))).asList();
		List<LogisticsStationApplyDTO> results = Lists.newArrayList();
		if(stationIds == null || stationIds.isEmpty()){
			return results;
		}
		
		LogisticsStationApplyExample exmple = new LogisticsStationApplyExample();
		Criteria  criteria = exmple.createCriteria().andIsDeletedEqualTo("n").andApplierStationIdIn(stationIds).andTypeEqualTo("applyLogistics");
		//if(orgId != 1){
		//	criteria.andApplierOrgIdEqualTo(orgId);
		//}
		if(!Iterables.isEmpty(orgIds)){
			criteria.andApplierOrgIdIn(orgIds);
		}
	
		List<LogisticsStationApply>	applies = logisticsStationApplyMapper.selectByExample(exmple);
//		Map<Long,LogisticsStationApply> applyMappings = Maps.uniqueIndex(applies, new Function<LogisticsStationApply,Long>(){
//			@Override
//			public Long apply(LogisticsStationApply input) {
//				return input.getApplierStationId();
//			}
//		});
		Map<Long,LogisticsStationApply> applyMappings = new HashMap<Long,LogisticsStationApply>();
		for(LogisticsStationApply apply:applies){
			applyMappings.put(apply.getApplierStationId(), apply);
		}
		
		
		List<CuntaoCainiaoStationRel> rels = cuntaoCainiaoStationRelBO.findCainiaoStationRels(stationIds);
		
		Map<Long,CuntaoCainiaoStationRel> relMappings = Maps.uniqueIndex(rels, new Function<CuntaoCainiaoStationRel,Long>(){
			@Override
			public Long apply(CuntaoCainiaoStationRel input) {
				return input.getObjectId();
			}
		});
		
		for(Long stationId : stationIds){
			if(stationId == null) continue;
			LogisticsStationApplyDTO dto = new LogisticsStationApplyDTO();
			if(relMappings.containsKey(stationId)){
				CuntaoCainiaoStationRel rel = relMappings.get(stationId);
				//新模型中已经存在，使用新模型中的物流状态
				if(rel.getLogisticsStationId()!=null){
					LogisticsStationDto logisticStationDto = logisticsStationBO.findLogisticStation(rel.getLogisticsStationId());
					setApplyStatus(dto, logisticStationDto);
				}else{
					// 历史数据 降级的淘帮手在新模型中还没有数据
					dto.setApplyStatus("SERVICING");
				}
			}else{
				//如果申请过
				if(applyMappings.containsKey(stationId)){
					LogisticsStationApply apply = applyMappings.get(stationId);
					BeanUtils.copyProperties(apply, dto);
					//理论上apply只可能是init，不是init的石头rel表里肯定已经有数据了，走不到这段逻辑
					if("init".equals(apply.getApplyStatus())){
						dto.setApplyStatus("TO_AUDIT");
					}
				}else{
					//如果没申请过关闭状态
					dto.setApplyStatus("CLOSE");
				}
			}
			dto.setApplierStationId(stationId);
			results.add(dto);
		}
		
		return results;
	}


	private void setApplyStatus(LogisticsStationApplyDTO dto, 	LogisticsStationDto logisticStationDto) {
		if(LogisticsStationStateEnum.TO_AUDIT.getCode().equals(logisticStationDto.getState().getCode())){
			dto.setApplyStatus("TO_AUDIT");
		}else if(LogisticsStationStateEnum.SERVICING.getCode().equals(logisticStationDto.getState().getCode())){
			dto.setApplyStatus("SERVICING");
		}else{
			dto.setApplyStatus("CLOSE");
		}
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Boolean auditLogistcsStationApply(Long applyId, Boolean isPass,String remark) {
		Assert.notNull(applyId,"auditLogistcsStationApply applyId is null");
		LogisticsStationApply record = new LogisticsStationApply();
		record.setId(applyId);
		record.setApplyStatus(isPass?"wait_cainiao_audit":"refuse");
		record.setRemark(remark);
		record.setGmtModified(new Date());
		record.setModifer("system");
		if(isPass){
			LogisticsStationApply apply = logisticsStationApplyMapper.selectByPrimaryKey(applyId);
			LogisticsApplyRequest request = new LogisticsApplyRequest();
			BeanUtils.copyProperties(apply, request);
			Long logisticStationId = this.applyCainiaoStation(request);
			record.setLogisticsStationId(logisticStationId);
		}
		int count = logisticsStationApplyMapper.updateByPrimaryKeySelective(record);
		return count == 1;
	}


	@Override
	public Boolean finishLogisticStationApply(Long logisticsStationId,Boolean isPass) {
		Assert.notNull(logisticsStationId,"finishLogisticStationApply logisticsStationId is null");
		LogisticsStationApplyExample example = new LogisticsStationApplyExample();
		example.createCriteria().andIsDeletedEqualTo("n").andLogisticsStationIdEqualTo(logisticsStationId).andTypeEqualTo("applyLogistics");
		LogisticsStationApply record = new LogisticsStationApply();
		record.setApplyStatus(isPass ? "pass" : "cainiao_refuse");
		record.setGmtModified(new Date());
		record.setModifer("system");
		int count = logisticsStationApplyMapper.updateByExampleSelective(record, example);
		return count == 1;
	}


	@Override
	public LogisticsStationApplyDTO getLogisticsStationApply(Long applyId) {
		LogisticsStationApply apply = logisticsStationApplyMapper.selectByPrimaryKey(applyId);
		LogisticsStationApplyDTO dto = new LogisticsStationApplyDTO();
		BeanUtils.copyProperties(apply, dto);
		return  dto;
	}


}
