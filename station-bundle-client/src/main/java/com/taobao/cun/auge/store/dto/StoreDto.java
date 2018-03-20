package com.taobao.cun.auge.store.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.Address;

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
	private String scmCode;
	
	/**
	 * 所属服务站ID
	 */
	private Long stationId;
	
	/**
	 * 店铺分类
	 */
	private StoreCategory storeCategory;
	
	/**
	 * 店铺类型
	 */
	private String category;
	
	/**
	 * 营业状态
	 */
	private StoreStatus storeStatus;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 淘宝用户ID
	 */
	private Long taobaoUserId;
	
	/**
	 * 门店地址
	 */
	private Address address;
	
	/**
	 * 电话号码
	 */
	private String mobile;
	
	/**
     * 门店卖家ID
     */
    private Long sellerId;
    
    /**
     * 卖家共享门店ID
     */
    private Long sellerShareStoreId;

	/**
	 * 合伙人姓名
	 */
	private String partnerName;
	
	/**
	 * 门店主图
	 */
	private String image;
	
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

	public String getScmCode() {
		return scmCode;
	}

	public void setScmCode(String scmCode) {
		this.scmCode = scmCode;
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


	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	
    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getSellerShareStoreId() {
        return sellerShareStoreId;
    }

    public void setSellerShareStoreId(Long sellerShareStoreId) {
        this.sellerShareStoreId = sellerShareStoreId;
    }

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
