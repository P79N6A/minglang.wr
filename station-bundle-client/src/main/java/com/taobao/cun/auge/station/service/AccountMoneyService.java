package com.taobao.cun.auge.station.service;

import java.math.BigDecimal;

import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 账户服务接口
 * @author quanzhu.wangqz
 *
 */
public interface AccountMoneyService {

	public Long addWaitFrozenMoney(Long taobaoUserId,BigDecimal money)  throws AugeServiceException;
}
