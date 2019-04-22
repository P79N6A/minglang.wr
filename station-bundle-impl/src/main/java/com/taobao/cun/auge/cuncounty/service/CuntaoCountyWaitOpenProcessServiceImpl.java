package com.taobao.cun.auge.cuncounty.service;

import javax.annotation.Resource;

import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyWaitOpenProcessBo;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * 县服务中心待开业审批流程

 * @author chengyu.zhoucy
 *
 */
@HSFProvider(serviceInterface = CuntaoCountyWaitOpenProcessService.class)
public class CuntaoCountyWaitOpenProcessServiceImpl implements CuntaoCountyWaitOpenProcessService {
	@Resource
	private CuntaoCountyWaitOpenProcessBo cuntaoCountyWaitOpenProcessBo;
	
	@Override
	public void start(Long countyId, String operator) {
		cuntaoCountyWaitOpenProcessBo.start(countyId, operator);
	}

	@Override
	public void agree(Long countyId) {
		cuntaoCountyWaitOpenProcessBo.agree(countyId);
	}

	@Override
	public void deny(Long countyId) {
		cuntaoCountyWaitOpenProcessBo.deny(countyId);
	}

}
