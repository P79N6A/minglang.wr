package com.taobao.cun.auge.station.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.station.adapter.AlipayTagAdapter;
import com.taobao.cun.auge.station.dto.AlipayTagDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.AlipayTagService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("alipayTagService")
@HSFProvider(serviceInterface= AlipayTagService.class)
public class AlipayTagServiceImpl implements AlipayTagService{

	@Autowired
	AlipayTagAdapter alipayTagAdapter;
	
	@Override
	public boolean dealTag(AlipayTagDto alipayTagDto) throws AugeServiceException {
		return alipayTagAdapter.dealTag(alipayTagDto);
	}

}
