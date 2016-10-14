package com.taobao.cun.auge.alilang.jingwei;

import com.alibaba.fastjson.annotation.JSONField;

public class PartnerMessage {
	private long taobaoUserId;
	private String name;
	private String mobile;
	private String alilangUserId;
	private String email;
	private String action;
	@JSONField(name="orgId")
	private long alilangOrgId;
	
	public long getAlilangOrgId() {
		return alilangOrgId;
	}
	public void setAlilangOrgId(long alilangOrgId) {
		this.alilangOrgId = alilangOrgId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getTaobaoUserId() {
		return taobaoUserId;
	}
	public void setTaobaoUserId(long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAlilangUserId() {
		return alilangUserId;
	}
	public void setAlilangUserId(String alilangUserId) {
		this.alilangUserId = alilangUserId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	
}
