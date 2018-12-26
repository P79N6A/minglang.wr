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
	 */
	void agree(String applyId);
	
	/**
	 * 拒绝
	 * @param applyId
	 */
	void disagree(String applyId);
	
	/**
	 * 获取申请单
	 * @param id
	 * @return
	 */
	TownLevelUpdateApplyDto getTownLevelUpdateApplyDto(Long id);
}
