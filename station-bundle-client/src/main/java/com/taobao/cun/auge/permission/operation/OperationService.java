package com.taobao.cun.auge.permission.operation;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface OperationService {
	/**
	 * 绑定数据操作到数据定向上
	 * @param bucUserId
	 * @param operationScene Acl上配置的操作名称
	 * @param operationName
	 * @return
	 */
	Map<String,List<Operation>> getPagedOperations(Integer bucUserId,List<String> operationsCode,List<PagedOperationData> operationDatas) throws AugeServiceException;
	
	List<Operation> getOperations(Integer bucUserId,List<String> operationsCode,List<OperationData> operationDatas) throws AugeServiceException;

}
