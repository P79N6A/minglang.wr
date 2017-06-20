package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 资产采购Dto
 * @author quanzhu.wangqz
 *
 */
public class AssetPurchaseDto extends OperatorDto implements Serializable {

	private static final long serialVersionUID = 7100435184292730173L;

    private String aliNo;
    
    private String serialNo;
    
    private String poNo;

    private String brand;

    private String model;

    private String category;
    
    private Long  ownerOrgId;
    
    private String ownerName;
    
    private String ownerWorkno;

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

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
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

	public Long getOwnerOrgId() {
		return ownerOrgId;
	}

	public void setOwnerOrgId(Long ownerOrgId) {
		this.ownerOrgId = ownerOrgId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerWorkno() {
		return ownerWorkno;
	}

	public void setOwnerWorkno(String ownerWorkno) {
		this.ownerWorkno = ownerWorkno;
	}
}
