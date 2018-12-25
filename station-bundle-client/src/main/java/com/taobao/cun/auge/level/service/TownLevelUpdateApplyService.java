package com.taobao.cun.auge.level.service;

import com.taobao.cun.auge.level.dto.TownLevelUpdateApplyDto;

/**
 * 镇域市场分层数据更新申请
 * 
 * @author chengyu.zhoucy
 *
 */
public interface TownLevelUpdateApplyService {
	/**
	 * 申请变更
	 * 
	 * @param townLevelUpdateApplyDto
	 */
	void apply(TownLevelUpdateApplyDto townLevelUpdateApplyDto);
	
	/**
	 * 同意
	 * @param applyId
	 * @param operator
	 */
	void agree(String applyId, String operator);
	
	/**
	 * 拒绝
	 * @param applyId
	 * @param operator
	 */
	void disagree(String applyId, String operator);
}
