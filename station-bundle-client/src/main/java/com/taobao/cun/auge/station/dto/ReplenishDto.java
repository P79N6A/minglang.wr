package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.station.enums.ReplenishStatusEnum;

/**
 * 铺货dto
 * @author quanzhu.wangqz
 *
 */
public class ReplenishDto  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7418246724647234934L;

	/**
	 * 补货金状态
	 */
	private ReplenishStatusEnum   status;
	
	private Long partnerInstanceId;
	
	private Long taobaoUserId;
	
	private Long  stationId;
	/**
	 * 下单链接
	 */
	private String orderUrl;

	public ReplenishStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ReplenishStatusEnum status) {
		this.status = status;
	}

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getOrderUrl() {
		return orderUrl;
	}

	public void setOrderUrl(String orderUrl) {
		this.orderUrl = orderUrl;
	}
}
