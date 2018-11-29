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
	 * 查询组织id
	 */
	private Long orgId;
	/**
	 * 大阿里编号
	 */
	private String aliNo;
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
	 * 是否全匹配
	 */
	private String isMatch;
	/**
	 * 县组织ID
	 */
	private Long countyOrgId;

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

	public String getIsMatch() {
		return isMatch;
	}

	public void setIsMatch(String isMatch) {
		this.isMatch = isMatch;
	}

	public Long getCountyOrgId() {
		return countyOrgId;
	}

	public void setCountyOrgId(Long countyOrgId) {
		this.countyOrgId = countyOrgId;
	}

}
