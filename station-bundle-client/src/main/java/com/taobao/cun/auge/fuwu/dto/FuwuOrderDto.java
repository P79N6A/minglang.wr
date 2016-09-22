package com.taobao.cun.auge.fuwu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FuwuOrderDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String orderNo;
	private Long userId;
	private String orderTitle;
	private String status;
	private BigDecimal basePrice;
	private BigDecimal paymentAmount;
	private BigDecimal executePrice;
	private String comments;
	private Date gmtOrder;

	public Date getGmtOrder() {
		return gmtOrder;
	}

	public void setGmtOrder(Date gmtOrder) {
		this.gmtOrder = gmtOrder;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getOrderTitle() {
		return orderTitle;
	}

	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public BigDecimal getExecutePrice() {
		return executePrice;
	}

	public void setExecutePrice(BigDecimal executePrice) {
		this.executePrice = executePrice;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
