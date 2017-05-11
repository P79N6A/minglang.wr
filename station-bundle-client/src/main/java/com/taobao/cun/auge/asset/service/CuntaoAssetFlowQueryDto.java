package com.taobao.cun.auge.asset.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @author quanzhu.wangqz
 *
 */
public class CuntaoAssetFlowQueryDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7748516459774437561L;
	
	/**
	 * 申请组织
	 */
	private String applyOrg;
	/**
	 * 采购状态
	 */
	private AssetFlowPurchaseStatusEnum purchaseStatus;
	
	/**
	 * 审批状态
	 */
	private AssetFlowApplyStatusEnum applyStatus;
	/**
	 * 申请人
	 */
	private String applier;
	/**
	 * 	申请人工号
	 */
	private String applierNo;
	
	/**
	 * 期望到货时间 开始时间
	 */
	private Date planReceiveTimeBegin;
	/**
	 * 期望到货时间 结束时间
	 */
	private Date planReceiveTimeEnd;
	
	private List<Long> applyIdList;
	
	private String fullIdPath;
	
	private Integer pageSize;
	private Integer pageNum;
	
	/**
     * 查询条件
     */
    private AssetFlowOrderByEnum[] pageOrder;
	
	public String getFullIdPath() {
		return fullIdPath;
	}

	public void setFullIdPath(String fullIdPath) {
		this.fullIdPath = fullIdPath;
	}


	public String getApplyOrg() {
		return applyOrg;
	}

	public void setApplyOrg(String applyOrg) {
		this.applyOrg = applyOrg;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public AssetFlowOrderByEnum[] getPageOrder() {
		return pageOrder;
	}

	public void setPageOrder(AssetFlowOrderByEnum[] pageOrder) {
		this.pageOrder = pageOrder;
	}

	public AssetFlowPurchaseStatusEnum getPurchaseStatus() {
		return purchaseStatus;
	}

	public void setPurchaseStatus(AssetFlowPurchaseStatusEnum purchaseStatus) {
		this.purchaseStatus = purchaseStatus;
	}

	public List<Long> getApplyIdList() {
		return applyIdList;
	}

	public void setApplyIdList(List<Long> applyIdList) {
		this.applyIdList = applyIdList;
	}

	public AssetFlowApplyStatusEnum getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(AssetFlowApplyStatusEnum applyStatus) {
		this.applyStatus = applyStatus;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}


}
