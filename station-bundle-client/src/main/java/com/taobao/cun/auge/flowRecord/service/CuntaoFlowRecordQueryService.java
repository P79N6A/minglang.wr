package com.taobao.cun.auge.flowRecord.service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.flowRecord.condition.CuntaoFlowRecordPageCondition;
import com.taobao.cun.auge.flowRecord.dto.CuntaoFlowRecordDto;

public interface CuntaoFlowRecordQueryService {
	/**
	 * 分页查询流程日志
	 * 
	 * @param pageCondition
	 * @return
	 */
	public PageDto<CuntaoFlowRecordDto> queryByPage(CuntaoFlowRecordPageCondition pageCondition);
}
