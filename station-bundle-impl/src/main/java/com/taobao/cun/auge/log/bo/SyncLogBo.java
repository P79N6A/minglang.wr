package com.taobao.cun.auge.log.bo;

import com.taobao.cun.auge.dal.domain.SyncLog;

public interface SyncLogBo {
	SyncLog addLog(SyncLog syncLog);
	
	void updateState(SyncLog syncLog);
}
