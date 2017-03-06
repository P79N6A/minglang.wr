package com.taobao.cun.auge.qualification.service;

import java.io.Serializable;
import java.util.Date;

public class C2BSettleInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -932773959034461996L;
	
	
	private Date qualiAuditPassTime;
	
	private Date invalidTime;
	
	private Date signC2BTime;
	
	private int settleIdentity;
	
	private String partnerInstanceStatus;


	public Date getInvalidTime() {
		return invalidTime;
	}

	public void setInvalidTime(Date invalidTime) {
		this.invalidTime = invalidTime;
	}

	public Date getSignC2BTime() {
		return signC2BTime;
	}

	public void setSignC2BTime(Date signC2BTime) {
		this.signC2BTime = signC2BTime;
	}

	public int getSettleIdentity() {
		return settleIdentity;
	}

	public void setSettleIdentity(int settleIdentity) {
		this.settleIdentity = settleIdentity;
	}

	public String getPartnerInstanceStatus() {
		return partnerInstanceStatus;
	}

	public void setPartnerInstanceStatus(String partnerInstanceStatus) {
		this.partnerInstanceStatus = partnerInstanceStatus;
	}

	public Date getQualiAuditPassTime() {
		return qualiAuditPassTime;
	}

	public void setQualiAuditPassTime(Date qualiAuditPassTime) {
		this.qualiAuditPassTime = qualiAuditPassTime;
	}
	
}
