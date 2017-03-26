package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.List;

import com.taobao.cun.auge.dal.domain.AccountMoney;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;

/**
 * 
 * @author quanzhu.wangqz
 *
 */
public class AccountMoneyConverter {
	
	public static AccountMoneyDto toAccountMoneyDto(AccountMoney accountMoney) {
		if (accountMoney == null) {
			return null;
		}

		AccountMoneyDto accountMoneyDto = new AccountMoneyDto();
		
		accountMoneyDto.setId(accountMoney.getId());
		accountMoneyDto.setAlipayAccount(accountMoney.getAlipayAccount());
		accountMoneyDto.setFrozenTime(accountMoney.getFrozenTime());
		accountMoneyDto.setMoney(accountMoney.getMoney());
		accountMoneyDto.setObjectId(accountMoney.getObjectId());
		accountMoneyDto.setState(AccountMoneyStateEnum.valueof(accountMoney.getState()));
		accountMoneyDto.setTaobaoUserId(accountMoney.getTaobaoUserId());
		accountMoneyDto.setTargetType(AccountMoneyTargetTypeEnum.valueof(accountMoney.getTargetType()));
		accountMoneyDto.setThawTime(accountMoney.getThawTime());
		accountMoneyDto.setType(AccountMoneyTypeEnum.valueof(accountMoney.getType()));
		accountMoneyDto.setAccountNo(accountMoney.getAccountNo());
		return accountMoneyDto;
	}

	public static AccountMoney toAccountMoney(AccountMoneyDto accountMoneyDto) {
		if (accountMoneyDto == null) {
			return null;
		}

		AccountMoney accountMoney = new AccountMoney();
		accountMoney.setAlipayAccount(accountMoneyDto.getAlipayAccount());
		accountMoney.setFrozenTime(accountMoneyDto.getFrozenTime());
		accountMoney.setMoney(accountMoneyDto.getMoney());
		accountMoney.setObjectId(accountMoneyDto.getObjectId());
		accountMoney.setState(accountMoneyDto.getState().getCode());
		accountMoney.setTaobaoUserId(accountMoneyDto.getTaobaoUserId());
		accountMoney.setTargetType(accountMoneyDto.getTargetType().getCode());
		accountMoney.setThawTime(accountMoneyDto.getThawTime());
		accountMoney.setType(accountMoneyDto.getType().getCode());
		accountMoney.setAccountNo(accountMoneyDto.getAccountNo());
		
		return accountMoney;
	}

	public static List<AccountMoneyDto> toAccountMoneyDtos(List<AccountMoney> accountMoney) {
		if (accountMoney == null) {
			return null;
		}

		List<AccountMoneyDto> list = new ArrayList<AccountMoneyDto>();
		for (AccountMoney AccountMoney_ : accountMoney) {
			list.add(toAccountMoneyDto(AccountMoney_));
		}

		return list;
	}

	public static List<AccountMoney> toAccountMoneys(List<AccountMoneyDto> accountMoney) {
		if (accountMoney == null) {
			return null;
		}

		List<AccountMoney> list = new ArrayList<AccountMoney>();
		for (AccountMoneyDto AccountMoneyDto : accountMoney) {
			list.add(toAccountMoney(AccountMoneyDto));
		}

		return list;
	}
}
