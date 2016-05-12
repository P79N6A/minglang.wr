package com.taobao.cun.auge.station.condition;

import java.io.Serializable;
import java.util.List;

public class PartnerCondition implements Serializable{
	
	private static final long serialVersionUID = -1827901793552358255L;

	// 合伙人id
	private Long id;

	// 淘宝user id
	private Long taobaoUserId;

	// 淘宝nick
	private String taobaoNick;

	// 姓名
	private String name;

	// 支付宝账号
	private String alipayAccount;

	// 身份证号码
	private String idenNum;

	// 手机号码
	private String mobile;

	// 邮箱
	private String email;

	// 经营类型
	private String businessType;

	// 现状描述
	private String description;

	// 身份证照片附件
	private List<AttachementCondition> attachements;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getIdenNum() {
		return idenNum;
	}

	public void setIdenNum(String idenNum) {
		this.idenNum = idenNum;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<AttachementCondition> getAttachements() {
		return attachements;
	}

	public void setAttachements(List<AttachementCondition> attachements) {
		this.attachements = attachements;
	}	
}
