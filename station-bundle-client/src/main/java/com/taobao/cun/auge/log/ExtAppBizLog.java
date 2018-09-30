package com.taobao.cun.auge.log;

import java.util.List;

/**
 * 扩展应用日志，可以存储详细内容
 * 
 * @author chengyu.zhoucy
 *
 */
public class ExtAppBizLog extends SimpleAppBizLog{
    private List<LogContent> logContents;

	public List<LogContent> getLogContents() {
		return logContents;
	}

	public void setLogContents(List<LogContent> logContents) {
		this.logContents = logContents;
	}

}
