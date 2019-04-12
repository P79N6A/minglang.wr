package com.taobao.cun.auge.flow;

import com.taobao.cun.crius.common.resultmodel.ResultModel;

public interface CuntaoWorkFlowDetailMobileService {
	/**
	 * 获取流程内容
	 * @param taskCode
	 * @param objectId
	 * @return
	 */
	ResultModel<FlowContent> getFlowContent(String taskCode, Long objectId);
}
