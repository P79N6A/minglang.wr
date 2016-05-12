package com.taobao.cun.auge.station.service;

import java.math.BigDecimal;
import java.util.Date;

import com.taobao.cun.auge.station.condition.ForcedCloseCondition;
import com.taobao.cun.auge.station.condition.TPInstanceCondition;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 合伙人实例服务
 * 
 * 合伙人每参与一次村淘，都会生成一个实例。
 * 
 * @author haihu.fhh
 *
 */
public interface TPInstanceService {

	/**
	 * 暂存人和村
	 * 
	 * @param condition
	 * @return
	 */
	public Long tempPartnerStation(TPInstanceCondition condition) throws AugeServiceException;

	/**
	 * 正式提交，人和村
	 * 
	 * @param condition
	 * @return
	 */
	public Long submitPartnerStation(TPInstanceCondition condition) throws AugeServiceException;

	/**
	 * 正式提交，暂存的人和村
	 * 
	 * @param condition
	 * @return
	 */
	public Long submitTempPartnerStation(TPInstanceCondition condition) throws AugeServiceException;

	/**
	 * 修改，人和村
	 * 
	 * @param condition
	 * @return
	 */
	public boolean updatePartnerStation(TPInstanceCondition condition) throws AugeServiceException;

	/**
	 * 删除合伙人实例
	 * 
	 * @param instanceId
	 * @return
	 */
	public boolean deletePartnerStation(Long instanceId) throws AugeServiceException;

	/**
	 * 签署入驻协议
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	public boolean signSettledProtocol(Long taobaoUserId) throws AugeServiceException;

	/**
	 * 签署管理协议
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	public boolean signManageProtocol(Long taobaoUserId) throws AugeServiceException;

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
	 * 合伙人申请退出
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	public boolean quitApplyByPartner(Long taobaoUserId) throws AugeServiceException;

	/**
	 * 小二审批合伙人申请退出
	 * 
	 * @param partnerInstanceId
	 * @param employeeId
	 * @param isAgree
	 * @return
	 */
	public boolean approvePartnerQuitApply(Long partnerInstanceId, String employeeId, boolean isAgree) throws AugeServiceException;

	/**
	 * 小二申请停业
	 * 
	 * @param forcedCloseCondition
	 * @param employeeId
	 * @return
	 */
	public boolean applyForcedClose(ForcedCloseCondition forcedCloseCondition, String employeeId) throws AugeServiceException;

	/**
	 * 小二申请撤点
	 * 
	 * @param forcedCloseCondition
	 * @param employeeId
	 * @return
	 */
	public boolean quitApplyByEmployee(Long partnerInstanceId, String employeeId) throws AugeServiceException;

	/**
	 * 详情，人和村。已脫敏
	 * 
	 * @param partnerStationId
	 * @return
	 */
	public PartnerInstanceDto findSafedStationPartner(Long partnerStationId) throws AugeServiceException;

}
