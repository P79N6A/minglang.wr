package com.taobao.cun.auge.event.domain;

import java.io.Serializable;

import com.taobao.cun.crius.event.annotation.EventDesc;
import com.taobao.cun.crius.event.annotation.EventField;

/**
 * 合伙人开业事件通知
 * @author zhousufa
 *
 */
@EventDesc("合伙人服务站")
public class PartnerStationStateChangeEvent implements Serializable{

	private static final long serialVersionUID = -2308718472108635283L;
	
	@EventField("服务站名称")
	private String stationName;
	@EventField("服务站id")
	private Long stationId;

	@EventField("当前合伙人实例状态")
	private String partnerInstanceState;

	@EventField("当前合伙人淘宝userId")
	private Long taobaoUserId;

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getPartnerInstanceState() {
		return partnerInstanceState;
	}

	public void setPartnerInstanceState(String partnerInstanceState) {
		this.partnerInstanceState = partnerInstanceState;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	

}
