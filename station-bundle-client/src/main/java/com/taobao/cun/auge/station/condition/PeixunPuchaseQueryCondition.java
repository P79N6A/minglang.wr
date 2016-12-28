package com.taobao.cun.auge.station.condition;

import com.taobao.cun.auge.common.PageQuery;

public class PeixunPuchaseQueryCondition extends PageQuery{

	private static final long serialVersionUID = 1L;
	private Long orgId;
	private String status;
	private String gmtExceptStart;
	private String gmtExceptEnd;
	private String receiveWorkNo;
	private String orgPath;
	private String applyWorkNo;
	private String applyName;
	
	public String getApplyName() {
		return applyName;
	}
	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}
	public String getApplyWorkNo() {
		return applyWorkNo;
	}
	public void setApplyWorkNo(String applyWorkNo) {
		this.applyWorkNo = applyWorkNo;
	}
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
	
	public String getGmtExceptStart() {
		return gmtExceptStart;
	}
	public void setGmtExceptStart(String gmtExceptStart) {
		this.gmtExceptStart = gmtExceptStart;
	}
	public String getGmtExceptEnd() {
		return gmtExceptEnd;
	}
	public void setGmtExceptEnd(String gmtExceptEnd) {
		this.gmtExceptEnd = gmtExceptEnd;
	}
	public String getReceiveWorkNo() {
		return receiveWorkNo;
	}
	public void setReceiveWorkNo(String receiveWorkNo) {
		this.receiveWorkNo = receiveWorkNo;
	}
	public String getOrgPath() {
		return orgPath;
	}
	public void setOrgPath(String orgPath) {
		this.orgPath = orgPath;
	}
	
	
}
