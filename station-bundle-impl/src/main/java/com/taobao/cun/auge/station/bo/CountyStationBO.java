package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.CountyStation;

/**
 * 
 * 县点服务类
 * @author quanzhu.wangqz
 *
 */
public interface CountyStationBO {
	
	/**
	 * 根据组织id查询县信息
	 * @param orgId
	 * @return
	 * @
	 */
	public CountyStation getCountyStationByOrgId(Long orgId)  ;

	public CountyStation getCountyStationById(Long id);
	
	public Long addCountyStation(CountyStation cs);
}
