package com.taobao.cun.auge.station.service;

import java.math.BigDecimal;
import java.util.Date;

import com.taobao.cun.auge.station.condition.ForcedCloseCondition;
import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.condition.QuitStationApplyCondition;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
/**
 * 实例服务接口
 * @author quanzhu.wangqz
 *
 */
public interface PatnerInstanceService {
	/**
	 * 新增暂存 人，服务站基础信息
	 * 
	 * @param condition
	 * @return
	 */
	public Long addTemp(PartnerInstanceCondition condition) throws AugeServiceException;
	
	
	/**
	 * 修改暂存 人，服务站基础信息
	 * 
	 * @param condition
	 * @return
	 */
	public Long updateTemp(PartnerInstanceCondition condition) throws AugeServiceException;

	/**
	 * 新增正式提交 人，服务站基础信息
	 * 
	 * @param condition
	 * @return
	 */
	public Long addSubmit(PartnerInstanceCondition condition) throws AugeServiceException;
	
	/**
	 * 修改正式提交 人，服务站基础信息
	 * 
	 * @param condition
	 * @return
	 */
	public Long updateSubmit(PartnerInstanceCondition condition) throws AugeServiceException;
	

	/**
	 * 修改， 人，服务站基础信息 主要包含（装修中，服务中，停业申请中，已停业等）有效合伙人状态下
	 * 
	 * @param condition
	 * @return
	 */
	public boolean update(PartnerInstanceCondition condition) throws AugeServiceException;

	/**
	 * 删除合伙人实例
	 * 
	 * @param instanceId
	 * @return
	 */
	public boolean delete(Long instanceId) throws AugeServiceException;

	/**
	 * 签署入驻协议
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	public void signSettledProtocol(Long taobaoUserId) throws AugeServiceException;

	/**
	 * 签署管理协议
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	public void signManageProtocol(Long taobaoUserId) throws AugeServiceException;

	/**
	 * 冻结保证金
	 * 
	 * @param taobaoUserId
	 * @param frozenMoney
	 * @return
	 */
	public boolean freezeBond(Long taobaoUserId, BigDecimal frozenMoney) throws AugeServiceException;

	/**
	 * 开业
	 * 
	 * @param partnerInstanceId
	 *      合伙人实例id
	 * @param openDate
	 *      开业时间
	 * @param isImme
	 *      是否立即开业
	 * @param employeeId
	 *      小二工号
	 * @return
	 */
	public boolean openStation(Long partnerInstanceId, Date openDate, boolean isImme, String employeeId) throws AugeServiceException;

	/**
	 * 合伙人主动申请停业
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	public boolean applyCloseByPartner(Long taobaoUserId) throws AugeServiceException;

	/**
	 * 小二确认合伙人申请停业
	 * 
	 * @param partnerInstanceId
	 * @param employeeId
	 * @param isAgree
	 * @return
	 */
	public boolean confirmClose(Long partnerInstanceId, String employeeId, boolean isAgree) throws AugeServiceException;

	/**
	 * 小二申请停业
	 * 
	 * @param forcedCloseCondition
	 * @param employeeId
	 * @return
	 */
	public void applyCloseByEmployee(ForcedCloseCondition forcedCloseCondition, String employeeId) throws AugeServiceException;
	
	/**
	 * 审批小二停业申请
	 * 
	 * @param forcedCloseCondition
	 * @param employeeId
	 * @return
	 */
	public void auditClose(Long stationApplyId, String approver,boolean isAgree) throws Exception;

	/**
	 * 小二申请撤点
	 * 
	 * @param forcedCloseCondition
	 * @param employeeId
	 * @return
	 */
	public void applyQuitByEmployee(QuitStationApplyCondition quitApplyCondition,String employeeId) throws AugeServiceException;
	
	/**
	 * 审批撤点
	 * 
	 * @param forcedCloseCondition
	 * @param employeeId
	 * @return
	 */
	public boolean auditQuit(ForcedCloseCondition forcedCloseCondition, String employeeId) throws AugeServiceException;
	
	
	/**
	 * 申请入驻
	 * 
	 * @param condition
	 * @return
	 */
	public Long applySettle(PartnerInstanceCondition condition,PartnerInstanceTypeEnum partnerInstanceTypeEnum) throws AugeServiceException;
	
}
