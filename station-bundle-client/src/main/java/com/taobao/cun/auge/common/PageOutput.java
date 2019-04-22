package com.taobao.cun.auge.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 分页查询返回结果,适合数据库/搜索引擎....需要分页的场合
 * @author chengyu.zhoucy
 *
 * @param <T>
 */
public class PageOutput<T>{

	/**
	 * 每页大小
	 */
	private long pageSize;
	
	/**
	 * 记录总数
	 */
	private long totalItems;
	
	/**
	 * 当前页
	 */
	private long page;
	
	/**
	 * 返回结果
	 */
	private List<T> result;
	/**
	 * 查询参数
	 */
	private Map<String, Object> params;
	
	private boolean hasMore = false;
	
	public PageOutput(){}
	
	public PageOutput(long page, long pageSize, long totalItems, List<T> result){
		this.page = page;
		this.pageSize = pageSize;
		this.totalItems = totalItems;
		this.result = result;
	}

    public boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public List<T> getResult() {
		return result == null ? new ArrayList<T>():result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
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
	
	public long getPrePage(){
		return page == 1 ? page : page - 1;
	}
	
	public long getNextPage(){
		return page == getPages() ? page : page + 1;
	}
	
	public long getPages(){
		if(getTotalItems() == 0){
			return 0;
		}
		
		return (long) (getTotalItems() % getPageSize() != 0 ? (getTotalItems() / getPageSize() +1) : (getTotalItems() / getPageSize()));
	}
}
