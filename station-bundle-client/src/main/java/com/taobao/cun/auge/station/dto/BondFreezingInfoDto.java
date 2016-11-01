package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;

public class BondFreezingInfoDto implements Serializable{
	
	private static final long serialVersionUID = -2492469494359797993L;
	
	// 是否已经冻结
	private boolean hasFrozen;
	// 协议
	private Date protocolConfirmTime;
	// 合伙人实例
	private PartnerInstanceDto partnerInstance;
	// 保证金
	private AccountMoneyDto acountMoney;

	public boolean getHasFrozen() {
		return hasFrozen;
	}

	public void setHasFrozen(boolean hasFrozen) {
		this.hasFrozen = hasFrozen;
	}

	public Date getProtocolConfirmTime() {
		return protocolConfirmTime;
	}

	public void setProtocolConfirmTime(Date protocolConfirmTime) {
		this.protocolConfirmTime = protocolConfirmTime;
	}

	public PartnerInstanceDto getPartnerInstance() {
		return partnerInstance;
	}

	public void setPartnerInstance(PartnerInstanceDto partnerInstance) {
		this.partnerInstance = partnerInstance;
	}

	public AccountMoneyDto getAcountMoney() {
		return acountMoney;
	}

	public void setAcountMoney(AccountMoneyDto acountMoney) {
		this.acountMoney = acountMoney;
	}

}
