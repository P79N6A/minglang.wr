package com.taobao.cun.auge.conversion;

import java.util.List;

import org.mapstruct.Mapper;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.station.dto.PartnerDto;

@Mapper
public interface PartnerConverter {
	
	PartnerDto toPartnerDto(Partner partner);
	
	Partner    toParnter(PartnerDto parnterDto);
	
	List<PartnerDto> toPartnerDtos(List<Partner> partner);
	
	List<Partner> toPartners(List<PartnerDto> partner);
}
