package com.taobao.cun.auge.log.service.impl;

import javax.annotation.Resource;

import com.taobao.cun.auge.log.bo.SyncLogBo;
import com.taobao.cun.auge.log.service.SyncLogService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = SyncLogService.class)
public class SyncLogServiceImpl implements SyncLogService {
	@Resource
	private SyncLogBo syncLogBo;
	
	@Override
	public void clear() {
		syncLogBo.clear();
	}

}
