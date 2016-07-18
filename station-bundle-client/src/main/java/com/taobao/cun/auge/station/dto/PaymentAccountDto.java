package com.taobao.cun.auge.station.dto;

public class PaymentAccountDto implements java.io.Serializable {

	private static final long serialVersionUID = -683234819820341334L;
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
	 * 
	 * @return
	 */
	private Long taobaoUserId;

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getAlipayId() {
		return alipayId;
	}

	public void setAlipayId(String alipayId) {
		this.alipayId = alipayId;
	}

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

	@Override
	public String toString() {
		return "PaymentAccountDto [fullName=" + fullName + ", idCardNumber=" + idCardNumber + ", accountNo=" + accountNo + ", alipayId="
				+ alipayId + "]";
	}

}
