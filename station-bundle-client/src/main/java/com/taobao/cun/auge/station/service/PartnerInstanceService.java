package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.AuditSettleDto;
import com.taobao.cun.auge.station.dto.ConfirmCloseDto;
import com.taobao.cun.auge.station.dto.ForcedCloseDto;
import com.taobao.cun.auge.station.dto.OpenStationDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDegradeDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceUpdateServicingDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;


/**
 * 实例服务接口
 * 
 * @author quanzhu.wangqz
 *
 */
public interface PartnerInstanceService {
	/**
	 * 暂存 人，服务站基础信息
	 * 
	 * @param condition
	 * @return
	 */
	public Long saveTemp(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException;
	
	/**
	 * 更改入驻中的信息    
	 * @param partnerInstanceDto
	 * @throws AugeServiceException
	 */
	public void updateSettle(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException;
	
	
	/**
	 * 审批入驻 当前只有小二审批淘帮手
	 * @param auditSettleDto
	 * @throws AugeServiceException
	 */
	public void auditSettleByManager(AuditSettleDto auditSettleDto) throws AugeServiceException;

	/**
	 * 修改， 人，服务站基础信息 主要包含（装修中，服务中，停业申请中等）有效合伙人状态下
	 * 
	 * @param condition
	 * @return
	 */
	public void update(PartnerInstanceUpdateServicingDto partnerInstanceUpdateServicingDto) throws AugeServiceException;

	/**
	 * 删除合伙人实例
	 * 
	 * @param instanceId
	 * @return
	 */
	public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto) throws AugeServiceException;

	/**
	 * 签署入驻协议
	 * 
	 * @param taobaoUserId
	 * @param waitFrozenMoney
	 * @param version 乐观锁
	 * @throws AugeServiceException
	 */
	public void signSettledProtocol(Long taobaoUserId, Double waitFrozenMoney, Long version) throws AugeServiceException;

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
	public boolean freezeBond(Long taobaoUserId, Double frozenMoney) throws AugeServiceException;

	/**
	 * 开业
	 * 
	 * @param partnerInstanceId
	 *            合伙人实例id
	 * @param openDate
	 *            开业时间
	 * @param isImme
	 *            是否立即开业
	 * @param employeeId
	 *            小二工号
	 * @return
	 */
	public boolean openStation(OpenStationDto openStationDto) throws AugeServiceException;

	/**
	 * 合伙人主动申请停业，目前给接口只有合伙人会调用，淘帮手，村拍档不能调用
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
	public boolean confirmClose(ConfirmCloseDto confirmCloseDto) throws AugeServiceException;

	/**
	 * 小二、TP商申请停业
	 * 
	 * @param forcedCloseCondition
	 * @param employeeId
	 * @return
	 */
	public void applyCloseByManager(ForcedCloseDto forcedCloseDto) throws AugeServiceException;

	/**
	 * 小二、TP商申请撤点
	 * 
	 * @param forcedCloseCondition
	 * @param employeeId
	 * @return
	 */
	public void applyQuitByManager(QuitStationApplyDto quitDto) throws AugeServiceException;

	/**
	 * 申请入驻
	 * 
	 * @param condition
	 * @return
	 */
	public Long applySettle(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException;

	/**
	 * 针对入驻失败的 申请入驻
	 * 
	 * @param condition
	 * @return
	 */
	public Long applyResettle(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException;

	/**
	 * 检查有没有开业包，要调用外部接口 到时候要写适配器封装外部接口 先定义在这里
	 * 
	 * @return
	 * @throws AugeServiceException
	 */
	public boolean checkKyPackage() throws AugeServiceException;

	/**
	 * 正式退出
	 * 
	 * @param partnerInstanceQuitDto
	 * @return
	 * @throws AugeServiceException
	 */
	public void quitPartnerInstance(PartnerInstanceQuitDto partnerInstanceQuitDto) throws AugeServiceException;
	
	/**
	 * 降级合伙人
	 * @param partnerInstanceDegradeDto
	 * @throws AugeServiceException
	 */
	public void degradePartnerInstance(PartnerInstanceDegradeDto degradeDto) throws AugeServiceException;
	
	
	/**
	 * 成功入驻
	 * @param settleSuccessDto
	 * @throws AugeServiceException
	 */
	public void applySettleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto) throws AugeServiceException;
	
}
