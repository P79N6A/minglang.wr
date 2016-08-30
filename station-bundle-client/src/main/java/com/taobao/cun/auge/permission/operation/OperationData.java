package com.taobao.cun.auge.permission.operation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class OperationData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4849124188289885417L;

	private String dataId;
	
    private Map<String,Object> properties = new HashMap<String,Object>();
    
	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public void addProperty(String propertyName,Object value){
		properties.put(propertyName, value);
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
}
