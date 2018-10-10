package com.taobao.cun.auge.station.transfer.process;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CountyStationTransferJob;

/**
 * 村点转交
 * @author chengyu.zhoucy
 *
 */
@Component
public class StationTransferProcessStarter extends AbstractTransferProcessStarter {
	
	@Override
	protected void postHandle(CountyStationTransferJob countyStationTransferJob) {
	}

}
