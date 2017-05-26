package com.taobao.cun.auge.asset.dto;

import java.util.List;

import com.taobao.cun.auge.asset.enums.AssetRolloutReceiverAreaTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutTypeEnum;
import com.taobao.cun.auge.common.OperatorDto;

public class AssetRolloutDto extends OperatorDto{

	private static final long serialVersionUID = -4939163358121761029L;

	 /**
     * 出库单id
     */
    private Long id;

    /**
     * 提交人姓名
     */
    private String applierName;

    /**
     * 提交人工号
     */
    private String applierWorkno;

    /**
     * 提交人所在组织
     */
    private String applierOrgName;

    /**
     * 提交人所在组织id
     */
    private Long applierOrgId;

    /**
     * 接收人姓名
     */
    private String receiverName;

    /**
     *接收人id:工号，淘宝userId
     */
    private String receiverId;

    /**
     * 接收人区域类型
     */
    private AssetRolloutReceiverAreaTypeEnum receiverAreaType;

    /**
     *接收人区域名称
     */
    private String receiverAreaName;

    /**
     * 接收人区域id
     */
    private Long receiverAreaId;

    /**
     *申请原因
     */
    private String reason;

    /**
     * 物流费用，单位元
     */
    private String logisticsCost;

    /**
     * 物流距离，单位千米
     */
    private Long logisticsDistance;

    /**
     *出库类型
     */
    private AssetRolloutTypeEnum type;

    /**
     * 出库状态
     */
     
    private AssetRolloutStatusEnum status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否免赔
     */
    private String isDeductible;

    /**
     * 赔付总额
     */
    private String totalPayment;
    
    /**
     * 资产总数
     */
    private List<AssetCategoryCountDto>  countList;
    
    /**
     *待入库资产总数
     */
	private List<AssetCategoryCountDto> waitSignCountList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApplierName() {
		return applierName;
	}

	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}

	public String getApplierWorkno() {
		return applierWorkno;
	}

	public void setApplierWorkno(String applierWorkno) {
		this.applierWorkno = applierWorkno;
	}

	public String getApplierOrgName() {
		return applierOrgName;
	}

	public void setApplierOrgName(String applierOrgName) {
		this.applierOrgName = applierOrgName;
	}

	public Long getApplierOrgId() {
		return applierOrgId;
	}

	public void setApplierOrgId(Long applierOrgId) {
		this.applierOrgId = applierOrgId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public AssetRolloutReceiverAreaTypeEnum getReceiverAreaType() {
		return receiverAreaType;
	}

	public void setReceiverAreaType(
			AssetRolloutReceiverAreaTypeEnum receiverAreaType) {
		this.receiverAreaType = receiverAreaType;
	}

	public String getReceiverAreaName() {
		return receiverAreaName;
	}

	public void setReceiverAreaName(String receiverAreaName) {
		this.receiverAreaName = receiverAreaName;
	}

	public Long getReceiverAreaId() {
		return receiverAreaId;
	}

	public void setReceiverAreaId(Long receiverAreaId) {
		this.receiverAreaId = receiverAreaId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getLogisticsCost() {
		return logisticsCost;
	}

	public void setLogisticsCost(String logisticsCost) {
		this.logisticsCost = logisticsCost;
	}

	public Long getLogisticsDistance() {
		return logisticsDistance;
	}

	public void setLogisticsDistance(Long logisticsDistance) {
		this.logisticsDistance = logisticsDistance;
	}

	public AssetRolloutTypeEnum getType() {
		return type;
	}

	public void setType(AssetRolloutTypeEnum type) {
		this.type = type;
	}

	public AssetRolloutStatusEnum getStatus() {
		return status;
	}

	public void setStatus(AssetRolloutStatusEnum status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsDeductible() {
		return isDeductible;
	}

	public void setIsDeductible(String isDeductible) {
		this.isDeductible = isDeductible;
	}

	public String getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(String totalPayment) {
		this.totalPayment = totalPayment;
	}

	public List<AssetCategoryCountDto> getCountList() {
		return countList;
	}

	public void setCountList(List<AssetCategoryCountDto> countList) {
		this.countList = countList;
	}

	public List<AssetCategoryCountDto> getWaitSignCountList() {
		return waitSignCountList;
	}

	public void setWaitSignCountList(List<AssetCategoryCountDto> waitSignCountList) {
		this.waitSignCountList = waitSignCountList;
	}
}
