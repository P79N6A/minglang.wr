package com.taobao.cun.auge.station.transfer.process;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.CountyStationTransferJob;
import com.taobao.cun.auge.dal.domain.TransferItem;
import com.taobao.cun.auge.dal.mapper.CountyStationTransferJobMapper;
import com.taobao.cun.auge.station.transfer.StationTransferBo;
import com.taobao.cun.auge.station.transfer.TransferException;
import com.taobao.cun.auge.station.transfer.TransferItemBo;
import com.taobao.cun.auge.station.transfer.dto.TransferJob;
import com.taobao.cun.auge.station.transfer.dto.TransferState;
import com.taobao.cun.common.util.BeanCopy;

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
	private StationTransferBo stationTransferBo;
	@Resource
	protected TransferItemBo transferItemBo;
	
	@Override
	@Transactional
	public void startTransferProcess(TransferJob transferJob) throws TransferException{
		CountyStationTransferJob countyStationTransferJob = createCountyStationTransferJob(transferJob);
		
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
		stationTransferBo.batchUpdateTransferState(ids, TransferState.TRANSFERING.name());
	}


	private CountyStationTransferJob createCountyStationTransferJob(TransferJob transferJob) {
		CountyStationTransferJob countyStationTransferJob = BeanCopy.copy(CountyStationTransferJob.class, transferJob);
		countyStationTransferJob.setGmtCreate(new Date());
		countyStationTransferJob.setGmtModified(new Date());
		countyStationTransferJob.setState("NEW");
		countyStationTransferJobMapper.insert(countyStationTransferJob);
		return countyStationTransferJob;
	}

}
