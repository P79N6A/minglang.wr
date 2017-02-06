package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 同步淘帮手直升成为合伙人dto
 * @author quanzhu.wangqz
 *
 */
public class SyncUpgradeToTPForTpaDto extends OperatorDto implements Serializable {

	private static final long serialVersionUID = -6630188995042877116L;
	
	/**
	 * 老的淘帮手实例id
	 */
	private Long oldPartnerInstanceId;
	
	/**
	 * 新的合伙人实例id
	 */
	private Long partnerInstanceId;
	

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}

	public Long getOldPartnerInstanceId() {
		return oldPartnerInstanceId;
	}

	public void setOldPartnerInstanceId(Long oldPartnerInstanceId) {
		this.oldPartnerInstanceId = oldPartnerInstanceId;
	}
	
	
}
