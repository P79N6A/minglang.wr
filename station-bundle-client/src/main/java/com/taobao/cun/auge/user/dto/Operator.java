package com.taobao.cun.auge.user.dto;

public class Operator {
	private String opWorkId;
	
	private String opName;
	
	private Long orgId;
	
	private String roleName;

	public String getOpWorkId() {
		return opWorkId;
	}

	public void setOpWorkId(String opWorkId) {
		this.opWorkId = opWorkId;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
