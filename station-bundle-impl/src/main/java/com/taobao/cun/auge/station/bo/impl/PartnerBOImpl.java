package com.taobao.cun.auge.station.bo.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.conversion.PartnerConverter;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.mapper.PartnerMapper;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.condition.PartnerCondition;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;

public class PartnerBOImpl implements PartnerBO {
	
	@Autowired
	PartnerMapper partnerMapper;
	
	@Override
	public Partner getNormalPartnerByTaobaoUserId(Long taobaoUserId)
			throws AugeServiceException {
		Partner partnerCondition = new Partner();
		partnerCondition.setTaobaoUserId(taobaoUserId);
		partnerCondition.setIsDeleted("n");
		partnerCondition.setState(PartnerStateEnum.NORMAL.getCode());
		return partnerMapper.selectOne(partnerCondition);
	}

	@Override
	public Long getNormalPartnerIdByTaobaoUserId(Long taobaoUserId)
			throws AugeServiceException {
		Partner partner = getNormalPartnerByTaobaoUserId(taobaoUserId);
		if (partner != null) {
			return partner.getId();
		}
		return null;
	}

	@Override
	public Long addPartner(PartnerCondition partnerCondition,String operator)
			throws AugeServiceException {
		if (partnerCondition ==null || StringUtils.isEmpty(operator)){
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		Partner partner = PartnerConverter.convertToDomain(partnerCondition);
		partner.setState(PartnerStateEnum.TEMP.getCode());
		DomainUtils.beforeInsert(partner, operator);
		partnerMapper.insert(partner);
		return partner.getId();
	}

	@Override
	public void updatePartner(PartnerCondition partnerCondition,String operator)
			throws AugeServiceException {
		if (partnerCondition ==null || StringUtils.isEmpty(operator)){
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		if (partnerCondition.getId() == null){
			throw new AugeServiceException(PartnerExceptionEnum.ID_IS_NULL);
		}
		Partner partner = PartnerConverter.convertToDomain(partnerCondition);
		DomainUtils.beforeUpdate(partner, operator);
		partnerMapper.updateByPrimaryKey(partner);
	}

	@Override
	public Partner getPartnerById(Long id) throws AugeServiceException {
		return partnerMapper.selectByPrimaryKey(id);
	}
}
