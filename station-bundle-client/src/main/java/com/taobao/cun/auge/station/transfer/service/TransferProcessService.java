package com.taobao.cun.auge.station.transfer.service;

import com.taobao.cun.auge.station.transfer.dto.CountyStationTransferCondition;
import com.taobao.cun.auge.station.transfer.dto.CountyStationTransferDetail;
import com.taobao.cun.auge.station.transfer.dto.TransferJob;

/**
 * 交接流程
 * 
 * @author chengyu.zhoucy
 *
 */
public interface TransferProcessService {
	/**
	 * 审批同意
	 * @param applyId
	 */
	void agree(String applyId, String operator, long orgId);
	
	/**
	 * 审批不同意
	 * @param applyId
	 */
	void disagree(String applyId, String operator);
	
	/**
	 * 发起流程
	 * @param transferJob
	 */
	void startTransferProcess(TransferJob transferJob);
	
	/**
	 * 准备流程数据
	 * @param countyStationId
	 * @return
	 */
	CountyStationTransferCondition prepare(Long countyStationId);
	
	/**
	 * 获取移交详情
	 * @param id
	 * @return
	 */
	CountyStationTransferDetail getCountyStationTransferDetail(Long id);
}
