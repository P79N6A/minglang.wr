package com.taobao.cun.auge.store.dto;

import java.io.Serializable;

import com.taobao.cun.auge.county.dto.AddressDto;

/**
 * 村淘门店
 *
 */
public class StoreDto implements Serializable{

	private static final long serialVersionUID = 3116443473224484893L;
	
	private Long id;
	/**
	 * 门店名称
	 */
	private String name;
	/**
	 * 共享门店ID
	 */
	private Long shareStoreId;
	
	/**
	 * SCM库存ID
	 */
	private Long scmId;
	
	/**
	 * 所属服务站ID
	 */
	private Long stationId;
	/**
	 * 店铺分类
	 */
	private StoreCategory storeCategory;
	
	/**
	 * 营业状态
	 */
	private StoreStatus storeStatus;
	
	/**
	 * 门店CODE，跟Station Code一致
	 */
	private String code;
	
	/**
	 * 淘宝用户ID
	 */
	private Long taobaoUserId;
	
	/**
	 * 门店地址
	 */
	private AddressDto addressDto;

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

	public Long getShareStoreId() {
		return shareStoreId;
	}

	public void setShareStoreId(Long shareStoreId) {
		this.shareStoreId = shareStoreId;
	}

	public Long getScmId() {
		return scmId;
	}

	public void setScmId(Long scmId) {
		this.scmId = scmId;
	}

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

	public StoreStatus getStoreStatus() {
		return storeStatus;
	}

	public void setStoreStatus(StoreStatus storeStatus) {
		this.storeStatus = storeStatus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public AddressDto getAddressDto() {
		return addressDto;
	}

	public void setAddressDto(AddressDto addressDto) {
		this.addressDto = addressDto;
	}
}
