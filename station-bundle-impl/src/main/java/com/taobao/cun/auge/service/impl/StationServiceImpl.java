package com.taobao.cun.auge.service.impl;

import java.util.List;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.conversion.StationConverter;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.StationMapper;
import com.taobao.cun.auge.service.station.StationService;
import com.taobao.cun.auge.station.domain.StationDTO;
import com.taobao.cun.common.resultmodel.ResultModel;
import com.taobao.cun.service.resource.AppResourceDto;
import com.taobao.cun.service.resource.AppResourceService;
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
	
	@Autowired
	private AppResourceService appResourceService;
	
	@Override
	public StationDTO getStation(Long id) {
		//ResultModel<List<AppResourceDto>> resource = appResourceService.queryAppResourceList("tpv_audit_msg");
	//	System.out.println(resource.getResult());
		Station station = stationMapper.selectByPrimaryKey(id);
		return stationConverter.toStationDTO(station);
	}
}
