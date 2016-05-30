package com.taobao.cun.auge.station.bo.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRel;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRelExample;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRelExample.Criteria;
import com.taobao.cun.auge.dal.mapper.PartnerMapper;
import com.taobao.cun.auge.dal.mapper.PartnerProtocolRelMapper;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.bo.ProtocolBO;
import com.taobao.cun.auge.station.enums.ProtocolTargetBizTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;

public class PartnerProtocolRelBOImpl implements PartnerProtocolRelBO{
	
	@Autowired
	PartnerProtocolRelMapper partnerProtocolRelMapper;
	
	@Autowired
	PartnerMapper partnerMapper;

	@Autowired
	PartnerStationRelMapper partnerStationRelMapper;
	
	@Autowired
	ProtocolBO protocolBO;

	@Override
	public void signProtocol(Long taobaoUserId, ProtocolTypeEnum type, Long businessId,
			ProtocolTargetBizTypeEnum bizType) {
		Date now = new Date();
		signProtocol(businessId, taobaoUserId, type, now, now, null, String.valueOf(taobaoUserId),
				ProtocolTargetBizTypeEnum.PARTNER_INSTANCE);
	}

	private void signProtocol(Long objectId, Long taobaoUserId, ProtocolTypeEnum type, Date confirmTime, Date startTime,
			Date endTime, String operator, ProtocolTargetBizTypeEnum targetType) {
		Long protocolId = protocolBO.findProtocolId(type);

		PartnerProtocolRel partnerProtocolRelDO = new PartnerProtocolRel();

		partnerProtocolRelDO.setTaobaoUserId(taobaoUserId);
		partnerProtocolRelDO.setProtocolId(protocolId);
		partnerProtocolRelDO.setObjectId(objectId);
		partnerProtocolRelDO.setTargetType(targetType.getCode());
		partnerProtocolRelDO.setConfirmTime(confirmTime);
		partnerProtocolRelDO.setStartTime(startTime);
		partnerProtocolRelDO.setEndTime(endTime);

		DomainUtils.beforeInsert(partnerProtocolRelDO, operator);

		partnerProtocolRelMapper.insertSelective(partnerProtocolRelDO);
	}
	
	
	@Override
	public void cancelProtocol(Long taobaoUserId, ProtocolTypeEnum type, Long businessId,
			ProtocolTargetBizTypeEnum bizType, String operator) {
		PartnerProtocolRelExample example = new PartnerProtocolRelExample();
		
		Criteria criteria = example.createCriteria();
		
		criteria.andTaobaoUserIdEqualTo(taobaoUserId);
		criteria.andTargetTypeEqualTo(bizType.getCode());
		criteria.andObjectIdEqualTo(businessId);
		

		Long protocolId = protocolBO.findProtocolId(type);
		criteria.andProtocolIdEqualTo(protocolId);

		DomainUtils.beforeDelete(example, operator);
		
		
		partnerProtocolRelMapper.deleteByExample(example);
	}
}
