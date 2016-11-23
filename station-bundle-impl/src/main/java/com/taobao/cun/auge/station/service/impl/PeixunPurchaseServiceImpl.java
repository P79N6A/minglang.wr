package com.taobao.cun.auge.station.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.bo.PeixunPurchaseBO;
import com.taobao.cun.auge.station.condition.PeixunPuchaseQueryCondition;
import com.taobao.cun.auge.station.dto.PeixunPurchaseDto;
import com.taobao.cun.auge.station.service.PeixunPurchaseService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("peixunPurchaseService")
@HSFProvider(serviceInterface = PeixunPurchaseService.class)
public class PeixunPurchaseServiceImpl implements PeixunPurchaseService{
    
	@Autowired
	PeixunPurchaseBO peixunPurchaseBO;
	
	@Override
	public Long createOrUpdatePeixunPurchase(PeixunPurchaseDto dto) {
		return peixunPurchaseBO.createOrUpdatePeixunPurchase(dto);
	}

	@Override
	public boolean audit(Long id, String operate, String auditDesc,Boolean isPass) {
		return peixunPurchaseBO.audit(id, operate, auditDesc,isPass);
	}

	@Override
	public boolean rollback(Long id, String operate) {
		return peixunPurchaseBO.rollback(id, operate);
	}

	@Override
	public boolean createOrder(Long id, String operate) {
		return peixunPurchaseBO.createOrder(id, operate);
	}

	@Override
	public PageDto<PeixunPurchaseDto> queryPeixunPurchaseList(
			PeixunPuchaseQueryCondition condition) {
		return peixunPurchaseBO.queryPeixunPurchaseList(condition);
	}

}
