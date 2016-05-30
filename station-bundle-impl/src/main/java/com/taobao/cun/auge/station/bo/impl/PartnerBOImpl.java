package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.PartnerMapper;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.convert.PartnerConverter;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;

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
		ValidateUtils.notNull(partnerDto, CommonExceptionEnum.PARAM_IS_NULL);
		Partner record = PartnerConverter.toParnter(partnerDto);
		DomainUtils.beforeInsert(record, partnerDto.getOperator());
		partnerMapper.insert(record);
		return record.getId();
	}

	@Override
	public void updatePartner(PartnerDto partnerDto)
			throws AugeServiceException {
		ValidateUtils.notNull(partnerDto, CommonExceptionEnum.PARAM_IS_NULL);
		ValidateUtils.notNull(partnerDto.getId(), CommonExceptionEnum.PARAM_IS_NULL);
		Partner record = PartnerConverter.toParnter(partnerDto);
		DomainUtils.beforeUpdate(record, partnerDto.getOperator());
		partnerMapper.updateByPrimaryKey(record);
	}

	@Override
	public Partner getPartnerById(Long partnerId) throws AugeServiceException {
		ValidateUtils.notNull(partnerId, CommonExceptionEnum.PARAM_IS_NULL);
		return partnerMapper.selectByPrimaryKey(partnerId);
	}

	@Override
	public void deletePartner(Long partnerId, String operator)
			throws AugeServiceException {
		Partner rel = new Partner();
		rel.setId(partnerId);
		DomainUtils.beforeDelete(rel, operator);
		partnerMapper.updateByPrimaryKeySelective(rel);
	}
}
