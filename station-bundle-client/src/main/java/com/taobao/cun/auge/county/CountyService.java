package com.taobao.cun.auge.county;

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

	
}
