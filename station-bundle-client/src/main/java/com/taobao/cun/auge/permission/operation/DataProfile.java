package com.taobao.cun.auge.permission.operation;

import java.util.Map;

public interface DataProfile extends OperationAware{

	 String getDataId();
	 
	 Map<String,Object> getDataProperties();
}
