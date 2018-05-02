package com.taobao.cun.auge.transition.transition;

import java.io.Serializable;
import java.util.Date;

/**
 * 封装一个状态变更基本信息类，方便框架层面做统一的处理
 * @author zhenhuan.zhangzh
 *
 */
public class BaseTransitionInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5960741609035104349L;
	
	private Long bizPrimaryKey;
	
	private Long taobaoUserId;
	
	private Long stationId;
	
	private String userType;

	public Long getBizPrimaryKey() {
		return bizPrimaryKey;
	}

	public void setBizPrimaryKey(Long bizPrimaryKey) {
		this.bizPrimaryKey = bizPrimaryKey;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	

}
