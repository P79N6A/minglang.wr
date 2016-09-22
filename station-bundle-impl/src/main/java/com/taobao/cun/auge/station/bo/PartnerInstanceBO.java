package com.taobao.cun.auge.station.bo;

import java.util.Date;
import java.util.List;

import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface PartnerInstanceBO {

	
	/**
	 * 根据实例id，查询stationApplyid
	 * 
	 * @param instanceId
	 * @return
	 */
	public Long findStationApplyId(Long instanceId);

	/**
	 * 根据stationId查询stationAPPLYid
	 * 
	 * @param stationId
	 * @return
	 */
	public Long findStationApplyIdByStationId(Long stationId) throws AugeServiceException;
	
	/**
	 * 
	 * @param stationId
	 * @return
	 * @throws AugeServiceException
	 */
	public Long findPartnerInstanceIdByStationId(Long stationId) throws AugeServiceException;

	/**
	 * 根据stationId查询stationAPPLYid
	 * 
	 * @param stationId
	 * @return
	 */
	public PartnerStationRel findPartnerInstanceByStationId(Long stationId) throws AugeServiceException;

	/**
	 *
	 * @param partnerId
	 * @param isCurrent
	 * @return
	 */
	public List<PartnerStationRel> getPartnerStationRelByPartnerId(Long partnerId, String isCurrent);
	
	/**
	 *
	 * @param stationId
	 * @param isCurrent
	 * @return
	 */
	public List<PartnerStationRel> getPartnerStationRelByStationId(Long stationId, String isCurrent);
	

	/**
	 * 根据stationapplyId查询实例id
	 * 
	 * @param stationApplyId
	 * @return
	 */
	public Long getInstanceIdByStationApplyId(Long stationApplyId) throws AugeServiceException;
	/**
	 * 根据stationapplyId查询实例
	 * @param stationApplyId
	 * @return
	 * @throws AugeServiceException
	 */
	public PartnerStationRel getPartnerStationRelByStationApplyId(Long stationApplyId) throws AugeServiceException;

	/**
	 * 根据taobaoUserId 查询合伙人实例表主键id
	 *
	 * @param taobaoUserId
	 * @return
	 */
	public Long getInstanceIdByTaobaoUserId(Long taobaoUserId, PartnerInstanceStateEnum instanceState) throws AugeServiceException;

	/**
	 * 根据taobaoUserId 查询合伙人实例表
	 *
	 * @param taobaoUserId
	 * @return
	 */
	public PartnerStationRel getPartnerInstanceByTaobaoUserId(Long taobaoUserId, PartnerInstanceStateEnum instanceState)
			throws AugeServiceException;

	/**
	 * 根据实例id查询村点id
	 * 
	 * @param instanceId
	 * @return
	 */
	public Long findStationIdByInstanceId(Long instanceId) throws AugeServiceException;

	/**
	 * 根据合伙人实例id，查询实例
	 * 
	 * @param instanceId
	 * @return
	 */
	public PartnerStationRel findPartnerInstanceById(Long instanceId) throws AugeServiceException;

	/**
	 * 查询子合伙人数量
	 * 
	 * @param instanceId
	 *            父合伙人实例id
	 * @param state
	 *            子合伙人状态
	 * @return
	 */
	public int findChildPartners(Long instanceId, PartnerInstanceStateEnum state) throws AugeServiceException;

	/**
	 * 查询子合伙人
	 * 
	 * @param instanceId
	 * @param stateEnums
	 * @return
	 * @throws AugeServiceException
	 */
	public List<PartnerStationRel> findChildPartners(Long instanceId, List<PartnerInstanceStateEnum> stateEnums) throws AugeServiceException;
	
	/**
	 * 查询一个村点上，所有的实例
	 * 
	 * @param stationId
	 * @return
	 * @throws AugeServiceException
	 */
	public List<PartnerStationRel> findPartnerInstances(Long stationId) throws AugeServiceException;
	
	/**
	 * 查询村点上，最后一个结束服务的实例
	 * 
	 * @param stationId
	 * @return
	 * @throws AugeServiceException
	 */
	public PartnerStationRel findLastClosePartnerInstance(Long stationId) throws AugeServiceException;
	
	/**
	 * 状态流转
	 * 
	 * @param instanceId
	 *            合伙人实例id
	 * @param preState
	 *            前置状态
	 * @param postState
	 *            后置状态
	 */
	public void changeState(Long instanceId, PartnerInstanceStateEnum preState, PartnerInstanceStateEnum postState, String operator)
			throws AugeServiceException;

	/**
	 * 更新实例
	 * 
	 */
	public void updatePartnerStationRel(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException;

	/**
	 * 获得当前实例业务数据，包含（partner,station,partnerLifecycleItems）
	 * @param instanceId
	 * @return
	 * @throws AugeServiceException
	 */
	public PartnerInstanceDto getPartnerInstanceById(Long instanceId) throws AugeServiceException;

	/**
	 * 设置开业时间
	 * 
	 * @param instanceId
	 * @param openDate
	 * @param operator
	 * @throws AugeServiceException
	 */
	public void updateOpenDate(Long instanceId, Date openDate, String operator) throws AugeServiceException;

	/**
	 * 新增实例关系
	 * 
	 * @param partnerInstanceDto
	 * @return
	 * @throws AugeServiceException
	 */
	public Long addPartnerStationRel(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException;

	/**
	 * 检查taobaouserid 是否可以入驻
	 * 
	 * @param taobaoUserId
	 * @return
	 * @throws AugeServiceException
	 */
	public boolean checkSettleQualification(Long taobaoUserId) throws AugeServiceException;

	/**
	 * 删除关系表数据
	 * 
	 * @param instanceId
	 * @throws AugeServiceException
	 */
	public void deletePartnerStationRel(Long instanceId, String operator) throws AugeServiceException;

	/**
	 * 获得待开业数据
	 * 
	 * @param fetchNum
	 * @return
	 * @throws AugeServiceException
	 */
	public List<Long> getWaitOpenStationList(int fetchNum) throws AugeServiceException;

	/**
	 * 获得待解冻保证金数据
	 * 
	 * @param fetchNum
	 * @return
	 * @throws AugeServiceException
	 */
	public List<Long> getWaitThawMoneyList(int fetchNum) throws AugeServiceException;

	/**
	 * 获得状态为活跃[settling,decorating,servicing,closing,closed,quitting(推出待解冻除外)]
	 * 的合伙人实例
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	public PartnerStationRel getActivePartnerInstance(Long taobaoUserId) throws AugeServiceException ;
	
	
	/**
	 *  获得有效的的淘帮手
	 * @param parentStationId
	 * @return
	 * @throws AugeServiceException
	 */
	public int  getActiveTpaByParentStationId(Long parentStationId) throws AugeServiceException;
	
	
	public void finishCourse(Long taobaoUserId) throws AugeServiceException;
	
	/**
	 * 是否所有合伙人都处于退出待解冻、已退出状态
	 * 
	 * @param stationId
	 * @throws AugeServiceException
	 */
	public boolean isAllPartnerQuit(Long stationId) throws AugeServiceException;

	/**
	 * 同一个站点，除instanceId的合伙人，其他合伙人是否都处于退出待解冻、已退出状态
	 * 
	 * @param instanceId
	 * @return
	 * @throws AugeServiceException
	 */
	public boolean isOtherPartnerQuit(Long instanceId) throws AugeServiceException;

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
	
	/**
	 * 获得当前服务站的历史对应关系
	 */
	public List<PartnerInstanceDto> getHistoryPartnerInstanceByStationId(Long stationId) throws AugeServiceException;

}
