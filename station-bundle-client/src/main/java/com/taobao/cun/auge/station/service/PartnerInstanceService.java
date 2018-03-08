package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.station.dto.PartnerInstanceTransDto;

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
import com.taobao.cun.settle.bail.dto.CuntaoBailDetailDto;


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
	public Long addTemp(PartnerInstanceDto partnerInstanceDto);
	
	/**
	 * 暂存 人，服务站基础信息
	 * 
	 * @param condition
	 * @return
	 */
	public Long updateTemp(PartnerInstanceDto partnerInstanceDto);
	
	/**
	 * 更改入驻中的信息    
	 * @param partnerInstanceDto
	 */
	public void updateSettle(PartnerInstanceDto partnerInstanceDto);
	
	
	/**
	 * 审批入驻 当前只有小二审批淘帮手
	 * @param auditSettleDto
	 */
	public void auditSettleByManager(AuditSettleDto auditSettleDto);
	
	/**
	 * 合伙人修改，人、服务站基础信息，只包含服务中
	 * 
	 * 目前只有合伙人修改淘帮手信息，调用
	 * 
	 */
	public void updateByPartner(PartnerInstanceUpdateServicingDto partnerInstanceUpdateServicingDto);

	/**
	 * 修改， 人，服务站基础信息 主要包含（装修中，服务中，停业申请中等）有效合伙人状态下
	 * 
	 * @param condition
	 * @return
	 */
	public void update(PartnerInstanceUpdateServicingDto partnerInstanceUpdateServicingDto);

	/**
	 * 删除合伙人实例
	 * 
	 * @param instanceId
	 * @return
	 */
	public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto);

	/**
	 * 签署入驻协议
	 * 
	 * @param taobaoUserId
	 * @param waitFrozenMoney
	 * @param version 乐观锁
	 */
	public void signSettledProtocol(Long taobaoUserId, Double waitFrozenMoney, Long version);

	/**
	 * 签署管理协议
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	public void signManageProtocol(Long taobaoUserId, Long version);

	/**
	 * 冻结保证金（新接口）
	 * 
	 * @param freezeBondDto
	 * @return boolean
	 */
	public boolean freezeBond(FreezeBondDto freezeBondDto);
	
	/**
	 * 冻结保证金(老接口，防止发布时报错,未来不在使用)
	 * 使用新接口freezeBond(FreezeBondDto freezeBondDto);
	 * 
	 * @param freezeBondDto
	 * @return boolean
	 */
	public boolean freezeBond(Long taobaoUserId, Double frozenMoney);

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
	public boolean openStation(OpenStationDto openStationDto);

	/**
	 * 合伙人主动申请停业，目前给接口只有合伙人会调用，淘帮手，村拍档不能调用
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	public void applyCloseByPartner(Long taobaoUserId);

	/**
	 * 小二确认合伙人申请停业
	 * 
	 * @param partnerInstanceId
	 * @param employeeId
	 * @param isAgree
	 * @return
	 */
	public void confirmClose(ConfirmCloseDto confirmCloseDto);

	/**
	 * 小二、TP商申请停业
	 * 
	 * @param forcedCloseCondition
	 * @param employeeId
	 * @return
	 */
	public void applyCloseByManager(ForcedCloseDto forcedCloseDto);
	
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
	public void applyQuitByManager(QuitStationApplyDto quitDto);

	/**
	 * 申请入驻
	 * 
	 * @param condition
	 * @return
	 */
	public Long applySettle(PartnerInstanceDto partnerInstanceDto);

	/**
	 * 针对入驻失败的 申请入驻
	 * 
	 * @param condition
	 * @return
	 */
	public Long applyResettle(PartnerInstanceDto partnerInstanceDto);

	/**
	 * 检查有没有开业包，要调用外部接口 到时候要写适配器封装外部接口 先定义在这里
	 * 
	 * @return
	 */
	public boolean checkKyPackage();

	/**
	 * 正式退出
	 * 
	 * @param partnerInstanceQuitDto
	 * @return
	 */
	public void quitPartnerInstance(PartnerInstanceQuitDto partnerInstanceQuitDto);
	
	/**
	 * 降级合伙人
	 * @param partnerInstanceDegradeDto
	 */
	public void degradePartnerInstance(PartnerInstanceDegradeDto degradeDto);
	
	
	/**
	 * 成功入驻
	 * @param settleSuccessDto
	 */
	public void applySettleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto);
	
	/**
	 * 降级成功
	 * @param degradeSuccessDto
	 */
	public void degradePartnerInstanceSuccess(DegradePartnerInstanceSuccessDto degradeSuccessDto);
	
	/**
	 * 评定合伙人层级(定时钟和审批流程调用)
	 * @param partnerInstanceLevelDto
	 */
	public void evaluatePartnerInstanceLevel(PartnerInstanceLevelDto partnerInstanceLevelDto);

	/**
	 * 更换TPA的TP
	 * @param changeTPDto
	 */
	public void changeTP(ChangeTPDto changeTPDto);
	
	/**
	 * 已停业村点恢复服务中
	 * @param instanceDto
	 */
	public void reService(Long instanceId, String operator);
	
	public void upgradeDecorateLifeCycle(Long instanceId, String operator);
	
	/**
	 * S7、S8合伙人层级晋升
	 * @param partnerInstanceLevelDto
	 */
	public void promotePartnerInstanceLevel(PartnerInstanceLevelDto partnerInstanceLevelDto) ;
	
	
	/**
	 * 升级淘帮手
	 *
	 * @param upgradeDto
	 */
	public void upgradePartnerInstance(PartnerInstanceUpgradeDto upgradeDto);
	
	/**
	 * 取消升级
	 * 
	 * @param cancelDto
	 */
	public void cancelUpgradePartnerInstance(CancelUpgradePartnerInstance cancelDto);
	/**
	 * 解冻保证金
	 * 
	 * @param instanceId
	 * @return
	 */
	public Boolean thawMoney(Long instanceId);
	
	/**
	 * 成功解冻保证金
	 * 
	 * @param partnerInstanceThrawSuccessDto
	 */
	public void thawMoneySuccess(PartnerInstanceThrawSuccessDto partnerInstanceThrawSuccessDto);
	
	/**
	 * C2B升级签订入住协议
	 * @param taobaoUserId
	 * @param signedNewProtocol
	 * @param isFrozenMoney
	 */
	public void signC2BSettledProtocol(Long taobaoUserId,boolean signedC2BProtocol,boolean isFrozenMoney);
	
	/**
	 * 更新服务站地址
	 * @param taobaoUserId
	 */
	public void updateStationAddress(Long taobaoUserId,StationDto station,boolean isSendMail);
	
	/**
	 * 更新服务站经纬度
	 * @param taobaoUserId
	 */
	public void updateStationLngLat(Long taobaoUserId,StationDto station);
	
	/**
	 * 淘帮手关闭独立物流站
	 * 
	 */
	public void closeCainiaoStationForTpa(Long partnerInstanceId, OperatorDto operatorDto);
	
	public void quitApprove(Long instanceId);
	
	void closeApprove(Long instanceId);
	
	/**
     * 为村站或门店申请卖家账号
     * @param taobaoUserId
	 */
	public void applySellerAccount(Long taobaoUserId);
	
	
	public List<CuntaoBailDetailDto> queryCuntaoBail(Long taobaoUserId);
	
    /**
     * 冻结铺货保证金（新接口）
     * 
     * @param freezeBondDto
     * @return boolean
     */
    public boolean freezeRePublishBond(FreezeBondDto freezeBondDto);
    /**
     * 提交村小二转型
     * @param transDto
     */
    public void commitTrans(PartnerInstanceTransDto transDto);
    /**
     * 转型  补交基础保证金后，业务接口
     * @param freezeBondDto
     * @return
     */
    public boolean freezeBondForTrans(FreezeBondDto freezeBondDto);
}