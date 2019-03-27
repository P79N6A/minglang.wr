package com.taobao.cun.auge.cuncounty.service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyBo;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Validated
@HSFProvider(serviceInterface = CuntaoCountyService.class)
public class CuntaoCountyServiceImpl implements CuntaoCountyService {
	@Resource
	private CuntaoCountyBo cuntaoCountyBo;
	@Override
	public void applyOpen(Long countyId, String operator) {
		cuntaoCountyBo.applyOpen(countyId, operator);
	}

}
