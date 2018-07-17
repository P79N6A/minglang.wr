package com.taobao.cun.auge.data.partner.service;

import java.util.List;

import javax.annotation.Resource;

import com.taobao.cun.auge.data.partner.bo.PartnerAccessBo;
import com.taobao.cun.auge.data.partner.dto.PartnerAccessDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = PartnerAccessService.class)
public class PartnerAccessServiceImpl implements PartnerAccessService {
	@Resource
	private PartnerAccessBo partnerAccessBo;
	
	@Override
	public List<PartnerAccessDto> queryPartnerAccessList(int day) {
		return partnerAccessBo.queryPartnerAccessList(day);
	}

}
