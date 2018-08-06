package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import com.taobao.cun.auge.dal.domain.Station;

/**
 * 待初始化的站点
 * @author chengyu.zhoucy
 *
 */
public class InitingStation {
	/**
	 * 待生成围栏的站点
	 */
	private List<Station> stations;
	
	/**
	 * 模板ID
	 */
	private Long templateId;
	
	public InitingStation(List<Station> stations, Long templateId) {
		this.stations = stations;
		this.templateId = templateId;
	}

	public List<Station> getStations() {
		return stations;
	}

	public void setStations(List<Station> stations) {
		this.stations = stations;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
}
