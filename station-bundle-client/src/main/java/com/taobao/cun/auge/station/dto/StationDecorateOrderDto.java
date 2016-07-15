package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;

public class StationDecorateOrderDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6550938997748924825L;

	private boolean paid;
	
	private boolean refund;
	
	private Long bizOrderId; //业务订单ID
	
	private Long auctionId; //商品ID
	
    private String auctionPicUrl; //商品图片链接
    
    private String auctionTitle;    //商品名称
    
    private Integer buyAmount; //购买数量
    
    private Long totalFee; //商品总价

    private Date createDate; //创建时间
    
	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public boolean isRefund() {
		return refund;
	}

	public void setRefund(boolean refund) {
		this.refund = refund;
	}

	public Long getBizOrderId() {
		return bizOrderId;
	}

	public void setBizOrderId(Long bizOrderId) {
		this.bizOrderId = bizOrderId;
	}

	public Long getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(Long auctionId) {
		this.auctionId = auctionId;
	}

	public String getAuctionPicUrl() {
		return auctionPicUrl;
	}

	public void setAuctionPicUrl(String auctionPicUrl) {
		this.auctionPicUrl = auctionPicUrl;
	}

	public String getAuctionTitle() {
		return auctionTitle;
	}

	public void setAuctionTitle(String auctionTitle) {
		this.auctionTitle = auctionTitle;
	}

	public Integer getBuyAmount() {
		return buyAmount;
	}

	public void setBuyAmount(Integer buyAmount) {
		this.buyAmount = buyAmount;
	}

	public Long getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Long totalFee) {
		this.totalFee = totalFee;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
}
