package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.utils.FeatureUtil;
import com.taobao.cun.auge.dal.domain.PartnerInstance;
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

		instanceDto.setState(PartnerInstanceStateEnum.valueof(instance.getPartnerState()));
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

		return null;
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
}
