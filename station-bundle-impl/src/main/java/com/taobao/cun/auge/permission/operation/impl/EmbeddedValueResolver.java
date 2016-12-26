package com.taobao.cun.auge.permission.operation.impl;

import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;

import com.taobao.cun.auge.permission.operation.OperationData;

public class EmbeddedValueResolver {

	private PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("{","}");
	
	String resovle(OperationData operationData,String value){
		return propertyPlaceholderHelper.replacePlaceholders(value, new PlaceholderResolver(){

			@Override
			public String resolvePlaceholder(String placeholderName) {
				return operationData.getProperties().get(placeholderName).toString();
			}
		});
	}
}
