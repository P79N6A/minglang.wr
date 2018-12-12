package com.taobao.cun.auge.asset.dto;

import com.taobao.cun.auge.common.PageQuery;

/**
 * 盘点信息查询参数
 * 
 * @author quanzhu.wangqz
 *
 */
public class AssetCheckInfoCondition extends PageQuery {
	
	private static final long serialVersionUID = 1124965828102911518L;

	/**
	 * 盘点人所在区域
	 */
	private String checkerAreaType;
	
	/**
	 * 查询组织id
	 */
	private Long orgId;
	/**
	 * 大阿里编号
	 */
	private String aliNo;
	/**
	 * 序列号
	 */
	private String serialNo;
	/**
	 * 资产大类型
	 */
	private String assetType;
	/**
	 * 资产类目
	 */
	private String categoryType;
	/**
	 * 盘点类型
	 */
	private String checkType;
	
	/**
	 *村小二淘宝账号
	 */
	private Long taobaoUserId;
	
	/**
	 * 盘点人所属区域id
	 */
	private Long checkerAreaId;
	
	/**
	 * 状态
	 */
	private String status;
	
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCheckerAreaId() {
		return checkerAreaId;
	}

	public void setCheckerAreaId(Long checkerAreaId) {
		this.checkerAreaId = checkerAreaId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getAliNo() {
		return aliNo;
	}

	public void setAliNo(String aliNo) {
		this.aliNo = aliNo;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getCheckerAreaType() {
		return checkerAreaType;
	}

	public void setCheckerAreaType(String checkerAreaType) {
		this.checkerAreaType = checkerAreaType;
	}
}
