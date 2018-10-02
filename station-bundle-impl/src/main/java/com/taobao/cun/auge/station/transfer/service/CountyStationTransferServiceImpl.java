package com.taobao.cun.auge.station.transfer.service;

import javax.annotation.Resource;

import com.taobao.cun.auge.station.transfer.dto.CountyStationTransferPhase;
import com.taobao.cun.auge.station.transfer.state.CountyTransferStateMgrBo;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = CountyStationTransferService.class)
public class CountyStationTransferServiceImpl implements CountyStationTransferService {
	@Resource
	private CountyTransferStateMgrBo countyTransferStateMgrBo;
	
	@Override
	public CountyStationTransferPhase getTransferPhase(Long id) {
		return countyTransferStateMgrBo.getTransferPhase(id);
	}

}
