package com.taobao.cun.auge.cuncounty.service;

import java.util.List;

import javax.annotation.Resource;

import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyWhitenameBo;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyWhitenameDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * 开县白名单
 * 
 * @author chengyu.zhoucy
 *
 */
@HSFProvider(serviceInterface = CuntaoCountyWhitenameService.class)
public class CuntaoCountyWhitenameServiceImpl implements CuntaoCountyWhitenameService {
	@Resource
	private CuntaoCountyWhitenameBo cuntaoCountyWhitenameBo;
	
	@Override
	public List<CuntaoCountyWhitenameDto> getCuntaoCountyWhitenames() {
		return cuntaoCountyWhitenameBo.getCuntaoCountyWhitenames();
	}

}
