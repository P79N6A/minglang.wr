package com.taobao.cun.auge.station.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.taobao.cun.auge.dal.mapper.TownBlacknameMapper;
import com.taobao.cun.auge.station.service.TownBlacknameService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 *  黑名单镇判断
 * @author chengyu.zhoucy
 *
 */
@Service("townBlacknameService")
@HSFProvider(serviceInterface = TownBlacknameService.class)
public class TownBlacknameServiceImpl implements TownBlacknameService {
	@Resource
	private TownBlacknameMapper townBlacknameMapper;
	
	@Override
	public boolean isBlackname(String countyName, String townName) {
		//先检查是否在白名单里,如果在白名单里就无需做后续校验了
		if(townBlacknameMapper.countWhitename(countyName, townName) > 0) {
			return false;
		}
		//如果是街道，则属于黑名单之列
		if(townName.endsWith("街道")) {
			return true;
		}
		//检查是否在黑名单中
		return townBlacknameMapper.countBlackname(countyName, townName) > 0;
	}

	@Override
	public void recordPartnerApplyBlackname(Long partnerApplyId) {
		try {
			townBlacknameMapper.insertInBlacknamePartnerApply(partnerApplyId);
		}catch(Throwable e) {
			//捕获所有异常
		}
	}

}
