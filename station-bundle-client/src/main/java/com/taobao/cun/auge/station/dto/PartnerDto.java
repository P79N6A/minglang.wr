package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class PartnerDto implements Serializable{

	private static final long serialVersionUID = -6662885547380230732L;
	
	
	/**
	 *  合伙人id
	 */
	private Long id;
	  /**
     * 姓名
     */
	@NotNull
    private String name;

    /**
     * 支付宝账号
     */
    private String alipayAccount;

    /**
     * 淘宝user_id
     */
    private Long taobaoUserId;

    /**
     * 淘宝nick
     */
    private String taobaoNick;

    /**
     * 身份证号码
     */
    private String idenNum;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * email
     */
    private String email;

    /**
     * 经营类型全职： fulltime 兼职：partime
     */
    private String businessType;

    /**
     * 现状描述
     */
    private String description;

    /**
     * 状态：temp:暂存，normal:正常
     */
    private String state;

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
