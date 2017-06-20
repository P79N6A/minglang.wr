package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

/**
 * 资产采购详情Dto
 * @author quanzhu.wangqz
 *
 */
public class AssetPurchaseDetailDto implements Serializable {

	private static final long serialVersionUID = 7100435184292730173L;
	
    private String aliNo;
    
    private String serialNo;
    
    private String brand;

    private String model;

    private String category;

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
}
