package com.taobao.cun.auge.permission.operation.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.ali.com.google.common.collect.Lists;
import com.alibaba.buc.acl.api.input.check.CheckPermissionsParam;
import com.alibaba.buc.acl.api.output.check.CheckPermissionsResult;
import com.alibaba.buc.acl.api.service.AccessControlService;
import com.alibaba.buc.api.exception.BucException;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.dal.domain.AppResourceExample;
import com.taobao.cun.auge.dal.mapper.AppResourceMapper;
import com.taobao.cun.auge.permission.operation.Operation;
import com.taobao.cun.auge.permission.operation.OperationData;
import com.taobao.cun.auge.permission.operation.OperationService;
import com.taobao.cun.auge.permission.operation.PagedOperationData;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = OperationService.class)
public class OperationServiceImpl implements OperationService {

	public static final Log logger = LogFactory.getLog(OperationServiceImpl.class);

	
	@Autowired
	private AccessControlService accessControlService;
	
	@Value("${acl.accessKey}")
	private String accessKey;
	
	@Autowired
	private AppResourceMapper appResourceMapper;
	
	OperationMatcher permissionMatcher = new PermissionMatcher();
	 
	OperationMatcher dataConditionMatcher = new DataConditionMatcher();
	
	OperationValueResolver valueResolver = new OperationValueResolver();
	    
	@Override
	public Map<String,List<Operation>> getPagedOperations(Integer bucUserId,List<String> operationsCodes,List<PagedOperationData> operationDatas){
		try {
			List<Operation> operations = getOperations(operationsCodes);
			CheckPermissionsResult checkPermissionsResult = getCheckPermissionResult(bucUserId, operations);
			return matchPagedOperations(operations,operationDatas,checkPermissionsResult);
		} catch (BucException e) {
			logger.error("getPagedOperations error!operationsCodes["+JSON.toJSONString(operationsCodes)+"] operationDatas["+operationDatas+"]",e);
			throw new  AugeServiceException(e);
		}
	}

	@Override
	public List<Operation> getOperations(Integer bucUserId, List<String> operationsCodes,
			List<OperationData> operationDatas) throws AugeServiceException {
		try {
			List<Operation> operations = getOperations(operationsCodes);
			CheckPermissionsResult checkPermissionsResult = getCheckPermissionResult(bucUserId, operations);
			return matchOperations(operations,operationDatas,checkPermissionsResult);
		} catch (Exception e) {
			logger.error("getOperations error!operationsCodes["+JSON.toJSONString(operationsCodes)+"] operationDatas["+operationDatas+"]",e);
			throw new  AugeServiceException(e);
		}
	}
	
	
	private CheckPermissionsResult getCheckPermissionResult(Integer bucUserId, List<Operation> operations) {
		List<String> permissionNames = operations.stream().filter(oper ->  StringUtils.isNotEmpty(oper.getPermission())).map(oper -> oper.getPermission()).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(permissionNames))return null;
		CheckPermissionsParam checkPermissionsParam = new CheckPermissionsParam();
		checkPermissionsParam.setAccessKey(accessKey);
		checkPermissionsParam.setUserId(bucUserId);
		checkPermissionsParam.setPermissionNames(permissionNames);
		CheckPermissionsResult checkPermissionsResult =	accessControlService.checkPermissions(checkPermissionsParam);
		return checkPermissionsResult;
	}

	private List<Operation> getOperations(List<String> operationsCode) throws BucException {
		AppResourceExample example = new AppResourceExample();
		example.createCriteria().andTypeIn(operationsCode).andIsDeletedEqualTo("n");
		List<AppResource> resources = appResourceMapper.selectByExample(example);
		List<Operation> operations = resources.stream().map(resource -> {
			 return createOperation(resource);
		}).collect(Collectors.toList());
		return operations;
	}

	private Operation createOperation(AppResource resource) {
		Operation operation = new Operation();
		operation.setPermission(resource.getPermissionNames());
		operation.setCondition(resource.getDataCondition());
		operation.setName(resource.getName());
		operation.setType(resource.getType());
		operation.setValue(resource.getValue());
		operation.setCode(resource.getCode());
		return operation;
	}

	private Map<String,List<Operation>> matchPagedOperations(List<Operation> operations,List<PagedOperationData> datas,CheckPermissionsResult checkPermissionsResult){
		Map<String,List<Operation>> result = Maps.newLinkedHashMap();
		for(PagedOperationData data : datas){
			List<Operation> matchedOperations = Lists.newArrayList();
			for(Operation operation : operations ){
				try {
					if(permissionMatcher.match(new InnerPermissionData(checkPermissionsResult), operation) && dataConditionMatcher.match(data, operation)){
						Operation matchedOperation = new Operation();
						 matchedOperation.setCode(operation.getCode());
						 matchedOperation.setCondition(operation.getCondition());
						 matchedOperation.setPermission(operation.getPermission());
						 matchedOperation.setType(operation.getType());
						if(operation.getValue() != null){
							matchedOperation.setValue(valueResolver.resovlerValue(data, operation.getValue()));
						}
						if(operation.getName()!= null){
							matchedOperation.setName(valueResolver.resovlerValue(data, operation.getName()));
						}
						matchedOperations.add(matchedOperation);
					}
				} catch (Exception e) {
					logger.error("match operation error!operationName:["+operation.getName()+"],operationData:["+JSON.toJSONString(data)+"]",e);
					//ingore exception
				}
			
			}
			result.put(data.getDataId(), matchedOperations);
		}
		return result;
	}

	
	private List<Operation> matchOperations(List<Operation> operations,List<OperationData> datas,CheckPermissionsResult checkPermissionsResult){
		List<Operation> matchedOperations = Lists.newArrayList();
		for(Operation operation : operations ){
			for(OperationData data : datas){
				try {
					if(permissionMatcher.match(new InnerPermissionData(checkPermissionsResult), operation) && dataConditionMatcher.match(data, operation)){
						 Operation matchedOperation = new Operation();
						 matchedOperation.setCode(operation.getCode());
						 matchedOperation.setCondition(operation.getCondition());
						 matchedOperation.setPermission(operation.getPermission());
						 matchedOperation.setType(operation.getType());
						if(operation.getValue() != null){
							matchedOperation.setValue(valueResolver.resovlerValue(data, operation.getValue()));
						}
						if(operation.getName()!= null){
							matchedOperation.setName(valueResolver.resovlerValue(data, operation.getName()));
						}
						matchedOperations.add(matchedOperation);
					}
				} catch (Exception e) {
					logger.error("match operation error!operationName:["+operation.getName()+"],operationData:["+JSON.toJSONString(data)+"]",e);
					//ingore exception
				}
			}
		}
		return matchedOperations;
	}
	

}
