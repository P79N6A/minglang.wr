package com.taobao.cun.auge.dal.domain;

import java.io.Serializable;
import java.util.Date;

public class CuntaoAssetFlowDetailForExcel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4987000110585342493L;
	private Long applyId;
	private Integer applyNum;
	private String name;
	private String type;
	private String sku;
	/**
	 * APPLY_ORG
	 */
	private String applyOrg;
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
	private Date planReceiveTime;
	/**
	 * APPLY_STATUS
	 */
	private String applyStatus;
	/**
	 * PURCHASE_STATUS
	 */
	private String purchaseStatus;
	/**
	 * RECEIVE_ADDRESS
	 */
	private String receiveAddress;
	/**
	 * REMARK
	 */
	private String remark;
	/**
	 * ASSET_SITUATION
	 */
	private String assetSituation;
	/**
	 * APPLY_TIME
	 */
	private Date applyTime;
	
	/**
	 * ASSET_OWNER_NO
	 */
	private String assetOwnerNo;
	
	
	/**
	 * 期望到货时间 开始时间
	 */
	private Date planReceiveTimeBegin;
	/**
	 * 期望到货时间 结束时间
	 */
	private Date planReceiveTimeEnd;
	
	
	private Long[] applyIdArray;
	
	private String[] orgIdArray;
	
	private String fullIdPath;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getApplyOrg() {
		return applyOrg;
	}

	public void setApplyOrg(String applyOrg) {
		this.applyOrg = applyOrg;
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

	public Date getPlanReceiveTime() {
		return planReceiveTime;
	}

	public void setPlanReceiveTime(Date planReceiveTime) {
		this.planReceiveTime = planReceiveTime;
	}

	public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}

	public String getPurchaseStatus() {
		return purchaseStatus;
	}

	public void setPurchaseStatus(String purchaseStatus) {
		this.purchaseStatus = purchaseStatus;
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

	public String getAssetSituation() {
		return assetSituation;
	}

	public void setAssetSituation(String assetSituation) {
		this.assetSituation = assetSituation;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getAssetOwnerNo() {
		return assetOwnerNo;
	}

	public void setAssetOwnerNo(String assetOwnerNo) {
		this.assetOwnerNo = assetOwnerNo;
	}

	public Date getPlanReceiveTimeBegin() {
		return planReceiveTimeBegin;
	}

	public void setPlanReceiveTimeBegin(Date planReceiveTimeBegin) {
		this.planReceiveTimeBegin = planReceiveTimeBegin;
	}

	public Date getPlanReceiveTimeEnd() {
		return planReceiveTimeEnd;
	}

	public void setPlanReceiveTimeEnd(Date planReceiveTimeEnd) {
		this.planReceiveTimeEnd = planReceiveTimeEnd;
	}

	public Long[] getApplyIdArray() {
		return applyIdArray;
	}

	public void setApplyIdArray(Long[] applyIdArray) {
		this.applyIdArray = applyIdArray;
	}

	public String[] getOrgIdArray() {
		return orgIdArray;
	}

	public void setOrgIdArray(String[] orgIdArray) {
		this.orgIdArray = orgIdArray;
	}

	public String getFullIdPath() {
		return fullIdPath;
	}

	public void setFullIdPath(String fullIdPath) {
		this.fullIdPath = fullIdPath;
	}
	
}
