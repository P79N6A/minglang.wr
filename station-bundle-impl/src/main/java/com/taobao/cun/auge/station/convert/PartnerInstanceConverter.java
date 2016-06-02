package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.common.lang.StringUtil;
import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.utils.FeatureUtil;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerInstance;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.example.PartnerInstanceExample;
import com.taobao.cun.auge.station.condition.PartnerInstancePageCondition;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.PartnerBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceCloseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceIsCurrentEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.enums.StationFixedTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;

public final class PartnerInstanceConverter {

	private PartnerInstanceConverter() {

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
		if (null != condition.getProviderId() && 0l != condition.getProviderId()) {
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
		if (null != condition.getPartnerInstanceState()
				&& StringUtil.isNotBlank(condition.getPartnerInstanceState().getCode())) {
			example.setPartnerState(condition.getPartnerInstanceState().getCode());
		}

		// if (null != condition.getPartnerType()) {
		// example.setPartnerType(condition.getPartnerType().getCode());
		// }

		if (StringUtil.isNotBlank(condition.getPartnerType())) {
			example.setPartnerType(condition.getPartnerType());
		}

		if (StringUtil.isNotBlank(condition.getPartnerName())) {
			example.setPartnerName(condition.getPartnerName());
		}
		example.setOrgIds(condition.getOrgIds());

		return example;
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
		instanceDto.setStationApplyId(instance.getStationApplyId());
		instanceDto.setType(PartnerInstanceTypeEnum.valueof(instance.getType()));
		instanceDto.setIsCurrent(PartnerInstanceIsCurrentEnum.valueof(instance.getIsCurrent()));
		instanceDto.setCloseType(PartnerInstanceCloseTypeEnum.valueof(instance.getCloseType()));

		instanceDto.setStationId(instance.getStationId());
		instanceDto.setPartnerId(instance.getPartnerId());
		instanceDto.setStationDto(convertStationDto(instance));
		instanceDto.setPartnerDto(convertPartnerDto(instance));
		instanceDto.setPartnerLifecycleDto(null);

		return instanceDto;
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
		instanceDto.setStationApplyId(psRel.getStationApplyId());
		instanceDto.setType(PartnerInstanceTypeEnum.valueof(psRel.getType()));
		instanceDto.setIsCurrent(PartnerInstanceIsCurrentEnum.valueof(psRel.getIsCurrent()));

		instanceDto.setCloseType(PartnerInstanceCloseTypeEnum.valueof(psRel.getCloseType()));

		instanceDto.setStationId(psRel.getStationId());
		instanceDto.setPartnerId(psRel.getPartnerId());
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
		stationDto.setStationNum(instance.getStationNum());
		stationDto.setCovered(instance.getCovered());

		stationDto.setProducts(instance.getProducts());
		stationDto.setLogisticsState(instance.getLogisticsState());
		stationDto.setFormat(instance.getFormat());
		stationDto.setAreaType(instance.getAreaType());
		stationDto.setManagerId(instance.getManagerId());
		stationDto.setProviderId(instance.getProviderId());
		stationDto.setFeature(FeatureUtil.toMap(instance.getFeature()));
		stationDto.setStatus(StationStatusEnum.valueof(instance.getStatus()));
		stationDto.setAddress(convertAddress(instance));
		stationDto.setFixedType(StationFixedTypeEnum.valueof(instance.getFixedType()));

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

		if (null != partnerInstanceDto.getState()) {
			rel.setState(partnerInstanceDto.getState().getCode());
		}
		rel.setBit(partnerInstanceDto.getBit());
		rel.setOpenDate(partnerInstanceDto.getOpenDate());
		rel.setStationApplyId(partnerInstanceDto.getStationApplyId());
		if (null != partnerInstanceDto.getType()) {

			rel.setType(partnerInstanceDto.getType().getCode());
		}

		if (null != partnerInstanceDto.getIsCurrent()) {
			rel.setIsCurrent(partnerInstanceDto.getIsCurrent().getCode());
		}
		if (null != partnerInstanceDto.getCloseType()) {
			rel.setCloseType(partnerInstanceDto.getCloseType().getCode());
		}
		rel.setStationId(partnerInstanceDto.getStationId());
		rel.setPartnerId(partnerInstanceDto.getPartnerId());

		return rel;
	}
}
