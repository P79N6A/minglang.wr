package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;

public class PeixunPurchaseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private Date gmtCreate;

	private String gmtCreateDesc;
	
	private Date gmtModified;

	private String modifier;

	private String creator;

	private String isDeleted;

	private String purchaseType;

	private Long applyOrgId;

	private String masterWorkNo;

	private Date gmtExceptOpen;
	
	private String gmtExceptOpenDesc;

	private String receiverWorkNo;

	private String receiverMobile;
	
	private String receiverWorkName;

	private Integer exceptNum;

	private String isShare;

	private String shareDesc;

	private String description;

	private String ouCode;

	private String categoryId;

	private String billMethod;

	private String receiveAddress;

	private String status;
	private String auditWorkNo;

	private Date gmtAudit;

	private String auditDesc;
	
	private String operator;
	
	private String orgName;

	private String loginId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	public Long getApplyOrgId() {
		return applyOrgId;
	}

	public void setApplyOrgId(Long applyOrgId) {
		this.applyOrgId = applyOrgId;
	}

	public String getMasterWorkNo() {
		return masterWorkNo;
	}

	public void setMasterWorkNo(String masterWorkNo) {
		this.masterWorkNo = masterWorkNo;
	}

	public Date getGmtExceptOpen() {
		return gmtExceptOpen;
	}

	public void setGmtExceptOpen(Date gmtExceptOpen) {
		this.gmtExceptOpen = gmtExceptOpen;
	}

	public String getReceiverWorkNo() {
		return receiverWorkNo;
	}

	public void setReceiverWorkNo(String receiverWorkNo) {
		this.receiverWorkNo = receiverWorkNo;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public Integer getExceptNum() {
		return exceptNum;
	}

	public void setExceptNum(Integer exceptNum) {
		this.exceptNum = exceptNum;
	}

	public String getIsShare() {
		return isShare;
	}

	public void setIsShare(String isShare) {
		this.isShare = isShare;
	}

	public String getShareDesc() {
		return shareDesc;
	}

	public void setShareDesc(String shareDesc) {
		this.shareDesc = shareDesc;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOuCode() {
		return ouCode;
	}

	public void setOuCode(String ouCode) {
		this.ouCode = ouCode;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getBillMethod() {
		return billMethod;
	}

	public void setBillMethod(String billMethod) {
		this.billMethod = billMethod;
	}

	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAuditWorkNo() {
		return auditWorkNo;
	}

	public void setAuditWorkNo(String auditWorkNo) {
		this.auditWorkNo = auditWorkNo;
	}

	public Date getGmtAudit() {
		return gmtAudit;
	}

	public void setGmtAudit(Date gmtAudit) {
		this.gmtAudit = gmtAudit;
	}

	public String getAuditDesc() {
		return auditDesc;
	}

	public void setAuditDesc(String auditDesc) {
		this.auditDesc = auditDesc;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getGmtCreateDesc() {
		return gmtCreateDesc;
	}

	public void setGmtCreateDesc(String gmtCreateDesc) {
		this.gmtCreateDesc = gmtCreateDesc;
	}

	public String getGmtExceptOpenDesc() {
		return gmtExceptOpenDesc;
	}

	public void setGmtExceptOpenDesc(String gmtExceptOpenDesc) {
		this.gmtExceptOpenDesc = gmtExceptOpenDesc;
	}

	public String getReceiverWorkName() {
		return receiverWorkName;
	}

	public void setReceiverWorkName(String receiverWorkName) {
		this.receiverWorkName = receiverWorkName;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	
}
