package com.taobao.cun.auge.station.transfer.process;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CountyStationTransferJob;
import com.taobao.cun.auge.station.transfer.TransferException;
import com.taobao.cun.auge.station.transfer.TransferJobBo;
import com.taobao.cun.auge.station.transfer.dto.TransferJob;
import com.taobao.cun.auge.station.transfer.process.flow.TransferWorkflow;

/**
 * 提前交接流程
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class AdvanceTransferProcessStarter implements TransferProcessStarter {
	@Resource
	private TransferWorkflow advanceTransferWorkflow;
	@Resource
	private TransferJobBo transferJobBo;
	
	@Override
	public void startTransferProcess(TransferJob transferJob) throws TransferException {
		CountyStationTransferJob countyStationTransferJob = transferJobBo.createCountyStationTransferJob(transferJob);
		advanceTransferWorkflow.start(countyStationTransferJob);
	}

}
