package com.taobao.cun.auge.level.service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.level.dto.TownLevelCondition;
import com.taobao.cun.auge.level.dto.TownLevelDto;

public interface TownLevelService {
	TownLevelDto getTownLevel(Long id);
	
	PageDto<TownLevelDto> query(TownLevelCondition townLevelCondition);
}
