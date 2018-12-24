package com.taobao.cun.auge.log.service;

import java.util.List;

import com.taobao.cun.auge.log.ExtAppBizLog;
import com.taobao.cun.auge.log.SimpleAppBizLog;

/**
 * 应用业务日志
 * 
 * @author chengyu.zhoucy
 *
 */
public interface AppBizLogService {
	/**
	 * 新加日志
	 * @param appBizLog
	 */
	void addLog(SimpleAppBizLog simpleAppBizLog);
	
	/**
	 * 新加日志
	 * @param appBizLog
	 */
	void addLog(ExtAppBizLog extAppBizLog);

	public void deleteLog(SimpleAppBizLog simpleAppBizLog);

	public List<SimpleAppBizLog> queryLog(SimpleAppBizLog simpleAppBizLog);
	
	
	
}
