package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.StationAreaTypeEnum;
import com.taobao.cun.auge.station.enums.StationFixedTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.enums.StationlLogisticsStateEnum;

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
		stationDto.setAreaType(StationAreaTypeEnum.valueof(station.getAreaType()));
		stationDto.setCovered(station.getCovered());
		stationDto.setDescription(station.getDescription());
		stationDto.setFixedType(StationFixedTypeEnum.valueof(station.getFixedType()));
		stationDto.setFormat(station.getFormat());
		stationDto.setId(station.getId());
		stationDto.setLogisticsState(StationlLogisticsStateEnum.valueof(station.getLogisticsState()));
		stationDto.setManagerId(station.getManagerId());
		stationDto.setName(station.getName());
		stationDto.setProducts(station.getProducts());
		stationDto.setProviderId(station.getProviderId());
		stationDto.setState(StationStateEnum.valueof(station.getState()));
		stationDto.setStationNum(station.getStationNum());
		stationDto.setStatus(StationStatusEnum.valueof(station.getStatus()));
		stationDto.setAddress(address);
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
		station.setAreaType(stationDto.getAreaType()==null? null:stationDto.getAreaType().getCode());
		station.setCovered(stationDto.getCovered());
		station.setDescription(stationDto.getDescription());
		station.setFixedType(stationDto.getFixedType()==null? null:stationDto.getFixedType().getCode());
		station.setFormat(stationDto.getFormat());
		station.setId(stationDto.getId());
		station.setLogisticsState(stationDto.getLogisticsState() ==null? null:stationDto.getLogisticsState().getCode());
		station.setManagerId(stationDto.getManagerId());
		station.setName(stationDto.getName());
		station.setProducts(stationDto.getProducts());
		station.setProviderId(station.getProviderId());
		station.setState(stationDto.getState() ==null? null: stationDto.getState().getCode());
		station.setStationNum(stationDto.getStationNum());
		station.setStatus(stationDto.getStatus() ==null? null: stationDto.getStatus().getCode());
		
		
		//老字段，人相关信息继续同步
		station.setTaobaoNick(stationDto.getTaobaoNick());
		station.setTaobaoUserId(stationDto.getTaobaoUserId());
		station.setAlipayAccount(stationDto.getAlipayAccount());
		return station;
	}

	public static List<StationDto> toStationDtos(List<Station> stations) {
		if (CollectionUtils.isEmpty(stations)) {
			return Collections.<StationDto>emptyList();
		}

		List<StationDto> list = new ArrayList<StationDto>(stations.size());
		for (Station station : stations) {
			if(null == station){
				continue;
			}
			list.add(toStationDto(station));
		}

		return list;
	}

	public static List<Station> toStations(List<StationDto> stationDtos) {
		if (CollectionUtils.isEmpty(stationDtos)) {
			return Collections.<Station>emptyList();
		}

		List<Station> list = new ArrayList<Station>();
		for (StationDto stationDto : stationDtos) {
			if(null == stationDto){
				continue;
			}
			list.add(toStation(stationDto));
		}

		return list;
	}

}
