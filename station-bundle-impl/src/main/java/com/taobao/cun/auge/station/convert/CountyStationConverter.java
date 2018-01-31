package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.common.utils.FeatureUtil;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.station.dto.CountyStationDto;
import com.taobao.cun.auge.station.enums.CountyStationLeaseTypeEnum;
import com.taobao.cun.auge.station.enums.CountyStationManageModelEnum;
import com.taobao.cun.auge.station.enums.CountyStationManageStatusEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 县服务中心 dto domain 转换器
 * @author quanzhu.wangqz
 *
 */
public class CountyStationConverter {

	public static CountyStationDto toCountyStationDto(CountyStation countyStation) {
		if (null == countyStation) {
			return null;
		}
		CountyStationDto countyStationDto = new CountyStationDto();
		countyStationDto.setAcreage(countyStation.getAcreage());
		countyStationDto.setAddressDetail(countyStation.getAddressDetail());
		countyStationDto.setCity(countyStation.getCity());
		countyStationDto.setCityDetail(countyStation.getCityDetail());
		countyStationDto.setCounty(countyStation.getCounty());
		countyStationDto.setCountyDetail(countyStation.getCountyDetail());
		countyStationDto.setElectricPayment(countyStation.getElectricPayment());
		countyStationDto.setEmployeeId(countyStation.getEmployeeId());
		countyStationDto.setEmployeePhone(countyStation.getEmployeePhone());
		if (StringUtils.isNotEmpty(countyStation.getFeature())) {
	        	Map<String,String> featureMap = FeatureUtil.toMap(countyStation.getFeature());
	        	countyStationDto.setFeatureMap(featureMap);
	    }
		countyStationDto.setFreeDeadline(countyStation.getFreeDeadline());
		countyStationDto.setGmtStartOperation(countyStation.getGmtStartOperation());
		countyStationDto.setId(countyStation.getId());
		countyStationDto.setLat(countyStation.getLat());
		countyStationDto.setLeasePayment(countyStation.getLeasePayment());
		countyStationDto.setLeaseProtocolBeginTime(countyStation.getLeaseProtocolBeginTime());
		countyStationDto.setLeaseProtocolEndTime(countyStation.getLeaseProtocolEndTime());
		countyStationDto.setLeaseType(CountyStationLeaseTypeEnum.valueof(countyStation.getLeaseType()));
		countyStationDto.setLeasingModel(countyStation.getLeasingModel());
		countyStationDto.setLng(countyStation.getLng());
		countyStationDto.setLogisticsOperator(countyStation.getLogisticsOperator());
		countyStationDto.setLogisticsPhone(countyStation.getLogisticsPhone());
		countyStationDto.setManageModel(CountyStationManageModelEnum.valueof(countyStation.getManageModel()));
		countyStationDto.setManageStatus(CountyStationManageStatusEnum.valueof(countyStation.getManageStatus()));
		countyStationDto.setName(countyStation.getName());
		countyStationDto.setOfficeDetail(countyStation.getOfficeDetail());
		countyStationDto.setOrgId(countyStation.getOrgId());
		countyStationDto.setParentId(countyStation.getParentId());
		countyStationDto.setPropertyPayment(countyStation.getPropertyPayment());
		countyStationDto.setProvince(countyStation.getProvince());
		countyStationDto.setProvinceDetail(countyStation.getProvinceDetail());
		countyStationDto.setRemark(countyStation.getRemark());
		countyStationDto.setSelfCosts(countyStation.getSelfCosts());
		countyStationDto.setStorageArea(countyStation.getStorageArea());
		countyStationDto.setTaobaoNick(countyStation.getTaobaoNick());
		countyStationDto.setTaobaoUserId(countyStation.getTaobaoUserId());
		countyStationDto.setTown(countyStation.getTown());
		countyStationDto.setTownDetail(countyStation.getTownDetail());
		countyStationDto.setWarehouseNum(countyStation.getWarehouseNum());
		countyStationDto.setWaterPayment(countyStation.getWaterPayment());
		

		return countyStationDto;
	}

