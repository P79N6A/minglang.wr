package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 物流基础服务
 * @author quanzhu.wangqz
 *
 */
public interface LogisticsStationBO {

	/**
	 * 根据主键删除
	 * @param id
	 * @param operator
	 * @throws AugeServiceException
	 */
	public void delete(Long id,String operator) throws AugeServiceException;
	
	
	public void changeState(Long id,String operator,String targetState) throws AugeServiceException;
	
}
