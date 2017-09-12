package com.taobao.cun.auge.logistics.convert;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.cainiao.cuntaonetwork.param.station.ApplyStationParam;
import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.utils.FeatureUtil;
import com.taobao.cun.auge.dal.domain.LogisticsStation;
import com.taobao.cun.auge.dal.domain.LogisticsStationExample;
import com.taobao.cun.auge.dal.domain.LogisticsStationExample.Criteria;
import com.taobao.cun.auge.logistics.dto.LogisticsApplyRequest;
import com.taobao.cun.auge.logistics.dto.LogisticsStationDto;
import com.taobao.cun.auge.logistics.dto.LogisticsStationQueryDto;
import com.taobao.cun.auge.logistics.enums.LogisticsStationStateEnum;
import com.taobao.cun.auge.logistics.util.LatitudeUtil;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;

public final class LogisticsStationConverter {

	public static final String SP = ";";

	private LogisticsStationConverter() {

	}

	public static LogisticsStationDto convert(ApplyStationParam param,Address address,LogisticsStationStateEnum state){
		LogisticsStationDto stationDto = new LogisticsStationDto();
		stationDto.setOperator("system");
		stationDto.setOperatorType(OperatorTypeEnum.SYSTEM);
		stationDto.setParentId(param.getCountyDomainId());
		stationDto.setName(param.getName());
		stationDto.setContactName(param.getContact());
		stationDto.setContactMobile(param.getMobile());
		stationDto.setTaobaoUserId(param.getUserId());
		stationDto.setParentId(param.getCountyDomainId());
		stationDto.setStationType(4);
		stationDto.setStatus(-1);
		stationDto.setServiceCode("108");
		stationDto.setFeatureMap(param.getFeature().asMap());
		stationDto.setManagers(ManagerUtil.toSet(param.getAuthUserId()+""));
		stationDto.setState(state);
		stationDto.setAddress(address);
		stationDto.setLogisticsStationNum(param.getCtCode());
		return stationDto;
	}
	
	public static String[] buildGeo(LogisticsApplyRequest request) {
		Long lastDivisionId = null;
		if (null != request.getTownId()) {
			lastDivisionId =request.getTownId();
		} else if (null != request.getCountyId()) {
			lastDivisionId = request.getCountyId();
		} else if (null != request.getCityId()) {
			lastDivisionId = request.getCityId();
		}else if (null != request.getProvinceId()) {
			lastDivisionId = request.getProvinceId();
		}

		String addressDetail = StringUtils.isNotEmpty(request.getCountry())?request.getCountry():request.getAddress();

		Map<String, String> map = LatitudeUtil.findLatitude(lastDivisionId.toString(), addressDetail);
		String lng = map.get("lng");
		String lat = map.get("lat");
		return new String[]{converUp(lng),converUp(lat)};
	}

	public static String converUp(String val){	
		if(StringUtils.isBlank(val)){
			return "";
		}
		Float a = Float.valueOf(val);
		Double b=a*1e5;		
		return String.valueOf(b.intValue());
	}
	
	public static String converDown(String val){
		if(StringUtils.isBlank(val)){
			return "";
		}
		Double a = Double.valueOf(val);
		DecimalFormat dFormat=new DecimalFormat("#.######");
		Double b=a/1e5;
		return dFormat.format(b);
	}
	
	public static Address buildAddress(LogisticsApplyRequest request) {
		Address address = new Address();

		address.setProvince(request.getProvinceId()+"");
		address.setProvinceDetail(request.getProvince());

		address.setCity(request.getCityId()+"");
		address.setCityDetail(request.getCity());

		address.setCounty(request.getCountyId()+"");
		address.setCountyDetail(request.getCounty());

		address.setTown(request.getTownId()+"");
		address.setTownDetail(request.getTown());

		address.setVillage(request.getCountryId()+"");
		address.setVillageDetail(request.getCountry());

		address.setAddressDetail(request.getAddress());
		String[] geo = buildGeo(request);
		address.setLng(geo[0]);
		address.setLat(geo[1]);

		return address;
	}
	
	public static LogisticsStation convert(LogisticsStationDto stationDto) {
		if (null == stationDto) {
			return null;
		}
		LogisticsStation station = new LogisticsStation();
		station.setId(stationDto.getId());
		station.setParentId(stationDto.getParentId());
		station.setCainiaoStationId(stationDto.getCainiaoStationId());
		station.setName(stationDto.getName());
		station.setContactName(stationDto.getContactName());
		station.setContactMobile(stationDto.getContactMobile());
		station.setContactPhone(stationDto.getContactPhone());
		station.setTaobaoUserId(stationDto.getTaobaoUserId());
		station.setLogisticsStationNum(stationDto.getLogisticsStationNum());
		station.setStationType(stationDto.getStationType());
		station.setStatus(stationDto.getStatus());
		if (null != stationDto.getState()) {
			station.setState(stationDto.getState().getCode());
		}
		station.setServiceCode(stationDto.getServiceCode());
		station.setManagers(ManagerUtil.toString(stationDto.getManagers()));
		station.setFeature(FeatureUtil.toString(stationDto.getFeatureMap()));

		Address address = stationDto.getAddress();
		if (null != address) {
			station.setProvince(address.getProvince());
			station.setProvinceDetail(address.getProvinceDetail());

			station.setCity(address.getCity());
			station.setCityDetail(address.getCityDetail());

			station.setCounty(address.getCounty());
			station.setCountyDetail(address.getCountyDetail());

			station.setTown(address.getTown());
			station.setTownDetail(address.getTownDetail());

			station.setVillage(address.getVillage());
			station.setVillageDetail(address.getVillageDetail());

			station.setAddressDetail(address.getAddressDetail());
			station.setLat(address.getLat());
			station.setLng(address.getLng());
		}

		return station;
	}

