package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.AccountMoney;
import com.taobao.cun.auge.dal.domain.AccountMoneyExample;
import com.taobao.cun.auge.dal.domain.AccountMoneyExample.Criteria;
import com.taobao.cun.auge.dal.mapper.AccountMoneyMapper;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.convert.AccountMoneyConverter;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

@Component("accountMoneyBO")
public class AccountMoneyBOImpl implements AccountMoneyBO {
	
	@Autowired
	AccountMoneyMapper accountMoneyMapper;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Long addAccountMoney(AccountMoneyDto accountMoneyDto)
			throws AugeServiceException {
		ValidateUtils.notNull(accountMoneyDto);
		AccountMoney accountMoney = AccountMoneyConverter.toAccountMoney(accountMoneyDto);
		DomainUtils.beforeInsert(accountMoney, accountMoneyDto.getOperator());
		accountMoneyMapper.insert(accountMoney);
		return accountMoney.getId();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Long updateAccountMoneyByObjectId(AccountMoneyDto accountMoneyDto)
			throws AugeServiceException {
		ValidateUtils.validateParam(accountMoneyDto);
		ValidateUtils.notNull(accountMoneyDto.getTargetType());
		ValidateUtils.notNull(accountMoneyDto.getObjectId());
		ValidateUtils.notNull(accountMoneyDto.getType());
		
		AccountMoneyExample example = new AccountMoneyExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andObjectIdEqualTo(accountMoneyDto.getObjectId());
		criteria.andTargetTypeEqualTo(accountMoneyDto.getTargetType().getCode());
		criteria.andTypeEqualTo(accountMoneyDto.getType().getCode());
		
		AccountMoney record = AccountMoneyConverter.toAccountMoney(accountMoneyDto);
		DomainUtils.beforeUpdate(record, accountMoneyDto.getOperator());
		
		accountMoneyMapper.updateByExampleSelective(record, example);
		return null;
	}

	@Override
	public AccountMoneyDto getAccountMoney(AccountMoneyTypeEnum type,
			AccountMoneyTargetTypeEnum targetType, Long objectId)
			throws AugeServiceException {
		ValidateUtils.notNull(type);
		ValidateUtils.notNull(targetType);
		ValidateUtils.notNull(objectId);
		AccountMoneyExample example = new AccountMoneyExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andObjectIdEqualTo(objectId);
		criteria.andTargetTypeEqualTo(targetType.getCode());
		criteria.andTypeEqualTo(type.getCode());
		
		List<AccountMoney> res = accountMoneyMapper.selectByExample(example);
		AccountMoney am = ResultUtils.selectOne(res);
		return AccountMoneyConverter.toAccountMoneyDto(am);
	}

}
