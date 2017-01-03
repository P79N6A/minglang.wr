package com.taobao.cun.auge.station.convert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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

	public static PartnerDto toPartnerDto(Partner partner) {
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
		partnerDto.setSolidPoint(partner.getSolidPoint());
		partnerDto.setLeaseArea(partner.getLeaseArea());
		partnerDto.setAliLangUserId(partner.getAlilangUserId());
		if (partner.getBusinessType() != null) {
			partnerDto.setBusinessType(PartnerBusinessTypeEnum.valueof(partner.getBusinessType()));
		}
		
		partnerDto.setDescription(partner.getDescription());
		if (partner.getState() != null) {
			partnerDto.setState(PartnerStateEnum.valueof(partner.getState()));
		}

		return partnerDto;
	}

	public static Partner toParnter(PartnerDto parnterDto,boolean isUpdate) {
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
		partner.setSolidPoint(parnterDto.getSolidPoint());
		partner.setLeaseArea(parnterDto.getLeaseArea());
		partner.setAlilangUserId(parnterDto.getAliLangUserId());
		if (parnterDto.getBusinessType() != null) {
			partner.setBusinessType(parnterDto.getBusinessType().getCode());
		}
		partner.setDescription(parnterDto.getDescription());
		if (parnterDto.getState() != null) {
			partner.setState(parnterDto.getState().getCode());
		}
		if(!isUpdate){
			addBirthday(partner);
		}
		return partner;
	}
	
	//初始化生日
	private static void addBirthday(Partner partner){
		if(StringUtils.isNotEmpty(partner.getIdenNum())&&partner.getIdenNum().length()==18){
			DateFormat format = new SimpleDateFormat("yyyyMMdd");  
			try {
				partner.setBirthday(format.parse(partner.getIdenNum().substring(6, 14)));
			} catch (Exception e) {
				// 暂时不影响正常保存
			}
		}
	}

	public static List<PartnerDto> toPartnerDtos(List<Partner> partner) {
		if (partner == null) {
			return null;
		}

		List<PartnerDto> list = new ArrayList<PartnerDto>();
		for (Partner partner_ : partner) {
			list.add(toPartnerDto(partner_));
		}

		return list;
	}

	public static List<Partner> toPartners(List<PartnerDto> partner) {
		if (partner == null) {
			return null;
		}

		List<Partner> list = new ArrayList<Partner>();
		for (PartnerDto partnerDto : partner) {
			list.add(toParnter(partnerDto,false));
		}

		return list;
	}
}
