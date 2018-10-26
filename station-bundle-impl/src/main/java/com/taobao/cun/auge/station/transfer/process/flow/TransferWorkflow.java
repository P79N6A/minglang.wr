package com.taobao.cun.auge.station.transfer.process.flow;

import com.taobao.cun.auge.dal.domain.CountyStationTransferJob;
import com.taobao.cun.auge.station.transfer.TransferException;

/**
 * 工作流
 * @author chengyu.zhoucy
 *
 */
public interface TransferWorkflow {
	/**
	 * 发起工作流
	 * @param countyStationTransferJob
	 * @throws TransferException
	 */
	void start(CountyStationTransferJob countyStationTransferJob) throws TransferException;
	
	/**
	 * 终止流程
	 * @param countyId
	 */
	void teminate(Long countyId);
}
