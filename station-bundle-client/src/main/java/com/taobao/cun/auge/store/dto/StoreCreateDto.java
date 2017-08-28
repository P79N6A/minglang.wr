package com.taobao.cun.auge.store.dto;

import java.io.Serializable;

public class StoreCreateDto implements Serializable{
	private static final long serialVersionUID = 3402121165343030411L;
	private String creator;
	/**
	 * 门店名称
	 */
	private String name;
	
	/**
	 * 后台主营类目
	 */
	private Integer categoryId;
	/**
	 * 核销账号
	 */
	private String writeOffAccount;
	
	/**
	 * 服务站ID
	 */
	private Long stationId;
	
	/**
	 * 门店分类
	 */
	private StoreCategory storeCategory;
	
	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public StoreCategory getStoreCategory() {
		return storeCategory;
	}

	public void setStoreCategory(StoreCategory storeCategory) {
		this.storeCategory = storeCategory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWriteOffAccount() {
		return writeOffAccount;
	}

	public void setWriteOffAccount(String writeOffAccount) {
		this.writeOffAccount = writeOffAccount;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
}
