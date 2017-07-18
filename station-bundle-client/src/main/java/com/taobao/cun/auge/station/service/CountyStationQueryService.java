package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.CountyStationDto;

/**
 * 县服务中心查询接口
 * @author quanzhu.wangqz
 *
 */
public interface CountyStationQueryService {
	
	/**
	 * 根据组织id 查询县服务中心
	 * @param orgId
	 * @return
	 */
	public CountyStationDto getCountyStationDtoByOrgId(Long orgId);
}
