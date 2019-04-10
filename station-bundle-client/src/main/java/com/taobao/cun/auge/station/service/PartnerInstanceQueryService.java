package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.condition.PartnerInstancePageCondition;
import com.taobao.cun.auge.station.condition.StationCondition;
import com.taobao.cun.auge.station.condition.StationStatisticsCondition;
import com.taobao.cun.auge.station.dto.*;
import com.taobao.cun.auge.station.enums.*;

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
	 */
	public PartnerInstanceDto queryInfo(Long stationId,OperatorDto operator);

	/**
	 * 查询合伙人实例信息
	 *
	 * @param condition
	 * @return
	 */
	public PartnerInstanceDto queryInfo(PartnerInstanceCondition condition);

	/**
	 * 查询最后一个退出的合伙人实例
	 *
	 * @param stationId
	 * @return
	 */
	public PartnerInstanceDto queryLastClosePartnerInstance(Long stationId);

	/**
	 * 查询村点上，所有的入驻实例
	 * @param stationId
	 * @return
	 */
	public List<PartnerInstanceDto> queryPartnerInstances(Long stationId);

	/**
	 * 是否所有合伙人都已经处于退出待解冻、退出状态
	 *
	 * @param stationId
	 * @return
	 */
	public boolean isAllPartnerQuit(Long stationId);

	/**
	 * 查询除instanceId外的，其他人是否都已经处于退出待解冻、退出状态
	 *
	 * @param instanceId
	 * @return
	 */
	public boolean isOtherPartnerQuit(Long instanceId);

	/**
	 * 根据组织、村点信息，查询实例列表
	 *
	 * @param stationCondition
	 * @return
	 */
	public PageDto<PartnerInstanceDto> queryByPage(StationCondition stationCondition);

	/**
	 * 使用stationapply state 查询
	 *
	 * @param pageCondition
	 * @return
	 */
	public PageDto<PartnerInstanceDto> queryByPage(PartnerInstancePageCondition pageCondition);


	/**
	 * 根据合伙人实例id，批量查询合伙人实例
	 *
	 * @param partnerInstanceIds
	 * @return
	 */
	public List<PartnerInstanceDto> queryByPartnerInstanceIds(List<Long> partnerInstanceIds);

	/**
	 * 根据合伙人实例id，批量查询合伙人实例
	 *
	 * @param taobaoUserIds
	 * @return
	 */
	public List<PartnerInstanceDto> queryByTaobaoUserIds(List<Long> taobaoUserIds);

	/**
	 * 获得状态为活跃[settling,decorating,servicing,closing,closed,quitting(退出待解冻除外)]
	 * 的合伙人实例
	 * 如果外部系统使用 要使用这个方法   改成使用	public InstanceDto getActiveInstance(Long taobaoUserId);
	 * 当前方法  会导致升级  PartnerInstanceTypeEnum枚举 导致   反序列化失败
	 *
	 * @param taobaoUserId
	 * @return
	 */
	public PartnerInstanceDto getActivePartnerInstance(Long taobaoUserId);

	/**
	 * 获得状态为活跃[settling,decorating,servicing,closing,closed,quitting(退出待解冻除外)]
	 * 的合伙人实例
	 *
	 * @param taobaoUserId
	 * @return
	 */
	public InstanceDto getActiveInstance(Long taobaoUserId);

	/**
	 * 批量查询
	 *
	 * @param taobaoUserId
	 * @param instanceTypes
	 * @param states
	 * @return
	 */
	public List<PartnerInstanceDto> getBatchActivePartnerInstance(List<Long> taobaoUserId,List<PartnerInstanceTypeEnum> instanceTypes,List<PartnerInstanceStateEnum> states);

	/**
	 * 获取用户账户资金情况，如保证金
	 *
	 * @param taobaoUserId
	 * @param type
	 * @return
	 */
	public AccountMoneyDto getAccountMoney(Long taobaoUserId, AccountMoneyTypeEnum type);


	/**
	 * 根据stationId查询，当前村上的实例id
	 *
	 * @param stationId
	 * @return
	 */
	public Long getPartnerInstanceIdByStationId(Long stationId);

	/**
	 *
	 * @param partnerInstanceId
	 * @return
	 */
	public CloseStationApplyDto getCloseStationApply(Long partnerInstanceId);

	/**
	 * 根据每次申请单id，查询停业申请单
	 *
	 * @param applyId
	 * @return
	 */
	public CloseStationApplyDto getCloseStationApplyById(Long applyId);

	/**
	 * 获取待冻结保证金信息
	 *
	 * @param taobaoUserId
	 * @return
	 */
	public BondFreezingInfoDto getBondFreezingInfoDto(Long taobaoUserId);

	/**
	 * 获取待签约协议信息,e.g. 入驻协议，管理协议
	 *
	 * @param taobaoUserId
	 * @param type
	 * @return
	 */
	public ProtocolSigningInfoDto getProtocolSigningInfo(Long taobaoUserId, ProtocolTypeEnum type);

	/**
	 * 获得指定类型数据的协议
	 * @param objectId
	 * @param targetType
	 * @return
	 */
	public PartnerProtocolRelDto getProtocolRel(Long objectId, PartnerProtocolRelTargetTypeEnum targetType, ProtocolTypeEnum type);

	/**
	 * 获得退出申请单
	 * @param instanceId
	 * @return
	 */
	public QuitStationApplyDto getQuitStationApply(Long instanceId);

	/**
	 * 根据每次申请单id，查询退出申请单
	 *
	 * @param applyId
	 * @return
	 */
	public QuitStationApplyDto getQuitStationApplyById(Long applyId);

	/**
	 * 获取合伙人层级信息
	 * @param taobaoUserId
	 * @return
	 */
	public PartnerInstanceLevelDto getPartnerInstanceLevel(Long taobaoUserId);

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
	 */
	public PartnerInstanceDto getCurrentPartnerInstanceByPartnerId(Long partnerId);

	/**
	 * 获得当前人的历史对应关系
	 */
	public List<PartnerInstanceDto> getHistoryPartnerInstanceByPartnerId(Long partnerId);

	/**
	 * 查询服务站，当前实例
	 *
	 * @param stationId
	 * @return
	 */
	public PartnerInstanceDto getCurrentPartnerInstanceByStationId(Long stationId);

	/**
	 * 获得当前服务站的历史对应关系
	 * @param stationId
	 * @return
	 */
	public List<PartnerInstanceDto> getHistoryPartnerInstanceByStationId(Long stationId);

	/* 根据合伙人组织ID路径获取入驻/退出流程中不同状态村点的数量
	 * @param orgIdPath
	 * @return
	 */
	public StationStatisticDto getStationStatistics(StationStatisticsCondition condition);

	/**
	 * 获取合伙人层级成长信息
	 * @param taobaoUserId
	 * @return
	 */
	public PartnerInstanceLevelGrowthDtoV2 getPartnerInstanceLevelGrowthDataV2(Long taobaoUserId);

	/**
	 * 获取合伙人成长趋势指标数据
	 * @param taobaoUserId
	 * @param statDate
	 * @return
	 */
	public List<PartnerInstanceLevelGrowthTrendDtoV2> getPartnerInstanceLevelGrowthTrendDataV2(Long taobaoUserId, String statDate);

	/**
	 * 根据淘宝userId，查询当前所在的村点id
	 *
	 * @param taobaoUserId
	 * @return
	 */
	public Long getCurStationIdByTaobaoUserId(Long taobaoUserId);

	/**
	 * 根据父站点ID查询淘帮手信息
	 * @param parentStationId
	 * @return
	 */
	public List<PartnerInstanceDto> queryTpaPartnerInstances(Long parentStationId);

	/**
	 * 根据父站点ID查询淘帮手信息
	 * @param parentStationId
	 * @return
	 */
	public List<PartnerInstanceDto> queryTpaPartnerInstances(Long parentStationId,PartnerInstanceStateEnum state);
	/**
	 * 获得铺货信息
	 * @param taobaoUserId
	 * @return
	 */
	public ReplenishDto getReplenishDtoByTaobaoUserId(Long taobaoUserId);
	/**
	 * 获得转型服务站待缴纳基础保证金金额
	 * @param taobaoUserId
	 * @return
	 */
	public BondFreezingInfoDto getBondFreezingInfoForTrans(Long taobaoUserId);

	/**
	 * 根据id获得  业务类型
	 * @param instanceId
	 * @return
	 */
	StationBizTypeEnum  getBizTypeByInstanceId(Long instanceId);

	/**
	 * 根据淘宝id获得  业务类型
	 * @param taobaoUserId
	 * @return
	 */
	StationBizTypeEnum  getBizTypeByTaobaoUserId(Long taobaoUserId);

	/**
	 * 给icuntao使用,获得服务站待确认协议的转型信息,若已确认则返回null
	 * @param taobaoUserId
	 * @return
	 */
	StationTransInfoTypeEnum getWaitConfirmTransInfoTypeByTaobaoUserId(Long taobaoUserId);

	/**
	 * 给icuntao使用,获得服务站待确认协议的收改切换信息,若已确认则返回null
	 * @param taobaoUserId
	 * @return
	 */
	PartnerInstanceRevenueStatusEnum getWaitConfirmRevenueTransInfoTypeByTaobaoUserId(Long taobaoUserId);


	/**
	 * 给icuntao使用,获得服务站的转型切换信息,若已确认则返回null
	 * @param taobaoUserId
	 * @return
	 */
	StationTransHandOverDto getStationTransHandOverInfoByTaobaoUserId(Long taobaoUserId);


	/**
	 * 给icuntao使用,获得服务站的转型切换类型及步骤信息,若已确认则返回null
	 * @param taobaoUserId
	 * @return
	 */
	String getStationTransHandOverTypeInfoByTaobaoUserId(Long taobaoUserId);
}


