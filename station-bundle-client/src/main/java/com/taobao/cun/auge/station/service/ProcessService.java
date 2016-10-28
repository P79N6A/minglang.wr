package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.PartnerInstanceLevelProcessDto;
import com.taobao.cun.auge.station.dto.StartProcessDto;

public interface ProcessService {
	
	public void startApproveProcess(StartProcessDto startProcessDto);
	
	public void startLevelApproveProcess(PartnerInstanceLevelProcessDto levelProcessDto);
	
}
