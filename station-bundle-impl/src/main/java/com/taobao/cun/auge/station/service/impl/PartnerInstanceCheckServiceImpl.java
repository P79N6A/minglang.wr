package com.taobao.cun.auge.station.service.impl;

import com.taobao.cun.auge.station.check.PartnerInstanceChecker;
import com.taobao.cun.auge.station.service.PartnerInstanceCheckService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("partnerInstanceCheckService")
@HSFProvider(serviceInterface = PartnerInstanceCheckService.class)
public class PartnerInstanceCheckServiceImpl implements PartnerInstanceCheckService {
	
	@Autowired
	PartnerInstanceChecker partnerInstanceChecker;

	@Override
	public void checkCloseApply(Long instanceId) {
		partnerInstanceChecker.checkCloseApply(instanceId);
	}

	@Override
	public void checkQuitApply(Long instanceId) {
		partnerInstanceChecker.checkQuitApply(instanceId);
	}

}
