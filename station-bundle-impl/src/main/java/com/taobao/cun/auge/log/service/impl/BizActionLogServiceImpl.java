package com.taobao.cun.auge.log.service.impl;

import javax.annotation.Resource;

import com.taobao.cun.auge.log.BizActionLogDto;
import com.taobao.cun.auge.log.bo.BizActionLogBo;
import com.taobao.cun.auge.log.service.BizActionLogService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = BizActionLogService.class)
public class BizActionLogServiceImpl implements BizActionLogService {
	@Resource
	private BizActionLogBo bizActionLogBo;
	
	@Override
	public void addLog(BizActionLogDto bizActionLogAddDto) {
		bizActionLogBo.addLog(bizActionLogAddDto);
	}

}
