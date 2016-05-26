package com.taobao.cun.auge.station.convert;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.dto.StationAddressDto;
import com.taobao.cun.auge.station.dto.StationDto;

/**
 * 服务站表dto转换
 * @author quanzhu.wangqz
 *
 */
public class StationConverter {

	public static StationDto toStationDto(Station station) {
		return null;
	}
	
	public static Station toStation(StationDto stationDto) {
		Station station = new Station();
		
		StationAddressDto address = stationDto.getStationAddressDto();
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
			station.setAddress(address.getAddress());
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
}
