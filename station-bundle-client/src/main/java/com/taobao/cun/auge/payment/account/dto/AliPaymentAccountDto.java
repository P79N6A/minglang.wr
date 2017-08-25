package com.taobao.cun.auge.payment.account.dto;

import java.io.Serializable;

public class AliPaymentAccountDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 全名
	 */
	private String fullName;
	/**
	 * 证件号码
	 */
	private String idCardNumber;
	/**
	 * 支付宝账号
	 */
	private String accountNo;
	/**
	 * 支付宝登陆Id
	 */
	private String alipayId;
	/**
	 * 淘宝userId
	 * @return
	 */
	private Long taobaoUserId;
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getIdCardNumber() {
		return idCardNumber;
	}
	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAlipayId() {
		return alipayId;
	}
	public void setAlipayId(String alipayId) {
		this.alipayId = alipayId;
	}
	public Long getTaobaoUserId() {
		return taobaoUserId;
	}
	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}
	
	
}
