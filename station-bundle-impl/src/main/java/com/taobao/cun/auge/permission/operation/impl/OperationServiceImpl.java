package com.taobao.cun.auge.permission.operation.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.ali.com.google.common.collect.Lists;
import com.alibaba.buc.acl.api.input.check.CheckPermissionsParam;
import com.alibaba.buc.acl.api.output.check.CheckPermissionsResult;
import com.alibaba.buc.acl.api.service.AccessControlService;
import com.alibaba.buc.api.exception.BucException;
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

	@Autowired
	private AccessControlService accessControlService;
	
	@Value("${acl.accessKey}")
	private String accessKey;
	
	@Autowired
	private AppResourceMapper appResourceMapper;
	
	OperationMatcher permissionMatcher = new PermissionMatcher();
	 
	OperationMatcher dataPermissionMatcher = new DataPermissionMatcher();
	
	DataOperationValueResolver valueResolver = new DataOperationValueResolver();
	    
	@Override
	public Map<String,List<Operation>> getPagedOperations(Integer bucUserId,List<String> operationsCodes,List<PagedOperationData> operationDatas){
		try {
			List<Operation> operations = getOperations(operationsCodes);
			CheckPermissionsResult checkPermissionsResult = getCheckPermissionResult(bucUserId, operations);
			return matchPagedOperations(operations,operationDatas,checkPermissionsResult);
		} catch (BucException e) {
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
		List<Operation> matchedOperations = Lists.newArrayList();
		for(PagedOperationData data : datas){
			for(Operation operation : operations ){
				if(permissionMatcher.match(new InnerPermissionData(checkPermissionsResult), operation) && dataPermissionMatcher.match(data, operation)){
					if(operation.getValue() != null){
						operation.setValue(valueResolver.resovlerValue(data, operation.getValue()));
					}
					if(operation.getName()!= null){
						operation.setName(valueResolver.resovlerValue(data, operation.getName()));
					}
					matchedOperations.add(operation);
				}
			}
			result.put(data.getDataId(), matchedOperations);
		}
		return result;
	}

	
	private List<Operation> matchOperations(List<Operation> operations,List<OperationData> datas,CheckPermissionsResult checkPermissionsResult){
		List<Operation> matchedOperations = Lists.newArrayList();
		for(OperationData data : datas){
			for(Operation operation : operations ){
				if(permissionMatcher.match(new InnerPermissionData(checkPermissionsResult), operation) && dataPermissionMatcher.match(data, operation)){
					if(operation.getValue() != null){
						operation.setValue(valueResolver.resovlerValue(data, operation.getValue()));
					}
					if(operation.getName()!= null){
						operation.setName(valueResolver.resovlerValue(data, operation.getName()));
					}
					matchedOperations.add(operation);
				}
			}
		}
		return matchedOperations;
	}
	

}
