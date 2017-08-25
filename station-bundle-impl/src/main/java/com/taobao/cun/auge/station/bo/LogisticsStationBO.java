package com.taobao.cun.auge.station.bo;

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
	 * @
	 */
	public void delete(Long id,String operator) ;
	
	
	public void changeState(Long id,String operator,String targetState) ;
	
}
