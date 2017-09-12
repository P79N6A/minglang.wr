package com.taobao.cun.auge.dal.domain;


public class AssetRolloutIncomeDetailExtExample extends AssetRolloutIncomeDetailExample{
	
	private String detailStatus;
	
	private Long incomeId;
	
	private Long rolloutId;
	
	public Long getRolloutId() {
		return rolloutId;
	}

	public void setRolloutId(Long rolloutId) {
		this.rolloutId = rolloutId;
	}

	public Long getIncomeId() {
		return incomeId;
	}

	public void setIncomeId(Long incomeId) {
		this.incomeId = incomeId;
	}

	public String getDetailStatus() {
		return detailStatus;
	}

	public void setDetailStatus(String detailStatus) {
		this.detailStatus = detailStatus;
	}
	
	
}