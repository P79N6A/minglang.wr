package com.taobao.cun.auge.station.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRelExt;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRelExtExample;
import com.taobao.cun.auge.dal.mapper.PartnerProtocolRelExtMapper;
import com.taobao.cun.auge.station.dto.PartnerProtocolDto;
import com.taobao.cun.auge.station.dto.ProtocolDto;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolGroupTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolStateEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.PartnerProtocolQueryService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
@Service("partnerProtocolQueryService")
@HSFProvider(serviceInterface = PartnerProtocolQueryService.class)
public class PartnerProtocolQueryServiceImpl implements PartnerProtocolQueryService{

	@Autowired
	PartnerProtocolRelExtMapper partnerProtocolRelExtMapper;
	
	@Override
	public List<PartnerProtocolDto> queryPartnerSignedProtocols(Long partnerInstanceId) throws AugeServiceException {
		ValidateUtils.notNull(partnerInstanceId);
		PartnerProtocolRelExtExample example = new PartnerProtocolRelExtExample();
		
		example.setObjectId(partnerInstanceId);
		example.setTargetType(PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE.getCode());
		
		List<PartnerProtocolRelExt> partnerProRels = partnerProtocolRelExtMapper.selectPartnerProtocolsByExample(example);
		
		List<PartnerProtocolDto> partnerProDtos = new ArrayList<PartnerProtocolDto>(partnerProRels.size());
		for(PartnerProtocolRelExt partnerProtocolRelDto:partnerProRels){
			partnerProDtos.add(convert(partnerProtocolRelDto));
		}
		
		return partnerProDtos;
	}
	
	private PartnerProtocolDto convert(PartnerProtocolRelExt partnerProRelExt){
		if(null == partnerProRelExt){
			return null;
		}
		
		PartnerProtocolDto partnerProDto = new PartnerProtocolDto();
		
		partnerProDto.setTaobaoUserId(partnerProRelExt.getTaobaoUserId());
		partnerProDto.setProtocolId(partnerProRelExt.getProtocolId());
		partnerProDto.setConfirmTime(partnerProRelExt.getConfirmTime());
		partnerProDto.setStartTime(partnerProRelExt.getStartTime());
		partnerProDto.setEndTime(partnerProRelExt.getEndTime());
		partnerProDto.setObjectId(partnerProRelExt.getObjectId());
		partnerProDto.setTargetType(PartnerProtocolRelTargetTypeEnum.valueof(partnerProRelExt.getTargetType()));
		
		ProtocolDto pDto = new ProtocolDto();
		
		pDto.setId(partnerProRelExt.getProtocolId());
		pDto.setSubmitTime(partnerProRelExt.getSubmitTime());
		pDto.setType(partnerProRelExt.getType());
		pDto.setProtocolTypeEnum(ProtocolTypeEnum.valueof(partnerProRelExt.getType()));
		pDto.setName(partnerProRelExt.getName());
		pDto.setVersion(partnerProRelExt.getVersion());
		pDto.setSubmitId(partnerProRelExt.getSubmitId());
		pDto.setState(partnerProRelExt.getState());
		pDto.setProtocolStateEnum(ProtocolStateEnum.valueof(partnerProRelExt.getState()));
		
		pDto.setDescription(partnerProRelExt.getDescription());
		pDto.setGroupType(partnerProRelExt.getGroupType());
		pDto.setProtocolGroupTypeEnum(ProtocolGroupTypeEnum.valueof(partnerProRelExt.getGroupType()));
		
		pDto.setGroupName(partnerProRelExt.getGroupName());
		
		partnerProDto.setProtocol(pDto);
		return partnerProDto;
	}

}
