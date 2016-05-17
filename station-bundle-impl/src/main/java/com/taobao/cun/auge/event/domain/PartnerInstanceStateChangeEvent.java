package com.taobao.cun.auge.event.domain;

import com.taobao.cun.crius.event.annotation.EventDesc;
import com.taobao.cun.crius.event.annotation.EventField;


/**
 * 合伙人服务站实例状态变更事件
 * 会暴露给外部系统
 * 1.场景：开业包
 * @author quanzhu.wangqz
 *
 */
@EventDesc("合伙人服务站实例")
public class PartnerInstanceStateChangeEvent {
	
	@EventField("服务站id")
	private Long stationId;
	
	@EventField("当前合伙人实例状态")
	private String partnerInstanceState;
	
	@EventField("生命周期状态,当服务站状态是入驻中，停业中，退出中时使用")
	private String lifecycleState;
	
	@EventField("当前合伙人淘宝userId")
	private Long taobaoUserId;
	
	@EventField("当前合伙人淘宝nick")
	private String taobaoNick;
	
	@EventField("合伙人类型:TP:合伙人  TPA:淘帮手  TPV：村拍档 ")
	private String partnerType; //经营者类型  TP:合伙人  TPA:淘帮手  TPV：村拍档 
	
	@EventField("执行时间(yyyy-MM-dd HH:mm:ss)   提交 | 装修 | 开业 | 停业 | 退出 ")
	private String execDate;   //执行时间   提交 | 装修 | 开业 | 停业 | 退出
	
	@EventField("服务站对应组织id")
	private Long ownOrgId;   //对应的业务组织
	
	@EventField("原始合伙人实例状态")
	private String prePartnerInstanceState;

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}


	public String getLifecycleState() {
		return lifecycleState;
	}

	public void setLifecycleState(String lifecycleState) {
		this.lifecycleState = lifecycleState;
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

	public String getPartnerInstanceState() {
		return partnerInstanceState;
	}

	public void setPartnerInstanceState(String partnerInstanceState) {
		this.partnerInstanceState = partnerInstanceState;
	}

	public String getPrePartnerInstanceState() {
		return prePartnerInstanceState;
	}

	public void setPrePartnerInstanceState(String prePartnerInstanceState) {
		this.prePartnerInstanceState = prePartnerInstanceState;
	}
}
