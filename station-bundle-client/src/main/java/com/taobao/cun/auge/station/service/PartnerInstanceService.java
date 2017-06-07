package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.dto.AuditSettleDto;
import com.taobao.cun.auge.station.dto.CancelUpgradePartnerInstance;
import com.taobao.cun.auge.station.dto.ChangeTPDto;
import com.taobao.cun.auge.station.dto.ConfirmCloseDto;
import com.taobao.cun.auge.station.dto.DegradePartnerInstanceSuccessDto;
import com.taobao.cun.auge.station.dto.ForcedCloseDto;
import com.taobao.cun.auge.station.dto.FreezeBondDto;
import com.taobao.cun.auge.station.dto.OpenStationDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDegradeDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceThrawSuccessDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceUpdateServicingDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceUpgradeDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.AugeSystemException;


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
	public Long addTemp(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException;
	
	/**
	 * 暂存 人，服务站基础信息
	 * 
	 * @param condition
	 * @return
	 */
	public Long updateTemp(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException;
	
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
	 * 合伙人修改，人、服务站基础信息，只包含服务中
	 * 
	 * 目前只有合伙人修改淘帮手信息，调用
	 * 
	 * @throws AugeServiceException
	 */
	public void updateByPartner(PartnerInstanceUpdateServicingDto partnerInstanceUpdateServicingDto) throws AugeServiceException;

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
	public void signManageProtocol(Long taobaoUserId, Long version) throws AugeServiceException;

	/**
	 * 冻结保证金（新接口）
	 * 
	 * @param freezeBondDto
	 * @return boolean
	 */
	public boolean freezeBond(FreezeBondDto freezeBondDto) throws AugeServiceException;
	
	/**
	 * 冻结保证金(老接口，防止发布时报错,未来不在使用)
	 * 使用新接口freezeBond(FreezeBondDto freezeBondDto) throws AugeServiceException;
	 * 
	 * @param freezeBondDto
	 * @return boolean
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
	public void applyCloseByPartner(Long taobaoUserId) throws AugeServiceException;

	/**
	 * 小二确认合伙人申请停业
	 * 
	 * @param partnerInstanceId
	 * @param employeeId
	 * @param isAgree
	 * @return
	 */
	public void confirmClose(ConfirmCloseDto confirmCloseDto) throws AugeBusinessException,AugeSystemException;

	/**
	 * 小二、TP商申请停业
	 * 
	 * @param forcedCloseCondition
	 * @param employeeId
	 * @return
	 */
	public void applyCloseByManager(ForcedCloseDto forcedCloseDto) throws AugeBusinessException,AugeSystemException;
	
	/**
	 * 系统自动停业
	 * 
	 * @param forcedCloseDto
	 */
	public void applyCloseBySystem(ForcedCloseDto forcedCloseDto);

	/**
	 * 小二、TP商申请撤点
	 * 
	 * @param forcedCloseCondition
	 * @param employeeId
	 * @return
	 */
	public void applyQuitByManager(QuitStationApplyDto quitDto) throws AugeBusinessException,AugeSystemException;

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
	
	/**
	 * 降级成功
	 * @param degradeSuccessDto
	 * @throws AugeServiceException
	 */
	public void degradePartnerInstanceSuccess(DegradePartnerInstanceSuccessDto degradeSuccessDto) throws AugeServiceException;
	
	/**
	 * 评定合伙人层级(定时钟和审批流程调用)
	 * @param partnerInstanceLevelDto
	 * @throws AugeServiceException
	 */
	public void evaluatePartnerInstanceLevel(PartnerInstanceLevelDto partnerInstanceLevelDto) throws AugeServiceException;

	/**
	 * 更换TPA的TP
	 * @param changeTPDto
	 * @throws AugeServiceException
	 */
	public void changeTP(ChangeTPDto changeTPDto) throws AugeServiceException;
	
	/**
	 * 已停业村点恢复服务中
	 * @param instanceDto
	 * @throws AugeServiceException
	 */
	public void reService(Long instanceId, String operator) throws AugeServiceException;
	
	public void upgradeDecorateLifeCycle(Long instanceId, String operator);
	
	/**
	 * S7、S8合伙人层级晋升
	 * @param partnerInstanceLevelDto
	 * @throws AugeServiceException
	 */
	public void promotePartnerInstanceLevel(PartnerInstanceLevelDto partnerInstanceLevelDto) throws AugeServiceException ;
	
	
	/**
	 * 升级淘帮手
	 *
	 * @param upgradeDto
	 * @throws AugeServiceException
	 * @throws AugeSystemException
	 */
	public void upgradePartnerInstance(PartnerInstanceUpgradeDto upgradeDto) throws AugeServiceException, AugeSystemException;
	
	/**
	 * 取消升级
	 * 
	 * @param cancelDto
	 * @throws AugeServiceException
	 * @throws AugeSystemException
	 */
	public void cancelUpgradePartnerInstance(CancelUpgradePartnerInstance cancelDto) throws AugeServiceException, AugeSystemException;
	/**
	 * 解冻保证金
	 * 
	 * @param instanceId
	 * @return
	 * @throws AugeServiceException
	 * @throws AugeSystemException
	 */
	public Boolean thawMoney(Long instanceId) throws AugeServiceException, AugeSystemException;
	
	/**
	 * 成功解冻保证金
	 * 
	 * @param partnerInstanceThrawSuccessDto
	 * @throws AugeServiceException
	 * @throws AugeSystemException
	 */
	public void thawMoneySuccess(PartnerInstanceThrawSuccessDto partnerInstanceThrawSuccessDto) throws AugeServiceException, AugeSystemException;
	
	/**
	 * C2B升级签订入住协议
	 * @param taobaoUserId
	 * @param signedNewProtocol
	 * @param isFrozenMoney
	 * @throws AugeServiceException
	 */
	public void signC2BSettledProtocol(Long taobaoUserId,boolean signedC2BProtocol,boolean isFrozenMoney) throws AugeServiceException;
	
	/**
	 * 更新服务站地址
	 * @param taobaoUserId
	 * @throws AugeServiceException
	 */
	public void updateStationAddress(Long taobaoUserId,StationDto station,boolean isSendMail) throws AugeServiceException;
	
	/**
	 * 更新服务站经纬度
	 * @param taobaoUserId
	 * @throws AugeServiceException
	 */
	public void updateStationLngLat(Long taobaoUserId,StationDto station) throws AugeServiceException;
	
	/**
	 * 淘帮手关闭独立物流站
	 * 
	 * @throws AugeServiceException
	 */
	public void closeCainiaoStationForTpa(Long partnerInstanceId, OperatorDto operatorDto) throws AugeServiceException;
	
	
}