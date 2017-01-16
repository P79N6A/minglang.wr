package com.taobao.cun.auge.station.dto;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 冻结保证金dto
 * @author quanzhu.wangqz
 *
 */
public class FreezeBondDto extends OperatorDto{

	private static final long serialVersionUID = -8878634406332198903L;
	/**
	 * 淘宝userId
	 */
	@NotNull(message="taobaoUserId not null")
	private Long taobaoUserId;
	
	/**
	 * 支付宝账号（支付宝内部id）
	 */
	private String accountNo;
	
	/**
	 * 支付宝登录账号
	 */
	private String alipayAccount;
	
	public Long getTaobaoUserId() {
		return taobaoUserId;
	}
	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAlipayAccount() {
		return alipayAccount;
	}
	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}
}
