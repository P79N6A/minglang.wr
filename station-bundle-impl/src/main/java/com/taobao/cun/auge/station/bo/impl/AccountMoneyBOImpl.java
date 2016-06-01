package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.dal.mapper.AccountMoneyMapper;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public class AccountMoneyBOImpl implements AccountMoneyBO {
	
	@Autowired
	AccountMoneyMapper accountMoneyMapper;
	
	@Override
	public Long addAccountMoney(AccountMoneyDto accountMoneyDto)
			throws AugeServiceException {
		accountMoneyMapper.
	}

	@Override
	public Long updateAccountMoney(AccountMoneyDto accountMoneyDto)
			throws AugeServiceException {
		return null;
	}

	@Override
	public AccountMoneyDto getAccountMoney(AccountMoneyTypeEnum type,
			AccountMoneyTargetTypeEnum targetType, Long objectId)
			throws AugeServiceException {
		return null;
	}

}
