package com.taobao.cun.auge.station.condition;

import java.util.Date;

import com.taobao.cun.auge.common.PageQuery;

public class PeixunPuchaseQueryCondition extends PageQuery{

	private static final long serialVersionUID = 1L;
	private Long orgId;
	private String status;
	private Date gmtExceptStart;
	private Date gmtExceptEnd;
	private String receiveWorkNo;
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getGmtExceptStart() {
		return gmtExceptStart;
	}
	public void setGmtExceptStart(Date gmtExceptStart) {
		this.gmtExceptStart = gmtExceptStart;
	}
	public Date getGmtExceptEnd() {
		return gmtExceptEnd;
	}
	public void setGmtExceptEnd(Date gmtExceptEnd) {
		this.gmtExceptEnd = gmtExceptEnd;
	}
	public String getReceiveWorkNo() {
		return receiveWorkNo;
	}
	public void setReceiveWorkNo(String receiveWorkNo) {
		this.receiveWorkNo = receiveWorkNo;
	}
	
	
}
