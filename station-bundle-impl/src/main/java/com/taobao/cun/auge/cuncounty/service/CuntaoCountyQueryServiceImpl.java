package com.taobao.cun.auge.cuncounty.service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyQueryBo;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDetailDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Validated
@HSFProvider(serviceInterface = CuntaoCountyQueryService.class)
public class CuntaoCountyQueryServiceImpl implements CuntaoCountyQueryService {
	@Resource
	private CuntaoCountyQueryBo CuntaoCountyQueryBo;
	
	@Override
	public CuntaoCountyDetailDto getCuntaoCountyDetail(Long countyId) {
		return CuntaoCountyQueryBo.getCuntaoCountyDetail(countyId);
	}

}
