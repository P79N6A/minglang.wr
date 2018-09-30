package com.taobao.cun.auge.log.service;

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
}
