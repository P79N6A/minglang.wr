package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.StationNumConfig;
import com.taobao.cun.auge.station.enums.StationNumConfigTypeEnum;

public interface StationNumConfigBO {
	/**
	 * 查询配置信息
	 * @param provinceCode
	 * @param typeEnum
	 * @return
	 */
	public StationNumConfig  getConfigByProvinceCode(String provinceCode,StationNumConfigTypeEnum typeEnum);
	/**
	 * 更新当前序列号
	 * @param provinceCode
	 * @param typeEnum
	 * @param seqNum
	 */
	public void  updateSeqNum(String provinceCode,StationNumConfigTypeEnum typeEnum,String seqNum);
	
	/**
	 * 更新当前序列号
	 * @param provinceCode
	 * @param typeEnum
	 * @param stationNum
	 */
	public void updateSeqNumByStationNum(String provinceCode,StationNumConfigTypeEnum typeEnum,String stationNum);
	
	/**
	 * 创建新的编号
	 * @param provinceCode
	 * @param typeEnum
	 * @return
	 */
	
	public String createStationNum(String provinceCode,StationNumConfigTypeEnum typeEnum,int level);
}
