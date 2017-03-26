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
	
	private Integer settleIdentity;
	
	private Integer qualiStatus;
	
	private String partnerInstanceStatus;
	
	private boolean newPartner;

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

	public Integer getSettleIdentity() {
		return settleIdentity;
	}

	public void setSettleIdentity(Integer settleIdentity) {
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

	public boolean isNewPartner() {
		return newPartner;
	}

	public void setNewPartner(boolean newPartner) {
		this.newPartner = newPartner;
	}

	public Integer getQualiStatus() {
		return qualiStatus;
	}

	public void setQualiStatus(Integer qualiStatus) {
		this.qualiStatus = qualiStatus;
	}
	
}
