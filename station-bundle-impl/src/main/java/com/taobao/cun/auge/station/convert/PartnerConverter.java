package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.List;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.enums.PartnerBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;

/**
 * 合伙人dto转换
 * @author quanzhu.wangqz
 *
 */
public class PartnerConverter {

	public PartnerDto toPartnerDto(Partner partner) {
		if (partner == null) {
			return null;
		}

		PartnerDto partnerDto = new PartnerDto();

		partnerDto.setId(partner.getId());
		partnerDto.setName(partner.getName());
		partnerDto.setAlipayAccount(partner.getAlipayAccount());
		partnerDto.setTaobaoUserId(partner.getTaobaoUserId());
		partnerDto.setTaobaoNick(partner.getTaobaoNick());
		partnerDto.setIdenNum(partner.getIdenNum());
		partnerDto.setMobile(partner.getMobile());
		partnerDto.setEmail(partner.getEmail());
		if (partner.getBusinessType() != null) {
			partnerDto.setBusinessType(PartnerBusinessTypeEnum.valueof(partner.getBusinessType()));
		}
		
		partnerDto.setDescription(partner.getDescription());
		if (partner.getState() != null) {
			partnerDto.setState(PartnerStateEnum.valueof(partner.getState()));
		}

		return partnerDto;
	}

	public Partner toParnter(PartnerDto parnterDto) {
		if (parnterDto == null) {
			return null;
		}

		Partner partner = new Partner();

		partner.setId(parnterDto.getId());
		partner.setName(parnterDto.getName());
		partner.setAlipayAccount(parnterDto.getAlipayAccount());
		partner.setTaobaoUserId(parnterDto.getTaobaoUserId());
		partner.setTaobaoNick(parnterDto.getTaobaoNick());
		partner.setIdenNum(parnterDto.getIdenNum());
		partner.setMobile(parnterDto.getMobile());
		partner.setEmail(parnterDto.getEmail());
		if (parnterDto.getBusinessType() != null) {
			partner.setBusinessType(parnterDto.getBusinessType().getCode());
		}
		partner.setDescription(parnterDto.getDescription());
		if (parnterDto.getState() != null) {
			partner.setState(parnterDto.getState().getCode());
		}

		return partner;
	}

	public List<PartnerDto> toPartnerDtos(List<Partner> partner) {
		if (partner == null) {
			return null;
		}

		List<PartnerDto> list = new ArrayList<PartnerDto>();
		for (Partner partner_ : partner) {
			list.add(toPartnerDto(partner_));
		}

		return list;
	}

	public List<Partner> toPartners(List<PartnerDto> partner) {
		if (partner == null) {
			return null;
		}

		List<Partner> list = new ArrayList<Partner>();
		for (PartnerDto partnerDto : partner) {
			list.add(toParnter(partnerDto));
		}

		return list;
	}
}
