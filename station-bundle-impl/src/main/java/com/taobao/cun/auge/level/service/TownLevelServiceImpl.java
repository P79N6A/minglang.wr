package com.taobao.cun.auge.level.service;

import javax.annotation.Resource;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.level.bo.TownLevelBo;
import com.taobao.cun.auge.level.dto.TownLevelCondition;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface= TownLevelService.class)
public class TownLevelServiceImpl implements TownLevelService {
	@Resource
	private TownLevelBo townLevelBo;
	
	@Override
	public TownLevelDto getTownLevel(Long id) {
		return townLevelBo.getTownLevel(id);
	}

	@Override
	public PageDto<TownLevelDto> query(TownLevelCondition townLevelCondition) {
		return townLevelBo.query(townLevelCondition);
	}

}
