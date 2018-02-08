package com.taobao.cun.auge.asset.service;

import java.io.Serializable;

public class CuntaoAssetFlowDetailForExcelDto implements Serializable  {

	private static final long serialVersionUID = 4860650030589625192L;
	
	private Long applyId;
	private Integer applyNum;
	private String name;
	private String typeDesc;
	private String sku;

	/**
	 * APPLY_ORG_DESC
	 */
	private String applyOrgDesc;
	/**
	 * APPLIER
	 */
	private String applier;
	/**
	 * APPLIER_NO
	 */
	private String applierNo;
	/**
	 * RECEIVER
	 */
	private String receiver;
	/**
	 * MOBILE
	 */
	private String mobile;
	/**
	 * ASSET_OWNER
	 */
	private String assetOwner;
	/**
	 * PLAN_RECEIVE_TIME
	 */
	private String planReceiveTime;
	/**
	 * APPLY_STATUS
	 */
	private String applyStatusDesc;
	/**
	 * PURCHASE_STATUS
	 */
	private String purchaseStatusDesc;
	/**
	 * RECEIVE_ADDRESS
	 */
	private String receiveAddress;
	/**
	 * REMARK
	 */
	private String remark;

	/**
	 * APPLY_TIME
	 */
	private String applyTime;
	
	/**
	 * ASSET_OWNER_NO
	 */
	private String assetOwnerNo;

	public Long getApplyId() {
		return applyId;
	}

	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}

	public Integer getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(Integer applyNum) {
		this.applyNum = applyNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getApplyOrgDesc() {
		return applyOrgDesc;
	}

	public void setApplyOrgDesc(String applyOrgDesc) {
		this.applyOrgDesc = applyOrgDesc;
	}

	public String getApplier() {
		return applier;
	}

	public void setApplier(String applier) {
		this.applier = applier;
	}

	public String getApplierNo() {
		return applierNo;
	}

	public void setApplierNo(String applierNo) {
		this.applierNo = applierNo;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAssetOwner() {
		return assetOwner;
	}

	public void setAssetOwner(String assetOwner) {
		this.assetOwner = assetOwner;
	}

	public String getPlanReceiveTime() {
		return planReceiveTime;
	}

	public void setPlanReceiveTime(String planReceiveTime) {
		this.planReceiveTime = planReceiveTime;
	}

	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getAssetOwnerNo() {
		return assetOwnerNo;
	}

	public void setAssetOwnerNo(String assetOwnerNo) {
		this.assetOwnerNo = assetOwnerNo;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public String getApplyStatusDesc() {
		return applyStatusDesc;
	}

	public void setApplyStatusDesc(String applyStatusDesc) {
		this.applyStatusDesc = applyStatusDesc;
	}

	public String getPurchaseStatusDesc() {
		return purchaseStatusDesc;
	}

	public void setPurchaseStatusDesc(String purchaseStatusDesc) {
		this.purchaseStatusDesc = purchaseStatusDesc;
	}
	
	

}
