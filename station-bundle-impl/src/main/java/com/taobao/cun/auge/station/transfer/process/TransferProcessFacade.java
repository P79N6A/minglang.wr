package com.taobao.cun.auge.station.transfer.process;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.transfer.TransferException;
import com.taobao.cun.auge.station.transfer.dto.CountyStationTransferCondition;
import com.taobao.cun.auge.station.transfer.dto.TransferJob;

@Component
public class TransferProcessFacade {
	@Resource
	private TransferProcessStarterFactory transferProcessStarterFactory;
	@Resource
	private TransferProcessPrepareBo transferProcessPrepareBo;
	/**
	 * 发起交接流程
	 * @param transferJob
	 */
	public void startTransferProcess(TransferJob transferJob) throws TransferException{
		transferProcessStarterFactory.getInstance(transferJob).startTransferProcess(transferJob);
	}
	
	public CountyStationTransferCondition prepare(Long countyStationId) {
		return transferProcessPrepareBo.prepare(countyStationId);
	}
}
