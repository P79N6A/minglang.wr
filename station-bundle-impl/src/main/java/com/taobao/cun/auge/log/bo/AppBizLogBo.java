package com.taobao.cun.auge.log.bo;

import com.taobao.cun.auge.log.ExtAppBizLog;
import com.taobao.cun.auge.log.SimpleAppBizLog;

/**
 * 记录应用日志
 * 
 * @author chengyu.zhoucy
 *
 */
public interface AppBizLogBo {
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
}
