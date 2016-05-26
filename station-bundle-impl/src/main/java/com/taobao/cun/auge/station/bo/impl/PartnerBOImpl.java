package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.mapper.PartnerMapper;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.validator.BeanValidator;

@Component("partnerBO")
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
	public Long addPartner(PartnerDto partnerDto)
			throws AugeServiceException {
		BeanValidator.validateWithThrowable(partnerDto);
	    //PartnerConverter converter = Mappers.getMapper(PartnerConverter.class);
		/*//Partner partner = PartnerConverter.convertToDomain(partnerCondition);
		partner.setState(PartnerStateEnum.TEMP.getCode());
		DomainUtils.beforeInsert(partner, operator);
		partnerMapper.insert(partner);
		return partner.getId();*/
		return null;
	}

	@Override
	public void updatePartner(PartnerDto partnerDto)
			throws AugeServiceException {
		/*Partner partner = PartnerConverter.convertToDomain(partnerCondition);
		DomainUtils.beforeUpdate(partner, operator);
		partnerMapper.updateByPrimaryKey(partner);*/
	}

	@Override
	public Partner getPartnerById(Long id) throws AugeServiceException {
		return partnerMapper.selectByPrimaryKey(id);
	}
}
