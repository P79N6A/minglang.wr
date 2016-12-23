package com.taobao.cun.auge.station.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.station.adapter.UicTagAdapter;
import com.taobao.cun.auge.station.dto.UserTagDto;
import com.taobao.cun.auge.station.service.UicTagService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
@Service("uicTagService")
@HSFProvider(serviceInterface= UicTagService.class)
public class UicTagServiceImpl implements UicTagService {
	
	@Autowired
	UicTagAdapter uicTagAdapter;

	@Override
	public void removeUserTag(UserTagDto userTagDto) {
		uicTagAdapter.removeUserTag(userTagDto);
	}

	@Override
	public void addUserTag(UserTagDto userTagDto) {
		uicTagAdapter.addUserTag(userTagDto);
		
	}

}
