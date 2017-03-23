package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

/**
 *  降级成功dto
 * @author quanzhu.wangqz
 *
 */
public class DegradePartnerInstanceSuccessDto extends OperatorDto implements Serializable {
	
	private static final long serialVersionUID = -902480451682542382L;
	/**
	 * 合伙人实例id
	 */
	public Long instanceId;
	
	public Long parentInstanceId;

	
	public Long getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}
	public Long getParentInstanceId() {
		return parentInstanceId;
	}
	public void setParentInstanceId(Long parentInstanceId) {
		this.parentInstanceId = parentInstanceId;
	}	
	
}
