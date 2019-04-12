package com.taobao.cun.auge.cuncounty.mobile;

import javax.annotation.Resource;

import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyQueryBo;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDto;
import com.taobao.cun.crius.bpm.mobile.FlowContent;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = CuntaoCountyProcessService.class)
public class CuntaoCountyProcessServiceImpl implements CuntaoCountyProcessService {
	@Resource
	private CuntaoCountyQueryBo cuntaoCountyQueryBo;
	
	@Override
	public FlowContent getCuntaoCountyDetail(String taskCode, Long id) {
		CuntaoCountyDto cuntaoCountyDto = cuntaoCountyQueryBo.getCuntaoCounty(id);
		return FlowContent.create(cuntaoCountyDto);
	}

}
