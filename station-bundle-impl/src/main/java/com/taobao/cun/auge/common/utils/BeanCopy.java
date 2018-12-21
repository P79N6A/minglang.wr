package com.taobao.cun.auge.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.google.common.base.Throwables;

/**
 * bean属性拷贝工具类
 * 
 * @author chengyu.zhoucy
 *
 */
public class BeanCopy {
	static{
		ConvertUtils.register(new Converter() {
			
			@Override
			@SuppressWarnings("unchecked")
			public <T> T convert(Class<T> type, Object value) {
				if(value == null){
					return null;
				}
				
				if(value instanceof String){
					try {
						return (T) (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String) value));
					} catch (ParseException e) {
						try {
							return (T) (new SimpleDateFormat("yyyy-MM-dd").parse((String) value));
						} catch (ParseException e1) {
							return null;
						}
					}
				}
				
				if(value instanceof Date){
					return (T) new Date(((Date) value).getTime());
				}
				
				if(value instanceof Long){
					return (T) new java.util.Date((Long)value);
				}
				return null;
			}

		}, Date.class);
	}
	/**
	 * 创建一个Bean，并且从源拷贝到新生产的Bean中
	 * @param clazz
	 * @param source
	 * @return
	 */
	public static <T> T copy(Class<T> clazz, Object source){
		if(source == null){
			return null;
		}
		try {
			T t = clazz.newInstance();
			BeanUtilsBean.getInstance().copyProperties(t, source);
			return t;
		} catch (Exception e) {
			Throwables.propagateIfPossible(e);
		}
		
		return null;
	}
	
	public static <T> List<T> copyList(Class<T> clazz, List<?> sources){
		List<T> results = new ArrayList<T>();
		if(sources != null){
			for(Object source : sources){
				results.add(copy(clazz, source));
			}
		}
		return results;
	}
}
