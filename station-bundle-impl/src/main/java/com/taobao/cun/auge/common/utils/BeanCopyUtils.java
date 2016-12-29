package com.taobao.cun.auge.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

/*
 * @author guolei.maogl 2015-1-10
 */
public class BeanCopyUtils {
	/**
	 * 拷贝source对象的非空属性到target对象上
	 * 
	 * @param source
	 * @param target
	 */
	public static void copyNotNullProperties(Object source, Object target) {
		Assert.notNull(source, "source is null");
		Assert.notNull(target, "target is null");

		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(target.getClass());
		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null) {
				PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						if (value != null && targetPd.getPropertyType().isInstance(value)) {
							Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						}
					} catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target", ex);
					}
				}
			}
		}
	}
}
