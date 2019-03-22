package com.taobao.cun.auge.cuncounty.service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import com.taobao.cun.auge.common.PageOutput;
import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyQueryBo;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyCondition;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDetailDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyListItem;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Validated
@HSFProvider(serviceInterface = CuntaoCountyQueryService.class)
public class CuntaoCountyQueryServiceImpl implements CuntaoCountyQueryService {
	@Resource
	private CuntaoCountyQueryBo cuntaoCountyQueryBo;
	
	@Override
	public CuntaoCountyDetailDto getCuntaoCountyDetail(Long countyId) {
		return cuntaoCountyQueryBo.getCuntaoCountyDetail(countyId);
	}

	@Override
	public PageOutput<CuntaoCountyListItem> query(CuntaoCountyCondition condition) {
		return cuntaoCountyQueryBo.query(condition);
	}

}
