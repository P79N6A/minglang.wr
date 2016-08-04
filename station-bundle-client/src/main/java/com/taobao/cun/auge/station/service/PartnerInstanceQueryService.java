package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.condition.PartnerInstancePageCondition;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.BondFreezingInfoDto;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelGrowthDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.dto.ProtocolSigningInfoDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 合伙人实例查询服务
 * 
 * @author haihu.fhh
 *
 */
public interface PartnerInstanceQueryService {
	
	/**
	 * 根据stationId,查询当前村点上所属人，以及村点信息
	 * 
	 * @param stationId
	 * @return
	 * @throws AugeServiceException
	 */
	public PartnerInstanceDto queryInfo(Long stationId,OperatorDto operator) throws AugeServiceException;

	/**
	 * 查询合伙人实例信息
	 * 
	 * @param condition
	 * @return
	 */
	public PartnerInstanceDto queryInfo(PartnerInstanceCondition condition) throws AugeServiceException;

	/**
	 * 使用stationapply state 查询
	 * 
	 * @param pageCondition
	 * @return
	 */
	public PageDto<PartnerInstanceDto> queryByPage(PartnerInstancePageCondition pageCondition);

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
	 * 根据instanceId查询stationApplyId[过渡阶段使用，即将废弃]
	 * 
	 * @param instanceId
	 * @return
	 */
	public Long getStationApplyId(Long instanceId) throws AugeServiceException;

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
	
	/**
	 * 获得指定类型数据的协议
	 * @param objectId
	 * @param targetType
	 * @return
	 * @throws AugeServiceException
	 */
	public PartnerProtocolRelDto getProtocolRel(Long objectId, PartnerProtocolRelTargetTypeEnum targetType, ProtocolTypeEnum type) throws AugeServiceException;
	
	/**
	 * 获得退出申请单
	 * @param instanceId
	 * @return
	 * @throws AugeServiceException
	 */
	public QuitStationApplyDto getQuitStationApply(Long instanceId)throws AugeServiceException;
	
		
	/**
	 * 获取合伙人层级信息
	 * @param taobaoUserId
	 * @return
	 * @throws AugeServiceException
	 */
	public PartnerInstanceLevelDto getPartnerInstanceLevel(Long taobaoUserId) throws AugeServiceException;
	
	/**
	 * 获取合伙人层级成长信息
	 * @param taobaoUserId
	 * @return
	 */
	public PartnerInstanceLevelGrowthDto getPartnerInstanceLevelGrowthData(Long taobaoUserId);

	

}