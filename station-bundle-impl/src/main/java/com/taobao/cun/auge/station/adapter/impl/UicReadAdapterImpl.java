package com.taobao.cun.auge.station.adapter.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.uic.service.CuntaoUicReadService;

public class UicReadAdapterImpl implements UicReadAdapter{

	@Autowired
	CuntaoUicReadService cuntaoUicReadService;

	@Override
	public String findTaobaoName(String taobaoUserId)  {
		ResultModel<String> result = cuntaoUicReadService.findTaobaoName(taobaoUserId);
		
		if(null != result && result.isSuccess()){
			return result.getResult();
		}
		return "";
	}
}
