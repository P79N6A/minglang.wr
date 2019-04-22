package com.taobao.cun.auge.flow;

import javax.annotation.Resource;

import com.taobao.cun.auge.client.result.ResultModel;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface= CuntaoWorkFlowDetailMobileService.class)
public class CuntaoWorkFlowDetailMobileServiceImpl implements CuntaoWorkFlowDetailMobileService {
	@Resource
	private GenericServiceInvoker genericServiceInvoker;
	
	@Override
	public ResultModel<Object> getFlowContent(String taskCode, Long objectId) {
		ResultModel<Object> result = new ResultModel<Object>();
		result.setResult(genericServiceInvoker.invoke(taskCode, objectId));
		result.setSuccess(true);
		return result;
	}
}
