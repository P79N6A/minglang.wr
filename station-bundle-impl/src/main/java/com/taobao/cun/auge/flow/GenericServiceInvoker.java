package com.taobao.cun.auge.flow;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class GenericServiceInvoker {
	@Resource
	private ServiceFactory serviceFactory;
	
	@SuppressWarnings("unchecked")
	Object invoke(String taskCode, Long objectId) {
		ServiceMeta serviceMeta = serviceFactory.getServiceMeta(taskCode);
		if(serviceMeta == null) {
			throw new UnsupportedOperationException("不支持该流程类型：" + taskCode);
		}
		Map<String, Object> result = (Map<String, Object>) serviceFactory.getService(taskCode).$invoke(serviceMeta.getServiceMethod(), 
				new String[] {"java.lang.String", "java.lang.Long"}, 
				new Object[] {taskCode, objectId});
		return result.get("result");
	}
}
