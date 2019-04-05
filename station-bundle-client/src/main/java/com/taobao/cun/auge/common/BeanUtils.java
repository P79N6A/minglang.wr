package com.taobao.cun.auge.common;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

public class BeanUtils {
	public static void copyMap(Map<String, ?> source, Object target) {
		source.keySet().forEach(k->{
			if(PropertyUtils.isWriteable(target, k)) {
				try {
					PropertyUtils.setProperty(target, k, source.get(k));
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				}
			}
		});
	}
}
