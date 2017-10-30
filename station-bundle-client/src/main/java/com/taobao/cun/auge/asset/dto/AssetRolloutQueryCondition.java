package com.taobao.cun.auge.asset.dto;

import com.taobao.cun.auge.common.PageQuery;



/**
 * 出库单查询条件Dto
 * @author quanzhu.wangqz
 *
 */
public class AssetRolloutQueryCondition extends PageQuery {

	private static final long serialVersionUID = -4929497832157431915L;
	/**
	 * 责任人工号
	 */
	private String workNo;
	
	public String getWorkNo() {
		return workNo;
	}
	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}
	
}
