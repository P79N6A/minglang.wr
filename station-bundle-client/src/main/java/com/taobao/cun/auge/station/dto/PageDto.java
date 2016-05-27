package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.List;

public class PageDto<T> implements Serializable {

	private static final long serialVersionUID = 3393206077213932523L;

	List<T> items;
	// 当前页
	private int pageNum;
	// 每页的数量
	private int pageSize;
	// 当前页的数量
	private int curPagesize;
	// 总记录数
	private long total;
	// 总页数
	private int pages;
	// 第一页
	private int firstPage;
	// 前一页
	private int prePage;
	// 下一页
	private int nextPage;
	// 最后一页
	private int lastPage;

	/**
	 * @return the items
	 */
	public List<T> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<T> items) {
		this.items = items;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getCurPagesize() {
		return curPagesize;
	}

	public void setCurPagesize(int curPagesize) {
		this.curPagesize = curPagesize;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(int firstPage) {
		this.firstPage = firstPage;
	}

	public int getPrePage() {
		return prePage;
	}

	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

}
