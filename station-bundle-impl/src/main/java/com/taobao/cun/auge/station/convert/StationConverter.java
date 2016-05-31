package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.List;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.StationFixedTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;

/**
 * 服务站表dto转换
 * 
 * @author quanzhu.wangqz
 *
 */
public class StationConverter {

	public static StationDto toStationDto(Station station) {
		if (null == station) {
			return null;
		}
		StationDto stationDto = new StationDto();

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
		address.setAddressDetail(station.getAddress());
		address.setLat(station.getLat());
		address.setLng(station.getLng());

		stationDto.setApplyOrg(station.getApplyOrg());
		stationDto.setAreaType(station.getAreaType());
		stationDto.setCovered(station.getCovered());
		stationDto.setDescription(station.getDescription());
		stationDto.setFixedType(StationFixedTypeEnum.valueof(station.getFixedType()));
		stationDto.setFormat(station.getFormat());
		stationDto.setId(station.getId());
		stationDto.setLogisticsState(station.getLogisticsState());
		stationDto.setManagerId(station.getManagerId());
		stationDto.setName(station.getName());
		stationDto.setProducts(station.getProducts());
		stationDto.setProviderId(station.getProviderId());
		stationDto.setState(StationStateEnum.valueof(station.getState()));
		stationDto.setStationNum(station.getStationNum());
		stationDto.setStatus(StationStatusEnum.valueof(station.getStatus()));
		return stationDto;
	}

	public static Station toStation(StationDto stationDto) {
		Station station = new Station();

		Address address = stationDto.getAddress();
		if (address != null) {
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
			station.setAddress(address.getAddressDetail());
			station.setLat(address.getLat());
			station.setLng(address.getLng());
		}
		station.setApplyOrg(stationDto.getApplyOrg());
		station.setAreaType(stationDto.getAreaType());
		station.setCovered(stationDto.getCovered());
		station.setDescription(stationDto.getDescription());
		if (stationDto.getFixedType() != null) {
			station.setFixedType(stationDto.getFixedType().getCode());
		}
		station.setFormat(stationDto.getFormat());
		station.setId(stationDto.getId());
		station.setLogisticsState(stationDto.getLogisticsState());
		station.setManagerId(stationDto.getManagerId());
		station.setName(stationDto.getName());
		station.setProducts(stationDto.getProducts());
		station.setProviderId(station.getProviderId());
		station.setState(stationDto.getState().getCode());
		station.setStationNum(stationDto.getStationNum());
		station.setStatus(stationDto.getStatus().getCode());
		return station;
	}

	public static List<StationDto> toStationDtos(List<Station> stations) {
		if (stations == null) {
			return null;
		}

		List<StationDto> list = new ArrayList<StationDto>();
		for (Station station : stations) {
			list.add(toStationDto(station));
		}

		return list;
	}

	public static List<Station> toStations(List<StationDto> stationDtos) {
		if (stationDtos == null) {
			return null;
		}

		List<Station> list = new ArrayList<Station>();
		for (StationDto stationDto : stationDtos) {
			list.add(toStation(stationDto));
		}

		return list;
	}

}