	public static CountyStation toCountyStation(CountyStationDto countyStationDto) {
		CountyStation countyStation = new CountyStation();

		if (null == countyStationDto) {
			return null;
		}
		countyStation.setAcreage(countyStationDto.getAcreage());
		countyStation.setAddressDetail(countyStationDto.getAddressDetail());
		countyStation.setCity(countyStationDto.getCity());
		countyStation.setCityDetail(countyStationDto.getCityDetail());
		countyStation.setCounty(countyStationDto.getCounty());
		countyStation.setCountyDetail(countyStationDto.getCountyDetail());
		countyStation.setElectricPayment(countyStationDto.getElectricPayment());
		countyStation.setEmployeeId(countyStationDto.getEmployeeId());
		countyStation.setEmployeePhone(countyStationDto.getEmployeePhone());
		countyStation.setFreeDeadline(countyStationDto.getFreeDeadline());
		countyStation.setGmtStartOperation(countyStationDto.getGmtStartOperation());
		countyStation.setId(countyStationDto.getId());
		countyStation.setLat(countyStationDto.getLat());
		countyStation.setLeasePayment(countyStationDto.getLeasePayment());
		countyStation.setLeaseProtocolBeginTime(countyStationDto.getLeaseProtocolBeginTime());
		countyStation.setLeaseProtocolEndTime(countyStationDto.getLeaseProtocolEndTime());
		countyStation.setLeaseType(countyStationDto.getLeaseType()==null? null: countyStationDto.getLeaseType().getCode());
		countyStation.setLeasingModel(countyStationDto.getLeasingModel());
		countyStation.setLng(countyStationDto.getLng());
		countyStation.setLogisticsOperator(countyStationDto.getLogisticsOperator());
		countyStation.setLogisticsPhone(countyStationDto.getLogisticsPhone());
		countyStation.setManageModel(countyStationDto.getManageModel()==null? null: countyStationDto.getManageModel().getCode());
		countyStation.setManageStatus(countyStationDto.getManageStatus()==null? null: countyStationDto.getManageStatus().getCode());
		countyStation.setName(countyStationDto.getName());
		countyStation.setOfficeDetail(countyStationDto.getOfficeDetail());
		countyStation.setOrgId(countyStationDto.getOrgId());
		countyStation.setParentId(countyStationDto.getParentId());
		countyStation.setPropertyPayment(countyStationDto.getPropertyPayment());
		countyStation.setProvince(countyStationDto.getProvince());
		countyStation.setProvinceDetail(countyStationDto.getProvinceDetail());
		countyStation.setRemark(countyStationDto.getRemark());
		countyStation.setSelfCosts(countyStationDto.getSelfCosts());
		countyStation.setStorageArea(countyStationDto.getStorageArea());
		countyStation.setTaobaoNick(countyStationDto.getTaobaoNick());
		countyStation.setTaobaoUserId(countyStationDto.getTaobaoUserId());
		countyStation.setTown(countyStationDto.getTown());
		countyStation.setTownDetail(countyStationDto.getTownDetail());
		countyStation.setWarehouseNum(countyStationDto.getWarehouseNum());
		countyStation.setWaterPayment(countyStationDto.getWaterPayment());
		return countyStation;
	}

	public static List<CountyStationDto> toCountyStationDtos(List<CountyStation> countyStations) {
		if (CollectionUtils.isEmpty(countyStations)) {
			return Collections.<CountyStationDto>emptyList();
		}

		List<CountyStationDto> list = new ArrayList<CountyStationDto>(countyStations.size());
		for (CountyStation countyStation : countyStations) {
			if(null == countyStation){
				continue;
			}
			list.add(toCountyStationDto(countyStation));
		}

		return list;
	}

	public static List<CountyStation> toStationDecorates(List<CountyStationDto> countyStationDtos) {
		if (CollectionUtils.isEmpty(countyStationDtos)) {
			return Collections.<CountyStation>emptyList();
		}

		List<CountyStation> list = new ArrayList<CountyStation>();
		for (CountyStationDto countyStationDto : countyStationDtos) {
			if(null == countyStationDto){
				continue;
			}
			list.add(toCountyStation(countyStationDto));
		}

		return list;
	}
}
