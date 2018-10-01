package com.taobao.cun.auge.station.transfer.process;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.transfer.dto.TransferJob;

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
