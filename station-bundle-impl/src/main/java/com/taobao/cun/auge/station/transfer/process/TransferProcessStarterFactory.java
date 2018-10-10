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
	
	public TransferProcessStarter getInstance(TransferJob transferJob) {
		return transferJob.getType().equals("county") ? countyStationTransferProcessStarter : stationTransferProcessStarter;
	}
}
