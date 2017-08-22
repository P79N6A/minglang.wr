package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.Date;


public class AssetRolloutIncomeDetailExtDto implements Serializable{

    private static final long serialVersionUID = 7100435184292730173L;

    private Long assetId;

    private String aliNo;

    private String brand;

    private String model;

    private String category;

    private String categoryName;
    
    private String price;
    
    private Date operatorTime;

    private String type;
    
    private String status;

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	public String getAliNo() {
		return aliNo;
	}

	public void setAliNo(String aliNo) {
		this.aliNo = aliNo;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Date getOperatorTime() {
		return operatorTime;
	}

	public void setOperatorTime(Date operatorTime) {
		this.operatorTime = operatorTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
