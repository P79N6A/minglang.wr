package com.taobao.cun.auge.county;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.flow.FlowContent;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = TestAService.class)
public class TestAServiceImpl implements TestAService{

	@Override
	public FlowContent test1(String taskCode, Long id) {
		return FlowContent.create(id);
	}

	@Override
	public FlowContent test2(String taskCode, Long id) {
		return FlowContent.create(Lists.newArrayList(1,2,3,4,5));
	}

}
