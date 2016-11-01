package com.taobao.cun.auge.station.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.convert.PartnerConverter;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.service.PartnerService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("partnerService")
@HSFProvider(serviceInterface = PartnerService.class)
public class PartnerServiceImpl implements PartnerService {
	@Resource
	private PartnerBO partnerBO;
	
	@Override
	public void updateById(PartnerDto partnerDto) {
		partnerBO.updatePartner(partnerDto);
	}

	@Override
	public PartnerDto getPartnerByAlilangUserId(String aliLangUserId) {
		return PartnerConverter.toPartnerDto(partnerBO.getPartnerByAliLangUserId(aliLangUserId));
	}

}
