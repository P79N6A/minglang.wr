package com.taobao.cun.auge.station.service.impl;

import java.util.List;

import com.taobao.cun.auge.station.bo.StationDataCheckBO;
import com.taobao.cun.auge.station.check.StationChecker;
import com.taobao.cun.auge.station.service.StationCheckService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("stationCheckService")
@HSFProvider(serviceInterface = StationCheckService.class)
public class StationCheckServiceImpl implements StationCheckService{
	
	@Autowired
	StationChecker stationChecker;
	
	@Autowired
	StationDataCheckBO stationDataCheckBO;
	
	@Override
	public void checkShutdownApply(Long stationId) {
		stationChecker.checkShutdownApply(stationId);
	}

	@Override
	public void checkAllWithCainiao(List<Long> stationIds) {
		stationDataCheckBO.checkAllWithCainiao(stationIds);
		
	}
}
