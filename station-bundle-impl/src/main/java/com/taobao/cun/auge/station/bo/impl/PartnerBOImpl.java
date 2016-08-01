package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerExample;
import com.taobao.cun.auge.dal.domain.PartnerExample.Criteria;
import com.taobao.cun.auge.dal.mapper.PartnerMapper;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.convert.PartnerConverter;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

@Component("partnerBO")
public class PartnerBOImpl implements PartnerBO {
	
	@Autowired
	PartnerMapper partnerMapper;
	
	
	@Override
	public Partner getNormalPartnerByTaobaoUserId(Long taobaoUserId)
			throws AugeServiceException {
		ValidateUtils.notNull(taobaoUserId);
		PartnerExample example = new PartnerExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andTaobaoUserIdEqualTo(taobaoUserId);
		criteria.andStateEqualTo(PartnerStateEnum.NORMAL.getCode());
		return ResultUtils.selectOne(partnerMapper.selectByExample(example)); 
	}

	@Override
	public Long getNormalPartnerIdByTaobaoUserId(Long taobaoUserId)
			throws AugeServiceException {
		ValidateUtils.notNull(taobaoUserId);
		Partner partner = getNormalPartnerByTaobaoUserId(taobaoUserId);
		if (partner != null) {
			return partner.getId();
		}
		return null;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Long addPartner(PartnerDto partnerDto)
			throws AugeServiceException {
		ValidateUtils.notNull(partnerDto);
		Partner record = PartnerConverter.toParnter(partnerDto);
		DomainUtils.beforeInsert(record, partnerDto.getOperator());
		partnerMapper.insert(record);
		return record.getId();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void updatePartner(PartnerDto partnerDto)
			throws AugeServiceException {
		ValidateUtils.notNull(partnerDto);
		ValidateUtils.notNull(partnerDto.getId());
		Partner record = PartnerConverter.toParnter(partnerDto);
		DomainUtils.beforeUpdate(record, partnerDto.getOperator());
		partnerMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public Partner getPartnerById(Long partnerId) throws AugeServiceException {
		ValidateUtils.notNull(partnerId);
		return partnerMapper.selectByPrimaryKey(partnerId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void deletePartner(Long partnerId, String operator)
			throws AugeServiceException {
		ValidateUtils.notNull(partnerId);
		Partner rel = new Partner();
		rel.setId(partnerId);
		DomainUtils.beforeDelete(rel, operator);
		partnerMapper.updateByPrimaryKeySelective(rel);
	}
}
