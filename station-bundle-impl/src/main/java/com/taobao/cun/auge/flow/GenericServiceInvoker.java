package com.taobao.cun.auge.flow;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class GenericServiceInvoker {
	@Resource
	private ServiceFactory serviceFactory;
	
	FlowContent invoke(String taskCode, Long objectId) {
		ServiceMeta serviceMeta = serviceFactory.getServiceMeta(taskCode);
		return (FlowContent) serviceFactory.getService(taskCode).$invoke(serviceMeta.getServiceMethod(), 
				new String[] {"java.lang.String", "java.lang.Long"}, 
				new Object[] {taskCode, objectId});
	}
}
