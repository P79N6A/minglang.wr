package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.mapper.PartnerLifecycleItemsMapper;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;

public class PartnerLifecycleBOImpl implements PartnerLifecycleBO {
	
	@Autowired
	PartnerLifecycleItemsMapper partnerLifecycleItemsMapper;
	
	@Override
	public void add(PartnerLifecycleItems partnerLifecycle) {
		
	}

}
