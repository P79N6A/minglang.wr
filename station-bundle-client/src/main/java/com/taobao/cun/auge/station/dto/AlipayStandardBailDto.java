package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

/**
 * 支付宝标准保证金Dto
 *
 */
public class AlipayStandardBailDto implements Serializable {
	
	public static final String ALIPAY_OP_TYPE_FREEZE = "0001";//冻结
	public static final String ALIPAY_OP_TYPE_UNFREEZE = "0002";//解冻
	public static final String ALIPAY_OP_TYPE_TRANSFER = "0003";//转移

	private static final long serialVersionUID = 1L;
	
	private String amount;//冻结金额
	
	private String opType;//操作类型
	
	private String outOrderNo;//外部订单号
	
	private String transferMemo;//转移备注
	
	private String userEmail;//冻结账号
	
	private String typeCode;//业务代理编号
	
	private String transInAccount;//转移的账号,这个账号是以2088开头的不是用户的支付宝账号
	
	private String userAccount;//用户支付宝的账号,以2088开头,这个是未来支付宝的统一用的趋势,目前先用userEmail

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public String getOutOrderNo() {
		return outOrderNo;
	}

	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}

	public String getTransferMemo() {
		return transferMemo;
	}

	public void setTransferMemo(String transferMemo) {
		this.transferMemo = transferMemo;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTransInAccount() {
		return transInAccount;
	}

	public void setTransInAccount(String transInAccount) {
		this.transInAccount = transInAccount;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

}

