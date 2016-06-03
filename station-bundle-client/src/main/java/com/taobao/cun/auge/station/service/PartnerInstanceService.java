package com.taobao.cun.auge.station.service;

import java.math.BigDecimal;

import com.taobao.cun.auge.station.dto.ConfirmCloseDto;
import com.taobao.cun.auge.station.dto.ForcedCloseDto;
import com.taobao.cun.auge.station.dto.OpenStationDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.QuitDto;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
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
	 * 修改， 人，服务站基础信息 主要包含（装修中，服务中，停业申请中，已停业等）有效合伙人状态下
	 * 
	 * @param condition
	 * @return
	 */
	public boolean update(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException;

	/**
	 * 删除合伙人实例
	 * 
	 * @param instanceId
	 * @return
	 */
	public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto) throws AugeServiceException;

	/**
	 * 协议是否待签约,e.g. 入驻协议，管理协议
	 * 
	 * @param taobaoUserId
	 * @param type
	 * @return
	 */
	public boolean getProtocolInfoToBeSigned(Long taobaoUserId, ProtocolTypeEnum type);

	/**
	 * 签署入驻协议
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	public void signSettledProtocol(Long taobaoUserId, Double waitFrozenMoney) throws AugeServiceException;

	/**
	 * 签署管理协议
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	public void signManageProtocol(Long taobaoUserId) throws AugeServiceException;

	/**
	 * 保证金是否待冻结
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	public boolean ifBondToBeFreezen(Long taobaoUserId);

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
	public void applyQuitByManager(QuitDto quitDto) throws AugeServiceException;

	/**
	 * 申请入驻
	 * 
	 * @param condition
	 * @return
	 */
	public Long applySettle(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException;

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

}
