package com.taobao.cun.auge.asset.dto;

import com.taobao.cun.auge.common.PageQuery;

/**
 * 
 * @author quanzhu.wangqz
 *
 */
public class QueryStationTaskCondition  extends PageQuery {

	private static final long serialVersionUID = 5422593447655623370L;

	private Long countyOrgId;
	/**
	 * y:已盘，n:未盘
	 */
	private String isDone;
	public Long getCountyOrgId() {
		return countyOrgId;
	}
	public void setCountyOrgId(Long countyOrgId) {
		this.countyOrgId = countyOrgId;
	}
	public String getIsDone() {
		return isDone;
	}
	public void setIsDone(String isDone) {
		this.isDone = isDone;
	}
	
}
