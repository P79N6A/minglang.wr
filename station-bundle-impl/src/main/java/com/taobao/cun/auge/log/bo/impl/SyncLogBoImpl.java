package com.taobao.cun.auge.log.bo.impl;


import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.SyncLog;
import com.taobao.cun.auge.dal.mapper.SyncLogMapper;
import com.taobao.cun.auge.log.bo.SyncLogBo;

@Component
public class SyncLogBoImpl implements SyncLogBo {
	@Resource
	private SyncLogMapper syncLogMapper;
	
	@Override
	public SyncLog addLog(SyncLog syncLog) {
		syncLog.setGmtCreate(new Date());
		syncLog.setGmtModified(new Date());
		syncLogMapper.insert(syncLog);
		return syncLog;
	}

	@Override
	public void updateState(SyncLog syncLog) {
		syncLog.setGmtModified(new Date());
		syncLogMapper.updateByPrimaryKey(syncLog);
	}

	@Override
	public void clear() {
		syncLogMapper.clearAll();
	}

}
