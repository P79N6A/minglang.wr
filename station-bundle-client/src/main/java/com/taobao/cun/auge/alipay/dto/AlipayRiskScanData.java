package com.taobao.cun.auge.alipay.dto;

import java.io.Serializable;

public class AlipayRiskScanData implements Serializable {

	private static final long serialVersionUID = 1609967437371579744L;
	private boolean risk;
	private String detail;

	

	public boolean isRisk() {
		return risk;
	}

	public void setRisk(boolean risk) {
		this.risk = risk;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
