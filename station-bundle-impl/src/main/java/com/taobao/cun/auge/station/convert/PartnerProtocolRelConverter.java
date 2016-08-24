package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.List;

import com.taobao.cun.auge.dal.domain.PartnerProtocolRel;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;

/**
 * 
 * @author quanzhu.wangqz
 *
 */
public class PartnerProtocolRelConverter {
	
	public static PartnerProtocolRelDto toPartnerProtocolRelDto(PartnerProtocolRel partnerProtocolRel) {
		if (partnerProtocolRel == null) {
			return null;
		}

		PartnerProtocolRelDto partnerProtocolRelDto = new PartnerProtocolRelDto();

		partnerProtocolRelDto.setConfirmTime(partnerProtocolRel.getConfirmTime());
		partnerProtocolRelDto.setEndTime(partnerProtocolRel.getEndTime());
		partnerProtocolRelDto.setObjectId(partnerProtocolRel.getObjectId());
		partnerProtocolRelDto.setProtocolId(partnerProtocolRel.getProtocolId());
		partnerProtocolRelDto.setStartTime(partnerProtocolRel.getStartTime());
		partnerProtocolRelDto.setTaobaoUserId(partnerProtocolRel.getTaobaoUserId());
		partnerProtocolRelDto.setTargetType(PartnerProtocolRelTargetTypeEnum.valueof(partnerProtocolRel.getTargetType()));
		return partnerProtocolRelDto;
	}

	public static PartnerProtocolRel toPartnerProtocolRel(PartnerProtocolRelDto partnerProtocolRelDto) {
		if (partnerProtocolRelDto == null) {
			return null;
		}

		PartnerProtocolRel PartnerProtocolRel = new PartnerProtocolRel();

		PartnerProtocolRel.setConfirmTime(partnerProtocolRelDto.getConfirmTime());
		PartnerProtocolRel.setEndTime(partnerProtocolRelDto.getEndTime());
		PartnerProtocolRel.setObjectId(partnerProtocolRelDto.getObjectId());
		PartnerProtocolRel.setProtocolId(partnerProtocolRelDto.getProtocolId());
		PartnerProtocolRel.setStartTime(partnerProtocolRelDto.getStartTime());
		PartnerProtocolRel.setTaobaoUserId(partnerProtocolRelDto.getTaobaoUserId());
		PartnerProtocolRel.setTargetType(partnerProtocolRelDto.getTargetType().getCode());

		return PartnerProtocolRel;
	}

	public static List<PartnerProtocolRelDto> toPartnerProtocolRelDtos(List<PartnerProtocolRel> partnerProtocolRel) {
		if (partnerProtocolRel == null) {
			return null;
		}

		List<PartnerProtocolRelDto> list = new ArrayList<PartnerProtocolRelDto>();
		for (PartnerProtocolRel partnerProtocolRel_ : partnerProtocolRel) {
			list.add(toPartnerProtocolRelDto(partnerProtocolRel_));
		}

		return list;
	}

	public static List<PartnerProtocolRel> toPartnerProtocolRels(List<PartnerProtocolRelDto> partnerProtocolRel) {
		if (partnerProtocolRel == null) {
			return null;
		}

		List<PartnerProtocolRel> list = new ArrayList<PartnerProtocolRel>();
		for (PartnerProtocolRelDto partnerProtocolRelDto : partnerProtocolRel) {
			list.add(toPartnerProtocolRel(partnerProtocolRelDto));
		}

		return list;
	}
}
