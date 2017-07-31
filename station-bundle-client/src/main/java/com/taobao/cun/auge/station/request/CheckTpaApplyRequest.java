package com.taobao.cun.auge.station.request;

import java.io.Serializable;
/**
 * 
 * @author zhenhuan.zhangzh
 *
 */
public class CheckTpaApplyRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 淘宝Nick
	 */
	String taobaoNick;
	
	/**
	 * 父站点ID
	 */
	Long partnerStationId;

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}

	public Long getPartnerStationId() {
		return partnerStationId;
	}

	public void setPartnerStationId(Long partnerStationId) {
		this.partnerStationId = partnerStationId;
	}
	
}
