package com.taobao.cun.auge.partner.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.partner.service.PartnerTradeService;
import com.taobao.cun.auge.station.adapter.TradeAdapter;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("partnerTradeService")
@HSFProvider(serviceInterface = PartnerTradeService.class)
public class PartnerTradeServiceImpl implements PartnerTradeService{

	@Autowired
	TradeAdapter tradeAdapter;
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	
	@Override
	public void validateNoEndTradeOrders(Long instanceId) throws AugeServiceException {
		ValidateUtils.notNull(instanceId);
		
		PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);
		
		tradeAdapter.validateNoEndTradeOrders(instance.getTaobaoUserId(), instance.getServiceEndTime());
	}
}