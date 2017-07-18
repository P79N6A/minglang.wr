package com.taobao.cun.auge.station.strategy;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;

public interface PartnerInstanceStrategy {
	
	/**
	 * 申请入驻
	 * @param partnerInstanceDto
	 * @
	 */
	public void applySettle(PartnerInstanceDto partnerInstanceDto);
	
	public void applySettleNewly(PartnerInstanceDto partnerInstanceDto);
	
	/**
	 * 入驻成功
	 * @param settleSuccessDto
	 * @
	 */
	public void settleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto,PartnerStationRel rel) ;
	
	/**
	 * 申请退出
	 * @param quitDto
	 * @param typeEnum
	 * @
	 */
	public void applyQuit(QuitStationApplyDto quitDto, PartnerInstanceTypeEnum typeEnum) ;
	
	/**
	 * 退出审批同意
	 * 
	 * @param partnerInstanceId
	 * @
	 */
	public void handleDifferQuitAuditPass(Long partnerInstanceId) ;
	
	/**
	 * 退出成功
	 * @param partnerInstanceQuitDto
	 * @
	 */
	public void quit(PartnerInstanceQuitDto partnerInstanceQuitDto) ;
	
	/**
	 * 删除
	 * @param partnerInstanceDeleteDto
	 * @param rel
	 * @
	 */
	public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto,PartnerStationRel rel) ;
	
	/**
	 * 校验是否还有子成员，例如，校验合伙人下面是否存在淘帮手
	 * 
	 * 退出时，校验
	 * 
	 * @param instance
	 * @
	 */
	public void validateExistChildrenForQuit(PartnerStationRel instance);
	
	/**
	 * 校验是否还有子成员，例如，校验合伙人下面是否存在淘帮手
	 * 
	 * 停业时，校验
	 * 
	 * @param partnerStationRel
	 * @
	 */
	public void validateClosePreCondition(PartnerStationRel partnerStationRel);

	public Boolean validateUpdateSettle(Long instanceId) ;
	
	/**
	 * 停业中
	 * 
	 * @param instanceId
	 * @param stationName
	 * @param operatorDto
	 * @
	 */
	public void startClosing(Long instanceId, String stationName, OperatorDto operatorDto) ;
	
	/**
	 * 系统主动停业
	 * 
	 * @param instanceId
	 * @param operatorDto
	 * @
	 */
	public void autoClosing(Long instanceId, OperatorDto operatorDto) ;
	
	/**
	 * 已停业
	 * @param instanceId
	 * @param taobaoUserId
	 * @param taobaoNick
	 * @param typeEnum
	 * @param operatorDto
	 * @
	 */
	public void closed(Long instanceId, Long taobaoUserId,String taobaoNick, PartnerInstanceTypeEnum typeEnum,OperatorDto operatorDto) ;
	
	/**
	 * 退出中
	 * 
	 * @param instanceId
	 * @param stationName
	 * @param operatorDto
	 * @
	 */
	public void startQuiting(Long instanceId, String stationName, OperatorDto operatorDto);

	/**
	 * 校验资产是否已经归还
	 * 
	 * @param instanceId
	 */
	public void validateAssetBack(Long instanceId);

	/**
	 * 校验村点上其他人是否都处于退出待解冻，已退出状态
	 * 
	 * @param instanceId
	 */
	public void validateOtherPartnerQuit(Long instanceId);
	
	/**
	 * 进入服务中
	 * 
	 * @param instanceId
	 * @param taobaoUserId
	 * @param operatorDto
	 */
	public void startService(Long instanceId, Long taobaoUserId, OperatorDto operatorDto);
}
