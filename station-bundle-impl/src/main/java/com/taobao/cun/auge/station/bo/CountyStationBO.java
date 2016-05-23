package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.station.exception.AugeServiceException;

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
	 * @throws AugeServiceException
	 */
	public CountyStation getCountyStationByOrgId(Long orgId)  throws AugeServiceException;
}
