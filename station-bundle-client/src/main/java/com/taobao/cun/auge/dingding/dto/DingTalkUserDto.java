package com.taobao.cun.auge.dingding.dto;

import java.io.Serializable;

public class DingTalkUserDto implements Serializable{

	private static final long serialVersionUID = -3730641193520998836L;

	/**
     * 用户id
     */
    private String userId;

    /**
     * 用户在钉钉中对应的id
     */
    private String dingUserId;

    /**
     * 用户类型小二还是合伙人
     */
    private String type;


    /**
     * 用户钉钉是否激活
     */
    private String active;

    /**
     * 在钉钉中的组织ID，多个ID之间用逗号分隔
     */
    private String deptIdList;

    /**
     * 钉钉手机号
     */
    private String phone;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDingUserId() {
		return dingUserId;
	}

	public void setDingUserId(String dingUserId) {
		this.dingUserId = dingUserId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getDeptIdList() {
		return deptIdList;
	}

	public void setDeptIdList(String deptIdList) {
		this.deptIdList = deptIdList;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
    
    
}
