package com.taobao.cun.auge.station.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRelExt;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRelExtExample;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Protocol;
import com.taobao.cun.auge.dal.mapper.PartnerProtocolRelExtMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.bo.ProtocolBO;
import com.taobao.cun.auge.station.dto.PartnerProtocolDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.dto.ProtocolDto;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolGroupTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolStateEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.PartnerProtocolQueryService;
import com.taobao.cun.recruit.partner.dto.PartnerApplyDto;
import com.taobao.cun.recruit.partner.enums.PartnerApplyConfirmIntentionEnum;
import com.taobao.cun.recruit.partner.service.PartnerApplyService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
@Service("partnerProtocolQueryService")
@HSFProvider(serviceInterface = PartnerProtocolQueryService.class)
public class PartnerProtocolQueryServiceImpl implements PartnerProtocolQueryService{

	@Autowired
	PartnerProtocolRelExtMapper partnerProtocolRelExtMapper;
	
	@Autowired
	PartnerProtocolRelBO partnerProtocolRelBO;	
	
	@Autowired
	private PartnerApplyService partnerApplyService;
	
	@Autowired
	private   PartnerInstanceBO partnerInstanceBO;
	@Autowired
	ProtocolBO protocolBO;
	
	@Override
	public List<PartnerProtocolDto> queryPartnerSignedProtocols(Long partnerInstanceId){
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

	@Override
	public Boolean querySignedProjectNoticeProtocol(Long taobaoUserId) {
		 ValidateUtils.notNull(taobaoUserId);
		 PartnerApplyDto paDto = partnerApplyService.getPartnerApplyByTaobaoUserId(taobaoUserId);
		 Assert.notNull(paDto, "partner apply not exists");
		 if (paDto.getConfirmIntention() == null) {
			 return Boolean.TRUE;
		 }
		 PartnerProtocolRelDto pprDto =  null;
		 if (PartnerApplyConfirmIntentionEnum.TPS_ELEC.getCode().equals(paDto.getConfirmIntention())) {
			  pprDto = partnerProtocolRelBO.getPartnerProtocolRelDto(ProtocolTypeEnum.PARTNER_APPLY_PROJECT_NOTICE_TPS, paDto.getId(), PartnerProtocolRelTargetTypeEnum.PARTNER_APPlY);
		 }else {
	         pprDto = partnerProtocolRelBO.getPartnerProtocolRelDto(ProtocolTypeEnum.PARTNER_APPLY_PROJECT_NOTICE, paDto.getId(), PartnerProtocolRelTargetTypeEnum.PARTNER_APPlY);
         }
		 if (pprDto != null) {
        	 return Boolean.TRUE;
         }
         return Boolean.FALSE;
	}

	@Override
	public String queryLastSignedSettlingProtocol(Long taobaoUserId) {
		 ValidateUtils.notNull(taobaoUserId);
		 PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		 if (rel == null) {
			 throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"当前账号不是村小二");
		 }
         PartnerProtocolRelDto pprDto = partnerProtocolRelBO.getPartnerProtocolRelDto(ProtocolTypeEnum.C2B_SETTLE_PRO, rel.getId(), PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
         if (pprDto != null) {
        	 Protocol p =  protocolBO.getProtocolById(pprDto.getProtocolId());
        	 if (p != null){
        		 if ("C2B入驻协议".equals(p.getName())) {
        			 return "SETTLING_PROTOCOL_V4";
        		 }
        		 if ("4.0入驻协议".equals(p.getName())) {
        			 return "SETTLING_PROTOCOL_V5";
        		 }
        		 if ("4.1入驻协议".equals(p.getName())) {
        			 return "SETTLING_PROTOCOL_V6";
        		 }
        	 }
         }
         pprDto = partnerProtocolRelBO.getPartnerProtocolRelDto(ProtocolTypeEnum.SETTLE_PRO, rel.getId(), PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
         if (pprDto != null) {
        	 return "SETTLING_PROTOCOL_V4";
         }
		return null;
	}

}
