package com.taobao.cun.auge.county.bo;

import java.util.List;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.county.dto.CountyDto;
import com.taobao.cun.auge.county.dto.CountyQueryCondition;
import com.taobao.cun.auge.county.dto.CountyStationQueryCondition;

public interface CountyBO {

	public CountyDto saveCountyStation(String operator,CountyDto countyDto);
	
	public List<CountyDto> getProvinceList(List<Long> areaOrgIds);
  
	public List<CountyDto> getCountyStationByProvince(String provinceCode);
	
	public List<CountyDto> getCountyStationList(List<Long> areaIds);
	
	public CountyDto getCountyStation(Long id);

	public CountyDto getCountyStationByOrgId(Long id);
	
	public List<CountyDto> getCountyStationByOrgIds(List<Long> ids);
	
	public PageDto<CountyDto> getCountyStationList(CountyStationQueryCondition queryCondition);
	
	public PageDto<CountyDto> queryCountyStation(CountyQueryCondition queryCondition);
	
	public CountyDto startOperate(String operator,CountyDto countyDto);
	
}
