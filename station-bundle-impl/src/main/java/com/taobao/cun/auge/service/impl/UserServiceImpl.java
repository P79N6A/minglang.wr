package com.taobao.cun.auge.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.StationMapper;
import com.taobao.cun.auge.service.UserService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * user service implementation
 *
 * @author leijuan
 */
@HSFProvider(serviceInterface = UserService.class)
public class UserServiceImpl implements UserService {


	@Autowired
	private StationMapper stationMapper;
	
    public String getNick(Long id) {
    	Station station = stationMapper.selectByPrimaryKey(id);
        return station.getStationNum();
    }
}
