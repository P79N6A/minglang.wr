package com.taobao.cun.auge.flow;

import com.taobao.cun.auge.client.result.ResultModel;

public interface CuntaoWorkFlowDetailMobileService {
	/**
	 * 获取流程内容
	 * @param taskCode
	 * @param objectId
	 * @return
	 */
	ResultModel<Object> getFlowContent(String taskCode, Long objectId);
}
