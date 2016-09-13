package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 降级合伙人dto
 * @author quanzhu.wangqz
 *
 */
public class PartnerInstanceDegradeDto extends OperatorDto implements Serializable {
	
	private static final long serialVersionUID = -902480451682542382L;
	/**
	 * 待降级的合伙人实例id
	 */
	public Long instanceId;
	/**
	 * 目标合伙人的淘宝userId
	 */
	public Long parentTaobaoUserId;
	
	public Long getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}
	public Long getParentTaobaoUserId() {
		return parentTaobaoUserId;
	}
	public void setParentTaobaoUserId(Long parentTaobaoUserId) {
		this.parentTaobaoUserId = parentTaobaoUserId;
	}
}
