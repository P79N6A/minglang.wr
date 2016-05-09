package com.taobao.cun.auge.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.conversion.StationConverter;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.StationMapper;
import com.taobao.cun.auge.service.StationService;
import com.taobao.cun.auge.station.domain.StationDTO;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * user service implementation
 *
 * @author leijuan
 */
@HSFProvider(serviceInterface = StationService.class)
public class StationServiceImpl implements StationService {


	@Autowired
	private StationMapper stationMapper;
	
	@Autowired
	private StationConverter stationConverter;
	
	@Override
	public StationDTO getStation(Long id) {
		Station station = stationMapper.selectByPrimaryKey(id);
		return stationConverter.toStationDTO(station);
	}
}
