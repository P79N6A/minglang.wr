package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;

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
	private Long partnerInstanceId;
	/**
	 * 开业时间
	 */
	private Date openDate; 
	/**
	 *是否立即开业
	 */
	private boolean isImme;
	
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
	public boolean isImme() {
		return isImme;
	}
	public void setImme(boolean isImme) {
		this.isImme = isImme;
	}
}
