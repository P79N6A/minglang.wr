package com.taobao.cun.auge.station.transfer.service;

import com.taobao.cun.auge.station.transfer.dto.CountyStationTransferPhase;

/**
 * 县点转交服务
 * 
 * @author chengyu.zhoucy
 *
 */
public interface CountyStationTransferService {
	/**
	 * 获取县点当前转交阶段
	 * @param id
	 * @return
	 */
	CountyStationTransferPhase getTransferPhase(Long id);
}
