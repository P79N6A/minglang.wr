package com.taobao.cun.auge.level.service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.level.dto.TownLevelCondition;
import com.taobao.cun.auge.level.dto.TownLevelDto;

/**
 * 市场分层服务
 * 
 * @author chengyu.zhoucy
 *
 */
public interface TownLevelService {
	/**
	 * 获取单个市场分层数据
	 * @param id
	 * @return
	 */
	TownLevelDto getTownLevel(Long id);
	
	/**
	 * 分页查询市场分层数据
	 * @param townLevelCondition
	 * @return
	 */
	PageDto<TownLevelDto> query(TownLevelCondition townLevelCondition);
}
