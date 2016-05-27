package com.taobao.cun.auge.common;

import java.io.Serializable;

public class PageQuery implements Serializable {
	
	private static final long serialVersionUID = 6995476690515605998L;

	private int pageNum;
	
	private int pageSize;

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

}
