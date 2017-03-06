package com.taobao.cun.auge.station.condition;

import java.util.List;

import com.taobao.cun.auge.common.PageQuery;

public class CuntaoQualificationPageCondition extends PageQuery{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8325075310656303204L;

	private List<Integer> statusList;

	public List<Integer> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<Integer> statusList) {
		this.statusList = statusList;
	}

	
	

}
