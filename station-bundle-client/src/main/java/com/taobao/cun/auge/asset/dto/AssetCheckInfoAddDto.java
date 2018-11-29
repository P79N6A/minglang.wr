package com.taobao.cun.auge.asset.dto;

import java.util.List;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 盘点新增信息dto
 * @author quanzhu.wangqz
 *
 */
public class AssetCheckInfoAddDto  extends OperatorDto{
	
	private static final long serialVersionUID = 5369060510054072175L;
	/**
	 * 大阿里编号
	 */
	String aliNo;
	/**
	 * 序列号
	 */
	String serialNo;
	/**
	 * 资产类型
	 */
	String categoryType;
	/**
	 * 盘点类型：AssetCheckInfoCheckTypeEnum
	 */
	String checkType;
	
	/**
	 * 盘点附件
	 */
	List<String> images;

	public String getAliNo() {
		return aliNo;
	}

	public void setAliNo(String aliNo) {
		this.aliNo = aliNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
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

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}
}
