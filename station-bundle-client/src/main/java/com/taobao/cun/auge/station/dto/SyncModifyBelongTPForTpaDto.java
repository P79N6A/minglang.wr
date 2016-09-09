package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 淘帮手修改所属合伙人dto
 * @author quanzhu.wangqz
 *
 */
public class SyncModifyBelongTPForTpaDto  extends OperatorDto implements Serializable {

	private static final long serialVersionUID = 1598145046144794651L;
	/**
	 * 淘帮手实例id
	 */
	private Long partnerInstanceId;
	
	/**
	 * 合伙人实例id
	 */
	private Long parentPartnerInstanceId;

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}

	public Long getParentPartnerInstanceId() {
		return parentPartnerInstanceId;
	}

	public void setParentPartnerInstanceId(Long parentPartnerInstanceId) {
		this.parentPartnerInstanceId = parentPartnerInstanceId;
	}
}
