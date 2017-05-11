package com.taobao.cun.auge.asset.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CuntaoAssetCategoryDto implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4496801337825358120L;

	/**
	 * 类目id
	 */
	public Long categoryId;
	
	/**
	 * 资产类目名
	 */
	private String name;
	/**
	 * 资产类型
	 */
	private String type;
	/**
	 * sku规格
	 */
	private List<String> sku;
	
	private List<Sku> skuObjects;
	
	/**
	 * 说明
	 */
	private String remark;
	
	private String poNo;//产品编号
	
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public List<String> getSku() {
		if(sku == null && getSkuObjects() != null) {
			sku = new ArrayList<String>();
			for(Sku s : getSkuObjects()) {
				sku.add(s.getName());
			}
 		}
		return sku;
	}
	public void setSku(List<String> sku) {
		this.sku = sku;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	
	public List<Sku> getSkuObjects() {
		return skuObjects;
	}
	public void setSkuObjects(List<Sku> skuObjects) {
		this.skuObjects = skuObjects;
	}

	public class Sku implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private Long id;
		private String name;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	} 

}
