package com.taobao.cun.auge.county.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.county.StationManagerService;
import com.taobao.cun.auge.county.dto.CountyDto;
import com.taobao.cun.auge.county.dto.StationManagerDto;
import com.taobao.cun.auge.dal.domain.StationManager;
import com.taobao.cun.auge.dal.domain.StationManagerExample;
import com.taobao.cun.auge.dal.domain.StationManagerExample.Criteria;
import com.taobao.cun.auge.dal.mapper.StationManagerMapper;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("StationManagerService")
@HSFProvider(serviceInterface = StationManagerService.class)
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class StationManagerServiceImpl implements StationManagerService {

	@Autowired
	StationManagerMapper stationManagerMapper;

	@Override
	public List<StationManagerDto> getManagersByStationId(Long stationId) {
		Assert.notNull(stationId);
		StationManagerExample e = new StationManagerExample();
		Criteria c = e.createCriteria();
		c.andIsDeletedEqualTo("n").andStationIdEqualTo(
				String.valueOf(stationId));
		List<StationManager> managers = stationManagerMapper.selectByExample(e);
		List<StationManagerDto> managerDtos = new ArrayList<StationManagerDto>();
		for (StationManager manager : managers) {
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

	@Override
	public void saveOrUpdateStationManager(String operator, CountyDto countyDto) {
		List<StationManagerDto> managers = countyDto.getManagers();
		// 批量逻辑删除原数据
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stationId", countyDto.getId());
		param.put("modifier", operator);
		stationManagerMapper.deleteByStationId(param);
		for (StationManagerDto manager : managers) {
			StationManager stationManager = new StationManager();
			stationManager.setCreator(operator);
			stationManager.setModifier(operator);
			stationManager.setGmtCreate(new Date());
			stationManager.setGmtModified(new Date());
			stationManager.setExtra(manager.getExtra());
			stationManager.setIsDeleted("n");
			stationManager.setModifier(operator);
			stationManager.setWorkerId(manager.getWorkerId());
			stationManager.setWorkerName(manager.getWorkerName());
			stationManager.setWorkerType(manager.getWorkerType());
			stationManager.setStationId("" + countyDto.getId());
			stationManagerMapper.insert(stationManager);
		}
	}

}
