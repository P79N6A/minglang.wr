package com.taobao.cun.auge.common.utils;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * 
 * @author quanzhu.wangqz
 *
 */
public class ResultUtils {
	
	/**
	 * 获得单个数据
	 * @param list
	 * @return
	 */
	public static <T> T selectOne(List<T> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}
	
	/**
	 * 获得总数
	 * @param list
	 * @return
	 */
	public static <T> int selectCount(List<T> list) {
		if (CollectionUtils.isEmpty(list)) {
			return 0;
		}
		return list.size();
	}
}
