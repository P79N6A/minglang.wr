package com.taobao.cun.auge.asset.dto;

import java.util.Map;

import com.taobao.cun.auge.common.OperatorDto;

public class FinishTaskForCountyDto  extends OperatorDto{

	private static final long serialVersionUID = -2643239573988241704L;
	/**
	 * 实物资产无法找到
	 * key:AssetCheckInfoCategoryTypeEnum 的code
	 * @return
	 */
	private Map<String,Long> lostAsset;
	/**
	 * 村点停业，未回收资产
	 * key:AssetCheckInfoCategoryTypeEnum 的code
	 */
	private Map<String,Long> waitBackAsset;
	/**
	 * 其他原因
	 */
	private String otherReason;
	public Map<String, Long> getLostAsset() {
		return lostAsset;
	}
	public void setLostAsset(Map<String, Long> lostAsset) {
		this.lostAsset = lostAsset;
	}
	public Map<String, Long> getWaitBackAsset() {
		return waitBackAsset;
	}
	public void setWaitBackAsset(Map<String, Long> waitBackAsset) {
		this.waitBackAsset = waitBackAsset;
	}
	public String getOtherReason() {
		return otherReason;
	}
	public void setOtherReason(String otherReason) {
		this.otherReason = otherReason;
	}
	
	
}
