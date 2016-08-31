package com.taobao.cun.auge.station.service;

import java.util.List;

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
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelGrowthTrendDto;
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
	 * 查询最后一个退出的合伙人实例
	 * 
	 * @param stationId
	 * @param operator
	 * @return
	 * @throws AugeServiceException
	 */
	public PartnerInstanceDto queryLastClosePartnerInstance(Long stationId) throws AugeServiceException;
	
	/**
	 * 查询村点上，所有的入驻实例
	 * @param stationId
	 * @return
	 * @throws AugeServiceException
	 */
	public List<PartnerInstanceDto> queryPartnerInstances(Long stationId) throws AugeServiceException;
	
	/**
	 * 是否所有合伙人都已经处于退出待解冻、退出状态
	 * 
	 * @param stationId
	 * @return
	 * @throws AugeServiceException
	 */
	public boolean isAllPartnerQuit(Long stationId) throws AugeServiceException;

	/**
	 * 查询除instanceId外的，其他人是否都已经处于退出待解冻、退出状态
	 * 
	 * @param instanceId
	 * @return
	 * @throws AugeServiceException
	 */
	public boolean isOtherPartnerQuit(Long instanceId) throws AugeServiceException;
	
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
	
	/**
	 * 获取合伙人成长趋势指标数据
	 * @param taobaoUserId
	 * @param statDate
	 * @return
	 */
	public List<PartnerInstanceLevelGrowthTrendDto> getPartnerInstanceLevelGrowthTrendData(Long taobaoUserId, String statDate);

	/**
	 * 获得当前实例业务数据，包含（partner,station,partnerLifecycleItems）
	 *
	 * @param partnerId
	 * @return
	 * @throws AugeServiceException
	 */
	public PartnerInstanceDto getCurrentPartnerInstanceByPartnerId(Long partnerId) throws AugeServiceException;

	/**
	 * 获得当前人的历史对应关系
	 */
	public List<PartnerInstanceDto> getHistoryPartnerInstanceByPartnerId(Long partnerId) throws AugeServiceException;


	

}