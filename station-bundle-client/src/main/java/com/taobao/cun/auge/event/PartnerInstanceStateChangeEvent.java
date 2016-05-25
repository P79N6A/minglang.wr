package com.taobao.cun.auge.event;

import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;


/**
 * 合伙人服务站实例状态变更事件
 * @author quanzhu.wangqz
 *
 */
public class PartnerInstanceStateChangeEvent {
	
	/**
	 * 实例id,内部使用，外部系统不要使用
	 */
	private Long partnerInstanceId;
	
	/**
	 * 服务站id
	 */
	private Long stationId;
	
	/**
	 * 当前合伙人淘宝userId
	 */
	private Long taobaoUserId;
	
	/**
	 * 当前合伙人淘宝nick
	 */
	private String taobaoNick;
	/**
	 * 状态转换枚举
	 */
	private PartnerInstanceStateChangeEnum stateChangeEnum;
	
	/**
	 * 合伙人类型:TP:合伙人  TPA:淘帮手  TPV：村拍档 
	 */
	private String partnerType;
	
	/**
	 * 执行时间(yyyy-MM-dd HH:mm:ss)   提交 | 装修 | 开业 | 停业 | 退出 
	 */
	private String execDate;   
	
	/**
	 * 服务站对应组织id
	 */
	private Long ownOrgId;
	
	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}

	public String getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(String partnerType) {
		this.partnerType = partnerType;
	}

	public String getExecDate() {
		return execDate;
	}

	public void setExecDate(String execDate) {
		this.execDate = execDate;
	}

	public Long getOwnOrgId() {
		return ownOrgId;
	}

	public void setOwnOrgId(Long ownOrgId) {
		this.ownOrgId = ownOrgId;
	}

	public PartnerInstanceStateChangeEnum getStateChangeEnum() {
		return stateChangeEnum;
	}

	public void setStateChangeEnum(PartnerInstanceStateChangeEnum stateChangeEnum) {
		this.stateChangeEnum = stateChangeEnum;
	}

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}
}
