package com.alibaba.shared.xfsm.support.spring;

import com.alibaba.shared.xfsm.spi.ResourceAnnotationHandler;

public class SimpleResourceAnnotationHandler implements ResourceAnnotationHandler {

	@Override
	public Object process(Object target) throws Exception {
		return 	SpringHolder.getService(target.getClass());
	}

}
