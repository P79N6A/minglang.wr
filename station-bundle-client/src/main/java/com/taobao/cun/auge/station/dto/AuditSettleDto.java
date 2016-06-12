package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 审批入驻dto
 * @author quanzhu.wangqz
 *
 */
public class AuditSettleDto  extends OperatorDto implements Serializable {

	private static final long serialVersionUID = -4362177405420491664L;
	
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
	public Boolean getIsAgree() {
		return isAgree;
	}
	public void setIsAgree(Boolean isAgree) {
		this.isAgree = isAgree;
	}
}
