package com.taobao.cun.auge.alipay.service;


import com.taobao.cun.auge.alipay.dto.AlipayRiskScanResult;
import com.taobao.cun.auge.client.result.ResultModel;

public interface AlipayRiskScanService {
	
	public ResultModel<AlipayRiskScanResult> checkEntryRisk(Long taobaoUserId);
	
	public ResultModel<AlipayRiskScanResult> checkEntryRisk(String taobaoNick);


}