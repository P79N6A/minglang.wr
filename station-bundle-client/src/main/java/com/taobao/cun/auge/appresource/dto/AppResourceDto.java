package com.taobao.cun.auge.appresource.dto;

import java.io.Serializable;
import java.util.Date;

public class AppResourceDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private Date gmtCreate;

	private Date gmtModified;

	private String creator;

	private String modifier;

	private String isDeleted;

	private String name;

	private String value;

	private String type;

	private String dataCondition;

	private String permissionNames;

	private String code;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDataCondition() {
		return dataCondition;
	}

	public void setDataCondition(String dataCondition) {
		this.dataCondition = dataCondition;
	}

	public String getPermissionNames() {
		return permissionNames;
	}

	public void setPermissionNames(String permissionNames) {
		this.permissionNames = permissionNames;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
