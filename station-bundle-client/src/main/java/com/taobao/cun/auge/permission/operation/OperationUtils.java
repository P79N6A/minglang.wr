package com.taobao.cun.auge.permission.operation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;


public abstract class OperationUtils {

	
	public static Map<String,Object> extractDataProperties(Object object){
		Map<String,Object> properties = new HashMap<String,Object>();
		Field[] fields = object.getClass().getDeclaredFields();
		for(Field field : fields){
			if(field.isAnnotationPresent(OperationProperty.class)){
				extractProperty(object, properties, field,field.getAnnotation(OperationProperty.class)); 
			}
			if(field.isAnnotationPresent(OperationProperties.class)){
				OperationProperty[] dps = field.getAnnotation(OperationProperties.class).properties();
				for(OperationProperty prop : dps){
					extractProperty(object, properties, field,prop); 
				}
			}
		}
		return properties;
	}

	public static List<PagedOperationData> buildPagedOperationDatas(List<PagedOperationSupport> operationSupports){
		List<PagedOperationData> operationDatas = new ArrayList<PagedOperationData>();
		for(PagedOperationSupport operationSupport : operationSupports){
			PagedOperationData operationData = new PagedOperationData();
			operationData.setDataId(operationSupport.getDataId());
			operationData.setProperties(operationSupport.getDataProperties());
			operationDatas.add(operationData);
		}
		return operationDatas;
	}
	
	public static List<OperationData> buildOperationDatas(List<OperationSupport> operationSupports){
		List<OperationData> operationDatas = new ArrayList<OperationData>();
		for(OperationSupport operationSupport : operationSupports){
			OperationData operationData = new OperationData();
			operationData.setProperties(operationSupport.getDataProperties());
			operationDatas.add(operationData);
		}
		return operationDatas;
	}
	
	
	public static void mergePagedOperations(List<PagedOperationSupport> operationSupports,Map<String,List<Operation>> operationsResult){
		for(PagedOperationSupport operationSupport : operationSupports){
			List<Operation> operations = operationsResult.get(operationSupport.getDataId());
			operationSupport.setOperations(operations);
		}
	}
	
	private static void extractProperty(Object object, Map<String, Object> properties,Field field,OperationProperty property) {
		try {
			field.setAccessible(true);
			Object value = field.get(object);
			String path = property.path();
			String name = property.name();
			if (!"".equals(path)) {
				value = PropertyUtils.getNestedProperty(value, path);
			}
			name = !"".equals(name) ? name : field.getName();
			properties.put(name, value);
		} catch (Exception e) {
			throw new RuntimeException("extractDataProperties exception",e);
		}
	}
	
}
