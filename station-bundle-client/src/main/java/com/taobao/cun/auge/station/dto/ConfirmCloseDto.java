package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

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
	private Long partnerInstanceId;
	/**
	 * 是否同意
	 */
	private boolean isAgree;
	
	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}
	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}
	public boolean isAgree() {
		return isAgree;
	}
	public void setAgree(boolean isAgree) {
		this.isAgree = isAgree;
	}
}
