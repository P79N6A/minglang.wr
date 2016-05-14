package com.taobao.cun.auge.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 添加domain默认参数工具类
 * 
 * @author quanzhu.wangqz
 *
 */
public class DomainUtils {

	public static <T> T beforeInsert(T obj, String operator, Class<T> clazz) {
		try {
			Date now = new Date();
			Method m1 = clazz.getDeclaredMethod("setIsDeleted", String.class);
			Method m2 = clazz.getDeclaredMethod("setGmtCreate", Date.class);
			Method m3 = clazz.getDeclaredMethod("setGmtModified", Date.class);
			Method m4 = clazz.getDeclaredMethod("setCreator", String.class);
			Method m5 = clazz.getDeclaredMethod("setModifier", String.class);

			m1.invoke(obj, "n");
			m2.invoke(obj, now);
			m3.invoke(obj, now);
			m4.invoke(obj, operator);
			m5.invoke(obj, operator);
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		}
		return obj;
	}
	
	public static <T> T beforeUpdate(T obj, String operator, Class<T> clazz) {
		try {
			Date now = new Date();
			Method m3 = clazz.getDeclaredMethod("setGmtModified", Date.class);
			Method m5 = clazz.getDeclaredMethod("setModifier", String.class);

			m3.invoke(obj, now);
			m5.invoke(obj, operator);
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		}
		return obj;
	}
	
	public static <T> T beforeDelete(T obj, String operator, Class<T> clazz) {
		try {
			Date now = new Date();
			Method m1 = clazz.getDeclaredMethod("setIsDeleted", String.class);
			Method m3 = clazz.getDeclaredMethod("setGmtModified", Date.class);
			Method m5 = clazz.getDeclaredMethod("setModifier", String.class);

			m1.invoke(obj, "y");
			m3.invoke(obj, now);
			m5.invoke(obj, operator);
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		}
		return obj;
	}

/*	public static void main(String[] args) {
		PartnerLifecycleItems a = beforeUpdate(new PartnerLifecycleItems(), "aaa",PartnerLifecycleItems.class);
		System.out.println(a.getIsDeleted());
		System.out.println(a.getModifier());
		System.out.println(a.getCreator());
		System.out.println(a.getGmtCreate());
		System.out.println(a.getGmtModified());

	}*/
}
