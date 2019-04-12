package com.taobao.cun.auge.flow;

import javax.annotation.Resource;

import com.taobao.cun.auge.client.result.ResultModel;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface= CuntaoWorkFlowDetailMobileService.class)
public class CuntaoWorkFlowDetailMobileServiceImpl implements CuntaoWorkFlowDetailMobileService {
	@Resource
	private GenericServiceInvoker genericServiceInvoker;
	
	@Override
	public ResultModel<FlowContent> getFlowContent(String taskCode, Long objectId) {
		FlowContent flowContent = genericServiceInvoker.invoke(taskCode, objectId);
		ResultModel<FlowContent> result = new ResultModel<FlowContent>();
		result.setResult(flowContent);
		result.setSuccess(true);
		return result;
	}
}
