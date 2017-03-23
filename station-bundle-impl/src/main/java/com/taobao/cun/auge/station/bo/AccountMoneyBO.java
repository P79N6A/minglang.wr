package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

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
	 * @throws AugeServiceException
	 */
	public Long addAccountMoney(AccountMoneyDto accountMoneyDto)  throws AugeServiceException;
	
	/**
	 * 修改
	 * @param accountMoneyDto
	 * @return
	 * @throws AugeServiceException
	 */
	public Long updateAccountMoneyByObjectId(AccountMoneyDto accountMoneyDto)  throws AugeServiceException;
	
	/**
	 * 查询
	 * @param type
	 * @param targetType
	 * @param objectId
	 * @return
	 * @throws AugeServiceException
	 */
	public AccountMoneyDto getAccountMoney(AccountMoneyTypeEnum type,AccountMoneyTargetTypeEnum targetType,Long objectId)  throws AugeServiceException;
}
