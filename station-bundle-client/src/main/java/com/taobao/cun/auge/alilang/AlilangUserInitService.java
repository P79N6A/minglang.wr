package com.taobao.cun.auge.alilang;

import com.taobao.cun.auge.station.dto.PartnerDto;

public interface AlilangUserInitService {
	public void init();

	void testInit(Long id);
	
	void syncInfo(PartnerDto pd);
}
