package com.taobao.cun.auge.permission.operation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;


public abstract class DataPropertyUtils {

	
	public static Map<String,Object> extractDataProperties(Object object){
		Map<String,Object> properties = new HashMap<String,Object>();
		Field[] fields = object.getClass().getDeclaredFields();
		for(Field field : fields){
			if(field.isAnnotationPresent(DataProperty.class)){
				extractProperty(object, properties, field,field.getAnnotation(DataProperty.class)); 
			}
			if(field.isAnnotationPresent(DataProperties.class)){
				DataProperty[] dps = field.getAnnotation(DataProperties.class).properties();
				for(DataProperty prop : dps){
					extractProperty(object, properties, field,prop); 
				}
			}
		}
		return properties;
	}

	public static List<OperationData> buildOperationDatas(List<DataProfile> datas){
		List<OperationData> operationDatas = new ArrayList<OperationData>();
		for(Object data : datas){
			if(data instanceof DataProfile){
				 throw new RuntimeException("data is not instanceof DataProfile"); 
			}
			OperationData operationData = new OperationData();
			DataProfile profile = (DataProfile)data;
			operationData.setDataId(profile.getDataId());
			operationData.setProperties(profile.getDataProperties());
			operationDatas.add(operationData);
		}
		return operationDatas;
	}
	
	public static void mergeOperations(List<DataProfile> datas,Map<String,List<Operation>> operationsResult){
		for(DataProfile data : datas){
			List<Operation> operations = operationsResult.get(data.getDataId());
			data.setOperations(operations);
		}
	}
	
	private static void extractProperty(Object object, Map<String, Object> properties,Field field,DataProperty property) {
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
