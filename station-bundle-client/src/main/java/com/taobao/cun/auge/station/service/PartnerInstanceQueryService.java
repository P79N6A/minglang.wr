package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.condition.PartnerInstancePageCondition;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.BondFreezingInfoDto;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.ProtocolSigningInfoDto;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface PartnerInstanceQueryService {

	/**
	 * 查询合伙人实例信息
	 * 
	 * @param partnerStationId
	 * @return
	 */
	public PartnerInstanceDto queryInfo(PartnerInstanceCondition condition) throws AugeServiceException;

	/**
	 * 
	 * @param pageCondition
	 * @return
	 */
	public PageDto<PartnerInstanceDto> queryByPage(PartnerInstancePageCondition pageCondition) throws AugeServiceException;;

	/**
	 * 获得状态为活跃[settling,decorating,servicing,closing,closed,quitting(退出待解冻除外)]
	 * 的合伙人实例
	 * 
	 * @param taobaoUserId
	 * @return
	 * @throws AugeServiceException
	 */
	public PartnerInstanceDto getActivePartnerInstance(Long taobaoUserId) throws AugeServiceException;;

	/**
	 * 获取用户账户资金情况，如保证金
	 * 
	 * @param taobaoUserId
	 * @param type
	 * @return
	 */
	public AccountMoneyDto getAccountMoney(Long taobaoUserId, AccountMoneyTypeEnum type) throws AugeServiceException;;

	/**
	 * 根据stationapplyId查询合伙人实例id[过渡阶段使用，即将废弃]
	 * 
	 * @param stationApplyId
	 * @return
	 */
	public Long getPartnerInstanceId(Long stationApplyId);

	/**
	 * 
	 * @param partnerInstanceId
	 * @return
	 * @throws AugeServiceException
	 */
	public CloseStationApplyDto getCloseStationApply(Long partnerInstanceId) throws AugeServiceException;

	/**
	 * 获取待冻结保证金信息
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	public BondFreezingInfoDto getBondFreezingInfoDto(Long taobaoUserId) throws AugeServiceException;

	/**
	 * 获取待签约协议信息,e.g. 入驻协议，管理协议
	 * 
	 * @param taobaoUserId
	 * @param type
	 * @return
	 */
	public ProtocolSigningInfoDto getProtocolSigningInfo(Long taobaoUserId, ProtocolTypeEnum type) throws AugeServiceException;

}
