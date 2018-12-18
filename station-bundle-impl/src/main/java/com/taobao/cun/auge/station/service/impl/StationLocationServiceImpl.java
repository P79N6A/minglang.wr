package com.taobao.cun.auge.station.service.impl;

import javax.annotation.Resource;

import com.taobao.cun.auge.station.dto.StationLocation;
import com.taobao.cun.auge.station.service.StationLocationService;
import com.taobao.cun.crius.tair.RdbTairFacade;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = StationLocationService.class, clientTimeout = 8000)
public class StationLocationServiceImpl implements StationLocationService {
	
	@Resource
	private RdbTairFacade rdbTairFacade;
	
	@Override
	public void saveStationLocation(StationLocation stationLocation) {
		rdbTairFacade.getTairList().lpush("cuntao_station_location", stationLocation);
	}

}
