package com.taobao.cun.auge.permission.operation.impl;

import org.apache.commons.lang3.StringUtils;

import com.taobao.cun.auge.permission.operation.Operation;
import com.taobao.cun.auge.permission.operation.OperationData;

public class PermissionMatcher implements OperationMatcher{
	
	@Override
	public boolean match(OperationData operationData,Operation operation) {
		if(StringUtils.isEmpty(operation.getPermission())) {
            return true;
        }
		if(operationData instanceof InnerPermissionData){
			InnerPermissionData permissionData = (InnerPermissionData)operationData;
			return permissionData.hasPermission(operation.getPermission());
		}
		return false;
	}

}
