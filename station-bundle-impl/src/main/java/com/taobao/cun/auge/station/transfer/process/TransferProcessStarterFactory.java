package com.taobao.cun.auge.station.transfer.process;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.transfer.dto.TransferJob;

/**
 * 转交发起流程实现类的工厂，根据是发起县点交接还是村点交接，返回不同实现
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class TransferProcessStarterFactory {
	@Resource
	private TransferProcessStarter stationTransferProcessStarter;
	@Resource
	private TransferProcessStarter countyStationTransferProcessStarter;
	@Resource
	private TransferProcessStarter AdvanceTransferProcessStarter;
	
	public TransferProcessStarter getInstance(TransferJob transferJob) {
		switch(transferJob.getType()) {
		case "county":
			return countyStationTransferProcessStarter;
		case "station":
			return stationTransferProcessStarter;
		case "advance":
			return AdvanceTransferProcessStarter;
		default:
			throw new UnsupportedOperationException("不支持该类型的交接流程：" + transferJob.getType());
		}
	}
}
