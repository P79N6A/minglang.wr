package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

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
import com.taobao.cun.auge.dal.mapper.ExPartnerMapper;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.convert.PartnerConverter;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

@Component("partnerBO")
public class PartnerBOImpl implements PartnerBO {
	
	@Autowired
	ExPartnerMapper exPartnerMapper;
	
	
	@Override
	public Partner getNormalPartnerByTaobaoUserId(Long taobaoUserId)
			throws AugeServiceException {
		ValidateUtils.notNull(taobaoUserId);
		PartnerExample example = new PartnerExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andTaobaoUserIdEqualTo(taobaoUserId);
		criteria.andStateEqualTo(PartnerStateEnum.NORMAL.getCode());
		return ResultUtils.selectOne(exPartnerMapper.selectByExample(example)); 
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
		exPartnerMapper.insert(record);
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
		exPartnerMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public Partner getPartnerById(Long partnerId) throws AugeServiceException {
		ValidateUtils.notNull(partnerId);
		return exPartnerMapper.selectByPrimaryKey(partnerId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void deletePartner(Long partnerId, String operator)
			throws AugeServiceException {
		ValidateUtils.notNull(partnerId);
		Partner rel = new Partner();
		rel.setId(partnerId);
		DomainUtils.beforeDelete(rel, operator);
		exPartnerMapper.updateByPrimaryKeySelective(rel);
	}

	@Override
	public Partner getPartnerByAliLangUserId(String aliLangUserId) throws AugeServiceException {
		ValidateUtils.notNull(aliLangUserId);
		return exPartnerMapper.selectByAlilangUserId(aliLangUserId); 
	}

	@Override
	public List<Partner> getPartnerByMobile(String mobile) throws AugeServiceException {
		ValidateUtils.notNull(mobile);
		PartnerExample example = new PartnerExample();
		example.createCriteria()
				.andMobileEqualTo(mobile)
				.andStateEqualTo(PartnerStateEnum.NORMAL.getCode())
				.andIsDeletedEqualTo("n");
		return exPartnerMapper.selectByExample(example); 
	}
}
