package com.taobao.cun.auge.asset.dto;

import java.util.Date;
import java.util.List;

import com.taobao.cun.auge.common.PageQuery;

/**
 * 盘点信息结果DTO
 * 
 * @author quanzhu.wangqz
 *
 */
public class AssetCheckInfoDto extends PageQuery {

	private static final long serialVersionUID = 1124965828102911518L;
	
	private Long infoId;
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
	/**
	 * 盘点人名字
	 */
	private String checkerName;
	/**
	 * 盘点人id
	 */
	private String checkerId;
	/**
	 * 盘点人所属区域类型
	 */
	private String checkerAreaType;

	/**
	 * 盘点人所属区域id
	 */
	private String checkerAreaId;

	/**
	 * 盘点人所属区域名称
	 */
	private String checkerAreaName;
	/**
	 * 盘点时间
	 */
	private Date checkTime;

	/**
	 * 图片
	 */
	private List<String> images;
	/**
	 * 状态
	 */
    private String status;
    /**
     * 村点信息
     */
    private AssetCheckStationExtInfo stationExtInfo;
    
	public Long getInfoId() {
		return infoId;
	}

	public void setInfoId(Long infoId) {
		this.infoId = infoId;
	}

	public AssetCheckStationExtInfo getStationExtInfo() {
		return stationExtInfo;
	}

	public void setStationExtInfo(AssetCheckStationExtInfo stationExtInfo) {
		this.stationExtInfo = stationExtInfo;
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

	public String getCheckerName() {
		return checkerName;
	}

	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}

	public String getCheckerId() {
		return checkerId;
	}

	public void setCheckerId(String checkerId) {
		this.checkerId = checkerId;
	}

	public String getCheckerAreaType() {
		return checkerAreaType;
	}

	public void setCheckerAreaType(String checkerAreaType) {
		this.checkerAreaType = checkerAreaType;
	}

	public String getCheckerAreaId() {
		return checkerAreaId;
	}

	public void setCheckerAreaId(String checkerAreaId) {
		this.checkerAreaId = checkerAreaId;
	}

	public String getCheckerAreaName() {
		return checkerAreaName;
	}

	public void setCheckerAreaName(String checkerAreaName) {
		this.checkerAreaName = checkerAreaName;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
