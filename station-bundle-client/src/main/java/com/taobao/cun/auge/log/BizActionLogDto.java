package com.taobao.cun.auge.log;

import java.io.Serializable;
import java.util.Date;

public class BizActionLogDto implements Serializable{
	private static final long serialVersionUID = -3867559952145996358L;

	private Long objectId;
	
	private String objectType;
	
	private BizActionEnum bizActionEnum;
	
	private String roleName;
	
	private String opWorkId;
	
	private Long opOrgId;
	
	private String value1;
	
	private String value2;
	
	private Date gmtCreate;
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}


	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public BizActionEnum getBizActionEnum() {
		return bizActionEnum;
	}

	public void setBizActionEnum(BizActionEnum bizActionEnum) {
		this.bizActionEnum = bizActionEnum;
	}

	public String getOpWorkId() {
		return opWorkId;
	}

	public void setOpWorkId(String opWorkId) {
		this.opWorkId = opWorkId;
	}

	public Long getOpOrgId() {
		return opOrgId;
	}

	public void setOpOrgId(Long opOrgId) {
		this.opOrgId = opOrgId;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}
}
