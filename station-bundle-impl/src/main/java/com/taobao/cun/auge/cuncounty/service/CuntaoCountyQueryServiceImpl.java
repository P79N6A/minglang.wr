package com.taobao.cun.auge.cuncounty.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import com.taobao.cun.auge.common.PageOutput;
import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyQueryBo;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyCondition;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDetailDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyListItem;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyStateCountDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * 县点查询
 * 
 * @author chengyu.zhoucy
 *
 */
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

	@Override
	public List<CuntaoCountyStateCountDto> groupCountyByState(CuntaoCountyCondition condition) {
		return cuntaoCountyQueryBo.groupCountyByState(condition);
	}

	@Override
	public CuntaoCountyDto getCuntaoCounty(Long countyId) {
		return cuntaoCountyQueryBo.getCuntaoCounty(countyId);
	}

	@Override
	public CuntaoCountyDto getCuntaoCountyByOrgId(Long orgId) {
		return cuntaoCountyQueryBo.getCuntaoCountyByOrgId(orgId);
	}
}
