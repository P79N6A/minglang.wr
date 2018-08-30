package com.taobao.cun.auge.monitor;

public interface BusinessMonitorBO {

	public void addBusinessMonitor(String businessCode, Long businessKey,String params,String errorCode,String errorMessage);
	
	public void fixBusinessMonitor(String businessCode,Long businessKey);
}
