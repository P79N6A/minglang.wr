package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 停业确认dto
 * @author quanzhu.wangqz
 *
 */
public class ConfirmCloseDto extends OperatorDto implements Serializable {
	
	private static final long serialVersionUID = 3650367624943113498L;
	/**
	 * 实例id
	 */
	@NotNull(message="instanceId not null")
	private Long partnerInstanceId;
	/**
	 * 是否同意
	 */
	@NotNull(message="isAgree not null")
	private Boolean isAgree;
	
	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}
	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}
	public Boolean isAgree() {
		return isAgree;
	}
	public void setAgree(Boolean isAgree) {
		this.isAgree = isAgree;
	}
}
