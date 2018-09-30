package com.taobao.cun.auge.county.bo;

import java.util.List;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.county.dto.CountyDto;
import com.taobao.cun.auge.county.dto.CountyQueryCondition;
import com.taobao.cun.auge.county.dto.CountyStationQueryCondition;
import com.taobao.cun.auge.user.dto.Operator;

public interface CountyBO {

	public CountyDto saveCountyStation(CountyDto countyDto, Operator operator);
	
	public List<CountyDto> getProvinceList(List<Long> areaOrgIds);
  
	public List<CountyDto> getCountyStationByProvince(String provinceCode);
	
	public List<CountyDto> getCountyStationList(List<Long> areaIds);
	
	public CountyDto getCountyStation(Long id);

	public CountyDto getCountyStationByOrgId(Long id);
	
	public List<CountyDto> getCountyStationByOrgIds(List<Long> ids);
	
	public PageDto<CountyDto> getCountyStationList(CountyStationQueryCondition queryCondition);
	
	public PageDto<CountyDto> queryCountyStation(CountyQueryCondition queryCondition);
	
	public CountyDto startOperate(CountyDto countyDto, Operator operator);

	List<CountyDto> getCountyStationByCity(String cityCode);

    List<CountyDto> getCountyStationByCounty(String countyCode);
    
    public boolean startOpen(Long countyStationId,Operator operator);
}
