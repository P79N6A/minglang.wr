package com.taobao.cun.auge.alilang;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Iterables;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.convert.PartnerConverter;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;

@Component
public class AlilangUserRegister {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private PartnerBO partnerBO;
	@Resource
	private PartnerInstanceBO partnerInstanceBO;
	@Resource
	private PartnerLifecycleBO partnerLifecycleBO;
	
	public void register(String mobile, String alilangUserId){
		List<Partner> partners = partnerBO.getPartnerByMobile(mobile);
		if(partners == null || Iterables.isEmpty(partners)){
			logger.warn("cann't find partner, mobile={}", mobile);
			return;
		}
		
		for(Partner partner : partners){
			PartnerStationRel partnerStationRel = partnerInstanceBO.getActivePartnerInstance(partner.getTaobaoUserId());
			if(partnerStationRel != null && PartnerInstanceTypeEnum.TP.getCode().equals(partnerStationRel.getType())){
				PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(partnerStationRel.getId());
				if(partnerInstanceDto.getPartnerLifecycleDto().getBond() != null && PartnerLifecycleBondEnum.HAS_FROZEN.getCode().equals(partnerInstanceDto.getPartnerLifecycleDto().getBond().getCode())){
					logger.info("register alilanguser, taobao_user_id={}, alilangUserId = {}", new Object[]{partner.getTaobaoUserId(), alilangUserId});
					PartnerDto partnerDto = PartnerConverter.toPartnerDto(partner);
					partnerDto.setAliLangUserId(alilangUserId);
					partnerBO.updatePartner(partnerDto);
					break;
				}
			}
		}
	}
}
