package com.taobao.cun.auge.alilang;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.convert.PartnerConverter;
import com.taobao.cun.auge.station.dto.PartnerDto;

@Component
public class AlilangUserRegister {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private PartnerBO partnerBO;
	
	public void register(long taobaoUserId, String alilangUserId){
		Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
		if(partner == null){
			logger.warn("cann't find partner, taobao_user_id={}", taobaoUserId);
			return;
		}
		
		logger.info("register alilanguser, taobao_user_id={}, alilangUserId = {}", new Object[]{taobaoUserId, alilangUserId});
		PartnerDto partnerDto = PartnerConverter.toPartnerDto(partner);
		partnerDto.setAliLangUserId(alilangUserId);
		partnerBO.updatePartner(partnerDto);
	}
}
