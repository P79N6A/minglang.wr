package com.taobao.cun.auge.log.service.impl;

import javax.annotation.Resource;

import com.taobao.cun.auge.log.ExtAppBizLog;
import com.taobao.cun.auge.log.SimpleAppBizLog;
import com.taobao.cun.auge.log.bo.AppBizLogBo;
import com.taobao.cun.auge.log.service.AppBizLogService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * 应用日志记录
 * 
 * @author chengyu.zhoucy
 *
 */
@HSFProvider(serviceInterface = AppBizLogService.class)
public class AppBizLogServiceImpl implements AppBizLogService {
	@Resource
	private AppBizLogBo appBizLogBo;

	@Override
	public void addLog(SimpleAppBizLog simpleAppBizLog) {
		appBizLogBo.addLog(simpleAppBizLog);
	}

	@Override
	public void addLog(ExtAppBizLog extAppBizLog) {
		appBizLogBo.addLog(extAppBizLog);
	}

}
