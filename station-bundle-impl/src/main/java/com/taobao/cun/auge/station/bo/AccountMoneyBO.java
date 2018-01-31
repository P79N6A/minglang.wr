package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;

/**
 * 账户基础服务
 * @author quanzhu.wangqz
 *
 */
public interface AccountMoneyBO {
	
	/**
	 * 新增
	 * @param accountMoneyDto
	 * @return
	 * @
	 */
	public Long addAccountMoney(AccountMoneyDto accountMoneyDto)  ;
	
	/**
	 * 修改
	 * @param accountMoneyDto
	 * @return
	 * @
	 */
	public Long updateAccountMoneyByObjectId(AccountMoneyDto accountMoneyDto)  ;
	
	/**
	 * 查询
	 * @param type
	 * @param targetType
	 * @param objectId
	 * @return
	 * @
	 */
	public AccountMoneyDto getAccountMoney(AccountMoneyTypeEnum type,AccountMoneyTargetTypeEnum targetType,Long objectId)  ;
}
