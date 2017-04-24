package com.taobao.cun.auge.common.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.taobao.cun.auge.common.PageDto;

public class PageDtoHelper {

	public static <S,T> PageDto<T> of(Page<S> sourceList,Converter<S, T> conventer){
		List<T> targetList = sourceList.stream().map(source -> conventer.convert(source)).collect(Collectors.toList());
		PageInfo<S> pageInfo = new PageInfo<S>(sourceList);
		PageDto<T> result = new PageDto<T>();
		result.setPageNum(pageInfo.getPageNum());
		result.setPageSize(pageInfo.getPageSize());
		result.setTotal(pageInfo.getTotal());
		result.setCurPagesize(pageInfo.getSize());
		result.setPages(pageInfo.getPages());
		result.setFirstPage(pageInfo.getFirstPage());
		result.setLastPage(pageInfo.getLastPage());
		result.setNextPage(pageInfo.getNextPage());
		result.setPrePage(pageInfo.getPrePage());
		result.setItems(targetList);
		return result;
	}
	
	
	public static <S> PageDto<S> of(Page<S> sourceList){
		PageInfo<S> pageInfo = new PageInfo<S>(sourceList);
		PageDto<S> result = new PageDto<S>();
		result.setPageNum(pageInfo.getPageNum());
		result.setPageSize(pageInfo.getPageSize());
		result.setTotal(pageInfo.getTotal());
		result.setCurPagesize(pageInfo.getSize());
		result.setPages(pageInfo.getPages());
		result.setFirstPage(pageInfo.getFirstPage());
		result.setLastPage(pageInfo.getLastPage());
		result.setNextPage(pageInfo.getNextPage());
		result.setPrePage(pageInfo.getPrePage());
		result.setItems(pageInfo.getList());
		return result;
	}
}
