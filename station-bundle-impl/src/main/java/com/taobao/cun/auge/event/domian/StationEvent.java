package com.taobao.cun.auge.event.domian;

import java.util.Date;

import com.taobao.cun.crius.event.annotation.EventDesc;
import com.taobao.cun.crius.event.annotation.EventField;


/**
 * 服务站状态变更事件
 * 会暴露给外部系统
 * 1.场景：开业包项目
 * @author quanzhu.wangqz
 *
 */
@EventDesc("服务站 ")
public class StationEvent {
	
	@EventField("服务站id")
	private Long stationId;
	
	@EventField("服务站状态")
	private String state;
	
	@EventField("生命周期状态,当服务站状态是入驻中，停业中，退出中时使用")
	private String lifecycleState;
	
	@EventField("当前合伙人淘宝userId")
	private Long taobaoUserId;
	
	@EventField("当前合伙人淘宝nick")
	private String taobaoNick;
	
	@EventField("合伙人类型:TP:合伙人  TPA:淘帮手  TPV：村拍档 ")
	private String operatorType; //经营者类型  TP:合伙人  TPA:淘帮手  TPV：村拍档 
	
	@EventField("执行时间(yyyy-MM-dd HH:mm:ss)   提交 | 装修 | 开业 | 停业 | 退出 ")
	private String execDate;   //执行时间   提交 | 装修 | 开业 | 停业 | 退出
	
	@EventField("村点对应组织id")
	private Long ownOrgId;   //对应的业务组织

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
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

}
