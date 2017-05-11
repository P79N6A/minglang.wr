package com.taobao.cun.auge.asset.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author quanzhu.wangqz
 *
 */
public class CuntaoAssetFlowDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6415264652360002619L;

	/**流程id
	 * 
	 */
	private Long assetFlowId;
	
	/**
	 * 申请组织
	 */
	private String applyOrg;
	/**
	 * 申请组织描述
	 */
	private String applyOrgDesc;
	/**
	 * 申请人
	 */
	private String applier;
	/**
	 * 	申请人工号
	 */
	private String applierNo;
	/**
	 * 收货人
	 */
	private String receiver;
	/**
	 * 收货人手机
	 */
	private String mobile;
	/**
	 * 资产责任人
	 */
	private String assetOwner;
	
	/**
	 * 资产责任人工号
	 */
	private String assetOwnerNo;
	/**
	 * 期望到货时间
	 */
	private Date planReceiveTime;
	/**
	 * 审批状态
	 */
	private AssetFlowApplyStatusEnum ApplyStatus;
	/**
	 * 采购状态
	 */
	private AssetFlowPurchaseStatusEnum PurchaseStatus;
	/**
	 * 收货地址
	 */
	private String receiveAddress;
	/**
	 * 申请原因
	 */
	private String remark;
	/**
	 * 资产概况
	 */
	private Map<String, List<CuntaoAssetSituationDto>> assetSituation;
	
	/**
	 * 资产详情
	 */
	private List<CuntaoAssetFlowDetailDto> assetFlowDetailList;
	
	/**
	 * 申请时间
	 */
	private Date applyTime;
	// 是否提交pr
	private boolean isApplyPr = false;
	
	public Long getAssetFlowId() {
		return assetFlowId;
	}
	public void setAssetFlowId(Long assetFlowId) {
		this.assetFlowId = assetFlowId;
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
	public List<CuntaoAssetFlowDetailDto> getAssetFlowDetailList() {
		return assetFlowDetailList;
	}
	public void setAssetFlowDetailList(
			List<CuntaoAssetFlowDetailDto> assetFlowDetailList) {
		this.assetFlowDetailList = assetFlowDetailList;
	}
	public Date getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	public AssetFlowApplyStatusEnum getApplyStatus() {
		return ApplyStatus;
	}
	public void setApplyStatus(AssetFlowApplyStatusEnum applyStatus) {
		ApplyStatus = applyStatus;
	}
	public AssetFlowPurchaseStatusEnum getPurchaseStatus() {
		return PurchaseStatus;
	}
	public void setPurchaseStatus(AssetFlowPurchaseStatusEnum purchaseStatus) {
		PurchaseStatus = purchaseStatus;
	}
	public String getAssetOwnerNo() {
		return assetOwnerNo;
	}
	public void setAssetOwnerNo(String assetOwnerNo) {
		this.assetOwnerNo = assetOwnerNo;
	}
	public Map<String, List<CuntaoAssetSituationDto>> getAssetSituation() {
		return assetSituation;
	}
	public void setAssetSituation(
			Map<String, List<CuntaoAssetSituationDto>> assetSituation) {
		this.assetSituation = assetSituation;
	}
	public boolean isApplyPr() {
		return isApplyPr;
	}
	public void setApplyPr(boolean isApplyPr) {
		this.isApplyPr = isApplyPr;
	}
	
}
