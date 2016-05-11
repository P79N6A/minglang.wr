package com.taobao.cun.auge.service.impl;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.conversion.StationConverter;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.StationMapper;
import com.taobao.cun.auge.service.station.StationService;
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
	
	@Autowired
	private Validator validator;
	
	@Override
	public StationDTO getStation(Long id) {
		TestBean bean = new TestBean();
		try {
			validator.validate(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Station station = stationMapper.selectByPrimaryKey(id);
		return stationConverter.toStationDTO(station);
	}
}
