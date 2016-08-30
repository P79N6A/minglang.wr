package com.taobao.cun.auge.permission.operation.impl;

import com.alibaba.buc.acl.api.output.check.CheckPermissionsResult;
import com.taobao.cun.auge.permission.operation.OperationData;

public class InnerPermissionData extends OperationData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8151124124702563202L;

	public InnerPermissionData(CheckPermissionsResult checkPermissionsResult){
		if(checkPermissionsResult != null && checkPermissionsResult.isSuccess()){
			checkPermissionsResult.getCheckPermissionResults().forEach(result -> {
				this.addProperty(result.getPermissionName(), result.isAccessible());
			});
		}
	}
	
	public Boolean hasPermission(String permission){
		if(this.getProperties().get(permission) != null){
			return (Boolean) this.getProperties().get(permission);
		}
		return true;
	}
}
