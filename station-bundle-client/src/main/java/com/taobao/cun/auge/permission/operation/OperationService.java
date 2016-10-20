package com.taobao.cun.auge.permission.operation;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface OperationService {
	/**
	 * 获取分页列表上的操作列表
	 * @param bucUserId 
	 * @param operationsCode
	 * @param operationDatas
	 * @return
	 * @throws AugeServiceException
	 */
	Map<String,List<Operation>> getPagedOperations(Integer bucUserId,List<String> operationsCode,List<PagedOperationData> operationDatas) throws AugeServiceException;
	
	/**
	 * 获取普通操作列表
	 * @param bucUserId
	 * @param operationsCode
	 * @param operationDatas
	 * @return
	 * @throws AugeServiceException
	 */
	List<Operation> getOperations(Integer bucUserId,List<String> operationsCode,List<OperationData> operationDatas) throws AugeServiceException;

}