package com.taobao.cun.auge.station.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.station.check.StationChecker;
import com.taobao.cun.auge.station.service.StationCheckService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("stationCheckService")
@HSFProvider(serviceInterface = StationCheckService.class)
public class StationCheckServiceImpl implements StationCheckService{
	
	@Autowired
	StationChecker stationChecker;
	
	@Override
	public void checkShutdownApply(Long stationId) {
		stationChecker.checkShutdownApply(stationId);
	}
}
