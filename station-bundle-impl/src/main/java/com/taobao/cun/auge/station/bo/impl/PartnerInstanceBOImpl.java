package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.mapper.PartnerMapper;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;

@Component
public class PartnerInstanceBOImpl implements PartnerInstanceBO {

	@Autowired
	PartnerMapper partnerMapper;

	@Autowired
	PartnerStationRelMapper partnerStationRelMapper;

	@Override
	public Long findPartnerInstanceId(Long taobaoUserId) {
		Partner partnerCondition = new Partner();
		partnerCondition.setTaobaoUserId(taobaoUserId);
		partnerCondition.setIsDeleted("n");
		Partner partner = partnerMapper.selectOne(partnerCondition);

		PartnerStationRel relCondition = new PartnerStationRel();
		relCondition.setPartnerId(partner.getId());
		relCondition.setIsCurrent("y");
		relCondition.setIsDeleted("n");

		PartnerStationRel rel = partnerStationRelMapper.selectOne(relCondition);

		return rel.getId();
	}

}
