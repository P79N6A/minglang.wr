package com.taobao.cun.auge.station.service.impl;

import com.taobao.cun.auge.station.adapter.WangwangAdapter;
import com.taobao.cun.auge.station.service.WangWangTagService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("wangWangTagService")
@HSFProvider(serviceInterface = WangWangTagService.class)
public class WangWangTagServiceImpl implements WangWangTagService {

	@Autowired
	WangwangAdapter wangwangAdapter;

	@Override
	public void addWangWangTagByNick(String taobaoNick) {
		wangwangAdapter.addWangWangTagByNick(taobaoNick);

	}

	@Override
	public void removeWangWangTagByNick(String taobaoNick) {
		wangwangAdapter.removeWangWangTagByNick(taobaoNick);
	}
}
