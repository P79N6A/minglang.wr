package com.taobao.cun.auge.lifecycle;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;


public class LifeCyclePhaseContext {

	/**
	 * 当前生命周期事件
	 */
	private String event;
	
	
	/**
	 * 合伙人实例对象
	 */
	private PartnerInstanceDto partnerInstance;
	
	/**
	 * 合伙人实例对象
	 */
	private String userType;
	
	/**
	 * 扩展信息
	 */
	private Map<String,Object> extensionInfos = Maps.newConcurrentMap();
	
	/**
	 * 原始状态
	 */
	private String sourceState;
	
	/**
	 * 目标状态
	 */
	private String targetState;
	
	public PartnerInstanceDto getPartnerInstance() {
		return partnerInstance;
	}

	public void setPartnerInstance(PartnerInstanceDto partnerInstance) {
		this.partnerInstance = partnerInstance;
	}

	public Map<String, Object> getExtensionInfos() {
		return extensionInfos;
	}

	public void setExtensionInfos(Map<String, Object> extensionInfos) {
		if(extensionInfos != null){
			this.extensionInfos.putAll(extensionInfos);
		}
	}

	public Object getExtension(String extensionKey) {
		return extensionInfos.get(extensionKey);
	}
	
	public Object getExtensionWithDefault(String extensionKey,Object defaultValue) {
		return Optional.ofNullable(extensionInfos.get(extensionKey)).orElse(defaultValue);
	}
	
	
	
	public PhaseKey getPhaseKey() {
		return new PhaseKey(userType,event);
	}
	
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getSourceState() {
		return sourceState;
	}

	public void setSourceState(String sourceState) {
		this.sourceState = sourceState;
	}

	public String getTargetState() {
		return targetState;
	}

	public void setTargetState(String targetState) {
		this.targetState = targetState;
	}
}
