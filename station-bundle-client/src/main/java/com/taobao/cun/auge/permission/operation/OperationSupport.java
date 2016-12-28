package com.taobao.cun.auge.permission.operation;

import java.util.Map;

public interface OperationSupport extends OperationAware{

	 Map<String,Object> getDataProperties();
	 
	 void setDataProperty(String name,Object value);
	 
	 void setDataPropertis(Map<String,Object> properties);
}
