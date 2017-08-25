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
	 */
	public List<Long> getWaitOpenStationList(int fetchNum);
	
	
	/**
	 * 定时开业
	 * @param instanceId
	 * @return
	 */
	public Boolean openStation(Long instanceId);
	
	
	/**
	 * 获得待解冻保证金数据
	 * @param fetchNum
	 * @return
	 */
	public List<Long>  getWaitThawMoneyList(int fetchNum);
	
	/**
	 * 解冻保证金
	 * @param instanceId
	 * @return
	 */
	public Boolean thawMoney(Long instanceId);
	
	
	
	/**
	 * 历史的已冻结保证金的账户，初始化账户表account_money AccountNo字段
	 * @param pageQuery
	 * @return
	 */
	public List<AccountMoneyDto>  getWaitInitAccountNoList(int fetchNum);
	
	/**
	 * 历史的已冻结保证金的账户，初始化账户表account_money AccountNo字段
	 * @param instanceId
	 * @return
	 */
	public Boolean initAccountNo(AccountMoneyDto accountMoneyDto);
	
	
}
