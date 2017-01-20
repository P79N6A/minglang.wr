package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 定时任务调用服务
 * @author quanzhu.wangqz
 *
 */
public interface PartnerInstanceScheduleService {

	/**
	 * 获得待开业数据
	 * @param fetchNum
	 * @return
	 * @throws AugeServiceException
	 */
	public List<Long> getWaitOpenStationList(int fetchNum) throws AugeServiceException;
	
	
	/**
	 * 定时开业
	 * @param instanceId
	 * @return
	 * @throws AugeServiceException
	 */
	public Boolean openStation(Long instanceId) throws AugeServiceException;
	
	
	/**
	 * 获得待解冻保证金数据
	 * @param fetchNum
	 * @return
	 * @throws AugeServiceException
	 */
	public List<Long>  getWaitThawMoneyList(int fetchNum) throws AugeServiceException;
	
	/**
	 * 解冻保证金
	 * @param instanceId
	 * @return
	 * @throws AugeServiceException
	 */
	public Boolean thawMoney(Long instanceId) throws AugeServiceException;
	
	
	
	/**
	 * 历史的已冻结保证金的账户，初始化账户表account_money AccountNo字段
	 * @param pageQuery
	 * @return
	 * @throws AugeServiceException
	 */
	public List<AccountMoneyDto>  getWaitInitAccountNoList(int fetchNum) throws AugeServiceException;
	
	/**
	 * 历史的已冻结保证金的账户，初始化账户表account_money AccountNo字段
	 * @param instanceId
	 * @return
	 * @throws AugeServiceException
	 */
	public Boolean initAccountNo(AccountMoneyDto accountMoneyDto) throws AugeServiceException;
	
	
}
