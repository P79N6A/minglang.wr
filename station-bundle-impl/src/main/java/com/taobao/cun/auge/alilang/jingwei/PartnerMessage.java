package com.taobao.cun.auge.alilang.jingwei;

public class PartnerMessage {
	private long taobaoUserId;
	private String name;
	private String mobile;
	private String alilangUserId;
	private String action;
	
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
