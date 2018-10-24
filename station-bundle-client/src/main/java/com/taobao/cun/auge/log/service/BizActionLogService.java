package com.taobao.cun.auge.log.service;

import java.util.List;

import com.taobao.cun.auge.log.BizActionEnum;
import com.taobao.cun.auge.log.BizActionLogDto;

/**
 * 业务动作记录
 * @author chengyu.zhoucy
 *
 */
public interface BizActionLogService {
	/**
	 * 添加一条日志
	 * @param bizActionLogAddDto
	 */
	void addLog(BizActionLogDto bizActionLogAddDto);
	
	/**
	 * 查询日志
	 * @param objectId
	 * @param objectType
	 * @param bizActionEnum
	 * @return
	 */
	List<BizActionLogDto> getBizActionLogs(Long objectId, String objectType, BizActionEnum bizActionEnum);
}
