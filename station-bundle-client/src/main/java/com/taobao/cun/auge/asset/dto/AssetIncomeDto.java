package com.taobao.cun.auge.asset.dto;

import java.util.List;

import com.taobao.cun.auge.asset.enums.AssetIncomeApplierAreaTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeSignTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeTypeEnum;
import com.taobao.cun.auge.common.OperatorDto;

public class AssetIncomeDto extends OperatorDto{

	private static final long serialVersionUID = -4939163358121761029L;

	/**
     * 
     * 入库单id
     */
    private Long id;

    /**
     *提交人姓名
     */
    private String applierName;

    /**
     * 提交人id:工号，淘宝userId
     */
    private String applierId;

    /**
     * 提交人区域类型：采购，县服务中心，服务站
     */
    private AssetIncomeApplierAreaTypeEnum applierAreaType;

    /**
     * 提交人区域名称
     */
    private String applierAreaName;

    /**
     * 提交人区域id:组织id,服务站id
     */
    private Long applierAreaId;

    /**
     * 接收人姓名
     */
    private String receiverName;

    /**
     * 接收人工号
     */
    private String receiverWorkno;

    /**
     * 接收人所在组织
     */
    private Long receiverOrgId;

    /**
     * 接收人所在组织名称
     */
    private String receiverOrgName;

    /**
     *入库单类型
     */
    private AssetIncomeTypeEnum type;

    /**
     *入库单状态
     */
    private AssetIncomeStatusEnum status;

    /**
     * po编号
     */
    private String poNo;

    /**
     *备注
     */
    private String remark;
    
    /**
     * 签收类型
     */
    private AssetIncomeSignTypeEnum signType;
    
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

	public String getApplierId() {
		return applierId;
	}

	public void setApplierId(String applierId) {
		this.applierId = applierId;
	}

	public AssetIncomeApplierAreaTypeEnum getApplierAreaType() {
		return applierAreaType;
	}

	public void setApplierAreaType(AssetIncomeApplierAreaTypeEnum applierAreaType) {
		this.applierAreaType = applierAreaType;
	}

	public String getApplierAreaName() {
		return applierAreaName;
	}

	public void setApplierAreaName(String applierAreaName) {
		this.applierAreaName = applierAreaName;
	}

	public Long getApplierAreaId() {
		return applierAreaId;
	}

	public void setApplierAreaId(Long applierAreaId) {
		this.applierAreaId = applierAreaId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverWorkno() {
		return receiverWorkno;
	}

	public void setReceiverWorkno(String receiverWorkno) {
		this.receiverWorkno = receiverWorkno;
	}

	public Long getReceiverOrgId() {
		return receiverOrgId;
	}

	public void setReceiverOrgId(Long receiverOrgId) {
		this.receiverOrgId = receiverOrgId;
	}

	public String getReceiverOrgName() {
		return receiverOrgName;
	}

	public void setReceiverOrgName(String receiverOrgName) {
		this.receiverOrgName = receiverOrgName;
	}

	public AssetIncomeTypeEnum getType() {
		return type;
	}

	public void setType(AssetIncomeTypeEnum type) {
		this.type = type;
	}

	public AssetIncomeStatusEnum getStatus() {
		return status;
	}

	public void setStatus(AssetIncomeStatusEnum status) {
		this.status = status;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public AssetIncomeSignTypeEnum getSignType() {
		return signType;
	}

	public void setSignType(AssetIncomeSignTypeEnum signType) {
		this.signType = signType;
	}
}
