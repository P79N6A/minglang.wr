package com.taobao.cun.auge.county;

import java.util.List;

import com.taobao.cun.auge.county.dto.CountyDto;
/**
 * 从center迁移至auge
 * 供org、motion、无线端调用
 * @author yi.shaoy
 *
 */
public interface CountyService {

	/**
	 * 新增或更新县运营中心
	 * 
	 * @param context
	 * @param countyStationDto
	 * @return
	 */
	public CountyDto saveCountyStation(String operator,CountyDto countyDto);
	
	/**
	 * 查询大区列表下的省编码与名称列表
	 * 
	 * @param areaIds
	 * @return
	 */
	List<CountyDto> getProvinceList(List<Long> areaOrgIds);
	
	/**
	 * 查询省下的县服务站列表
	 * 
	 * @param areaIds
	 * @return
	 */
	List<CountyDto> getCountyStationByProvince(String provinceCode);

	/**
	 * 查询大区列表下的县运营中心列表
	 * 
	 * @param context
	 * @param areaIds
	 * @return
	 */
	List<CountyDto> getCountyStationList(List<Long> areaIds);
	
	/**
	 * 查询县运营中心的信息
	 * 
	 * @param context
	 * @param id
	 *            县运营中心id
	 * @return
	 */
	CountyDto getCountyStation(Long id);

	/**
	 * 查询县运营中心的信息
	 *
	 * @param context
	 * @param id
	 *            组织id
	 * @return
	 */
	CountyDto getCountyStationByOrgId(Long id);
	
}
