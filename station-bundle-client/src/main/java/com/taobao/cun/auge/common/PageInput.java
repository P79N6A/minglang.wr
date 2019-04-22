package com.taobao.cun.auge.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页查询输入,适合数据库/搜索引擎....需要分页的场合
 * 
 * @author chengyu.zhoucy
 *
 */
public class PageInput {
	/**
	 * 每页大小
	 */
	private long pageSize;
	/**
	 * 当前页
	 */
	private long page;
	
	/**
	 * 查询参数
	 */
	private Map<String, Object> params = new HashMap<String ,Object>();
	
	public PageInput(){}
	
	public PageInput(long page, long pageSize){
		this.page = page;
		this.pageSize = pageSize;
	}
	
	public PageInput addParam(String key, Object value){
		params.put(key, value);
		return this;
	}
	
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	public long getPageSize() {
		return pageSize;
	}
	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}
	public long getPage() {
		return page;
	}
	public void setPage(long page) {
		this.page = page;
	}
	public long getStartItem() {
		return pageSize * (page - 1);
	}

	public long getEndItem() {
		return pageSize * page;
	}
}