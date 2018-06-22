package com.taobao.cun.auge.station.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.taobao.cun.auge.dal.mapper.TownBlacknameMapper;
import com.taobao.cun.auge.station.service.TownBlacknameService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("townBlacknameService")
@HSFProvider(serviceInterface = TownBlacknameService.class)
public class TownBlacknameServiceImpl implements TownBlacknameService {
	@Resource
	private TownBlacknameMapper townBlacknameMapper;
	
	@Override
	public boolean isBlackname(String countyName, String townName) {
		//如果是街道，则属于黑名单之列
		if(townName.endsWith("街道")) return true;
		//检查是否在黑名单中
		return townBlacknameMapper.countBlackname(countyName, townName) > 0;
	}

}