	public static LogisticsStationExample convert2Example(LogisticsStationQueryDto condiDto) {
		LogisticsStationExample example = new LogisticsStationExample();

		Criteria criteria = example.createCriteria().andIsDeletedEqualTo("n");

		if (StringUtils.isNotEmpty(condiDto.getName())) {
			criteria.andNameLike("%" + condiDto.getName() + "%");
		}

		if (StringUtils.isNotEmpty(condiDto.getContactName())) {
			criteria.andContactNameLike("%" + condiDto.getContactName() + "%");
		}

		if (StringUtils.isNotEmpty(condiDto.getManager())) {
			criteria.andManagersLike("%" + condiDto.getManager() + "%");
		}

		if (StringUtils.isNotEmpty(condiDto.getContactMobile())) {
			criteria.andContactMobileEqualTo(condiDto.getContactMobile());
		}

		if (StringUtils.isNotEmpty(condiDto.getContactPhone())) {
			criteria.andContactPhoneEqualTo(condiDto.getContactPhone());
		}

		if (null != condiDto.getStationType()) {
			criteria.andStationTypeEqualTo(condiDto.getStationType());
		}

		if (null != condiDto.getTaobaoUserId()) {
			criteria.andTaobaoUserIdEqualTo(condiDto.getTaobaoUserId());
		}

		Address address = condiDto.getAddress();
		if (null != address) {
			if (StringUtils.isNotEmpty(address.getProvince())) {
				criteria.andProvinceEqualTo(address.getProvince());
			}
			if (StringUtils.isNotEmpty(address.getCity())) {
				criteria.andCityEqualTo(address.getCity());
			}
			if (StringUtils.isNotEmpty(address.getCounty())) {
				criteria.andCountyEqualTo(address.getCounty());
			}
			if (StringUtils.isNotEmpty(address.getTown())) {
				criteria.andTownEqualTo(address.getTown());
			}
			if (StringUtils.isNotEmpty(address.getVillage())) {
				criteria.andVillageEqualTo(address.getVillage());
			}
		}
		return example;
	}

	public static List<LogisticsStationDto> convert(List<LogisticsStation> stations) {
		if (CollectionUtils.isEmpty(stations)) {
			return Collections.<LogisticsStationDto> emptyList();
		}
		List<LogisticsStationDto> stationDtos = new ArrayList<LogisticsStationDto>(stations.size());
		for (LogisticsStation station : stations) {
			if (null == station) {
				continue;
			}
			stationDtos.add(convert(station));
		}
		return stationDtos;
	}

	public static LogisticsStationDto convert(LogisticsStation station) {
		if (null == station) {
			return null;
		}
		LogisticsStationDto stationDto = new LogisticsStationDto();
		stationDto.setId(station.getId());
		stationDto.setParentId(station.getParentId());
		stationDto.setCainiaoStationId(station.getCainiaoStationId());
		stationDto.setName(station.getName());
		stationDto.setContactName(station.getContactName());
		stationDto.setContactMobile(station.getContactMobile());
		stationDto.setContactPhone(station.getContactPhone());
		stationDto.setTaobaoUserId(station.getTaobaoUserId());

		stationDto.setStationType(station.getStationType());
		stationDto.setStatus(station.getStatus());
		stationDto.setServiceCode(station.getServiceCode());
		stationDto.setFeatureMap(FeatureUtil.toMap(station.getFeature()));
		stationDto.setManagers(ManagerUtil.toSet(station.getManagers()));
		stationDto.setState(LogisticsStationStateEnum.valueof(station.getState()));

		stationDto.setAddress(buildAddress(station));
		stationDto.setLogisticsStationNum(station.getLogisticsStationNum());
		return stationDto;
	}

	private static Address buildAddress(LogisticsStation station) {
		Address address = new Address();

		address.setProvince(station.getProvince());
		address.setProvinceDetail(station.getProvinceDetail());

		address.setCity(station.getCity());
		address.setCityDetail(station.getCityDetail());

		address.setCounty(station.getCounty());
		address.setCountyDetail(station.getCountyDetail());

		address.setTown(station.getTown());
		address.setTownDetail(station.getTownDetail());

		address.setVillage(station.getVillage());
		address.setVillageDetail(station.getVillageDetail());

		address.setAddressDetail(station.getAddressDetail());

		address.setLat(station.getLat());
		address.setLng(station.getLng());

		return address;
	}
}
