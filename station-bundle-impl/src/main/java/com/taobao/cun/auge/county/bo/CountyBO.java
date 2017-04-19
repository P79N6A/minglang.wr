package com.taobao.cun.auge.county.bo;

import java.util.List;

import com.taobao.cun.auge.county.dto.CountyDto;
import com.taobao.cun.auge.county.dto.CountyStationQueryCondition;
import com.taobao.cun.dto.station.CountyStationDto;
import com.taobao.cun.settle.common.model.PagedResultModel;

public interface CountyBO {

	public CountyDto saveCountyStation(String operator,CountyDto countyDto);
	
	public List<CountyDto> getProvinceList(List<Long> areaOrgIds);
  
	public List<CountyDto> getCountyStationByProvince(String provinceCode);
	
	public List<CountyDto> getCountyStationList(List<Long> areaIds);
	
	public CountyDto getCountyStation(Long id);

	public CountyDto getCountyStationByOrgId(Long id);
	
	public PagedResultModel<List<CountyDto>> getCountyStationList(CountyStationQueryCondition queryCondition);
	

}
