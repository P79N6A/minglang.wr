package com.taobao.cun.auge.station.bo.dto;

import java.util.List;

/**
 * 为围栏查询站点的条件
 * 
 * @author chengyu.zhoucy
 *
 */
public class FenceInitingStationQueryCondition {
	private List<String> stationTypes;
	
	private List<String> stationLocations;
	
	private Long templateId;
	
	public List<String> getStationLocations() {
		return stationLocations;
	}

	public List<String> getStationTypes() {
		return stationTypes;
	}

	public void setStationTypes(List<String> stationTypes) {
		this.stationTypes = stationTypes;
	}

	public void setStationLocations(List<String> stationLocations) {
		this.stationLocations = stationLocations;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
}
