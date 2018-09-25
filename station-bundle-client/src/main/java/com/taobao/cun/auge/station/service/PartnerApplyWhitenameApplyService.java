package com.taobao.cun.auge.station.service;

public interface PartnerApplyWhitenameApplyService {
	boolean apply(Long partnerApplyId, String taobaoUserId, String applierId);
	
	void agree(String taobaoUserId);
}
