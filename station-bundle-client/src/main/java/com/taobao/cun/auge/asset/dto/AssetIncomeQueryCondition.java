package com.taobao.cun.auge.asset.dto;

import com.taobao.cun.auge.common.PageQuery;



/**
 * 入库单查询条件Dto
 * @author quanzhu.wangqz
 *
 */
public class AssetIncomeQueryCondition extends PageQuery {

	private static final long serialVersionUID = -4929497832157431915L;
	/**
	 * 接收人工号
	 */
	private String workNo;
	/**
	 * 入库状态
	 */
	private String status;
	/**
	 * 入库类型
	 */
	private String type;
	
	
	public String getWorkNo() {
		return workNo;
	}
	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
