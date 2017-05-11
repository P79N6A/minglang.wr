package com.taobao.cun.auge.county.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.taobao.cun.auge.county.StationManagerService;
import com.taobao.cun.auge.county.dto.StationManagerDto;
import com.taobao.cun.auge.dal.domain.StationManager;
import com.taobao.cun.auge.dal.domain.StationManagerExample;
import com.taobao.cun.auge.dal.domain.StationManagerExample.Criteria;
import com.taobao.cun.auge.dal.mapper.StationManagerMapper;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
@Service("StationManagerService")
@HSFProvider(serviceInterface = StationManagerService.class,serviceVersion="1.0.0.daily.fjx")
public class StationManagerServiceImpl implements StationManagerService{

	@Autowired
	StationManagerMapper stationManagerMapper;
	
	@Override
	public List<StationManagerDto> getManagersByStationId(Long stationId) {
		Assert.notNull(stationId);
		StationManagerExample e= new StationManagerExample();
		Criteria c=e.createCriteria();
		c.andIsDeletedEqualTo("n").andStationIdEqualTo(String.valueOf(stationId));
		List<StationManager> managers=stationManagerMapper.selectByExample(e);
		List<StationManagerDto> managerDtos = new ArrayList<StationManagerDto>();
		for(StationManager manager: managers){
			StationManagerDto managerDto = new StationManagerDto();
			managerDto.setExtra(manager.getExtra());
			managerDto.setId(manager.getId());
			managerDto.setStationId(manager.getStationId());
			managerDto.setWorkerId(manager.getWorkerId());
			managerDto.setWorkerName(manager.getWorkerName());
			managerDto.setWorkerType(manager.getWorkerType());
			managerDtos.add(managerDto);
		}
		return managerDtos;
	}

}
