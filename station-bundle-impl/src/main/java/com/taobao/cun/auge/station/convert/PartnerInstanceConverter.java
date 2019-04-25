package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.taobao.cun.auge.station.enums.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.utils.FeatureUtil;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerInstance;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.example.PartnerInstanceExample;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.condition.PartnerInstancePageCondition;
import com.taobao.cun.auge.station.dto.InstanceDto;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.rule.PartnerLifecycleRule;
import com.taobao.cun.auge.station.rule.PartnerLifecycleRuleItem;
import com.taobao.cun.auge.station.rule.PartnerLifecycleRuleParser;

public final class PartnerInstanceConverter {

	private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceConverter.class);

	private PartnerInstanceConverter() {

	}

	public static List<PartnerInstanceDto> convert(List<PartnerInstance> instances) {
		if (CollectionUtils.isEmpty(instances)) {
			return Collections.<PartnerInstanceDto> emptyList();
		}
		List<PartnerInstanceDto> instanceDtos = new ArrayList<PartnerInstanceDto>(instances.size());
		for (PartnerInstance instance : instances) {
			if (null == instance) {
				continue;
			}
			instanceDtos.add(convert(instance));
		}
		return instanceDtos;
	}

	private static PartnerInstanceDto convert(PartnerInstance instance) {
		if (null == instance) {
			return null;
		}

		PartnerInstanceDto instanceDto = new PartnerInstanceDto();

		instanceDto.setId(instance.getId());
		instanceDto.setApplierId(instance.getApplierId());
		instanceDto.setApplyTime(instance.getApplyTime());
		instanceDto.setServiceBeginTime(instance.getServiceBeginTime());
		instanceDto.setServiceEndTime(instance.getServiceEndTime());
		instanceDto.setParentStationId(instance.getParentStationId());

		instanceDto.setState(PartnerInstanceStateEnum.valueof(instance.getState()));
		instanceDto.setBit(instance.getBit());
		instanceDto.setOpenDate(instance.getOpenDate());
		instanceDto.setApplierType(instance.getApplierType());
		instanceDto.setTaobaoUserId(instance.getTaobaoUserId());
		instanceDto.setType(PartnerInstanceTypeEnum.valueof(instance.getType()));
		instanceDto.setIsCurrent(PartnerInstanceIsCurrentEnum.valueof(instance.getIsCurrent()));
		instanceDto.setCloseType(PartnerInstanceCloseTypeEnum.valueof(instance.getCloseType()));
		instanceDto.setVersion(instance.getVersion());
		instanceDto.setMode(instance.getMode());
		instanceDto.setStationId(instance.getStationId());
		instanceDto.setPartnerId(instance.getPartnerId());
		instanceDto.setSellerId(instance.getSellerId());
		instanceDto.setTransStatusEnum(PartnerInstanceTransStatusEnum.valueof(instance.getTransStatus()));
		instanceDto.setStationDto(convertStationDto(instance));
		instanceDto.setPartnerDto(convertPartnerDto(instance));
		
		PartnerLifecycleDto convertLifecycleDto = convertLifecycleDto(instance);
		instanceDto.setPartnerLifecycleDto(convertLifecycleDto);

		if (StringUtils.isNotBlank(instance.getLevel())) {
			PartnerInstanceLevelDto level = new PartnerInstanceLevelDto();
			level.setCurrentLevel(PartnerInstanceLevelEnum.valueof(instance.getLevel()));
			level.setExpectedLevel(PartnerInstanceLevelEnum.valueof(instance.getExpectedLevel()));
			instanceDto.setPartnerInstanceLevel(level);
		}

		try {
			StationApplyStateEnum parseStationApplyState = PartnerLifecycleRuleParser
					.parseStationApplyState(instance.getType(), instance.getState(), convertLifecycleDto);
			instanceDto.setStationApplyState(parseStationApplyState);
		} catch (Exception e) {
			logger.error("新老状态转换失败。PartnerInstance= " + JSONObject.toJSONString(instance), e);
		}
		return instanceDto;
	}

	private static PartnerLifecycleDto convertLifecycleDto(PartnerInstance instance) {
		PartnerLifecycleDto lifecleDto = new PartnerLifecycleDto();

		lifecleDto.setPartnerType(PartnerInstanceTypeEnum.valueof(instance.getType()));
		lifecleDto.setBusinessType(PartnerLifecycleBusinessTypeEnum.valueof(instance.getLifecycleBusinessType()));
		lifecleDto.setSettledProtocol(PartnerLifecycleSettledProtocolEnum.valueof(instance.getSettledProtocol()));
		lifecleDto.setBond(PartnerLifecycleBondEnum.valueof(instance.getBond()));
		lifecleDto.setQuitProtocol(PartnerLifecycleQuitProtocolEnum.valueof(instance.getQuitProtocol()));
		lifecleDto.setLogisticsApprove(PartnerLifecycleLogisticsApproveEnum.valueof(instance.getLogisticsApprove()));
		lifecleDto.setPartnerInstanceId(instance.getId());
		lifecleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.valueof(instance.getCurrentStep()));
		lifecleDto.setRoleApprove(PartnerLifecycleRoleApproveEnum.valueof(instance.getRoleApprove()));
		lifecleDto.setConfirm(PartnerLifecycleConfirmEnum.valueof(instance.getConfirm()));
		lifecleDto.setSystem(PartnerLifecycleSystemEnum.valueof(instance.getSystem()));
		return lifecleDto;
	}
	
	public static List<PartnerInstanceDto> convertRel2Dto(List<PartnerStationRel> psRels) {
		if (CollectionUtils.isEmpty(psRels)) {
			return Collections.<PartnerInstanceDto>emptyList();
		}
		List<PartnerInstanceDto> instanceDtos = new ArrayList<PartnerInstanceDto>(psRels.size());
		for (PartnerStationRel psRel : psRels) {
			if (null == psRel) {
				continue;
			}
			instanceDtos.add(convert(psRel));
		}
		return instanceDtos;
	}

	public static PartnerInstanceDto convert(PartnerStationRel psRel) {
		if (null == psRel) {
			return null;
		}

		PartnerInstanceDto instanceDto = new PartnerInstanceDto();

		instanceDto.setId(psRel.getId());
		instanceDto.setApplierId(psRel.getApplierId());
		instanceDto.setApplyTime(psRel.getApplyTime());
		instanceDto.setServiceBeginTime(psRel.getServiceBeginTime());
		instanceDto.setServiceEndTime(psRel.getServiceEndTime());
		instanceDto.setParentStationId(psRel.getParentStationId());

		instanceDto.setState(PartnerInstanceStateEnum.valueof(psRel.getState()));
		instanceDto.setBit(psRel.getBit());
		instanceDto.setOpenDate(psRel.getOpenDate());
		instanceDto.setApplierType(psRel.getApplierType());
		instanceDto.setType(PartnerInstanceTypeEnum.valueof(psRel.getType()));
		instanceDto.setIsCurrent(PartnerInstanceIsCurrentEnum.valueof(psRel.getIsCurrent()));

		instanceDto.setCloseType(PartnerInstanceCloseTypeEnum.valueof(psRel.getCloseType()));
		instanceDto.setTaobaoUserId(psRel.getTaobaoUserId());
		instanceDto.setStationId(psRel.getStationId());
		instanceDto.setPartnerId(psRel.getPartnerId());
		instanceDto.setVersion(psRel.getVersion());
		instanceDto.setSellerId(psRel.getSellerId());
		instanceDto.setMode(psRel.getMode());
		instanceDto.setTransStatusEnum(PartnerInstanceTransStatusEnum.valueof(psRel.getTransStatus()));
		instanceDto.setShopId(psRel.getShopId());
		instanceDto.setDistributionChannelId(psRel.getDistributionChannelId());
		instanceDto.setDistributorCode(psRel.getDistributorCode());
		instanceDto.setIncomeMode(psRel.getIncomeMode());
		instanceDto.setIncomeModeBeginTime(psRel.getIncomeModeBeginTime());

		return instanceDto;
	}
	
	public static InstanceDto convertToInstanceDto(PartnerStationRel psRel) {
		if (null == psRel) {
			return null;
		}

		InstanceDto instanceDto = new InstanceDto();

		instanceDto.setId(psRel.getId());
		instanceDto.setApplierId(psRel.getApplierId());
		instanceDto.setApplyTime(psRel.getApplyTime());
		instanceDto.setServiceBeginTime(psRel.getServiceBeginTime());
		instanceDto.setServiceEndTime(psRel.getServiceEndTime());
		instanceDto.setParentStationId(psRel.getParentStationId());

		instanceDto.setState(PartnerInstanceStateEnum.valueof(psRel.getState()));
		instanceDto.setBit(psRel.getBit());
		instanceDto.setOpenDate(psRel.getOpenDate());
		instanceDto.setApplierType(psRel.getApplierType());
		instanceDto.setType(InstanceTypeEnum.valueof(psRel.getType()));
		instanceDto.setIsCurrent(PartnerInstanceIsCurrentEnum.valueof(psRel.getIsCurrent()));

		instanceDto.setCloseType(PartnerInstanceCloseTypeEnum.valueof(psRel.getCloseType()));
		instanceDto.setTaobaoUserId(psRel.getTaobaoUserId());
		instanceDto.setStationId(psRel.getStationId());
		instanceDto.setPartnerId(psRel.getPartnerId());
		instanceDto.setVersion(psRel.getVersion());
		instanceDto.setSellerId(psRel.getSellerId());
		instanceDto.setMode(psRel.getMode());
		instanceDto.setTransStatusEnum(PartnerInstanceTransStatusEnum.valueof(psRel.getTransStatus()));
		instanceDto.setShopId(psRel.getShopId());
		instanceDto.setDistributionChannelId(psRel.getDistributionChannelId());
		instanceDto.setDistributorCode(psRel.getDistributorCode());
		return instanceDto;
	}

	public static PartnerInstanceDto convert(PartnerStationRel psRel, Station station, Partner partner) {
		PartnerInstanceDto instanceDto = convert(psRel);

		if (null != instanceDto) {
			StationDto stationDto = StationConverter.toStationDto(station);
			instanceDto.setStationDto(stationDto);

			PartnerDto partnerDto = PartnerConverter.toPartnerDto(partner);
			instanceDto.setPartnerDto(partnerDto);
		}

		return instanceDto;
	}

	private static StationDto convertStationDto(PartnerInstance instance) {
		if (null == instance) {
			return null;
		}
		StationDto stationDto = new StationDto();

		stationDto.setId(instance.getStationId());
		stationDto.setName(instance.getStationName());
		stationDto.setDescription(instance.getStationDescription());
		stationDto.setApplyOrg(instance.getApplyOrg());
		stationDto.setCountyStationName(instance.getCountyStationName());
		stationDto.setStationNum(instance.getStationNum());
		stationDto.setCovered(instance.getCovered());

		stationDto.setProducts(instance.getProducts());
		stationDto.setLogisticsState(StationlLogisticsStateEnum.valueof(instance.getLogisticsState()));
		stationDto.setFormat(instance.getFormat());
		stationDto.setAreaType(StationAreaTypeEnum.valueof(instance.getAreaType()));
		stationDto.setManagerId(instance.getManagerId());
		stationDto.setProviderId(instance.getProviderId());
		stationDto.setFeature(FeatureUtil.toMap(instance.getFeature()));
		stationDto.setStatus(StationStatusEnum.valueof(instance.getStatus()));
		stationDto.setAddress(convertAddress(instance));
		stationDto.setFixedType(StationFixedTypeEnum.valueof(instance.getFixedType()));
		stationDto.setCategory(instance.getCategory());
		stationDto.setPartnerInstanceIsOnTown(PartnerInstanceIsOnTown.valueof(instance.getIsOnTown()));
		return stationDto;
	}

	private static PartnerDto convertPartnerDto(PartnerInstance instance) {
		if (null == instance) {
			return null;
		}
		PartnerDto partnerDto = new PartnerDto();

		partnerDto.setAlipayAccount(instance.getAlipayAccount());
		partnerDto.setTaobaoUserId(instance.getTaobaoUserId());
		partnerDto.setTaobaoNick(instance.getTaobaoNick());
		partnerDto.setIdenNum(instance.getIdenNum());
		partnerDto.setMobile(instance.getMobile());
		partnerDto.setEmail(instance.getEmail());
		partnerDto.setBusinessType(PartnerBusinessTypeEnum.valueof(instance.getBusinessType()));

		partnerDto.setId(instance.getPartnerId());
		partnerDto.setName(instance.getPartnerName());
		partnerDto.setDescription(instance.getPartnerDescription());
		partnerDto.setState(PartnerStateEnum.valueof(instance.getPartnerState()));
        partnerDto.setFlowerName(instance.getFlowerName());
		return partnerDto;
	}

	private static Address convertAddress(PartnerInstance instance) {
		if (null == instance) {
			return null;
		}
		Address address = new Address();

		address.setProvince(instance.getProvince());
		address.setProvinceDetail(instance.getProvinceDetail());

		address.setCity(instance.getCity());
		address.setCityDetail(instance.getCityDetail());

		address.setCounty(instance.getCounty());
		address.setCountyDetail(instance.getCountyDetail());

		address.setTown(instance.getTown());
		address.setTownDetail(instance.getTownDetail());

		address.setVillage(instance.getVillage());
		address.setVillageDetail(instance.getVillageDetail());

		address.setAddressDetail(instance.getAddress());
		address.setLat(instance.getLat());
		address.setLng(instance.getLng());

		return address;
	}

	public static PartnerStationRel convert(PartnerInstanceDto partnerInstanceDto) {
		if (null == partnerInstanceDto) {
			return null;
		}

		PartnerStationRel rel = new PartnerStationRel();

		rel.setId(partnerInstanceDto.getId());
		rel.setApplierId(partnerInstanceDto.getApplierId());
		rel.setApplierType(partnerInstanceDto.getApplierType());
		rel.setApplyTime(partnerInstanceDto.getApplyTime());
		rel.setServiceBeginTime(partnerInstanceDto.getServiceBeginTime());
		rel.setServiceEndTime(partnerInstanceDto.getServiceEndTime());
		rel.setParentStationId(partnerInstanceDto.getParentStationId());
		rel.setMode(partnerInstanceDto.getMode());
		if (null != partnerInstanceDto.getState()) {
			rel.setState(partnerInstanceDto.getState().getCode());
		}

		rel.setBit(partnerInstanceDto.getBit());
		rel.setOpenDate(partnerInstanceDto.getOpenDate());

		if (null != partnerInstanceDto.getType()) {
			rel.setType(partnerInstanceDto.getType().getCode());
		}
		if (null != partnerInstanceDto.getIsCurrent()) {
			rel.setIsCurrent(partnerInstanceDto.getIsCurrent().getCode());
		}
		if (null != partnerInstanceDto.getCloseType()) {
			rel.setCloseType(partnerInstanceDto.getCloseType().getCode());
		}
		rel.setTaobaoUserId(partnerInstanceDto.getTaobaoUserId());
		rel.setStationId(partnerInstanceDto.getStationId());
		rel.setPartnerId(partnerInstanceDto.getPartnerId());
		rel.setVersion(partnerInstanceDto.getVersion());
		rel.setSellerId(partnerInstanceDto.getSellerId());
		rel.setTransStatus(partnerInstanceDto.getTransStatusEnum() == null? null:partnerInstanceDto.getTransStatusEnum().getCode());
		return rel;
	}

	public static PartnerInstanceExample convert(PartnerInstancePageCondition condition) {
		PartnerInstanceExample example = new PartnerInstanceExample();

		if (null == condition) {
			return example;
		}

		if (StringUtil.isNotBlank(condition.getStationNum())) {
			example.setStationNum(condition.getStationNum());
		}
		if (StringUtil.isNotBlank(condition.getStationName())) {
			example.setStationName(condition.getStationName());
		}
		if (StringUtil.isNotBlank(condition.getManagerId())) {
			example.setManagerId(condition.getManagerId());
		}
		if (null != condition.getProviderId() && 0L != condition.getProviderId()) {
			example.setProviderId(condition.getProviderId());
		}
		Address address = condition.getAddress();
		if (null != address) {
			example.setProvince(address.getProvince());
			example.setCity(address.getCity());
			example.setCounty(address.getCounty());
			example.setTown(address.getTown());
		}

		if (StringUtil.isNotBlank(condition.getTaobaoNick())) {
			example.setTaobaoNick(condition.getTaobaoNick());
		}

		PartnerInstanceTypeEnum partnerType = condition.getPartnerType();
		if (null != partnerType) {
			example.setPartnerType(partnerType.getCode());
		}

		if (null != condition.getPartnerInstanceLevel()) {
			example.setPartnerInstanceLevel(condition.getPartnerInstanceLevel().getLevel().toString());
		}

		if (StringUtil.isNotBlank(condition.getPartnerName())) {
			example.setPartnerName(condition.getPartnerName());
		}

		if (StringUtil.isNotBlank(condition.getOrgIdPath())) {
			example.setOrgIdPath(condition.getOrgIdPath());
		}
		
		if (StringUtil.isNotBlank(condition.getFullIdPath())) {
			example.setFullIdPath(condition.getFullIdPath());
		}

		StationApplyStateEnum stationApplyState = condition.getStationApplyState();
		if (null != stationApplyState) {
			PartnerLifecycleRule rule = PartnerLifecycleRuleParser.parsePartnerLifecycleRule(partnerType,
					stationApplyState.getCode());

			PartnerInstanceStateEnum instanceState = rule.getState();
			if (null != instanceState) {
				example.setPartnerState(instanceState.getCode());
			}

			if (PartnerInstanceStateEnum.SETTLING.equals(instanceState)
					|| PartnerInstanceStateEnum.CLOSING.equals(instanceState)
					|| PartnerInstanceStateEnum.QUITING.equals(instanceState)) {
				example.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING.getCode());
			} else {
				example.setCurrentStep(PartnerLifecycleCurrentStepEnum.END.getCode());
			}

			if (null != rule.getBusinessType()) {
				example.setBusinessType(rule.getBusinessType().getCode());
			}

			PartnerLifecycleRuleItem settledProtocol = rule.getSettledProtocol();
			if (null != settledProtocol) {
				example.setSettledProtocol(settledProtocol.getValue());
				example.setSettledProtocolOp(settledProtocol.getEqual());
			}

			PartnerLifecycleRuleItem bond = rule.getBond();
			if (null != bond) {
				example.setBond(bond.getValue());
				example.setBondOp(bond.getEqual());
			}

			PartnerLifecycleRuleItem roleApprove = rule.getRoleApprove();
			if (null != roleApprove) {
				example.setRoleApprove(roleApprove.getValue());
				example.setRoleApproveOp(roleApprove.getEqual());
			}

			PartnerLifecycleRuleItem quitProtocol = rule.getQuitProtocol();
			if (null != quitProtocol) {
				example.setQuitProtocol(quitProtocol.getValue());
				example.setQuitProtocolOp(quitProtocol.getEqual());
			}

			PartnerLifecycleRuleItem logisticsApprove = rule.getLogisticsApprove();
			if (null != logisticsApprove) {
				example.setLogisticsApprove(logisticsApprove.getValue());
				example.setLogisticsApproveOp(logisticsApprove.getEqual());
			}

			PartnerLifecycleRuleItem confirm = rule.getConfirm();
			if (null != confirm) {
				example.setConfirm(confirm.getValue());
				example.setConfirmOp(confirm.getEqual());
			}

			PartnerLifecycleRuleItem system = rule.getSystem();
			if (null != system) {
				example.setSystem(system.getValue());
				example.setSystemOp(system.getEqual());
			}
			
			PartnerLifecycleRuleItem decorate=rule.getDecorateStatus();
			if(null!=decorate){
				example.setDecorateStatus(decorate.getValue());
			}
			
			PartnerLifecycleRuleItem course=rule.getCourseStatus();
			if(null!=course){
				example.setCourseStatus(course.getValue());
			}
		}
		
		//手机端调用，会传instancestate
		if (null != condition.getInstanceState()) {
			example.setPartnerState(condition.getInstanceState().getCode());
			if(PartnerInstanceStateEnum.SETTLING.equals(condition.getInstanceState())){
				example.setBusinessType(PartnerLifecycleBusinessTypeEnum.SETTLING.getCode());
				example.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING.getCode());
			}
			if(PartnerInstanceStateEnum.CLOSING.equals(condition.getInstanceState())){
				example.setBusinessType(PartnerLifecycleBusinessTypeEnum.CLOSING.getCode());
				example.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING.getCode());
			}
			if(PartnerInstanceStateEnum.QUITING.equals(condition.getInstanceState())){
				example.setBusinessType(PartnerLifecycleBusinessTypeEnum.QUITING.getCode());
				example.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING.getCode());
			}
		}
		 
		if (null != condition.getParentStationId()) {
			example.setParentStationId(condition.getParentStationId());
		}
		
		if (null != condition.getIsCurrent()) {
			example.setIsCurrent(Boolean.TRUE.equals(condition.getIsCurrent()) ? "y" : "n");
		}
		if(null != condition.getTpCategoryEnum()){
			example.setCategory(condition.getTpCategoryEnum().getCode());
		}
		if(!Strings.isNullOrEmpty(condition.getOwnDept())) {
			example.setOwnDept(condition.getOwnDept());
		}

		if(!Strings.isNullOrEmpty(condition.getMode())){
			example.setMode(condition.getMode());
		}

		if (condition.getBizTypeEnum() != null) {
			if (condition.getBizTypeEnum().equals(StationBizTypeEnum.STATION)) {
				example.setModeIsNull("y");
				example.setCategoryIsNull("y");
			}
			if (condition.getBizTypeEnum().equals(StationBizTypeEnum.YOUPIN)) {
				example.setMode(StationModeEnum.V4.getCode());
				example.setCategoryIsNull("y");
			}
			if (condition.getBizTypeEnum().equals(StationBizTypeEnum.YOUPIN_ELEC)) {
				example.setMode(StationModeEnum.V4.getCode());
				example.setCategory(TPCategoryEnum.ELEC.getCode());
			}

		}
		return example;
	}
	
	public static PartnerInstanceStateChangeEnum convertClosingStateChange(PartnerStationRel partnerStationRel) {
		if (PartnerInstanceStateEnum.DECORATING.getCode().equals(partnerStationRel.getState())) {
			return PartnerInstanceStateChangeEnum.DECORATING_CLOSING;
		} else if (PartnerInstanceStateEnum.SERVICING.getCode().equals(partnerStationRel.getState())) {
			return PartnerInstanceStateChangeEnum.START_CLOSING;
		} else {
			// 状态校验,只有装修中，或者服务中可以停业
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"只有服务中、装修中服务站才可以申请停业,当前服务站是"+PartnerInstanceStateEnum.valueof(partnerStationRel.getState()).getDesc());
		}
	}
}
