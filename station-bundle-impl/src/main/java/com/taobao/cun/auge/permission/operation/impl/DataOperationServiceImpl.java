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
import com.taobao.cun.auge.permission.operation.DataOperation;
import com.taobao.cun.auge.permission.operation.DataOperationService;
import com.taobao.cun.auge.permission.operation.OperationData;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = DataOperationService.class)
public class DataOperationServiceImpl implements DataOperationService {

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
	public Map<String,List<DataOperation>> getDataOperations(Integer bucUserId,List<String> operationsCode,List<OperationData> operationDatas){
		try {
			List<DataOperation> operations = getDataOperations(operationsCode);
			CheckPermissionsResult checkPermissionsResult = getCheckPermissionResult(bucUserId, operations);
			return matchOperations(operations,operationDatas,checkPermissionsResult);
		} catch (BucException e) {
			e.printStackTrace();
		}
		return null;
	}

	private CheckPermissionsResult getCheckPermissionResult(Integer bucUserId, List<DataOperation> operations) {
		List<String> permissionNames = operations.stream().filter(oper ->  StringUtils.isNotEmpty(oper.getPermission())).map(oper -> oper.getPermission()).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(permissionNames))return null;
		CheckPermissionsParam checkPermissionsParam = new CheckPermissionsParam();
		checkPermissionsParam.setAccessKey(accessKey);
		checkPermissionsParam.setUserId(bucUserId);
		checkPermissionsParam.setPermissionNames(permissionNames);
		CheckPermissionsResult checkPermissionsResult =	accessControlService.checkPermissions(checkPermissionsParam);
		return checkPermissionsResult;
	}

	private List<DataOperation> getDataOperations(List<String> operationsCode) throws BucException {
		AppResourceExample example = new AppResourceExample();
		example.createCriteria().andTypeIn(operationsCode).andIsDeletedEqualTo("n");
		List<AppResource> resources = appResourceMapper.selectByExample(example);
		List<DataOperation> operations = resources.stream().map(resource -> {
			 return createOperation(resource);
		}).collect(Collectors.toList());
		return operations;
	}

	private DataOperation createOperation(AppResource resource) {
		DataOperation operation = new DataOperation();
		operation.setPermission(resource.getPermissionNames());
		operation.setCondition(resource.getDataCondition());
		operation.setName(resource.getName());
		operation.setType(resource.getType());
		operation.setValue(resource.getValue());
		return operation;
	}

	private Map<String,List<DataOperation>> matchOperations(List<DataOperation> operations,List<OperationData> datas,CheckPermissionsResult checkPermissionsResult){
		Map<String,List<DataOperation>> result = Maps.newLinkedHashMap();
		List<DataOperation> matchedOperations = Lists.newArrayList();
		for(OperationData data : datas){
			for(DataOperation operation : operations ){
				if(permissionMatcher.match(new InnerPermissionData(checkPermissionsResult), operation) && dataPermissionMatcher.match(data, operation)){
					if(operation.getValue() != null){
						operation.setValue(valueResolver.resovlerValue(data, operation.getValue()));
					}
					matchedOperations.add(operation);
				}
			}
			result.put(data.getDataId(), matchedOperations);
		}
		return result;
	}
	

}
