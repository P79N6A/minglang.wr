package com.taobao.cun.auge.common.utils;

import java.util.Collections;
import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.taobao.cun.auge.client.page.DefaultPageResult;

public final class DefaultPageResultUtil {

	private DefaultPageResultUtil() {

	}

	public static <T, C> DefaultPageResult<C> success(Page<T> page, List<C> items) {
		PageInfo<T> pageInfo = new PageInfo<T>(page);

		DefaultPageResult<C> result = new DefaultPageResult<C>();

		result.setPageNum(pageInfo.getPageNum());
		result.setPageSize(pageInfo.getPageSize());
		result.setTotal(pageInfo.getTotal());
		result.setCurPagesize(pageInfo.getSize());
		result.setPages(pageInfo.getPages());
		result.setFirstPage(pageInfo.getFirstPage());
		result.setLastPage(pageInfo.getLastPage());
		result.setNextPage(pageInfo.getNextPage());
		result.setPrePage(pageInfo.getPrePage());
		result.setItems(items);

		return result;
	}
	
	public static <T> DefaultPageResult<T> unSuccess(int pageNum,int pageSize){
		DefaultPageResult<T> result = new DefaultPageResult<T>();

		result.setPageNum(pageNum);
		result.setPageSize(pageSize);
		result.setTotal(0);
		result.setCurPagesize(0);
		result.setPages(0);
		result.setFirstPage(0);
		result.setLastPage(0);
		result.setNextPage(0);
		result.setPrePage(0);
		result.setItems(Collections.<T>emptyList());

		return result;
	}
}
