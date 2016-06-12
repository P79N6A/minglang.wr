package com.taobao.cun.auge.common;

import javax.validation.constraints.NotNull;

public class PageQuery extends OperatorDto {
	
	private static final long serialVersionUID = 6995476690515605998L;

	@NotNull(message="pageNum is null")
	private int pageNum;
	
	@NotNull(message="pageSize is null")
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
