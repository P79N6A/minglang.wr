package com.taobao.cun.auge.dal.example;

import java.io.Serializable;

public class DwiCtStationTpaDExtExample implements Serializable{
	
	private static final long serialVersionUID = -4294227034332011562L;

	/**
	 * 连续的月份
	 */
	private String[] bizMonths;
	
	/**
	 * 连续n个月
	 */
	private Integer lastMonthCount;
	
	/**
	 * 限制的订单数
	 */
	private Long orderNumLimit;
	
	/**
	 * 限制的含佣GMV
	 */
	private Double gmvLimit;

	public String[] getBizMonths() {
		return bizMonths;
	}

	public void setBizMonths(String[] bizMonths) {
		this.bizMonths = bizMonths;
	}

	public Integer getLastMonthCount() {
		return lastMonthCount;
	}

	public void setLastMonthCount(Integer lastMonthCount) {
		this.lastMonthCount = lastMonthCount;
	}

	public Long getOrderNumLimit() {
		return orderNumLimit;
	}

	public void setOrderNumLimit(Long orderNumLimit) {
		this.orderNumLimit = orderNumLimit;
	}

	public Double getGmvLimit() {
		return gmvLimit;
	}

	public void setGmvLimit(Double gmvLimit) {
		this.gmvLimit = gmvLimit;
	}
}
