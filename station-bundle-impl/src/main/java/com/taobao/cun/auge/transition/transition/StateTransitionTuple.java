package com.taobao.cun.auge.transition.transition;

import java.io.Serializable;
import java.util.Map;

/**
 * 状态变更元组
 * @author zhenhuan.zhangzh
 *
 */
public class StateTransitionTuple {

	private Map<String, Serializable> record;
	
	private Map<String, Serializable> modifiedRecord;

	
	public StateTransitionTuple(Map<String, Serializable> record,Map<String, Serializable> modifiedRecord){
		this.record = record; 
		this.modifiedRecord = modifiedRecord;
	}
	
	public Object getNewValue(String key){
		if(modifiedRecord != null){
			return modifiedRecord.get(key);
		}
		return null;
	}
	
	public Object getValue(String key){
		if(record != null){
			return record.get(key);
		}
		return null;
	}
	
	public boolean isInsert(){
		return modifiedRecord == null;
	}
	
	public boolean isUpdate(){
		return modifiedRecord != null;
	}
}
