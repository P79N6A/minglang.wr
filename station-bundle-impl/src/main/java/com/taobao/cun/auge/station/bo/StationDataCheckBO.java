package com.taobao.cun.auge.station.bo;

import java.util.List;


/**
 * 村点信息数据对账使用
 * @author quanzhu.wangqz
 *
 */
public interface StationDataCheckBO {

	/**
	 * 和菜鸟全量数据对账
	 * @param staitonId
	 */
	public void checkAllWithCainiao(List<Long> stationIds);
}
