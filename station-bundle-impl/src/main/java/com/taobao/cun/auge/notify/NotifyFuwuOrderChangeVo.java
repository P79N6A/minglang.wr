package com.taobao.cun.auge.notify;

import java.math.BigDecimal;

public class NotifyFuwuOrderChangeVo extends DefaultNotifyPublishVo{

	private static final long serialVersionUID = 1L;
	private Long userId;
	private String orderNum;
	private String productCode;
	private String orderStatus;
	private String partnerName;
	private String partnerPhone;
	private String partnerAddress;
	private BigDecimal orderPrice;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public String getPartnerPhone() {
		return partnerPhone;
	}
	public void setPartnerPhone(String partnerPhone) {
		this.partnerPhone = partnerPhone;
	}
	public String getPartnerAddress() {
		return partnerAddress;
	}
	public void setPartnerAddress(String partnerAddress) {
		this.partnerAddress = partnerAddress;
	}
	public BigDecimal getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}
	
	
}
