package com.taobao.cun.auge.permission.operation;

import java.io.Serializable;

public class Operation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4547203727501519563L;

	/**
	 * 操作名称
	 */
	private String name;
	
	/**
	 * 操作的值
	 */
	private String value;
	
	/**
	 * 操作的分类
	 */
	private String type;
	
	/**
	 * 操作的acl权限
	 */
	private String permission;
	
	/**
	 * 操作的condition
	 */
	private String condition;
	
	/**
	 * 操作的code
	 */
	private String code;
	
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

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
