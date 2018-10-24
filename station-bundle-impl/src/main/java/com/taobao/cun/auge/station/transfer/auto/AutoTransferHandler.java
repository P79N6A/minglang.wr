package com.taobao.cun.auge.station.transfer.auto;

import com.taobao.cun.auge.dal.domain.CountyStation;

/**
 * 自动转交接口
 * 
 * @author chengyu.zhoucy
 *
 */
public interface AutoTransferHandler {
	/**
	 * 转交
	 * @param countyStation
	 */
	void transfer(CountyStation countyStation, String operator, Long opOrgId);
}
