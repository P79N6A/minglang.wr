package com.taobao.cun.auge.permission.operation.impl;

import com.taobao.cun.auge.permission.operation.DataOperation;
import com.taobao.cun.auge.permission.operation.OperationData;

public interface OperationMatcher {

	
	boolean match(OperationData data,DataOperation operation);
	
}
