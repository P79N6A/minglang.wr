package com.taobao.cun.auge.log.bo;

import java.util.List;

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
	
	/**
	 * 查询日志
	 * @param SimpleAppBizLog
	 */
	public List<SimpleAppBizLog> queryLog(SimpleAppBizLog simpleAppBizLog);
	
	/**
	 * 删除日志
	 * @param SimpleAppBizLog
	 */
	public void deleteLog(SimpleAppBizLog simpleAppBizLog);
	
	/**
	 * 编辑日志
	 * @param SimpleAppBizLog
	 */
	public void updateLog(SimpleAppBizLog updateAppBizLog,SimpleAppBizLog whereAppBizLog);

}
