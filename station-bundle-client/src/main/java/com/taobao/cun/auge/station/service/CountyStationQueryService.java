package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.CountyStationDto;

/**
 * 该接口已废弃，请使用{@link com.taobao.cun.auge.cuncounty.service.CuntaoCountyQueryService}
 * @author quanzhu.wangqz
 *
 */
@Deprecated
public interface CountyStationQueryService {
	
	/**
	 * 根据组织id 查询县服务中心
	 * @param orgId
	 * @return
	 */
	public CountyStationDto getCountyStationDtoByOrgId(Long orgId);
}
