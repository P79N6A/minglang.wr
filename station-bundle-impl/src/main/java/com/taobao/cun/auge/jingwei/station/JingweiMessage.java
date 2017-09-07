package com.taobao.cun.auge.jingwei.station;

import java.io.Serializable;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.dal.domain.SyncLog;

public class JingweiMessage {
	private String action;
	
	private Map<String, Serializable> modifiedRow;
	
	private Map<String, Serializable> rowDataMap;
	
	private String type;

	public JingweiMessage(String action, String type, Map<String, Serializable> rowDataMap, Map<String, Serializable> modifiedRow){
		this.action = action;
		this.type = type;
		this.modifiedRow = modifiedRow;
		this.rowDataMap = rowDataMap;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Map<String, Serializable> getModifiedRow() {
		return modifiedRow;
	}

	public void setModifiedRow(Map<String, Serializable> modifiedRow) {
		this.modifiedRow = modifiedRow;
	}

	public Map<String, Serializable> getRowDataMap() {
		return rowDataMap;
	}

	public void setRowDataMap(Map<String, Serializable> rowDataMap) {
		this.rowDataMap = rowDataMap;
	}
	
	public SyncLog toSyncLog(){
		SyncLog syncLog = new SyncLog();
		syncLog.setContent(JSON.toJSONString(this));
		syncLog.setState("new");
		syncLog.setType(type);
		return syncLog;
	}
}
