package com.taobao.cun.auge.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;

/**
 * 添加domain默认参数工具类
 * 
 * @author quanzhu.wangqz
 *
 */
public class DomainUtils {

	public static <T> T beforeInsert(T obj, String operator) {
		try {
			Date now = new Date();
			Method m1 = obj.getClass().getDeclaredMethod("setIsDeleted", String.class);
			Method m2 = obj.getClass().getDeclaredMethod("setGmtCreate", Date.class);
			Method m3 = obj.getClass().getDeclaredMethod("setGmtModified", Date.class);
			Method m4 = obj.getClass().getDeclaredMethod("setCreator", String.class);
			Method m5 = obj.getClass().getDeclaredMethod("setModifier", String.class);

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
	
	public static <T> T beforeUpdate(T obj, String operator) {
		try {
			Date now = new Date();
			Method m3 = obj.getClass().getDeclaredMethod("setGmtModified", Date.class);
			Method m5 = obj.getClass().getDeclaredMethod("setModifier", String.class);

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
	
	public static <T> T beforeDelete(T obj, String operator) {
		try {
			Date now = new Date();
			Method m1 = obj.getClass().getDeclaredMethod("setIsDeleted", String.class);
			Method m3 = obj.getClass().getDeclaredMethod("setGmtModified", Date.class);
			Method m5 = obj.getClass().getDeclaredMethod("setModifier", String.class);
			
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
		PartnerLifecycleItems a = beforeInsert(new PartnerLifecycleItems(), "aaa");
		System.out.println(a.getIsDeleted());
		System.out.println(a.getModifier());
		System.out.println(a.getCreator());
		System.out.println(a.getGmtCreate());
		System.out.println(a.getGmtModified());

	}*/
}
