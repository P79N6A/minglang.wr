package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 开业村点dto
 * @author quanzhu.wangqz
 *
 */
public class OpenStationDto  extends OperatorDto implements Serializable {

	private static final long serialVersionUID = 8125007573364622437L;
	
	/**
	 * 合伙人实例id
	 */
	@NotNull(message="instanceId not null")
	private Long partnerInstanceId;
	/**
	 * 开业时间
	 */
	@NotNull(message="openDate not null")
	private Date openDate; 
	/**
	 *是否立即开业
	 */
	@NotNull(message="isImme not null")
	private Boolean isImme;
	
	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}
	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}
	public Date getOpenDate() {
		return openDate;
	}
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}
	public Boolean isImme() {
		return isImme;
	}
	public void setImme(Boolean isImme) {
		this.isImme = isImme;
	}
}
