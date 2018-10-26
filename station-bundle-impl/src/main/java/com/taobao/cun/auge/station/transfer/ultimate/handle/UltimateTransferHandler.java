package com.taobao.cun.auge.station.transfer.ultimate.handle;

import com.taobao.cun.auge.dal.domain.CountyStation;

/**
 * 最终转交接口
 * 
 * @author chengyu.zhoucy
 *
 */
public interface UltimateTransferHandler {
	/**
	 * 转交
	 * @param countyStation
	 * @param operator
	 * @param opOrgId
	 */
	void transfer(CountyStation countyStation, String operator, Long opOrgId);
}
