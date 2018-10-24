package com.taobao.cun.auge.station.transfer.process;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.CountyStationTransferJob;
import com.taobao.cun.auge.dal.domain.TransferItem;
import com.taobao.cun.auge.dal.mapper.CountyStationTransferJobMapper;
import com.taobao.cun.auge.station.transfer.TransferException;
import com.taobao.cun.auge.station.transfer.TransferItemBo;
import com.taobao.cun.auge.station.transfer.TransferJobBo;
import com.taobao.cun.auge.station.transfer.dto.TransferJob;
import com.taobao.cun.auge.station.transfer.dto.TransferState;
import com.taobao.cun.auge.station.transfer.state.StationTransferStateMgrBo;

/**
 * 县村转交抽象实现
 * 
 * @author chengyu.zhoucy
 *
 */
public abstract class AbstractTransferProcessStarter implements TransferProcessStarter {
	@Resource
	private TransferWorkflow transferWorkflow;
	@Resource
	private CountyStationTransferJobMapper countyStationTransferJobMapper;
	@Resource
	private StationTransferStateMgrBo stationTransferStateMgrBo;
	@Resource
	protected TransferItemBo transferItemBo;
	@Resource
	private TransferJobBo transferJobBo;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void startTransferProcess(TransferJob transferJob) throws TransferException{
		CountyStationTransferJob countyStationTransferJob = transferJobBo.createCountyStationTransferJob(transferJob);
		
		transStations(countyStationTransferJob);
		
		postHandle(countyStationTransferJob);
		
		startWorkflow(countyStationTransferJob);
	}

	protected abstract void postHandle(CountyStationTransferJob countyStationTransferJob);

	private void startWorkflow(CountyStationTransferJob countyStationTransferJob) throws TransferException {
		transferWorkflow.start(countyStationTransferJob);
	}

	private void transStations(CountyStationTransferJob countyStationTransferJob) {
		String[] array = countyStationTransferJob.getStationIdList().split(",");
		
		List<Long> ids = Lists.newArrayList();
		for(String id : array) {
			ids.add(Long.parseLong(id));
			
			TransferItem item = new TransferItem();
			item.setCreator(countyStationTransferJob.getCreator());
			item.setGmtCreate(new Date());
			item.setGmtModified(new Date());
			item.setJobId(countyStationTransferJob.getId());
			item.setRefId(Long.parseLong(id));
			item.setRefType("station");
			item.setState("NEW");
			transferItemBo.insert(item);
		}
		stationTransferStateMgrBo.batchUpdateTransferState(ids, TransferState.TRANSFERING.name());
	}
}
