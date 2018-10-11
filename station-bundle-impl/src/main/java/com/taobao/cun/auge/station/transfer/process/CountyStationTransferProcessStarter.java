package com.taobao.cun.auge.station.transfer.process;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CountyStationTransferJob;
import com.taobao.cun.auge.dal.domain.TransferItem;
import com.taobao.cun.auge.station.transfer.dto.TransferState;
import com.taobao.cun.auge.station.transfer.state.CountyTransferStateMgrBo;

/**
 * 县点转交
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CountyStationTransferProcessStarter extends AbstractTransferProcessStarter {
	@Resource
	private CountyTransferStateMgrBo countyTransferStateMgrBo;
	
	@Override
	protected void postHandle(CountyStationTransferJob countyStationTransferJob) {
		countyTransferStateMgrBo.updateTransferState(countyStationTransferJob.getCountyStationId(),TransferState.TRANSFERING.name());
		
		TransferItem item = new TransferItem();
		item.setCreator(countyStationTransferJob.getCreator());
		item.setGmtCreate(new Date());
		item.setGmtModified(new Date());
		item.setJobId(countyStationTransferJob.getId());
		item.setRefId(countyStationTransferJob.getCountyStationId());
		item.setRefType("county");
		item.setState("NEW");
		transferItemBo.insert(item);
	}

}