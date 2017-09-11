package com.taobao.cun.auge.alipay.service;

import com.taobao.cun.auge.alipay.dto.AlipayRiskScanResult;

public interface AlipayRiskScanService {
	
	public AlipayRiskScanResult checkEntryRisk(Long taobaoUserId);
	
	public AlipayRiskScanResult checkEntryRisk(String taobaoNick);


}