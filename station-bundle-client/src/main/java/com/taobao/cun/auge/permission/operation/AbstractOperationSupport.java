package com.taobao.cun.auge.permission.operation;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractOperationSupport implements OperationSupport {

	
	private Map<String,Object> properties = new HashMap<String,Object>();
	

	@Override
	public Map<String, Object> getDataProperties() {
		return properties;
	}

	@Override
	public void setDataProperty(String name, Object value) {
		properties.put(name, value);
	}

	@Override
	public void setDataPropertis(Map<String, Object> properties) {
		properties.putAll(properties);
		
	}


}
