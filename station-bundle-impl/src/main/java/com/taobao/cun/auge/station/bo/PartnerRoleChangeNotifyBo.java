package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum.PartnerInstanceType;

/**
 * 合伙人角色变化时发送消息通知外部系统
 * 
 * @author chengyu.zhoucy
 *
 */
public interface PartnerRoleChangeNotifyBo {
	/**
	 * 发送添加角色时的消息
	 * @param taobaoUserId
	 * @param partnerInstanceType
	 */
	void sendAddRoleMsg(Long taobaoUserId, PartnerInstanceType partnerInstanceType);
	
	/**
	 * 发送删除角色时的消息
	 * @param taobaoUserId
	 * @param partnerInstanceType
	 */
	void sendRemoveRoleMsg(Long taobaoUserId, PartnerInstanceType partnerInstanceType);
}
